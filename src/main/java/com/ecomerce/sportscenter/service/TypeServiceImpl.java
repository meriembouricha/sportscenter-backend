package com.ecomerce.sportscenter.service;

import com.ecomerce.sportscenter.entity.Type;
import com.ecomerce.sportscenter.model.TypeResponse;
import com.ecomerce.sportscenter.repository.TypeRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;

    public TypeServiceImpl(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    public List<TypeResponse> getAllTypes() {
       log.info("Fetching all Types !!");
       List<Type> typeList = typeRepository.findAll();
       List<TypeResponse> typeResponses = typeList.stream()
               .map(this::convertTotypeResponse)
               .collect(Collectors.toList());
        log.info("Fetched all Types !!");

        return typeResponses;
    }

    private TypeResponse convertTotypeResponse(Type type) {
        return TypeResponse.builder()
                .id(type.getId())
                .name(type.getName())
                .build();
    }


}
