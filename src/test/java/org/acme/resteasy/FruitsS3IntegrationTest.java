package org.acme.resteasy;

import java.util.Optional;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.test.junit.QuarkusTest;
import org.acme.resteasy.domain.model.Fruit;
import org.acme.resteasy.infrastructure.persistence.s3.S3Config;
import org.acme.resteasy.infrastructure.persistence.s3.S3FruitRepository;
import org.acme.resteasy.infrastructure.persistence.s3.S3RequestProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

import static org.junit.Assert.assertTrue;

@QuarkusTest
@Testcontainers
public class FruitsS3IntegrationTest {

    @Inject
    public ObjectMapper objectMapper;

    @Inject
    public S3RequestProvider s3RequestProvider;


    private static final String ACCESS_KEY = "AKIAIOSFODNN7EXAMPLE";
    private static final String SECRET_KEY = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY";
    private static final int port = 9000;

    private static S3Config s3Config;
    private static S3Client s3Client;
    private static S3FruitRepository s3FruitRepository;

    @Container
    public static GenericContainer<?> s3 = new GenericContainer<>(DockerImageName.parse("minio/minio"))
            .withExposedPorts(port)
            .withEnv("MINIO_ACCESS_KEY", ACCESS_KEY)
            .withEnv("MINIO_SECRET_KEY", SECRET_KEY)
            .withCommand("server /data");

    @BeforeAll
    static void setUp() {
        s3.start();
        s3Config = new S3Config(ACCESS_KEY, SECRET_KEY, endpointUrl(s3), "integration-test-region");
        s3Client = s3Config.s3Client();
        s3Client.createBucket(CreateBucketRequest.builder().bucket("test-bucket").build());
    }

    @BeforeEach
    public void oneByOneSetup() {
        s3FruitRepository = new S3FruitRepository(s3Client, objectMapper, s3RequestProvider);
    }

    @AfterAll
    public static void tearDown() {
        s3.stop();
    }

    @Test
    public void test_uploadObject() {
        Fruit fruit = Fruit.apple();
        s3FruitRepository.saveNew(fruit);

        Optional<Fruit> retrievedFruit = s3FruitRepository.fruitById(fruit.id);
        assertTrue(retrievedFruit.isPresent());
    }

    private static String endpointUrl(GenericContainer<?> s3) {
        return "http://" + s3.getHost() + ":" + s3.getMappedPort(port);
    }
}