package com.moneylion.techassesment.moneylion.service;

import com.moneylion.techassesment.moneylion.controller.UpdateUserFeatureAccessRequest;

public interface IUserFeatureService {

	/**
	 * @param email
	 * @param featureName
	 * @return User's access to feature 
	 */
	public boolean checkUserFeatureAccess(final String email, final String featureName);

	/**
	 * @param request
	 * @return true if featureAccess is updated or created otherwise false
	 */
	public boolean updateUserFeatureAccess(final UpdateUserFeatureAccessRequest request);

}
