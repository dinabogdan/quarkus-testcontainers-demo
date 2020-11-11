package org.acme.resteasy.infrastructure.persistence.s3;

import java.io.IOException;
import java.util.Optional;

import javax.enterprise.context.Dependent;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.acme.resteasy.domain.model.Fruit;
import org.acme.resteasy.domain.repository.FruitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Dependent
public class S3FruitRepository implements FruitRepository {

    private static final Logger logger = LoggerFactory.getLogger(S3FruitRepository.class);

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;
    private final S3RequestProvider s3RequestProvider;

    public S3FruitRepository(S3Client s3Client, ObjectMapper objectMapper, S3RequestProvider s3RequestProvider) {
        this.s3Client = s3Client;
        this.objectMapper = objectMapper;
        this.s3RequestProvider = s3RequestProvider;
    }

    @Override
    public Optional<Fruit> fruitById(String id) {
        GetObjectRequest getObjectRequest = s3RequestProvider.getObjectRequest(fruitObjectPathKey(id));
        String object = s3Client.getObject(getObjectRequest, ResponseTransformer.toBytes()).asUtf8String();

        try {
            FruitDto fruitDto = objectMapper.readValue(object, FruitDto.class);
            return Optional.of(domainFruitFrom(fruitDto));
        } catch (IOException exception) {
            logger.error("The requested fruit could  not be retrieved");
        }
        return Optional.empty();
    }


    @Override
    public void saveNew(Fruit fruit) {
        PutObjectRequest putObjectRequest = s3RequestProvider.putObjectRequest(fruitObjectPathKey(fruit.id));
        FruitDto fruitDto = FruitDto.from(fruit);
        try {
            PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromString(objectMapper.writeValueAsString(fruitDto)));
            if (!response.sdkHttpResponse().isSuccessful()) {
                throw new RuntimeException("The fruit was not stored");
            }
        } catch (JsonProcessingException e) {
            logger.error("The fruit can't be stored");
        }
    }

    private Fruit domainFruitFrom(FruitDto fruitDto) {
        return new Fruit(fruitDto.id, fruitDto.name, fruitDto.color);
    }

    private String fruitObjectPathKey(String key) {
        return "fruits/" + key + ".json";

    }
}
