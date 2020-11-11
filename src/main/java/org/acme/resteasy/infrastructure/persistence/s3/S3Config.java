package org.acme.resteasy.infrastructure.persistence.s3;

import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@ApplicationScoped
public class S3Config {

    private final String accessKeyId;
    private final String secretAccessKey;
    private final String endpointUrl;
    private final String regionName;

    public S3Config(@ConfigProperty(name = "s3.accessKeyId") String accessKeyId,
                    @ConfigProperty(name = "s3.secretAccessKey") String secretAccessKey,
                    @ConfigProperty(name = "s3.endpointUrl") String endpointUrl,
                    @ConfigProperty(name = "s3.regionName") String regionName) {
        this.accessKeyId = accessKeyId;
        this.secretAccessKey = secretAccessKey;
        this.endpointUrl = endpointUrl;
        this.regionName = regionName;
    }

    @Produces
    public S3Client s3Client() {
        return S3Client.builder()
                .credentialsProvider(credentialsProvider())
                .region(Region.of(regionName))
                .endpointOverride(s3Endpoint())
                .build();
    }

    @Produces
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    private AwsCredentialsProvider credentialsProvider() {
        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey));
    }

    private URI s3Endpoint() {
        return URI.create(endpointUrl);
    }
}
