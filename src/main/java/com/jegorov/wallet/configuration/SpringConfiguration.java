package com.jegorov.wallet.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackages = "com.jegorov.wallet.engine.repository")
public class SpringConfiguration implements WebMvcConfigurer {

}
