package com.facevisitor.api.service.auth;

import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.common.exception.UnAuthorizedException;
import com.facevisitor.api.common.utils.ServerUtils;
import com.facevisitor.api.domain.face.FaceMeta;
import com.facevisitor.api.domain.user.Authority;
import com.facevisitor.api.domain.user.AuthorityRepository;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.UserRepository;
import com.facevisitor.api.dto.user.Join;
import com.facevisitor.api.dto.user.Login;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class AuthService {

    @Value("${secret.oauth.clientId}")
    String clientId;

    @Value("${secret.oauth.clientSecurity}")
    String clientSecurity;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    ModelMapper modelMapper;


    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public ResponseEntity login(Login login, HttpServletRequest request) {


        if (!userRepository.findByEmail(login.getEmail()).isPresent()) {
            throw new BadRequestException("가입되지 않았습니다.");
        }

        String host = ServerUtils.myHostname(request);
        log.debug("host : {}", host);

        // Android Test
        if (host.startsWith("http://10.0.2.2")) {
            host = host.replace("http://10.0.2.2", "http://localhost");
        }

        ResponseEntity responseEntity = null;
        URL url;
        StringBuilder response = new StringBuilder();

        try {
            url = UriComponentsBuilder.fromHttpUrl(String.format("%s/oauth/token", host))
                    .queryParam("grant_type", "password")
                    .queryParam("username", login.getEmail())
                    .queryParam("password", login.getPassword())
                    .build()
                    .encode()
                    .toUri()
                    .toURL();
//            url = new URL(String.format("%s/oauth/token?grant_type=password&username=%s&password=%s", host, email, password));
            HttpURLConnection myConnection = createConnection(url);

            if (myConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                responseEntity = ResponseEntity.ok(response.toString());
            } else {
                throw new BadRequestException("로그인 정보를 다시 확인해주세요");
            }
            myConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseEntity;
    }

    public ResponseEntity refreshToken(String refreshToken, HttpServletRequest request) {
        // 탈퇴한 계정인지 체크
        String host = ServerUtils.myHostname(request);

        // Android Test
        if (host.startsWith("http://10.0.2.2")) {
            host = host.replace("http://10.0.2.2", "http://localhost");
        }

        ResponseEntity responseEntity = null;
        URL url;
        StringBuilder response = new StringBuilder();

        try {

            url = UriComponentsBuilder.fromHttpUrl(String.format("%s/oauth/token", host))
                    .queryParam("grant_type", "refresh_token")
                    .queryParam("refresh_token", refreshToken)
                    .build().toUri().toURL();

            HttpURLConnection myConnection = createConnection(url);
            if (myConnection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(myConnection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                responseEntity = ResponseEntity.ok(response.toString());
            } else {
                throw new UnAuthorizedException("토큰 재발급 실패");
            }

            myConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseEntity;
    }


    private HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection myConnection = (HttpURLConnection) url.openConnection();
        myConnection.setRequestProperty("Content-Type", "application/json");
        String target = String.format("%s:%s", clientId, clientSecurity);

        byte[] targetBytes = new byte[0];
        targetBytes = target.getBytes(StandardCharsets.UTF_8);
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(targetBytes);

        log.debug("auth ::: {}", String.format("Basic %s", new String(encodedBytes)));
        myConnection.setRequestProperty("Authorization", String.format("Basic %s", new String(encodedBytes)));

        myConnection.setReadTimeout(10000);
        myConnection.setConnectTimeout(15000);
        myConnection.setRequestMethod("POST");
        myConnection.setDoInput(true);
        myConnection.setDoOutput(true);
        return myConnection;
    }


    public User join(Join join) {
        User mappedUser = new User();
        mappedUser.setEmail(join.getEmail());
        mappedUser.setPassword(join.getPassword());
        mappedUser.setName(join.getName());
        mappedUser.setPassword(encodePassword(join.getPassword()));
        mappedUser.setAuthorities(userAuthority());
        mappedUser.setPhone(join.getPhone());
        FaceMeta faceMeta = new FaceMeta();
        faceMeta.setFaceId(join.getFaceIds());
        faceMeta.setGender(join.getGender());
        faceMeta.setLowAge(join.getLowAge());
        faceMeta.setHighAge(join.getHighAge());
        mappedUser.addFaceMeta(faceMeta);
        return userRepository.save(mappedUser);
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public Set<Authority> userAuthority() {
        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByRole("USER"));
        return authorities;
    }
}
