package com.doruk.restaurant.restaurant.services.impl;

import com.doruk.restaurant.domain.GeoLocation;
import com.doruk.restaurant.domain.RestaurantCreateUpdateRequest;
import com.doruk.restaurant.domain.entities.*;
import com.doruk.restaurant.repositories.RestaurantRepository;
import com.doruk.restaurant.services.GeoLocationService;
import com.doruk.restaurant.services.impl.RestaurantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

    @Mock RestaurantRepository restaurantRepository;
    @Mock GeoLocationService geoLocationService;

    @Test
    void createRestaurant_shouldBuildsAndSavesEntity() {
        // --- arrange: request ---
        Address address = Address.builder()
                .streetNumber("221B")
                .streetName("Baker Street")
                .unit("Flat 1")
                .city("London")
                .state("Greater London")
                .postalCode("NW1")
                .country("UK")
                .build();

        TimeRange weekday = TimeRange.builder().openTime("09:00").closeTime("18:00").build();
        TimeRange weekend = TimeRange.builder().openTime("10:00").closeTime("16:00").build();

        OperatingHours hours = OperatingHours.builder()
                .monday(weekday).tuesday(weekday).wednesday(weekday)
                .thursday(weekday).friday(weekday).saturday(weekend)
                .sunday(null).build();

        List<String> photoUrls = List.of("https://cdn.example/p1.jpg", "https://cdn.example/p2.jpg");

        RestaurantCreateUpdateRequest request = RestaurantCreateUpdateRequest.builder()
                .name("Test Restaurant")
                .cuisineType("Turkish")
                .contactInformation("+90 555 111 22 33")
                .address(address)
                .operatingHours(hours)
                .photoIds(photoUrls)
                .build();


        when(geoLocationService.geoLocate(address))
                .thenReturn(GeoLocation.builder().latitude(51.50).longitude(-0.12).build());

        when(restaurantRepository.save(any(Restaurant.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        RestaurantServiceImpl service =
                new RestaurantServiceImpl(restaurantRepository, geoLocationService);


        // --- act ---
        Restaurant result = service.createRestaurant(request);


        // --- assert ---
        ArgumentCaptor<Restaurant> captor = ArgumentCaptor.forClass(Restaurant.class);
        verify(restaurantRepository, times(1)).save(captor.capture());
        Restaurant saved = captor.getValue();

        assertThat(saved.getName()).isEqualTo("Test Restaurant");
        assertThat(saved.getCuisineType()).isEqualTo("Turkish");
        assertThat(saved.getContactInformation()).isEqualTo("+90 555 111 22 33");
        assertThat(saved.getAddress()).isEqualTo(address);
        assertThat(saved.getOperatingHours()).isEqualTo(hours);
        assertThat(saved.getGeoLocation()).isNotNull();
        assertThat(saved.getAverageRating()).isEqualTo(0f);
        assertThat(saved.getPhotos().stream().map(Photo::getUrl).toList())
                .containsExactlyElementsOf(photoUrls);

        assertThat(result.getName()).isEqualTo(saved.getName());
    }
}