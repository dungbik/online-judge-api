package yoonleeverse.onlinejudge.api.common.service;

import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.problem.entity.TestCase;

import java.nio.file.Path;
import java.util.List;

public interface StorageService {

    void store(MultipartFile file, String path);

    List<TestCase> loadTestCase(MultipartFile file, String path);

    Path load(String filename);
}
