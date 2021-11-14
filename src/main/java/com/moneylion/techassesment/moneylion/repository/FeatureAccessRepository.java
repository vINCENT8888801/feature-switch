package com.moneylion.techassesment.moneylion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moneylion.techassesment.moneylion.entity.FeatureAccess;

public interface FeatureAccessRepository extends JpaRepository<FeatureAccess, Long>{

	@Query("SELECT fa FROM FeatureAccess fa "
			+ "INNER JOIN User u ON fa.id.userId = u.id "
			+ "INNER JOIN Feature f ON fa.id.featureId = u.id "
			+ "WHERE u.email = (:email)"
			+ "AND f.name =(:featureName)")
	FeatureAccess findByUserEmailAndFeatureName(@Param("email")String email,@Param("featureName")String featureName);

}
