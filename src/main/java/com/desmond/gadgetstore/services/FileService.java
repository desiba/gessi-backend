package com.desmond.gadgetstore.services;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void saveImage(MultipartFile file);
}
