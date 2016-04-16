package com.qio.model.tenant.helper;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import com.qio.lib.apiHelpers.APIHeaders;
import com.qio.lib.apiHelpers.MTenantAPIHelper;
import com.qio.lib.common.BaseHelper;
import com.qio.lib.common.Microservice;
import com.qio.model.tenant.Tenant;
import com.qio.model.tenant.helper.TenantHelper;
import com.qio.model.tenant.Tenant;
import com.qio.testHelper.TestHelper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;


public class TenantHelper {
	Tenant tenant;
	
	private BaseHelper baseHelper = new BaseHelper();
	private  MTenantAPIHelper tenantAPI = new MTenantAPIHelper();
	private static String userName;
	private static String password;
	private static String microservice;
	private static String environment;
	private static APIHeaders apiRequestHeaders;
	private TenantHelper tenantHelper;
	private Tenant requestTenant;
	private Tenant responseTenant;
	
	// creates a tenant with default values for its properties.
	public TenantHelper(){
		java.util.Date date= new java.util.Date();
		String timestamp = Long.toString(date.getTime());
		tenant = new Tenant(timestamp);
	}
	
	/*
	 * Tenant object 
	 */

	public Tenant getTenant(){
		return tenant;
	}
	
	// This method will create an AssetType that can be then used to create an asset
	public Tenant createTenant() throws JsonGenerationException, JsonMappingException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException{
		Config userConfig = ConfigFactory.load("user_creds.conf");
		Config envConfig = ConfigFactory.load("environments.conf");
			
		userName = userConfig.getString("user.username");
		password = userConfig.getString("user.password");
		environment = envConfig.getString("env.name");
		apiRequestHeaders = new APIHeaders(userName, password);
		microservice = Microservice.TENANT.toString();
			
		tenantHelper = new TenantHelper();
		requestTenant = new Tenant();
		responseTenant = new Tenant();
		requestTenant = tenantHelper.getTenant();
			
		responseTenant = TestHelper.getResponseObjForCreate(baseHelper, requestTenant, microservice, environment, apiRequestHeaders, tenantAPI, Tenant.class);
		return responseTenant;
	}
}
