package com.facevisitor.api.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Slf4j
@Configuration
public class QueryDslConfig {


    @Autowired
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory getJPAQueryFactory() {
        return new JPAQueryFactory(() -> entityManager);
    }
}
