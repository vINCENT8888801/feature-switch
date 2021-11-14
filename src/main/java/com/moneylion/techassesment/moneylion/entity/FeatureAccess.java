package com.moneylion.techassesment.moneylion.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class FeatureAccess {

	@EmbeddedId
	private FeatureAccessKey id;

	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@MapsId("featureId")
	@JoinColumn(name = "feature_id")
	private Feature feature;

	@Column(nullable = false)
	private boolean canAccess;

	public FeatureAccess() {
	}

	public FeatureAccess(User user, Feature feature, boolean canAccess) {
		this.id = new FeatureAccessKey(user.getId(), feature.getId());
		this.user = user;
		this.feature = feature;
		this.canAccess = canAccess;
	}

	public FeatureAccessKey getId() {
		return id;
	}

	public void setId(FeatureAccessKey id) {
		this.id = id;
	}

	public Feature getFeature() {
		return feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public boolean isCanAccess() {
		return canAccess;
	}

	public void setCanAccess(boolean canAccess) {
		this.canAccess = canAccess;
	}

	@Override
	public String toString() {
		return "FeatureAccess [user=" + user + ", feature=" + feature + ", canAccess=" + canAccess + "]";
	}

}
