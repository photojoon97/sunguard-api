package com.joon.sunguard_api.config;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestYaml {

    private static final Logger logger = LoggerFactory.getLogger(TestYaml.class);

    @Autowired
    private SetYaml setYaml;

    @Test
    void yaml_test() {
        logger.info("YAML 설정값 확인: {}", setYaml.getValue()); // 로그 출력
        assertThat(setYaml.getValue()).isEqualTo("HelloYAML");
    }
}
