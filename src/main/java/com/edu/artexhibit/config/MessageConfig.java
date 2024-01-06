package com.edu.artexhibit.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:message.properties")
@EnableConfigurationProperties(MessageProperties.class)
public class MessageConfig {

}
