package com.messenger.chatservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * @author shashidhar
 */
@ConfigurationProperties(prefix = "datastax.astra")
@Data
public class DatastaxConnectionConfiguration {

    private File secureConnectBundle;
}
