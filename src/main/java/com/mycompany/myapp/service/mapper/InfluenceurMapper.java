package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Influenceur;
import com.mycompany.myapp.service.dto.InfluenceurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Influenceur} and its DTO {@link InfluenceurDTO}.
 */
@Mapper(componentModel = "spring")
public interface InfluenceurMapper extends EntityMapper<InfluenceurDTO, Influenceur> {}
