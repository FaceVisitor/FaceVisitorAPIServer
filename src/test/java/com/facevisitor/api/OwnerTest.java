package com.facevisitor.api;

import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.security.Authority;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.auth.TokenDto;
import com.facevisitor.api.owner.dto.auth.OJoin;
import com.facevisitor.api.owner.dto.auth.OLogin;
import com.facevisitor.api.owner.service.OauthService;
import com.facevisitor.api.repository.AuthorityRepository;
import com.facevisitor.api.repository.UserRepository;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@AutoConfigureMockMvc
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OwnerTest extends BaseTest {

    @Value("${secret.oauth.clientId}")
    String clientId;

    @Value("${secret.oauth.clientSecurity}")
    String clientSecurity;

    @Autowired
    MockMvc mockMvc;

    @Value("${spring.base_url}")
    String base_url;

    @Autowired
    OauthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    PasswordEncoder passwordEncoder;

    String email = "test";
    String password = "1234";
    RestTemplate restTemplate = new RestTemplate();

    @Test
    public void 회원가입() throws Exception {
        createOwnerAuthority();
        OJoin join = new OJoin();
        join.setEmail(email);
        join.setPassword(password);
        join.setName("허주영 사장");
        join.setPhone("01026588178");
        User join1 = authService.join(join);
    }


    @Test
    public void 로그인() throws Exception {
        if (!userRepository.findByEmail(email).isPresent()) {
            createOwnerAuthority();
            OJoin join = new OJoin();
            join.setEmail(email);
            join.setPassword(password);
            join.setName("허주영 사장");
            join.setPhone("01026588178");
            User join1 = authService.join(join);
        }

        OLogin oLogin = new OLogin();
        oLogin.setEmail(email);
        oLogin.setPassword(password);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", oLogin.getEmail());
        map.add("password", oLogin.getPassword());
        map.add("grant_type", "password");
        TokenDto tokenDto = getTokenDto(map);
        System.out.println(tokenDto);

    }

    private TokenDto getTokenDto(MultiValueMap<String, String> map) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, getBasicAuthHeaders());
        ResponseEntity<TokenDto> tokenResponse = restTemplate.postForEntity(base_url + "/oauth/token", request, TokenDto.class);
        if (tokenResponse.getStatusCode().is2xxSuccessful()) {
            return tokenResponse.getBody();
        } else {
            throw new BadRequestException("로그인 정보를 다시 확인해주세요");
        }
    }

    private HttpHeaders getBasicAuthHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.setBasicAuth(clientId, clientSecurity);
        return httpHeaders;
    }

    @Test
    public void 리프레시토큰() throws Exception {
        OLogin oLogin = new OLogin();
        oLogin.setEmail("sajang@facevisitor.com");
        oLogin.setPassword("asdf4112");
        String contentAsString = mockMvc.perform(
                post("/api/v1/owner/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(oLogin)))
                .andExpect(status().is2xxSuccessful())
                .andDo(print())
                .andReturn().getResponse().getContentAsString();
        ModelMapper modelMapper = new ModelMapper();
        TokenDto tokenDto = modelMapper.map(contentAsString, TokenDto.class);
        String refresh_token = tokenDto.getRefresh_token();
        HashMap<String, String> refreshToken = new HashMap<>();
        refreshToken.put("refresh_token", refresh_token);
        mockMvc.perform(post("/api/v1/onwer/auth/refresh_token").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(refreshToken))
        ).andExpect(status().is2xxSuccessful()).andDo(print());

    }

    @Test
    public void 매장리스트조회() throws Exception {

    }



    public Authority createOwnerAuthority() {
        if (!authorityRepository.findByRole("OWNER").isPresent()) {
            Authority authority = new Authority();
            authority.setRole("OWNER");
            return authorityRepository.save(authority);
        } else {
            return authorityRepository.findByRole("OWNER").orElseThrow(NotFoundException::new);
        }
    }

    public Authority createUserAuthority() {
        if (!authorityRepository.findByRole("USER").isPresent()) {
            Authority authority = new Authority();
            authority.setRole("USER");
            return authorityRepository.save(authority);
        } else {
            return authorityRepository.findByRole("USER").orElseThrow(NotFoundException::new);
        }
    }

    public Authority createSupserAuthority() {
        if (!authorityRepository.findByRole("SUPER").isPresent()) {
            Authority authority = new Authority();
            authority.setRole("SUPER");
            return authorityRepository.save(authority);
        } else {
            return authorityRepository.findByRole("SUPER").orElseThrow(NotFoundException::new);
        }
    }


}
