package com.doruk.restaurant.services;

import com.doruk.restaurant.domain.GeoLocation;
import com.doruk.restaurant.domain.entities.Address;

public interface GeoLocationService {
    GeoLocation geoLocate(Address address);
}
