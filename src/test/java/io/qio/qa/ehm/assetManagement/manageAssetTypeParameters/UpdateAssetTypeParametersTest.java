/**
 * © Qio Technologies Ltd. 2016. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF QIO TECHNOLOGIES LTD.
 */
package io.qio.qa.ehm.assetManagement.manageAssetTypeParameters;

import java.util.ArrayList;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import io.qio.qa.ehm.common.BaseTestSetupAndTearDown;
import io.qio.qa.lib.ehm.apiHelpers.assetType.MAssetTypeAPIHelper;
import io.qio.qa.lib.ehm.model.assetType.AssetType;
import io.qio.qa.lib.ehm.model.assetType.AssetTypeParameter;
import io.qio.qa.lib.ehm.model.assetType.helper.AssetTypeHelper;
import io.qio.qa.lib.ehm.model.assetType.helper.ParameterDataType;
import io.qio.qa.lib.ehm.common.APITestUtil;
import io.qio.qa.lib.assertions.CustomAssertions;
import io.qio.qa.lib.exception.ServerResponse;

public class UpdateAssetTypeParametersTest extends BaseTestSetupAndTearDown {

	private static MAssetTypeAPIHelper assetTypeAPI;
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	private String assetTypeId;
	private String assetTypeParameterId;
	private ServerResponse serverResp;

	private final int FIRST_ELEMENT = 0;

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("asset");
		assetTypeAPI = new MAssetTypeAPIHelper();
	}

	@Before
	public void initSetupBeforeEveryTest() {
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = assetTypeHelper.getAssetTypeWithOneParameter(ParameterDataType.String);
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		assetTypeId = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		assetTypeParameterId = APITestUtil.getElementId(responseAssetType.getParameters().get(FIRST_ELEMENT).get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeId);
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() {
		baseCleanUpAfterAllTests(assetTypeAPI);
	}

	private String createAssetTypeWithAllParametersOnly() {
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeIdLocal = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeIdLocal);
		return assetTypeIdLocal;
	}

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-1192")) and issue in linkedIssues("RREHM-55")

	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-1085 ()
	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenAbbrHasSpaces() {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(APITestUtil.getCurrentTimeStamp() + " This has spaces");

		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation must not contain Spaces", serverResp);
	}

	// RREHM-1093 ()
	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenAbbrLongerThan255Chars() {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(APITestUtil.TWOFIFTYSIX_CHARS);

		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should Less Than 255 Character", serverResp);
	}

	// RREHM-935 ()
	@Test
	public void shouldNotBeAllowedToUpdateParametersWhenTheyHaveAbbrSimilarToExistingParameterAbbr() {

		// Creating an AssetType with all parameters with one parameter having
		// abbreviation as TRYTODUPLICATEME
		requestAssetType = assetTypeHelper.getAssetTypeWithAllParameters();
		List<AssetTypeParameter> existingAssetTypeParmaeters = requestAssetType.getParameters();
		for (AssetTypeParameter assetTypeParameter : existingAssetTypeParmaeters) {
			if (assetTypeParameter.getDatatype().equals(ParameterDataType.String.toString()))
				assetTypeParameter.setAbbreviation("TRYTODUPLICATEME");
		}
		requestAssetType.setParameters(existingAssetTypeParmaeters);
		responseAssetType = APITestUtil.getResponseObjForCreate(requestAssetType, microservice, environment, apiRequestHelper, assetTypeAPI, AssetType.class);
		String assetTypeIdLocal = APITestUtil.getElementId(responseAssetType.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(assetTypeIdLocal);

		// Preparing the AssetTypeParameter for the update request
		List<AssetTypeParameter> updateAssetTypeParameters = new ArrayList<AssetTypeParameter>();
		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			if (!responseAssetTypeParameter.getDatatype().equals(ParameterDataType.String.toString())) {
				responseAssetTypeParameter.setId(APITestUtil.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
				responseAssetTypeParameter.set_links(null);
				responseAssetTypeParameter.setAbbreviation("TRYTODUPLICATEME");
				updateAssetTypeParameters.add(responseAssetTypeParameter);
			}
		}

		// Setting up the update request
		requestAssetType.setParameters(updateAssetTypeParameters);
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	// RREHM-934 ()
	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenItsAbbrIsBlank() {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("");

		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should not be Empty or Null", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenItsAbbrIsNull() {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation(null);

		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "java.lang.NullPointerException", "No message available", serverResp);
	}

	// RREHM-933 ()
	@Test
	public void shouldNotBeAllowedToUpdateTwoOrMoreParametersWithSameAbbrValue() {

		String assetTypeIdLocal = createAssetTypeWithAllParametersOnly();

		// Preparing the AssetTypeParameter for the update request
		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			responseAssetTypeParameter.setId(APITestUtil.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
			responseAssetTypeParameter.set_links(null);
		}
		responseAssetType.getParameters().get(FIRST_ELEMENT).setAbbreviation("NEWDUPLICATE");
		responseAssetType.getParameters().get(FIRST_ELEMENT + 1).setAbbreviation("NEWDUPLICATE");

		// Setting up the update request
		requestAssetType.setParameters(responseAssetType.getParameters());
		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter Abbreviation Should not Contain Duplicate Entries", serverResp);
	}

	// RREHM-923 ()
	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenBaseuomIsBlank() {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setBaseuom("");

		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter BaseUom Should not be Empty or Null", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenBaseuomIsNull() {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setBaseuom(null);

		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "com.qiotec.application.exceptions.InvalidInputException", "Parameter BaseUom Should not be Empty or Null", serverResp);
	}

	// RREHM-1084 ()
	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenDatatypeIsInvalid() {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setDatatype("FicticiousDataType");

		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	@Test
	public void shouldNotBeAllowedToUpdateParameterWhenDatatypeIsBlank() {

		requestAssetType.getParameters().get(FIRST_ELEMENT).setId(assetTypeParameterId);
		requestAssetType.getParameters().get(FIRST_ELEMENT).setDatatype("");

		serverResp = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeId, apiRequestHelper, assetTypeAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", serverResp);
	}

	/*
	 * NEGATIVE TESTS END
	 */

	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-920 ()
	@Test
	public void shouldBeAllowedToUpdateParameterWhenAbbrValueIsUniqueWithinTheScopeOfSameAssetType() {

		String assetTypeIdLocal = createAssetTypeWithAllParametersOnly();

		// Preparing the AssetTypeParameter for the update request
		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			responseAssetTypeParameter.setAbbreviation(responseAssetTypeParameter.getAbbreviation() + "NEW");
			responseAssetTypeParameter.setId(APITestUtil.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
			responseAssetTypeParameter.set_links(null);
		}

		// Setting up the update request
		requestAssetType.setParameters(responseAssetType.getParameters());

		responseAssetType = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(200, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		AssetType updatedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeIdLocal, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
	}

	// RREHM-918 ()
	@Test
	public void shouldBeAllowedToUpdateParameterWithValidValuesForDescriptionBaseuomAndDatatype() {

		String assetTypeIdLocal = createAssetTypeWithAllParametersOnly();

		// Preparing the AssetTypeParameter for the update request
		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			responseAssetTypeParameter.setBaseuom(responseAssetTypeParameter.getBaseuom() + "NEW");
			responseAssetTypeParameter.setDescription(responseAssetTypeParameter.getDescription() + "NEW");
			responseAssetTypeParameter.setDatatype(ParameterDataType.Integer.toString());
			responseAssetTypeParameter.setId(APITestUtil.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
			responseAssetTypeParameter.set_links(null);
		}

		// Setting up the update request
		requestAssetType.setParameters(responseAssetType.getParameters());

		responseAssetType = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(200, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		AssetType updatedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeIdLocal, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
	}

	// RREHM-1076 ()
	@Test
	public void shouldBeAllowedToUpdateParameterWithSpecialCharsInItsAbbr() {

		for (char specialChar : APITestUtil.SPECIAL_CHARS.toCharArray()) {
			String assetTypeIdLocal = createAssetTypeWithAllParametersOnly();

			// Preparing the AssetTypeParameter for the update request
			for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
				responseAssetTypeParameter.setAbbreviation(specialChar + responseAssetTypeParameter.getAbbreviation());
				responseAssetTypeParameter.setId(APITestUtil.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
				responseAssetTypeParameter.set_links(null);
			}

			// Setting up the update request
			requestAssetType.setParameters(responseAssetType.getParameters());

			responseAssetType = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHelper, assetTypeAPI, AssetType.class);
			CustomAssertions.assertRequestAndResponseObj(200, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

			AssetType updatedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeIdLocal, apiRequestHelper, assetTypeAPI, AssetType.class);
			CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
		}
	}

	// RREHM-1615 ()
	@Test
	public void shouldBeAllowedToUpdateParameterWithBlankDescription() {

		String assetTypeIdLocal = createAssetTypeWithAllParametersOnly();

		// Preparing the AssetTypeParameter for the update request
		for (AssetTypeParameter responseAssetTypeParameter : responseAssetType.getParameters()) {
			responseAssetTypeParameter.setDescription("");
			responseAssetTypeParameter.setId(APITestUtil.getElementId(responseAssetTypeParameter.get_links().getSelfLink().getHref()));
			responseAssetTypeParameter.set_links(null);
		}

		// Setting up the update request
		requestAssetType.setParameters(responseAssetType.getParameters());

		responseAssetType = APITestUtil.getResponseObjForUpdate(requestAssetType, microservice, environment, assetTypeIdLocal, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(200, APITestUtil.responseCodeForInputRequest, requestAssetType, responseAssetType);

		AssetType updatedAssetType = APITestUtil.getResponseObjForRetrieve(microservice, environment, assetTypeIdLocal, apiRequestHelper, assetTypeAPI, AssetType.class);
		CustomAssertions.assertRequestAndResponseObj(responseAssetType, updatedAssetType);
	}

	/*
	 * POSITIVE TESTS END
	 */

}
