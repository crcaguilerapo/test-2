package org.crcaguilerapo.paymentPlatform.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.crcaguilerapo.paymentPlatform.domain.ports.out.IPaymentRepository;
import org.crcaguilerapo.paymentPlatform.domain.usecases.AntiFraudUsecase;
import org.crcaguilerapo.paymentPlatform.domain.usecases.PaymentUsecase;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.postgres.PaymentRepository;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.http.AntiFraudController;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.postgres.Seeders;
import org.crcaguilerapo.paymentPlatform.infrastructure.adapters.out.queue.BankController;
import org.crcaguilerapo.paymentPlatform.infrastructure.services.Serialization;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DependencyInjection {

    @Value("${database.url}")
    private String dataBaseUrl;

    @Value("${database.user}")
    private String dataBaseUser;

    @Value("${database.password}")
    private String dataBasePassword;

    @Value("${aws.sqs.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.sqs.secretAccessKey}")
    private String secretAccessKey;

    @Value("${aws.sqs.region}")
    private String region;

    @Value("${aws.sqs.url}")
    private String sqsUrl;

    @Value("${third-party.anti-fraud-service.url}")
    private String antiFraudUrl;

    @Value("${third-party.bank-service.url}")
    private String bankUrl;

    @Bean
    public IPaymentRepository paymentRepository(
            DSLContext ctx
    ) {
        return new PaymentRepository(ctx);
    }

    @Bean
    public AntiFraudUsecase antiFraudUsecase(AntiFraudController antiFraudController) {
        return new AntiFraudUsecase(antiFraudController);
    }

    @Bean
    public PaymentUsecase paymentExecutorUsecase(
            AntiFraudUsecase antiFraudUsecase,
            BankController bankController,
            IPaymentRepository paymentRepository
    ) {
        return new PaymentUsecase(
                antiFraudUsecase,
                bankController,
                paymentRepository
        );
    }

    @Bean
    public Serialization serialization(ObjectMapper objectMapper) {
        return new Serialization(objectMapper);
    }

    @Bean
    public DSLContext ctx() {
        try {
            Connection conn = DriverManager
                    .getConnection(
                            this.dataBaseUrl,
                            this.dataBaseUser,
                            this.dataBasePassword
                    );
            return  DSL.using(conn, SQLDialect.POSTGRES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SqsClient sqsClient() {
        var credentials = AwsBasicCredentials.create(
                accessKeyId,
                secretAccessKey
        );
        AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

        return SqsClient.builder()
                .credentialsProvider(credentialsProvider)
                .region(Region.of(region))
                .endpointOverride(URI.create(sqsUrl))
                .build();
    }

    @Bean
    public BankController bankController(
            SqsClient sqsClient,
            Serialization serialization
    ) {
        return new BankController(sqsClient, serialization, bankUrl);
    }

    @Bean
    public AntiFraudController antiFraudController(
            RestTemplate restTemplate,
            Serialization serialization
    ) {
        return new AntiFraudController(restTemplate, serialization, antiFraudUrl);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public Seeders seeder(DSLContext ctx) {
        Seeders seeders = new Seeders(ctx);
        seeders.start();
        return seeders;
    }
}
