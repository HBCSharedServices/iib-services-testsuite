package com.qio.model.asset.helper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.model.asset.Asset;
import com.qio.model.assetType.AssetType;
import com.qio.model.assetType.helper.AssetTypeHelper;

import com.qio.model.tenant.Tenant;
import com.qio.model.tenant.helper.TenantHelper;


public class AssetHelper {
	Asset asset;
	private AssetTypeHelper assetTypeHelper;
	private AssetType responseAssetType;
	private String assetTypeId;
	
	private TenantHelper tenantHelper;
	private Tenant responseTenant;
	private String tenantId;
	
	
	// creates an asset that links to an assetType and tenant.
	public AssetHelper() {
		assetTypeId="";
		tenantId="";
		
		java.util.Date date= new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		asset = new Asset(timestamp, assetTypeId, tenantId);
	}
	
	public Asset getAssetCreateDependencies(String assetTypeFlavor)throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		//Create Asset Type
		assetTypeHelper = new AssetTypeHelper();
		responseAssetType = new AssetType();	
		responseAssetType = assetTypeHelper.createAssetType(assetTypeFlavor);
		
		String assetTypeHref=responseAssetType.get_links().getSelf().getHref();
					
		int i=assetTypeHref.lastIndexOf("/");
		int length=assetTypeHref.length();
		String assetTypeId=assetTypeHref.substring(i+1, length);
					
		//Create Tenant
		tenantHelper = new TenantHelper();
		responseTenant = new Tenant();
		responseTenant = tenantHelper.createTenant();
		
		asset.setAssetType(assetTypeId);
		asset.setTenant(responseTenant.getTenantId());
		return asset;
	}
	
	public Asset getAssetWithPredefinedAssetTypeAndTenant(String assetTypeId, String tenantId){
		asset.setAssetType(assetTypeId);
		asset.setTenant(tenantId);
		return asset;
	}
}
