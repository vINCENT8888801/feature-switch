package com.moneylion.techassesment.moneylion.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Feature {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "feature")
	private Set<FeatureAccess> featureAccesses;

	public Feature() {
	}

	public Feature(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<FeatureAccess> getFeatureAccesses() {
		return featureAccesses;
	}

	public void setFeatureAccesses(Set<FeatureAccess> featureAccesses) {
		this.featureAccesses = featureAccesses;
	}

	@Override
	public String toString() {
		return "Feature [id=" + id + ", name=" + name + "]";
	}

}
