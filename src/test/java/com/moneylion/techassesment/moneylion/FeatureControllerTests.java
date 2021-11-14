package com.moneylion.techassesment.moneylion;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.moneylion.techassesment.moneylion.controller.UpdateUserFeatureAccessRequest;
import com.moneylion.techassesment.moneylion.entity.Feature;
import com.moneylion.techassesment.moneylion.entity.FeatureAccess;
import com.moneylion.techassesment.moneylion.entity.User;
import com.moneylion.techassesment.moneylion.repository.FeatureAccessRepository;
import com.moneylion.techassesment.moneylion.repository.FeatureRepository;
import com.moneylion.techassesment.moneylion.repository.UserRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = MoneyLionTechAssesmentApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@AutoConfigureTestDatabase
class FeatureControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FeatureRepository featureRepository;

	@Autowired
	private FeatureAccessRepository featureAccessRepository;


	@AfterEach
	public void resetDb() {
		featureAccessRepository.deleteAll();
		userRepository.deleteAll();
		featureRepository.deleteAll();
	}

	@Test
	public void givenExistingUserFeatureAndFeatureAccessReturnCorrectResponse() throws Exception {
		// Generate Existing data
		User existingUser = new User("test@email.com");
		userRepository.save(existingUser);
		Feature existingFeature = new Feature("testFeature");
		featureRepository.save(existingFeature);
		FeatureAccess existingFeatureAccess = new FeatureAccess(existingUser, existingFeature, false);
		featureAccessRepository.save(existingFeatureAccess);

		mvc.perform(MockMvcRequestBuilders.get("/feature?email={email}&featureName={featureName}",
				existingUser.getEmail(), existingFeature.getName()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("canAccess", is(existingFeatureAccess.isCanAccess())));

	}

	@Test
	public void givenExitUserAndFeatureButFeatureAccessNotExistingReturnCorrectResponse() throws Exception {
		User existingUser = new User("test@email.com");
		userRepository.save(existingUser);
		Feature existingFeature = new Feature("testFeature1");
		featureRepository.save(existingFeature);

		mvc.perform(MockMvcRequestBuilders.get("/feature?email={email}&featureName={featureName}",
				existingUser.getEmail(), existingFeature.getName()).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("canAccess", is(false)));
	}

	@Test
	public void givenInexistingUserEmailShallReturn404() throws Exception {

		mvc.perform(MockMvcRequestBuilders
				.get("/feature?email={email}&featureName={featureName}", "idonotexist@email.com", "testFeature")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void givenInexistingFeatureNameShallReturn404() throws Exception {
		User existingUser = new User("test@email.com");
		userRepository.save(existingUser);

		mvc.perform(MockMvcRequestBuilders
				.get("/feature?email={email}&featureName={featureName}", "test@email.com", "featureNotExist")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError());
	}

	@Test
	public void post_validRequestThatRequireUpdate_shallReturn200() throws Exception {
		User existingUser = new User("test3@email.com");
		userRepository.save(existingUser);
		Feature existingFeature = new Feature("testFeature3");
		featureRepository.save(existingFeature);
		FeatureAccess existingFeatureAccess = new FeatureAccess(existingUser, existingFeature, false);
		featureAccessRepository.save(existingFeatureAccess);

		UpdateUserFeatureAccessRequest request = new UpdateUserFeatureAccessRequest();
		request.setEmail(existingUser.getEmail());
		request.setEnable(true);
		request.setFeatureName(existingFeature.getName());

		mvc.perform(MockMvcRequestBuilders.post("/feature").contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(request))).andExpect(status().isOk());
	}

	@Test
	public void post_validRequestThatDoesNotRequireUpdate_shallReturn304() throws Exception {
		User existingUser = new User("test3@email.com");
		userRepository.save(existingUser);
		Feature existingFeature = new Feature("testFeature3");
		featureRepository.save(existingFeature);
		FeatureAccess existingFeatureAccess = new FeatureAccess(existingUser, existingFeature, false);
		featureAccessRepository.save(existingFeatureAccess);

		UpdateUserFeatureAccessRequest request = new UpdateUserFeatureAccessRequest();
		request.setEmail(existingUser.getEmail());
		request.setEnable(existingFeatureAccess.isCanAccess());
		request.setFeatureName(existingFeature.getName());

		mvc.perform(MockMvcRequestBuilders.post("/feature").contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(request))).andExpect(status().isNotModified());
	}
	
	@Test
	public void post_validRequestFeatureAccessNotExisting_shallReturn200() throws Exception {
		User existingUser = new User("test3@email.com");
		userRepository.save(existingUser);
		Feature existingFeature = new Feature("testFeature3");
		featureRepository.save(existingFeature);

		UpdateUserFeatureAccessRequest request = new UpdateUserFeatureAccessRequest();
		request.setEmail(existingUser.getEmail());
		request.setEnable(false);
		request.setFeatureName(existingFeature.getName());

		mvc.perform(MockMvcRequestBuilders.post("/feature").contentType(MediaType.APPLICATION_JSON)
				.content(JsonUtil.toJson(request))).andExpect(status().isOk());
	}

}
