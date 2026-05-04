package org.example.application.mapper;

import lombok.RequiredArgsConstructor;
import org.example.application.dto.car.CarSearchResponseDto;
import org.example.domain.car.Car;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarMapper {
    private final ModelMapper modelMapper;

    public CarSearchResponseDto toDto(Car car) {
        return modelMapper.map(car, CarSearchResponseDto.class);
    }
}
