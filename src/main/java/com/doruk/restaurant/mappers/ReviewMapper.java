package com.doruk.restaurant.mappers;

import com.doruk.restaurant.domain.ReviewCreateUpdateRequest;
import com.doruk.restaurant.domain.dtos.ReviewCreateUpdateRequestDto;
import com.doruk.restaurant.domain.dtos.ReviewDto;
import com.doruk.restaurant.domain.entities.Review;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDto dto);

    ReviewDto toDto(Review review);
}
