package com.edu.artexhibit.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(value = "message")
public class MessageProperties {
    private String categoryIsDeletedSuccessfully;
    private String eventIsDeletedSuccessfully;
    private String portfolioIsDeletedSuccessfully;
    private String artistIsDeletedSuccessfully;
    private String artCollectorIsDeletedSuccessfully;
    private String notificationIsDeletedSuccessfully;
    private String passwordIsChangedSuccessfully;
    private String commentIsDeletedSuccessfully;
}
