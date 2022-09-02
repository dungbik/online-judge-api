package yoonleeverse.onlinejudge.api.common.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface StorageService {

    void store(MultipartFile file, String path);

    Path load(String filename);
}
