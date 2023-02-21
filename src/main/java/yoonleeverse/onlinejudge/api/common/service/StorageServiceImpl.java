package yoonleeverse.onlinejudge.api.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.common.constant.Constants.FileExtension;
import yoonleeverse.onlinejudge.api.problem.entity.TestCase;
import yoonleeverse.onlinejudge.util.StringUtil;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static yoonleeverse.onlinejudge.api.common.constant.Constants.FileExtension.*;

@Service
@Slf4j
public class StorageServiceImpl implements StorageService {

    private final static List<FileExtension> COMPRESSED_FILE_EXTENSION_LIST = List.of(ZIP);
    private final static List<FileExtension> IMAGE_FILE_EXTENSION_LIST = List.of(PNG, JPG, JPEG);
    private Path root;

    @PostConstruct
    private void init() {
        Path currentPath = FileSystems.getDefault().getPath("").toAbsolutePath();
        this.root = currentPath.resolve("Storage");
        try {
            Files.createDirectories(this.root);
        } catch (IOException e) {
            log.warn("파일 업로드용 root 폴더 생성 실패");
        }
    }

    @Override
    public void store(MultipartFile file, String path) {
        if (file.isEmpty()) {
            throw new RuntimeException("저장할 파일이 존재하지 않습니다.");
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        Path targetPath = this.root.resolve(path);

        boolean isImageFile = IMAGE_FILE_EXTENSION_LIST.stream()
                .anyMatch(e -> extension.equalsIgnoreCase(e.name()));
        if (isImageFile) {
            transferTo(file, targetPath);
        }
    }

    @Override
    public List<TestCase> loadTestCase(MultipartFile file, String path) {
        if (file.isEmpty()) {
            throw new RuntimeException("저장할 파일이 존재하지 않습니다.");
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        boolean isCompressedFile = COMPRESSED_FILE_EXTENSION_LIST.stream()
                .anyMatch(e -> extension.equalsIgnoreCase(e.name()));
        Path targetPath = this.root.resolve(path);
        if (isCompressedFile) {
            return unzipTestCase(file, targetPath);
        }

        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    private File transferTo(MultipartFile file, Path targetPath) {
        String fileName = StringUtils.getFilename(file.getOriginalFilename());
        String filePath = targetPath + File.separator + fileName;
        File newFile = new File(filePath);
        File directory = new File(newFile.getParentFile().getAbsolutePath());
        directory.mkdirs();
        try {
            file.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("파일 저장을 실패하였습니다.");
        }

        return newFile;
    }

    private List<TestCase> unzipTestCase(MultipartFile multipartFile, Path targetPath) {
        Map<Integer, TestCase> testCaseMap = new HashMap<>();

        File file = transferTo(multipartFile, targetPath);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                Path newPath = zipSlipProtect(zipEntry, targetPath);
                String[] splitName = zipEntry.getName().split("\\.");
                if (zipEntry.isDirectory() || !newPath.getParent().toFile().exists() || splitName.length < 2) {
                    continue;
                }
                boolean isIn = splitName[1].equalsIgnoreCase("in");
                boolean isOut = splitName[1].equalsIgnoreCase("out");
                if (!isIn && !isOut) {
                    continue;
                }
                int id = Integer.parseInt(splitName[0]);
                String str = new String(zis.readAllBytes(), StandardCharsets.UTF_8);
                log.debug("[unzipTestCase] id[{}] str[{}] isIn[{}] isOut[{}]", id, str, isIn, isOut);
                TestCase testCase = testCaseMap.getOrDefault(id, new TestCase(id, null, null, null));
                if (isIn) {
                    testCase.setInput(str);
                } else if (isOut) {
                    testCase.setOutput(str);
                    testCase.setOutputMD5(StringUtil.encryptMD5(str));
                }
                testCaseMap.put(id, testCase);
            }
            zis.closeEntry();

            List<TestCase> testCases = testCaseMap.values().stream()
                    .sorted(Comparator.comparingInt(value -> value.getId()))
                    .collect(Collectors.toList());
            return testCases;
        } catch (Exception e) {
            log.debug("[unzipTestCase] Exception[{}]", e);
        } finally {
            try {
                FileSystemUtils.deleteRecursively(targetPath);
            } catch (Exception e) {
                log.warn(String.format("테스트케이스 폴더 삭제를 실패하였습니다. targetPath=%s", targetPath));
            }
        }

        return null;
    }

    private Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) throws IOException {
        Path targetDirResolved = targetDir.resolve(zipEntry.getName());

        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new IOException("Bad zip entry: " + zipEntry.getName());
        }
        return normalizePath;
    }
}
