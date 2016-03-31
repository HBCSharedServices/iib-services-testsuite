package com.qio.assetManagement.manageAssettypeAttributes;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MAssetTypeAPIHelper;
import com.qio.lib.assertions.CustomAssertions;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.exception.ServerResponse;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;
import com.qio.model.assetType.helper.AttributeDataType;


public class ManageAssetTypeAttributes {

	private BaseHelper baseHelper = new BaseHelper();
	private  MAssetTypeAPIHelper assetTypeAPI = new MAssetTypeAPIHelper();
	private String userName = "technician";
	private String password = "user@123";
	private String microservice = "asset-types";
	private String environment = ".qiotec.internal";
	private APIHeaders apiRequestHeaders = new APIHeaders(userName, password);
	private AssetTypeHelper assetTypeHelper;
	private AssetType requestAssetType;
	private AssetType responseAssetType;
	

	private final int FIRST_ELEMENT = 0;
	private final int SECOND_ELEMENT = 1;
	
	//JEET:
	//Could this be pulled out so it can be reused and if we need to change it, we change in one place?
	private String specialChars="~^%{&@}$#*()+=!~";
	
	@Before
	public void initTest(){
		// Initializing a new set of objects before each test case.
		assetTypeHelper = new AssetTypeHelper();
		requestAssetType = new AssetType();
		responseAssetType = new AssetType();
	}
	
	/*
	 * NEGATIVE TESTS START
	 */

	// RREHM-453 (AssetType with two Attributes with same abbreviation)
	@Test
	public void shouldNotCreateAssetTypeWhenTwoAttrsHaveSameAbbr() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		
		// Setting AssetType Attribute abbreviation of first attribute to the value of abbr of second attribute
		String abbrForSecondAttribute=requestAssetType.getAttributes().get(SECOND_ELEMENT).getAbbreviation();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation(abbrForSecondAttribute);
	
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation Should not Contain Duplicate Entries",
				serverResp);
	}
	
	
	// RREHM-451 (AssetType Attribute abbreviation contains special chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrContainsSpecialChars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		
		String defaultAbbr=requestAssetType.getAttributes().get(FIRST_ELEMENT).getAbbreviation();
		int count=specialChars.length();
		
		for (int i=0; i < count; i++) {
			requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation(specialChars.charAt(i)+defaultAbbr);
					
			ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

			CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Asset Type Attribute Abbreviation must not contain illegal characters", serverResp);
		}
	}
	
	// RREHM-446 (AssetType Attribute abbreviation contains spaces)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrContainsSpaces() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation("This Abbreviation contains spaces");
			
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation must not contain Spaces", serverResp);
	}
		
	// RREHM-450 (AssetType Attribute abbreviation is set to blank, i.e. abbreviation = "")
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsBlank() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation("");
		
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation Should not be Empty or Null",
				serverResp);
	}
	
	// RREHM-481 (AssetType Attribute abbreviation is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		//Example
		//requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(AttributeDataType.String);
	
		// Setting AssetType Attribute abbreviation to null, so that it is not sent in the request.
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation(null);
		
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"java.lang.NullPointerException",
				"No message available",
				serverResp);
	}
	
	// RREHM-443 (AssetType Attribute abbreviation is longer than 50 Chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrAbbrIsLongerThan50Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setAbbreviation("51charlong51charlong51charlong51charlong51charSlong");
		
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
		
		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute Abbreviation Should Less Than 50 Character",
				serverResp);
	}
	
	// RREHM-456 (AssetType Attribute name is set to blank, i.e. name = "")
	@Test
	public void shouldNotCreateAssetTypeWhenAttrNameIsBlank() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setName("");
		
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);

		CustomAssertions.assertServerError(500,
				"com.qiotec.application.exceptions.InvalidInputException",
				"Attribute name should not be empty or null",
				serverResp);
	}
	
	// RREHM-479 (AssetType Attribute name is removed from the request)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrNameIsNull() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		
		// Setting AssetType Attribute name to null, so that it is not sent in the request.
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setName(null);
			
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
			
		CustomAssertions.assertServerError(500, "java.lang.NullPointerException",
					"No message available", serverResp);
	}
	
	// RREHM-452 (AssetType Attribute name is longer than 255 Chars)
	@Test
	public void shouldNotCreateAssetTypeWhenAttrNameIsLongerThan255Chars() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithAllAttributes();
		requestAssetType.getAttributes().get(FIRST_ELEMENT).setName("256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256charactelong256characteRlong");
			
		ServerResponse serverResp = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, ServerResponse.class);
			
		CustomAssertions.assertServerError(500, "com.qiotec.application.exceptions.InvalidInputException",
					"Attribute Name should be less than 255 characters", serverResp);
	}
	/*
	 * NEGATIVE TESTS END
	 */
	
	/*
	 * POSITIVE TESTS START
	 */
	// RREHM-543 (AssetType with one Attribute of float data type)
	//@Test
	// Uncomment when ready with assertions
	public void shouldCreateAssetTypeWithUniqueAbbrWithOneAttrOfFloatDataType() throws JsonGenerationException, JsonMappingException, IOException{
		requestAssetType = assetTypeHelper.getAssetTypeWithOneAttribute(AttributeDataType.Float);

		int expectedRespCode = 201;
		
		responseAssetType = baseHelper.getServerResponseForInputRequest(requestAssetType, microservice, environment, apiRequestHeaders, assetTypeAPI, AssetType.class);
		
		System.out.println(responseAssetType.get_links().getSelf().getHref());
		System.out.println(responseAssetType.getAttributes().get(0).get_links().getSelf().getHref());
		
		//Uncomment after figuring out why they are different
		//assertEquals("Unexpected response code", requestAssetType, responseAssetType);
	}
}
