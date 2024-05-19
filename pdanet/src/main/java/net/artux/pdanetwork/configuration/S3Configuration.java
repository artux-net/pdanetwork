package net.artux.pdanetwork.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Configuration {

    @Value("${s3.endpoint}")
    private String endpoint;

    @Value("${s3.region}")
    private String region;

    @Value("${s3.bucket.name}")
    private String bucketName;

    @Value("${s3.id}")
    private String id;

    @Value("${s3.key}")
    private String key;

    @Bean
    public AWSCredentials getCredentials() {
        return new AWSCredentials() {
            @Override
            public String getAWSAccessKeyId() {
                return id;
            }

            @Override
            public String getAWSSecretKey() {
                return key;
            }
        };
    }

    @Bean
    public AmazonS3 getAmazonS3(AWSCredentials credentials) {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSCredentialsProvider() {
                    @Override
                    public AWSCredentials getCredentials() {
                        return credentials;
                    }

                    @Override
                    public void refresh() {

                    }
                })
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .build();
    }

    public void createBucketBucket(AmazonS3 client) {
        if (client.doesBucketExist(bucketName)) {
            return;
        }
        client.createBucket(bucketName);
    }

}
