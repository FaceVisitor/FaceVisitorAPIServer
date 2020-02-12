package com.facevisitor.api.owner.service;


import com.facevisitor.api.common.exception.BadRequestException;
import com.facevisitor.api.domain.security.Authority;
import com.facevisitor.api.domain.security.AuthorityRepository;
import com.facevisitor.api.domain.user.User;
import com.facevisitor.api.domain.user.repo.UserRepository;
import com.facevisitor.api.dto.auth.TokenDto;
import com.facevisitor.api.owner.dto.auth.OJoin;
import com.facevisitor.api.owner.dto.auth.OLogin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
@Slf4j
public class OauthService {

    @Value("${secret.oauth.clientId}")
    String clientId;

    @Value("${secret.oauth.clientSecurity}")
    String clientSecurity;

    @Value("${spring.base_url}")
    String base_url;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    RestTemplate restTemplate = new RestTemplate();

    public void join(OJoin oJoin){
        User user = new User();
        user.setName(oJoin.getName());
        user.setEmail(oJoin.getEmail());
        user.setPassword(encodePassword(oJoin.getPassword()));
        user.setPhone(oJoin.getPhone());
        user.setAuthorities(ownerAuthority());
        userRepository.save(user);
    }

    private TokenDto getTokenDto(MultiValueMap<String, String> map) {
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, getBasicAuthHeaders());
        ResponseEntity<TokenDto> tokenResponse = restTemplate.postForEntity(base_url+"/oauth/token", request, TokenDto.class);
        if(tokenResponse.getStatusCode().is2xxSuccessful()){
            return tokenResponse.getBody();
        }else{
            throw new BadRequestException("로그인 정보를 다시 확인해주세요");
        }
    }

    private HttpHeaders getBasicAuthHeaders(){
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.setBasicAuth(clientId, clientSecurity);
        return httpHeaders;
    }

    public TokenDto login(OLogin login) {
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("username", login.getEmail());
        map.add("password", login.getPassword());
        map.add("grant_type", "password");
        return getTokenDto(map);
    }

    public TokenDto refreshToken(String refreshToken) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("refresh_token", refreshToken);
        map.add("grant_type", "refresh_token");
        return getTokenDto(map);
    }

    public Set<Authority> ownerAuthority() {
        HashSet<Authority> authorities = new HashSet<>();
        authorities.add(authorityRepository.findByRole("OWNER"));
        return authorities;
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
