package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InfluenceurDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InfluenceurDTO.class);
        InfluenceurDTO influenceurDTO1 = new InfluenceurDTO();
        influenceurDTO1.setId(1L);
        InfluenceurDTO influenceurDTO2 = new InfluenceurDTO();
        assertThat(influenceurDTO1).isNotEqualTo(influenceurDTO2);
        influenceurDTO2.setId(influenceurDTO1.getId());
        assertThat(influenceurDTO1).isEqualTo(influenceurDTO2);
        influenceurDTO2.setId(2L);
        assertThat(influenceurDTO1).isNotEqualTo(influenceurDTO2);
        influenceurDTO1.setId(null);
        assertThat(influenceurDTO1).isNotEqualTo(influenceurDTO2);
    }
}
