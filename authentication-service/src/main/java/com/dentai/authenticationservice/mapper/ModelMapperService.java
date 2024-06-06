package com.dentai.authenticationservice.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ModelMapperService {

    Logger logger = org.slf4j.LoggerFactory.getLogger(ModelMapperService.class);
    private final ModelMapper modelMapper;

    /**
     * Constructs a new ModelMapperService with the specified ModelMapper instance.
     *
     * @param modelMapper the ModelMapper instance
     */
    public ModelMapperService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        logger.info("ModelMapperService: started with modelMapper | {}", modelMapper);
    }

    /**
     * Configures and returns the ModelMapper instance for handling request mapping.
     *
     * @return the ModelMapper instance for request mapping
     */
    public ModelMapper forRequest() {
        this.modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        logger.info("ModelMapperService: forRequest started with modelMapper | {}", modelMapper);
        return this.modelMapper;
    }

    /**
     * Configures and returns the ModelMapper instance for handling response mapping.
     *
     * @return the ModelMapper instance for response mapping
     */
    public ModelMapper forResponse() {
        this.modelMapper.getConfiguration()
                .setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        logger.info("ModelMapperService: forResponse started with modelMapper | {}", modelMapper);
        return this.modelMapper;
    }
}