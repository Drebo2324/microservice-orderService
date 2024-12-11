package com.drebo.microservices.order.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        //convert String price in DTO to/from BigDecimal price in Entity
        modelMapper.typeMap(String.class, BigDecimal.class).setConverter(t -> new BigDecimal(t.getSource()));
        modelMapper.typeMap(BigDecimal.class, String.class).setConverter(t -> t.getSource().toString());
        return modelMapper;
    }
}
