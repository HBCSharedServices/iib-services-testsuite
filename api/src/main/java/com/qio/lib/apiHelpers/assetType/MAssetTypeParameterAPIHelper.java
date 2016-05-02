package com.qio.lib.apiHelpers.assetType;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.connection.ConnectionResponse;

public class MAssetTypeParameterAPIHelper extends MAssetTypeAPIHelper {
	private final String getAllAssetTypeParametersEndpoint = "/parameters";
	private final String getSingleAssetTypeParameterEndpoint = "/parameters/{assetTypeParameterId}";

	public ConnectionResponse retrieve(String microservice, String environment, String assetTypeId, APIHeaders apiRequestHeaders) {
		return super.retrieve(microservice, environment, assetTypeId + getAllAssetTypeParametersEndpoint, apiRequestHeaders);
	}

	public ConnectionResponse retrieve(String microservice, String environment, String assetTypeId, String assetTypeParameterId,
			APIHeaders apiRequestHeaders) {
		return super.retrieve(microservice, environment, assetTypeId + replaceAssetTypeParameterIdInSingleAssetTypeParameterEndpoint(
				assetTypeParameterId), apiRequestHeaders);
	}

	private String replaceAssetTypeParameterIdInSingleAssetTypeParameterEndpoint(String assetTypeParameterId) {
		String singleAssetTypeParameterEndpoint = getSingleAssetTypeParameterEndpoint.replace("{assetTypeParameterId}", assetTypeParameterId);
		return singleAssetTypeParameterEndpoint;
	}
}
