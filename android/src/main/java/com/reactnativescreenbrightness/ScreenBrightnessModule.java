package com.reactnativescreenbrightness;

import androidx.annotation.NonNull;

import android.provider.Settings;
import android.view.WindowManager;
import android.content.Intent;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import java.util.Map;
import java.util.HashMap;

@ReactModule(name = ScreenBrightnessModule.NAME)
public class ScreenBrightnessModule extends ReactContextBaseJavaModule {
    public static final String NAME = "ScreenBrightness";

    public ScreenBrightnessModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }


   @ReactMethod
   public void setBrightness(int brightness, Promise promise) {
         try {
            
            // get permission android for write system settings brightness
               if (Settings.System.canWrite(reactContext)) {
                  boolean curBrightnessValue = Settings.System.putInt(reactContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
                  promise.resolve(curBrightnessValue);
               }else{
                  askPermission();
               }            
        } catch(Exception e) {
            promise.reject("Create Event Error", e);
        }
      }

      @ReactMethod
      public void askPermission() {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(reactContext)) {
               Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
               intent.setData(android.net.Uri.parse("package:" + reactContext.getPackageName()));
               intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               reactContext.startActivity(intent);
            }
         }
      }
}
