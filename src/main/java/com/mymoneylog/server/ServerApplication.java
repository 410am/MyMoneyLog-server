package com.mymoneylog.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
@EntityScan(basePackages = "com.mymoneylog.server.entity")
public class ServerApplication {
    // 해놓은거 valid 처리, 공통 응답처리, jpa, querydsl

    // TODO 공통 예외 처리 필요
    // TODO 스프링 시큐리티 토큰 발행 및 검사 로직(전역)
    // TODO entity/user user.java 이거 내 테이블로 변경

    // jpa, querydsl 공부
    // 테스트 코드 작성 (junit 둥 방법 찾아서 해보기)

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner printMappings(ApplicationContext ctx) {
        return args -> {
            System.out.println("========== MAPPINGS START ==========");
            RequestMappingHandlerMapping mapping = ctx.getBean(RequestMappingHandlerMapping.class);
            mapping.getHandlerMethods().forEach((info, method) -> {
                System.out.println(info + " -> " + method);
            });
            System.out.println("=========== MAPPINGS END ===========");
        };
    }

}
