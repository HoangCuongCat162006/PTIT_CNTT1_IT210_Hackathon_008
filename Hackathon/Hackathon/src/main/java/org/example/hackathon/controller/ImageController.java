package org.example.hackathon.controller;

import org.example.hackathon.service.FileStorageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class ImageController {

    private final FileStorageService fileStorageService;

    public ImageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/images/{fileName}")
    public ResponseEntity<InputStreamResource> showImage(@PathVariable String fileName) throws IOException {
        Path path = fileStorageService.getFilePath(fileName);

        if (!Files.exists(path)) {
            return defaultImage();
        }

        MediaType mediaType = MediaType.IMAGE_JPEG;
        String lowerName = fileName.toLowerCase();

        if (lowerName.endsWith(".png")) {
            mediaType = MediaType.IMAGE_PNG;
        } else if (lowerName.endsWith(".jpg") || lowerName.endsWith(".jpeg")) {
            mediaType = MediaType.IMAGE_JPEG;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                .contentType(mediaType)
                .body(new InputStreamResource(Files.newInputStream(path)));
    }

    @GetMapping("/images/default")
    public ResponseEntity<InputStreamResource> defaultImage() {
        String svg = """
                <svg xmlns="http://www.w3.org/2000/svg" width="120" height="80">
                  <rect width="100%" height="100%" fill="#e9ecef"/>
                  <text x="50%" y="50%" dominant-baseline="middle" text-anchor="middle"
                        font-size="14" fill="#6c757d">No Image</text>
                </svg>
                """;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("image/svg+xml"))
                .body(new InputStreamResource(new ByteArrayInputStream(svg.getBytes())));
    }
}