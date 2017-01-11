/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package crittercism;

import android.app.Activity;

import org.json.JSONObject;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import com.crittercism.app.Crittercism;
import com.crittercism.app.CritterCallback;
import com.crittercism.app.CritterUserData;
import com.crittercism.app.CritterUserDataRequest;
import com.crittercism.app.CrittercismConfig;

@Kroll.module(name="CrittercismModule", id="crittercism")
public class CrittercismModuleModule extends KrollModule
{

	private static final String LCAT = "[CrittercismModule]";
	private static final boolean DBG = TiConfig.LOGD;

	public CrittercismModuleModule()
	{
		super();
	}

	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
        //  Log.d(LCAT, "The app was just created...");
	}
    
    // Handle that last crash
    public void handleLastCrash() {
        
        CritterCallback cb = new CritterCallback() {

            @Override public void onCritterDataReceived(CritterUserData userData) {
                boolean crashedOnLastLoad = userData.crashedOnLastLoad();
                
                if(crashedOnLastLoad){
                    Log.e(LCAT, "App crashed the last time it was loaded!");
                };

            };
        };
        
        CritterUserDataRequest request = new CritterUserDataRequest(cb).requestDidCrashOnLastLoad();
        
        request.makeRequest();
    }
    
    // Create the handled exception
    private static Exception createException(String name, String reason, String stackTrace)
	{
		Exception ex = new Exception(reason);
		if(stackTrace != null)
		{
            String[] stackObjs	= stackTrace.split("\n");
            
			int nLength	= stackObjs.length;
			StackTraceElement[] elements	= new StackTraceElement[nLength];
            
			for(int nI = 0; nI < nLength; nI++)
			{
				elements[nI]	= new StackTraceElement("Unity3D", stackObjs[nI], "", -1);
			}
            ex.setStackTrace(elements);
		};
        
		return ex;
	}

    // Methods
	@Kroll.method
	public void start(KrollDict dict)
	{
        String app_id = (String)dict.get("appID");
        Boolean should_collect_logcat = (Boolean)dict.get("setLogcatReportingEnabled");
        String custom_app_version = (String)dict.get("customAppVersion");
        
		Activity activity = TiApplication.getAppCurrentActivity();
        
        if(app_id != null){
            // We have an app id, now we can do this

            try{
                
                CrittercismConfig config = new CrittercismConfig();
                    
                // Set the version name
                if(custom_app_version != null){
                    config.setCustomVersionName(custom_app_version);
                    config.setVersionCodeToBeIncludedInVersionString(false);
                } else {
                    config.setCustomVersionName(activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionName);
                    config.setVersionCodeToBeIncludedInVersionString(true);
                }
            
                // Send logcat info
                if(should_collect_logcat !=null && should_collect_logcat){
                    config.setLogcatReportingEnabled(should_collect_logcat);
                }
                
                // Initialize.
                Crittercism.initialize(activity.getApplicationContext(), app_id, config);
                
                handleLastCrash();
            } catch(Exception e) {
                // Well... we couldn't start...
                // Log.e(LCAT, "Can't inialize because of: "+e);
            };
        } else {
            // There's no app id... we can't start it
            // Log.e(LCAT, "Crittercism cannot be initialized without an app id!");
        }
	}
    
    // Force a crash
    @Kroll.method
	public void crash(String reason)
	{
        if(reason == null){
            reason = "Crashing your app! Take that!";
        };
        
        throw new RuntimeException(reason);
	}
    
    // Set a username
    @Kroll.method
    public void setUsername(String username)
	{
        try {
            Crittercism.setUsername(username);
        } catch (Exception e) {
            // Well... that didn't work...
        }
	}
    
    // Leave a breadcrumb
    @Kroll.method
    public void leaveBreadcrumb(String crumb)
	{
        if(crumb.length() > 140){ return; };

        try {
            Crittercism.leaveBreadcrumb(crumb);
        } catch (Exception e) {
            // Well... that didn't work...
        }
	}
    
    // Set the opt-out status to true or false
    @Kroll.method
    public void setOptOutStatus(boolean optOut)
	{
        try {
            Crittercism.setOptOutStatus(optOut);
        } catch (Exception e) {
            // Well... that didn't work...
        };
	}

    // Log a handled exception
    @Kroll.method
	public void logHandledException(KrollDict dict)
	{
        String name = (String)dict.get("name");
        String reason = (String)dict.get("reason");
        String stackTrace = (String)dict.get("stackTrace");
        
		if(reason == null || name == null)	{  return;	};
        
		Exception handled_exception = createException("Handled Exception: " + name, reason, stackTrace);

        try {
            Crittercism.logHandledException(handled_exception);
        } catch (Exception e) {
            // Well... that didn't work...
        };
	}
    
    // Set the meta data keys
    @Kroll.method
    public void setMetaData(KrollDict dict)
	{
        if( dict == null ) { return; };
        
		JSONObject metadata = new JSONObject();
    
        for(String key: dict.keySet()){
            try {
                metadata.put(key, dict.get(key));
            } catch (Exception e) {
                // Well... that didn't work...
            };
        };
        
        Crittercism.setMetadata(metadata);
	}
}