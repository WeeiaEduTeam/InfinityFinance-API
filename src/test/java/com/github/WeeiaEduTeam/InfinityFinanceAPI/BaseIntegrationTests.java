package com.github.WeeiaEduTeam.InfinityFinanceAPI;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


@SpringBootTest
@ActiveProfiles("test")
@Testcontainers
public class BaseIntegrationTests {

    private static final String SPRING_DB_URL_PROPERTY = "spring.datasource.url";
    private static final String SPRING_DB_USERNAME_PROPERTY = "spring.datasource.username";
    private static final String SPRING_DB_PASSWORD_PROPERTY = "spring.datasource.password";
    private static final PostgreSQLContainer<?> postgresSQLContainer;

    static {
        postgresSQLContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:11.6"))
                .withDatabaseName("test")
                .withUsername("postgres")
                .withPassword("postgres")
                .withExposedPorts(5432)
                .withStartupAttempts(1)
                .withReuse(true);

        postgresSQLContainer.start();
    }


    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add(SPRING_DB_URL_PROPERTY, postgresSQLContainer::getJdbcUrl);
        registry.add(SPRING_DB_PASSWORD_PROPERTY, postgresSQLContainer::getPassword);
        registry.add(SPRING_DB_USERNAME_PROPERTY, postgresSQLContainer::getUsername);
    }
}
