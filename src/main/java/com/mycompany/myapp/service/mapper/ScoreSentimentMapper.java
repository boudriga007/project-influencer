package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Influenceur;
import com.mycompany.myapp.domain.ScoreSentiment;
import com.mycompany.myapp.service.dto.InfluenceurDTO;
import com.mycompany.myapp.service.dto.ScoreSentimentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ScoreSentiment} and its DTO {@link ScoreSentimentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScoreSentimentMapper extends EntityMapper<ScoreSentimentDTO, ScoreSentiment> {
    @Mapping(target = "influenceur", source = "influenceur", qualifiedByName = "influenceurId")
    ScoreSentimentDTO toDto(ScoreSentiment s);

    @Named("influenceurId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    InfluenceurDTO toDtoInfluenceurId(Influenceur influenceur);
}
