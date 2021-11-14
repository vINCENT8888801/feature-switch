package com.moneylion.techassesment.moneylion.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.moneylion.techassesment.moneylion.service.IUserFeatureService;

@RestController
@RequestMapping("/feature")
public class FeatureController {

	@Autowired
	private IUserFeatureService userFeatureService;

	@GetMapping
	public ResponseEntity<UserFeatureAccessResponse> getUserFeatureAccess(@RequestParam("email") final String email,
			@RequestParam("featureName") final String featureName) {
		final boolean canAccess = userFeatureService.checkUserFeatureAccess(email, featureName);
		UserFeatureAccessResponse response = new UserFeatureAccessResponse(canAccess);

		return ResponseEntity.ok(response);
	}

	@PostMapping
	public ResponseEntity<Void> updateUserFeatureAccess(@RequestBody UpdateUserFeatureAccessRequest request) {

		final boolean isUpdated = userFeatureService.updateUserFeatureAccess(request);

		if (isUpdated) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_MODIFIED);
		}

	}

}
