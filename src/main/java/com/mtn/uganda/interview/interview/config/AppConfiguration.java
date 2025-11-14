package com.mtn.uganda.interview.interview.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfiguration {
    private DataConfig data = new DataConfig();
    private FeatureFlags feature = new FeatureFlags();
    private PaginationConfig pagination = new PaginationConfig();

    @Data
    public static class DataConfig {
        private String usersFile = "classpath:mock-data/users.json";
        private String postsFile = "classpath:mock-data/posts.json";
        private boolean simulateDelay = false;
        private int delayMs = 100;
    }

    @Data
    public static class FeatureFlags {
        private boolean cachingEnabled = true;
        private boolean auditLoggingEnabled = true;
    }

    @Data
    public static class PaginationConfig {
        private int defaultPageSize = 10;
        private int maxPageCount = 100;
    }

}

