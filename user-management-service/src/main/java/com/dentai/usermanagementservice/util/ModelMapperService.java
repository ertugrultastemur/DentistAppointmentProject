package com.dentai.usermanagementservice.util;

import com.dentai.usermanagementservice.dto.PatientDto;
import com.dentai.usermanagementservice.model.Patient;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.function.Consumer;

/**
 * Service class for configuring and providing a ModelMapper instance.
 */
@Service
public class ModelMapperService {

    private final ModelMapper modelMapper;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * Constructs a ModelMapperService with the specified ModelMapper instance.
     *
     * @param modelMapper the ModelMapper instance
     */
    public ModelMapperService(ModelMapper modelMapper){
        logger.info("ModelMapperService: ctor entered");
        this.modelMapper = modelMapper;
    }

    /**
     * Returns a ModelMapper instance configured for request mappings.
     *
     * @return a ModelMapper instance for request mappings
     */
    public ModelMapper forRequest() {
        logger.info("ModelMapperService: forRequest method entered");
        this.modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return this.modelMapper;
    }

    /**
     * Returns a ModelMapper instance configured for response mappings.
     *
     * @return a ModelMapper instance for response mappings
     */
    public ModelMapper forResponse() {
        logger.info("ModelMapperService: forResponse entered");
        this.modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return this.modelMapper;
    }

    public ModelMapper forRequestWithIgnore(Class<PatientDto> sourceType, Class<Patient> destinationType, Set<String> fieldsToIgnore) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STANDARD);

        TypeMap<PatientDto, Patient> typeMap = mapper.createTypeMap(sourceType, destinationType);
        typeMap.addMappings(new PropertyMap<PatientDto, Patient>() {
            @Override
            protected void configure() {
                for (String field : fieldsToIgnore) {
                    if ("id".equals(field)) {
                        map().setId(null);
                    }
                    if ("xRays".equals(field)) {
                        map().setId(null);
                    }
                }
            }
        });

        return mapper;
    }
}


