package yoonleeverse.onlinejudge.api.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import yoonleeverse.onlinejudge.api.common.constant.Constants.FileExtension;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
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
        boolean isCompressedFile = COMPRESSED_FILE_EXTENSION_LIST.stream()
                .anyMatch(e -> extension.equalsIgnoreCase(e.name()));
        Path targetPath = this.root.resolve(path);
        if (isCompressedFile) {
            unzipFile(file, targetPath);
            return;
        }

        boolean isImageFile = IMAGE_FILE_EXTENSION_LIST.stream()
                .anyMatch(e -> extension.equalsIgnoreCase(e.name()));
        if (isImageFile) {
            transferTo(file, targetPath);
        }
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

    private void unzipFile(MultipartFile multipartFile, Path targetPath) {
        File file = transferTo(multipartFile, targetPath);
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(file))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                boolean isDirectory = false;
                if (zipEntry.getName().endsWith(File.separator)) {
                    isDirectory = true;
                }
                Path newPath = zipSlipProtect(zipEntry, targetPath);
                if (isDirectory) {
                    Files.createDirectories(newPath);
                } else {
                    if (newPath.getParent() != null) {
                        if (Files.notExists(newPath.getParent())) {
                            Files.createDirectories(newPath.getParent());
                        }
                    }
                    Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file.exists()) {
            if (!file.delete()) {
                log.warn(String.format("압축파일 삭제를 실패하였습니다. targetPath=%s", targetPath));
            }
        }
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
