package com.team2.hrbank.common.config;

import com.team2.hrbank.HrbankApplication;
import io.github.openfeign.querydsl.jpa.spring.repository.config.EnableQuerydslRepositories;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableQuerydslRepositories(basePackageClasses = HrbankApplication.class)
public class QuerydslConfig {

    /*@Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }*/

}
