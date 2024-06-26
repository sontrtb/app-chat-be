package com.sonfe.AppChat.controller;

import com.sonfe.AppChat.dto.response.ApiResponse;
import com.sonfe.AppChat.storage.FileSystemStorageService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/file")
public class FileUploadController {
    @Autowired
    private FileSystemStorageService fileService;

    @PostMapping("/upload")
    public ApiResponse<String> uploadFile(@RequestParam("file") MultipartFile file) {
        return ApiResponse.<String>builder()
                .result(fileService.store(file))
                .build();
    }

    @GetMapping("/resource/{fileName}")
    public Resource getFileResource(@PathVariable String fileName) throws MalformedURLException {
        return fileService.loadAsResource(fileName);
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) throws IOException {
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(fileService.loadAsByte(fileName));
    }
}
