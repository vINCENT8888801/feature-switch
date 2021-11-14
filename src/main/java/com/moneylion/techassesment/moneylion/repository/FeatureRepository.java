package com.moneylion.techassesment.moneylion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moneylion.techassesment.moneylion.entity.Feature;

public interface FeatureRepository extends JpaRepository<Feature, Long> {

	public Feature findByName(String name);

}
