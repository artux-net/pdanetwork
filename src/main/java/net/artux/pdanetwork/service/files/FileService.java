package net.artux.pdanetwork.service.files;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface FileService {

  void reset();

  default String readFile(String path) throws IOException {
    return IOUtils.toString(new URL(path), StandardCharsets.UTF_8);
  }

}
