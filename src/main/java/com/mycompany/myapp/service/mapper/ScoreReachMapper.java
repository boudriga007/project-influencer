package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Influenceur;
import com.mycompany.myapp.domain.ScoreReach;
import com.mycompany.myapp.service.dto.InfluenceurDTO;
import com.mycompany.myapp.service.dto.ScoreReachDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ScoreReach} and its DTO {@link ScoreReachDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScoreReachMapper extends EntityMapper<ScoreReachDTO, ScoreReach> {
    @Mapping(target = "influenceur", source = "influenceur", qualifiedByName = "influenceurId")
    ScoreReachDTO toDto(ScoreReach s);

    @Named("influenceurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InfluenceurDTO toDtoInfluenceurId(Influenceur influenceur);
}
