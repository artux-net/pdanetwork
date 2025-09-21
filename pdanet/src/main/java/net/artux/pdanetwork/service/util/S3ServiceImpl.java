package net.artux.pdanetwork.service.util;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service<String> {

    @Value("${s3.bucket.name}")
    private String bucketName;
    private final AmazonS3 client;

    @Override
    public boolean put(String key, String content) {
        try {
            client.putObject(bucketName, key, content);
            return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    @Override
    public String get(String key) {
        return client.getObjectAsString(bucketName, key);
    }

    @Override
    public void delete(String key) {
        client.deleteObject(bucketName, key);
    }

    @Override
    public List<String> getEntries(String prefix) {
        return client.listObjects(bucketName, prefix)
                .getObjectSummaries()
                .stream()
                .map(s -> s.getKey())
                .toList();
    }

}
