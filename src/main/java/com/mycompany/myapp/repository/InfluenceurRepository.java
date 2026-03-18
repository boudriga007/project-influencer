package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Influenceur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Influenceur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InfluenceurRepository extends JpaRepository<Influenceur, Long>, JpaSpecificationExecutor<Influenceur> {}
