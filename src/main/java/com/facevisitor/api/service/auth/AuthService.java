package com.facevisitor.api.service.auth;

import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.common.exception.UnAuthorizedException;
import com.facevisitor.api.common.utils.ServerUtils;
import com.facevisitor.api.config.security.jwt.JwtUtils;
import com.facevisitor.api.domain.face.FaceId;
import com.facevisitor.api.domain.face.FaceImage;
import com.facevisitor.api.domain.face.FaceMeta;
import com.facevisitor.api.domain.security.Authority;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.user.Join;
import com.facevisitor.api.repository.AuthorityRepository;
import com.facevisitor.api.repository.UserRepository;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.facevisitor.api.common.string.exception.ExceptionString.NOT_FOUND_FACE;

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
    JwtUtils jwtUtils;

    @Autowired
    ModelMapper modelMapper;



    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }





    public Map<String,String> login(List<String> faceIds) {
        User user = userRepository.findByFaceIds(faceIds).orElse(null);
        if(user == null){
            throw new BadRequestException(NOT_FOUND_FACE);
        }

        String accessToken = jwtUtils.createAccessToken(user.getEmail());
        String refreshToken = jwtUtils.createRefreshToken(user.getEmail());
        HashMap<String,String> loginMeta = new HashMap<>();
        loginMeta.put("access_token", accessToken);
        loginMeta.put("refresh_token",refreshToken);
        loginMeta.put("createdAt", LocalDateTime.now().toString());
        return loginMeta;
    }

    public Map<String,String> loginTest(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user == null){
            throw new NotFoundException();
        }
        String accessToken = jwtUtils.createAccessToken(email);
        String refreshToken = jwtUtils.createRefreshToken(email);
        HashMap<String,String> loginMeta = new HashMap<>();
        loginMeta.put("access_token", accessToken);
        loginMeta.put("refresh_token",refreshToken);
        loginMeta.put("createdAt", LocalDateTime.now().toString());
        return loginMeta;
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





    public User join(Join join) {
        User mappedUser = new User();
        mappedUser.setEmail(join.getEmail());
        mappedUser.setPassword(join.getPassword());
        mappedUser.setName(join.getName());
        mappedUser.setPassword(encodePassword(join.getPassword()));
        mappedUser.setAuthorities(userAuthority());
        mappedUser.setPhone(join.getPhone());

        FaceMeta faceMeta = new FaceMeta();
        faceMeta.setGender(join.getGender());
        faceMeta.setLowAge(join.getLowAge());
        faceMeta.setHighAge(join.getHighAge());
        List<FaceId> faceIds = join.getFaceIds().stream()
                .map(id -> FaceId.builder().faceId(id).build()).collect(Collectors.toList());
        for(FaceId id : faceIds){
            faceMeta.addFaceId(id);
        }
        List<FaceImage> faceImages = join.getFaceImageUrl().stream().map(url -> {
            FaceImage faceImage = new FaceImage();
            faceImage.setUrl(url);
            return faceImage;
        }).collect(Collectors.toList());
        for(FaceImage faceImage : faceImages){
            faceMeta.addFaceImage(faceImage);
        }
        mappedUser.addFaceMeta(faceMeta);
        return userRepository.save(mappedUser);
    }



    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
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


    public Set<Authority> userAuthority() {
        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByRole("USER").orElseThrow(NotFoundException::new));
        return authorities;
    }

}
