package com.dk0124.cdr.es.dao.upbit;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootApplication
@ActiveProfiles("docker")
@TestPropertySource(locations="classpath:application.yml")
public class WithContainer {
}
