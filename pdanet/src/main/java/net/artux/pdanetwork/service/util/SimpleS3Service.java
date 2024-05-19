package net.artux.pdanetwork.service.util;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class SimpleS3Service<T> implements S3Service<T> {

    private final String bucketName;
    private final AmazonS3 client;
    private final ObjectMapper mapper;
    private final Class<T> tClass;
    private final Logger logger = LoggerFactory.getLogger(SimpleS3Service.class);

    protected SimpleS3Service(String bucketName, AmazonS3 client, ObjectMapper mapper, Class<T> tClass) {
        this.bucketName = bucketName;
        this.client = client;
        this.mapper = mapper;
        this.tClass = tClass;
    }

    @Override
    public boolean put(String key, T content) {
        try {
            client.putObject(bucketName, key, mapper.writeValueAsString(content));
            return true;
        } catch (Exception e) {
            logger.error("", e);
        }
        return false;
    }

    @Nullable
    @Override
    public T get(String key) {
        try {
            return mapper.readValue(client.getObjectAsString(bucketName, key), tClass);
        } catch (JsonProcessingException e) {
            logger.error("Error get", e);
            return null;
        }
    }

    public void delete(String key) {
        client.deleteObject(bucketName, key);
    }

    @Override
    public List<String> getEntries(String prefix) {
        return client.listObjects(bucketName, prefix).getObjectSummaries().stream().map(s -> s.getKey()).toList();
    }

}
