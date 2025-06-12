package com.learningsystem.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MediaStorageService {

    private final Map<Long, String> fileStorage = new HashMap<>();
    private final Map<Long, String> fileNames = new HashMap<>(); 
    private final AtomicLong fileIdGenerator = new AtomicLong(1);

    public String uploadFile(MultipartFile file) {
        try {
            Long fileId = fileIdGenerator.getAndIncrement();
            String fileContent = new String(file.getBytes()); 
            fileStorage.put(fileId, fileContent);
            fileNames.put(fileId, file.getOriginalFilename());
            return "File uploaded successfully with ID: " + fileId + " (Name: " + file.getOriginalFilename() + ")";
        } catch (Exception ex) {
            throw new RuntimeException("File upload failed", ex);
        }
    }

    public String getFileContent(Long fileId) {
        if (!fileStorage.containsKey(fileId)) 
            throw new RuntimeException("File not found");
        return fileStorage.get(fileId);
    }

    public String getFileName(Long fileId) {
        if (!fileNames.containsKey(fileId)) 
            throw new RuntimeException("File name not found");
        return fileNames.get(fileId);
    }

    public void deleteFile(Long fileId) {
        if (!fileStorage.containsKey(fileId)) 
            throw new RuntimeException("File not found");
        fileStorage.remove(fileId);
        fileNames.remove(fileId);
    }
}
