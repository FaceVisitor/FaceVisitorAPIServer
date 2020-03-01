package com.facevisitor.api;

import com.facevisitor.api.common.exception.NotFoundException;
import com.facevisitor.api.domain.security.Authority;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.dto.auth.TokenDto;
import com.facevisitor.api.owner.dto.auth.OJoin;
import com.facevisitor.api.owner.service.OauthService;
import com.facevisitor.api.repository.AuthorityRepository;
import com.facevisitor.api.repository.UserRepository;
import com.facevisitor.api.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Ignore
@Transactional
public class BaseTest {


    @Autowired
    Gson gson;

    @Autowired
    protected AuthorityRepository authorityRepository;

    @Autowired
    protected UserRepository userRepository;


    @Autowired
    AuthService authService;

    @Autowired
    OauthService oauthService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    String email = "sajang@facevisitor.com";
    String password = "asdf4112";
    String name = "일반유저";

    @Value("${secret.oauth.clientId}")
    String clientId;

    @Value("${secret.oauth.clientSecurity}")
    String clientSecurity;

    String access_token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsiZmFjZV92aXNpdG9yX29hdXRoMiJdLCJ1c2VyX25hbWUiOiJzYWphbmdAZmFjZXZpc2l0b3IuY29tIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImV4cCI6MTU4NDY4MTM2MSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9PV05FUiJdLCJqdGkiOiI2ODZhYjdhNC04ZTQwLTQzYzUtYmY5My03M2JiY2NlNWRkNDIiLCJjbGllbnRfaWQiOiJnanduZHVkIn0.NuU72HnmWLSQeKNcNOavszu59yLnMRcQP1exshlbK4lcSawIvsEpV83Hq0bR_F4z5SwMkEPPIlOIyNAr5T_dQRnLLSA9rkeXP_c4KO1Nifl5rr7xKezU4OFVoImWqA-vSJltIpGn6StevrOMOJZg1MnDElylUgO_Tal-E_9hgxRLj3miSspvL5MWfw4_66BeBibwQKvkSu82dWv_rhgLXlkslsgZ_honBKwqkvBK9-XNwyDC8oN5uZcmv76LC7Oc9yIBXcE0WuRI8XUMPHSZfNfu6yq3XnNhUf1pIOzOZGeEFtoLmngrBbVOsz_xhyKEiM4d7EPItVl-lQvNL2Z8WA";

    @Before
    public void setup() throws Exception {

    }

    public TokenDto getTokenData() throws Exception {
        String contentAsString = mockMvc
                .perform(post("/oauth/token").with(httpBasic(clientId, clientSecurity))
                        .param("username", email)
                        .param("password", password)
                        .param("grant_type", "password")
                ).andDo(print()).andReturn().getResponse().getContentAsString();
        Gson gson = new Gson();
        return gson.fromJson(contentAsString, TokenDto.class);
    }

    public Authority createOwnerAuthority(){
        if (!authorityRepository.findByRole("OWNER").isPresent()) {
            Authority authority = new Authority();
            authority.setRole("OWNER");
            return authorityRepository.save(authority);
        } else {
            return authorityRepository.findByRole("OWNER").orElseThrow(NotFoundException::new);
        }
    }

    public Authority createUserAuthority(){
        if (!authorityRepository.findByRole("USER").isPresent()) {
            Authority authority = new Authority();
            authority.setRole("USER");
            return authorityRepository.save(authority);
        } else {
            return authorityRepository.findByRole("USER").orElseThrow(NotFoundException::new);
        }
    }

    public Authority createSupserAuthority(){
        if (!authorityRepository.findByRole("SUPER").isPresent()) {
            Authority authority = new Authority();
            authority.setRole("SUPER");
            return authorityRepository.save(authority);
        } else {
            return authorityRepository.findByRole("SUPER").orElseThrow(NotFoundException::new);
        }
    }

    public User createOwner() throws Exception {
        createOwnerAuthority();
        if (!userRepository.findByEmail(email).isPresent()) {
            OJoin join = new OJoin();
            join.setEmail(email);
            join.setPassword(password);
            join.setName("허주영 사장");
            join.setPhone("01026588178");
            return oauthService.join(join);
        } else {
            return userRepository.findByEmail(email).get();
        }
    }

    public MockHttpServletRequestBuilder getWithUser(String url) throws Exception{
        createOwner();
        return MockMvcRequestBuilders.get(url).header("Authorization", "Bearer " +getTokenData().getAccess_token()).contentType(
                MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder postWithUser(String url) throws Exception{
        createOwner();
        return MockMvcRequestBuilders.post(url).header("Authorization", "Bearer " +getTokenData().getAccess_token()).contentType(
                MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder putWithUser(String url) throws Exception{
        createOwner();
        return MockMvcRequestBuilders.put(url).header("Authorization", "Bearer " +getTokenData().getAccess_token()).contentType(
                MediaType.APPLICATION_JSON);
    }

    public MockHttpServletRequestBuilder deleteWithUser(String url) throws Exception{
        createOwnerAuthority();
        return MockMvcRequestBuilders.delete(url).header("Authorization", "Bearer " +getTokenData().getAccess_token()).contentType(
                MediaType.APPLICATION_JSON);
    }

}
