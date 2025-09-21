package com.doruk.restaurant.restaurant.services.impl;

import com.doruk.restaurant.domain.entities.Photo;
import com.doruk.restaurant.services.StorageService;
import com.doruk.restaurant.services.impl.PhotoServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

public class PhotoServiceImplTest {

    @Test
    void uploadPhoto_shouldReturnPhotoWithUrlAndDate() {

        StorageService storageService = Mockito.mock(StorageService.class);

        PhotoServiceImpl photoService = new PhotoServiceImpl(storageService);

        MultipartFile fakeFile = Mockito.mock(MultipartFile.class);

        Mockito.when(storageService.store(any(MultipartFile.class), anyString()))
                .thenReturn("http://test.com/photo.jpg");

        Photo result = photoService.uploadPhoto(fakeFile);

        assertNotNull(result);
        assertEquals("http://test.com/photo.jpg", result.getUrl());
        assertNotNull(result.getUploadDate());
    }
}
