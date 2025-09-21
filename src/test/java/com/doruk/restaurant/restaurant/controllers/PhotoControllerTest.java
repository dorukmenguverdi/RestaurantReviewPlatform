package com.doruk.restaurant.restaurant.controllers;

import com.doruk.restaurant.controllers.PhotoController;
import com.doruk.restaurant.domain.dtos.PhotoDto;
import com.doruk.restaurant.domain.entities.Photo;
import com.doruk.restaurant.mappers.PhotoMapper;
import com.doruk.restaurant.services.PhotoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PhotoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PhotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean private PhotoService photoService;
    @MockBean private PhotoMapper photoMapper;

    @Test
    void uploadPhoto_shouldReturnDtoJson() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "demo.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "fake-bytes".getBytes()
        );

        LocalDateTime fixedTime = LocalDateTime.of(2025,1,1,12,0);

        Photo photo = Photo.builder()
                .url("https://cdn.example/demo.jpg")
                .uploadDate(fixedTime)
                .build();


        PhotoDto dto = PhotoDto.builder()
                .url(photo.getUrl())
                .uploadDate(photo.getUploadDate())
                .build();

        Mockito.when(photoService.uploadPhoto(any())).thenReturn(photo);
        Mockito.when(photoMapper.toDto(photo)).thenReturn(dto);

        mockMvc.perform(multipart("/api/photos").file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.url").value("https://cdn.example/demo.jpg"));
    }
}
