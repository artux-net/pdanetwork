package net.artux.pdanetwork;

import net.artux.pdanetwork.service.util.S3Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class S3Test {

    @Autowired
    private S3Service s3Service;

    @Test
    public void getStories() {
        System.out.println(s3Service.getEntries("story-"));
    }

}