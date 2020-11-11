package org.acme.resteasy.infrastructure.persistence.s3;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
public class S3RequestProvider {

    private final String bucketName;


    public S3RequestProvider(@ConfigProperty(name = "s3.bucket") String bucketName) {
        this.bucketName = bucketName;
    }

    public GetObjectRequest getObjectRequest(String objectKey) {
        return GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }

    public PutObjectRequest putObjectRequest(String objectKey) {
        return PutObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
    }
}
