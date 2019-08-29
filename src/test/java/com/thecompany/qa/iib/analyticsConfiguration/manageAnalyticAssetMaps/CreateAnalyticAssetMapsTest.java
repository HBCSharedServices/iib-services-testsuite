/**
 * © TheCompany QA 2019. All rights reserved.
 * CONFIDENTIAL AND PROPRIETARY INFORMATION OF TheCompany.
 */

/* package com.thecompany.qa.iib.analyticsConfiguration.manageAnalyticAssetMaps;

import com.thecompany.qa.iib.common.BaseTestSetupAndTearDown;
import com.thecompany.qa.lib.assertions.CustomAssertions;
import com.thecompany.qa.lib.iib.apiHelpers.analytics.MAnalyticAssetMapAPIHelper;
import com.thecompany.qa.lib.iib.common.AnalyticsUtil;
import com.thecompany.qa.lib.iib.common.AssetUtil;
import com.thecompany.qa.lib.iib.model.analyticAssetMap.AnalyticAssetMap;
import com.thecompany.qa.lib.iib.model.analyticAssetMap.AssetTemplateModelAttribute;
import com.thecompany.qa.lib.iib.model.analyticAssetMap.helper.AnalyticAssetMapHelper;
import com.thecompany.qa.lib.iib.model.asset.AssetResponse;
import com.thecompany.qa.lib.exception.ServerResponse;
import com.thecompany.qa.lib.common.MAbstractAPIHelper;
import com.thecompany.qa.lib.common.BaseHelper;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class CreateAnalyticAssetMapsTest extends BaseTestSetupAndTearDown {

	private static MAnalyticAssetMapAPIHelper analyticAssetMapAPI;
	private AnalyticAssetMapHelper analyticAssetMapHelper;
	private AnalyticAssetMap requestAnalyticAssetMap;
	private AnalyticAssetMap responseAnalyticAssetMap;
	private AssetResponse responseAsset;
	private static AssetUtil assetUtil;
	private ServerResponse serverResp;

	private static String assetId;
	private final int FIRST_ELEMENT = 0;

	final static Logger logger = Logger.getRootLogger();

	@BeforeClass
	public static void initSetupBeforeAllTests() {
		baseInitSetupBeforeAllTests("analytics");
		analyticAssetMapAPI = new MAnalyticAssetMapAPIHelper();
		// TO DO
		// WE want to create an asset here that goes against the assettype id we use for the analytic asset map
		// testing - BUT WE WANT TO HAVE ONLY A LINE OR TWO, NOT LOTS OF IMPORTS AND DECLARATIONS
		assetId = "56fca9d633c5721c670641ef";
	}

	@Before
	public void initSetupBeforeEveryTest() {
		analyticAssetMapHelper = new AnalyticAssetMapHelper();
		requestAnalyticAssetMap = new AnalyticAssetMap();
		responseAnalyticAssetMap = new AnalyticAssetMap();
		serverResp = new ServerResponse();
	}

	@AfterClass
	public static void cleanUpAfterAllTests() throws JsonGenerationException, JsonMappingException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, IOException {
		baseCleanUpAfterAllTests(analyticAssetMapAPI);
	}

	// I commented this out as I had deleted some of the files i was pointing out
	*/

	// The following test cases go here:
	// issuetype=Test and issue in (linkedIssues("RREHM-2202")) and issue in linkedIssues("RREHM-1832")

	/*
	 * NEGATIVE TESTS START
	 */
	/*
	// RREHM-2401 ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAssetIdIsNotExistentNonValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();

		requestAnalyticAssetMap.setAsset("NonExistentId");
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		// TODO: Response Body makes the below call fail: [{"logref":"error","message":"Invalid Asset id in the request","links":[]}]
		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		// CustomAssertions.assertServerError(404, "xxxx", "Invalid Asset id in the request", serverResp);
	}

	// RREHM-2400 ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAssetIdIsNotExistentValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();

		requestAnalyticAssetMap.setAsset("572bbadb13b38458022a33e6"); // Non existent but valid id format
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		// TODO: JEET: Response Body makes the below call fail: [{"logref":"error","message":"Invalid Asset id in the request","links":[]}]
		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		// CustomAssertions.assertServerError(404, "xxxx", "Invalid Asset id in the request", serverResp);
	}
	
	// RREHM-2399 ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticIdIsNotExistentValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();

		requestAnalyticAssetMap.setAsset(assetId); // Non existent but valid id format
		requestAnalyticAssetMap.setAnalytic("572bbadb13b38458022a33e6"); // Non existent but valid id format

		// TODO: JEET: Response Body makes the below call fail: [{"logref":"error","message":"Analytic 572bbadb13b38458022a33e6 not found.","links":[]}]
		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		// CustomAssertions.assertServerError(404, "xxxx", "Analytic 572bbadb13b38458022a33e6 not found.", serverResp);
	}
	
	// RREHM-2398 ()
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticIdIsNotExistentNonValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithNoAssetTemplateModelAttributeAndAnalyticInputParameters();

		requestAnalyticAssetMap.setAsset(assetId); // Non existent but valid id format
		requestAnalyticAssetMap.setAnalytic("NonExistentId"); // Non existent non valid id format

		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		CustomAssertions.assertServerError(500, "java.lang.IllegalArgumentException", "invalid ObjectId [NonExistentId]", serverResp);
	}
	
	// RREHM-2420 ()
	//BUG: RREHM-2417
	@Test
	public void shouldNotCreateAnalyticAssetMapWhenAssetTypeAttributeInAssetTemplateModelAttributeIsNotExistentButValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAssetTemplateModelAttributesWithAssetAttributeLink(AnalyticsUtil.analyticAttributesWithLinksMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
		String analyticAttribute = requestAnalyticAssetMap.getAssetTemplateModelAttributes().get(FIRST_ELEMENT).getAnalyticAttribute();
		requestAnalyticAssetMap.getAssetTemplateModelAttributes().get(FIRST_ELEMENT).setAssetTypeAttribute(analyticAttribute);
		
		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", "TODO", serverResp);
	}
	
	// RREHM-2421 ()
	//BUG: RREHM-2417
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticAttributeInAssetTemplateModelAttributeIsNotExistentButValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAssetTemplateModelAttributesWithAssetAttributeLink(AnalyticsUtil.analyticAttributesWithLinksMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
		String assetTypeAttribute = requestAnalyticAssetMap.getAssetTemplateModelAttributes().get(FIRST_ELEMENT).getAssetTypeAttribute();

		requestAnalyticAssetMap.getAssetTemplateModelAttributes().get(FIRST_ELEMENT).setAnalyticAttribute(assetTypeAttribute);
		
		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		CustomAssertions.assertServerError(400, "org.springframework.http.converter.HttpMessageNotReadableException", "TODO", serverResp);
	}
	
	// RREHM-2418 ()
	//BUG: RREHM-2417
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAnalyticInputInAnalyticInputParameterIsNotExistentButValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAnalyticInputParameters(AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
		String assetTypeParameter = requestAnalyticAssetMap.getAnalyticInputParameters().get(FIRST_ELEMENT).getAssetTypeParameter();
		requestAnalyticAssetMap.getAnalyticInputParameters().get(FIRST_ELEMENT).setAnalyticInput(assetTypeParameter);
		
		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		//CustomAssertions.assertServerError(400, "DONOTKNOWYET", "TODO", serverResp);
	}
	
	// RREHM-2419 ()
	//BUG: RREHM-2417
	@Ignore
	public void shouldNotCreateAnalyticAssetMapWhenAssetTypeParameterInAnalyticInputParameterIsNotExistentButValid() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAnalyticInputParameters(AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);
		String analyticParameter = requestAnalyticAssetMap.getAnalyticInputParameters().get(FIRST_ELEMENT).getAnalyticInput();
		requestAnalyticAssetMap.getAnalyticInputParameters().get(FIRST_ELEMENT).setAssetTypeParameter(analyticParameter);
		
		serverResp = MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, ServerResponse.class);
		//CustomAssertions.assertServerError(400, "DONOTKNOWYET", "TODO", serverResp);
	}
	
	// RREHM-2422 ()
	
	// RREHM-2423 ()
	// I commented this out as I had deleted some of the files i was pointing out

	*/
	
	/*
	 * NEGATIVE TESTS END
	 */
	
	
	/*
	 * POSITIVE TESTS START
	 */

	/*
	// RREHM-2396 ()
	@Ignore
	public void shouldCreateAnalyticAssetMapWithAnalyticInputParametersOnly() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAnalyticInputParameters(AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		responseAnalyticAssetMap = MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);

		String analyticAssetMapId = BaseHelper.getElementId(responseAnalyticAssetMap.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(analyticAssetMapId);

		AnalyticAssetMap committedAnalyticAssetMap = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, analyticAssetMapId, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);
		CustomAssertions.assertRequestAndResponseObj(responseAnalyticAssetMap, committedAnalyticAssetMap);
	}
	
	// RREHM-2397 ()
	@Ignore
	public void shouldCreateAnalyticAssetMapWithAssetTemplateModelAttributesOnly() {
		requestAnalyticAssetMap = analyticAssetMapHelper.getAnalyticAssetMapWithAssetTemplateModelAttributes(AnalyticsUtil.analyticAttributesWithoutLinks, AnalyticsUtil.analyticAttributesWithLinksMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		responseAnalyticAssetMap = MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap, microservice, environment, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);

		String analyticAssetMapId = BaseHelper.getElementId(responseAnalyticAssetMap.get_links().getSelfLink().getHref());
		idsForAllCreatedElements.add(analyticAssetMapId);

		AnalyticAssetMap committedAnalyticAssetMap = MAbstractAPIHelper.getResponseObjForRetrieve(microservice, environment, analyticAssetMapId, apiRequestHelper, analyticAssetMapAPI, AnalyticAssetMap.class);
		CustomAssertions.assertRequestAndResponseObj(responseAnalyticAssetMap, committedAnalyticAssetMap);
	}
	
	// RREHM-2229 ()
	@Ignore
	public void shouldCreateAnalyticAssetMapWithLinkedAndNotLinkedAttributesAndAnalyticInputParameters(){
		requestAnalyticAssetMap=analyticAssetMapHelper.getAssetTypeWithAllAttributesAndParameters(AnalyticsUtil.analyticAttributesWithoutLinks,AnalyticsUtil.analyticAttributesWithLinksMap,AnalyticsUtil.analyticInputsMap);

		requestAnalyticAssetMap.setAsset(assetId);
		requestAnalyticAssetMap.setAnalytic(AnalyticsUtil.analyticIdForAnalyticAssetMapTests);

		requestAnalyticAssetMap.setEnabled(null);
		//requestAnalyticAssetMap.setLifetimeStart("2014-03-01T14:30:01.000Z");
		requestAnalyticAssetMap.setLifetimeEnd("2033-01-01T04:30:01.000Z");

		//logger.info("AAA " + requestAnalyticAssetMap.getEnabled());
		List<AssetTemplateModelAttribute> assetTemplateModelAttribute=requestAnalyticAssetMap.getAssetTemplateModelAttributes();
		assetTemplateModelAttribute.get(0).setValue("0.5");
		requestAnalyticAssetMap.setAssetTemplateModelAttributes(assetTemplateModelAttribute);

		responseAnalyticAssetMap=MAbstractAPIHelper.getResponseObjForCreate(requestAnalyticAssetMap,microservice,environment,apiRequestHelper,analyticAssetMapAPI,AnalyticAssetMap.class);

		String analyticAssetMapId=BaseHelper.getElementId(responseAnalyticAssetMap.get_links().getSelfLink().getHref());
		//idsForAllCreatedElements.add(analyticAssetMapId);

		AnalyticAssetMap committedAnalyticAssetMap=MAbstractAPIHelper.getResponseObjForRetrieve(microservice,environment,analyticAssetMapId,apiRequestHelper,analyticAssetMapAPI,AnalyticAssetMap.class);
		CustomAssertions.assertRequestAndResponseObj(responseAnalyticAssetMap,committedAnalyticAssetMap);
		}

		// I commented this out as I had deleted some of the files i was pointing out

}
*/
