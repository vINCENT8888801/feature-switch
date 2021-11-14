package com.moneylion.techassesment.moneylion.service;

import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.moneylion.techassesment.moneylion.controller.UpdateUserFeatureAccessRequest;
import com.moneylion.techassesment.moneylion.entity.Feature;
import com.moneylion.techassesment.moneylion.entity.FeatureAccess;
import com.moneylion.techassesment.moneylion.entity.User;
import com.moneylion.techassesment.moneylion.repository.FeatureAccessRepository;
import com.moneylion.techassesment.moneylion.repository.FeatureRepository;
import com.moneylion.techassesment.moneylion.repository.UserRepository;

/**
 * @author WeiSeng
 *
 */
@Service
@Transactional
public class UserFeatureService implements IUserFeatureService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FeatureRepository featureRepository;

	@Autowired
	private FeatureAccessRepository featureAccessRepository;

	public boolean checkUserFeatureAccess(final String email, final String featureName) {

		final User user = getUserByEmail(email);
		final Feature feature = getFeatureByFeatureName(featureName);

		FeatureAccess featureAccess = featureAccessRepository.findByUserEmailAndFeatureName(email, featureName);
		if (Objects.isNull(featureAccess)) {
			featureAccess = new FeatureAccess(user, feature, false);
			featureAccessRepository.save(featureAccess);
			return false;
		} else {
			return featureAccess.isCanAccess();
		}
	}

	public boolean updateUserFeatureAccess(final UpdateUserFeatureAccessRequest request) {

		final User user = getUserByEmail(request.getEmail());
		final Feature feature = getFeatureByFeatureName(request.getFeatureName());

		FeatureAccess featureAccess = featureAccessRepository.findByUserEmailAndFeatureName(request.getEmail(),
				request.getFeatureName());
		if (Objects.isNull(featureAccess)) {
			featureAccess = new FeatureAccess(user, feature, request.isEnable());
			featureAccessRepository.save(featureAccess);
			return true;
		} else {
			if (featureAccess.isCanAccess() == request.isEnable()) {
				return false;
			} else {
				featureAccess.setCanAccess(request.isEnable());
				featureAccessRepository.save(featureAccess);
				return true;
			}
		}

	}

	private User getUserByEmail(final String email) {
		User user = userRepository.findByEmail(email);
		if (Objects.isNull(user)) {
			throw new ResourceNotFoundException("User not found");
		}
		return user;
	}

	private Feature getFeatureByFeatureName(final String featureName) {
		Feature feature = featureRepository.findByName(featureName);
		if (Objects.isNull(feature)) {
			throw new ResourceNotFoundException("Feature not found");
		}
		return feature;
	}

}
