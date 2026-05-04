package org.example.infrastructure.config;

import org.example.application.dto.car.CarSearchResponseDto;
import org.example.domain.car.Car;
import org.example.domain.car.Model;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.record.RecordModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.registerModule(new RecordModule());
        mapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true);

        TypeMap<Car, CarSearchResponseDto> typeMap = mapper.createTypeMap(Car.class, CarSearchResponseDto.class);
        typeMap.setConverter(context -> {
            Car car = context.getSource();
            Model model = car.getModel();

            List<String> componentNames = car.getComponents().values().stream()
                    .map(component -> component.type().name())
                    .toList();

            return new CarSearchResponseDto(
                    car.getId().value().toString(),
                    model.brand(),
                    model.name(),
                    model.brand() + " " + model.name(),
                    car.totalPrice().getAmountInCents(),
                    car.isAvailable(),
                    model.year().getValue(),
                    model.engineType().name(),
                    model.bodyType().name(),
                    componentNames
            );
        });

        return mapper;
    }
}
