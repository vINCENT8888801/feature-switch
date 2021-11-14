package com.moneylion.techassesment.moneylion.controller;

public class UserFeatureAccessResponse {

	private boolean canAccess;

	public UserFeatureAccessResponse(boolean canAccess) {
		this.canAccess = canAccess;
	}

	public boolean isCanAccess() {
		return canAccess;
	}

	public void setCanAccess(boolean canAccess) {
		this.canAccess = canAccess;
	}

}
