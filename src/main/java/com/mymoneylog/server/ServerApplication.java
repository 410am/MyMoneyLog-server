package com.mymoneylog.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
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

}
