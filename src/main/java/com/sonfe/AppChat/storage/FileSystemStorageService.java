package com.sonfe.AppChat.storage;

import com.sonfe.AppChat.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

import static com.sonfe.AppChat.exception.ErrorCode.INIT_STORAGE_ERROR;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new AppException(INIT_STORAGE_ERROR);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        // check file size
        float fileSize = file.getSize() / 1_000_000.0f;
        if(fileSize > 5f) {
            throw new RuntimeException("Max file");
        }

        // rename file
        String originalFilename = file.getOriginalFilename();
        String generatedFileName = UUID.randomUUID() + "_" + originalFilename;

        // save
        Path destinationFile = this.rootLocation.resolve(
                        Paths.get(generatedFileName))
                .normalize().toAbsolutePath();
        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new RuntimeException(
                    "Cannot store file outside current directory.");
        }

        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

       return generatedFileName;
    }

    @Override
    public Stream<Path> loadAll() {
        return Stream.empty();
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) throws MalformedURLException {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException(
                        "Could not read file: " + filename);
            }
    }

    @Override
    public byte[] loadAsByte(String filename) throws IOException {
        Path file = load(filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return StreamUtils.copyToByteArray(resource.getInputStream());
        } else {
            throw new RuntimeException(
                    "Could not read file: " + filename);
        }
    }

    @Override
    public void deleteAll() {

    }
}
