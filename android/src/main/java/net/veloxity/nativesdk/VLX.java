
package net.veloxity.nativesdk;

import android.app.Application;
import android.telecom.Call;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import net.veloxity.sdk.DataUsageListener;
import net.veloxity.sdk.LicenseException;
import net.veloxity.sdk.ServiceStatus;
import net.veloxity.sdk.Veloxity;
import net.veloxity.sdk.VeloxityOptions;
import net.veloxity.sdk.WebServiceEndpointException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class VLX extends ReactContextBaseJavaModule {

    private static String licenseKey;
    private static int priority;
    private static String dialogTitle;
    private static String dialogMessage;
    private static String dialogPositive;
    private static String dialogNegative;
    private static String webServiceEndpoint;

    private static boolean askPermission;

    public VLX(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @ReactMethod
    public static void setLicenseKey(String licenseKey) {
        VLX.licenseKey = licenseKey;
    }

    @ReactMethod
    public static void setPriority(int priority) {
        VLX.priority = priority;
    }

    @ReactMethod
    public static void setWebServiceUrl(String webServiceEndpoint) {
        VLX.webServiceEndpoint = webServiceEndpoint;
    }
    
    @ReactMethod
    public static void setAskPermission(boolean askPermission) {
        VLX.askPermission = askPermission;
    }

    @ReactMethod
    public static void setAuthorizationMenu(String dialogTitle, String dialogMessage, String dialogPositive, String dialogNegative) {
        VLX.dialogTitle = dialogTitle;
        VLX.dialogMessage = dialogMessage;
        VLX.dialogPositive = dialogPositive;
        VLX.dialogNegative = dialogNegative;
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @ReactMethod
    public void start() {
        try {
            if (dialogTitle != null || dialogMessage != null || dialogPositive != null || dialogNegative != null) {
                VeloxityOptions veloxityOptions = new VeloxityOptions.Builder(getReactApplicationContext()).setPriority(priority)
                        .setLicenseKey(licenseKey)
                        .setWebServiceEndpoint(webServiceEndpoint)
                        .setDialogTitle(dialogTitle)
                        .setDialogMessage(dialogMessage)
                        .setDialogPositive(dialogPositive)
                        .setDialogNegative(dialogNegative)
                        .setNeverAskedPermission(!askPermission)

                        .setListener(new DataUsageListener() {
                            @Override
                            public void onDataUsageResult(boolean isServiceStarted) {
                                if (isServiceStarted)
                                    sendEvent("AuthSucceed",
                                            null);
                                else
                                    sendEvent("AuthFailed",
                                            null);
                            }

                            @Override
                            public void onCompleted() {
                                sendEvent("onCompleted",
                                        null);
                            }
                        })
                        .build();
                Veloxity.initialize(veloxityOptions);
            } else {
                VeloxityOptions veloxityOptions = new VeloxityOptions.Builder(getReactApplicationContext()).setPriority(priority)
                        .setLicenseKey(licenseKey)
                        .setWebServiceEndpoint(webServiceEndpoint)
                        .setNeverAskedPermission(!askPermission)
                        .build();
                Veloxity.initialize(veloxityOptions);
            }

        } catch (LicenseException | WebServiceEndpointException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "VLX";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    @ReactMethod
    public void optIn() {
        try {
            if (dialogTitle != null || dialogMessage != null || dialogPositive != null || dialogNegative != null) {
                VeloxityOptions veloxityOptions = new VeloxityOptions.Builder(getReactApplicationContext()).setPriority(priority)
                        .setLicenseKey(licenseKey)
                        .setWebServiceEndpoint(webServiceEndpoint)
                        .setDialogTitle(dialogTitle)
                        .setDialogMessage(dialogMessage)
                        .setDialogPositive(dialogPositive)
                        .setDialogNegative(dialogNegative)
                        .setNeverAskedPermission(!askPermission)

                        .setListener(new DataUsageListener() {
                            @Override
                            public void onDataUsageResult(boolean isServiceStarted) {
                                if (isServiceStarted)
                                    sendEvent("AuthSucceed",
                                            null);
                                else
                                    sendEvent("AuthFailed",
                                            null);
                            }

                            @Override
                            public void onCompleted() {
                                sendEvent("OnCompleted",
                                        null);
                            }
                        })
                        .build();
                Veloxity.optIn(veloxityOptions);
            } else {
                VeloxityOptions veloxityOptions = new VeloxityOptions.Builder(getReactApplicationContext()).setPriority(priority)
                        .setLicenseKey(licenseKey)
                        .setWebServiceEndpoint(webServiceEndpoint)
                        .setNeverAskedPermission(!askPermission)
                        .build();
                Veloxity.optIn(veloxityOptions);
            }
        } catch (LicenseException e) {
            e.printStackTrace();
        } catch (WebServiceEndpointException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void optOut() {
        Veloxity.optOut(getReactApplicationContext());
    }

    @ReactMethod
    public void getServiceStatus(Callback callback) {
        if (Veloxity.getServiceStatus(getReactApplicationContext()) == ServiceStatus.RUNNING)
            callback.invoke(true);
        else
            callback.invoke(false);
    }

    @ReactMethod
    public void isServiceRunning(Callback callback) {
        callback.invoke(Veloxity.isServiceRunning(getReactApplicationContext()));
    }
    
    @ReactMethod
    public static void registerLifeCycleCallbacks(Application application) {
        Veloxity.registerLifeCycleCallbacks(application);
    }

    @ReactMethod
    public void sendCustomData(ReadableMap object) {
        try {
            Veloxity.sendCustomData(getReactApplicationContext(), toJSONObject(object));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void getDeviceId(Callback callback) {
        callback.invoke(Veloxity.getDeviceId(getReactApplicationContext()));
    }

    @ReactMethod
    public void sendMsisdn(String msisdn) {
        Veloxity.sendMsisdn(getReactApplicationContext(), msisdn);
    }

    @ReactMethod
    public void sendDataUsageStatus(String webServiceEndpoint, boolean isDataUsageAccepted) {
        Veloxity.sendDataUsageStatus(getReactApplicationContext(), webServiceEndpoint, isDataUsageAccepted);
    }

    public static boolean isInVeloxityProcess(Application app) {
        return Veloxity.isInVeloxityProcess(app);
    }

    public static JSONObject toJSONObject(ReadableMap readableMap) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();

        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableType type = readableMap.getType(key);

            switch (type) {
                case Null:
                    jsonObject.put(key, null);
                    break;
                case Boolean:
                    jsonObject.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    jsonObject.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    jsonObject.put(key, readableMap.getString(key));
                    break;
                case Map:
                    jsonObject.put(key, toJSONObject(readableMap.getMap(key)));
                    break;
                case Array:
                    jsonObject.put(key, toJSONArray(readableMap.getArray(key)));
                    break;
            }
        }

        return jsonObject;
    }

    public static JSONArray toJSONArray(ReadableArray readableArray) throws JSONException {
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < readableArray.size(); i++) {
            ReadableType type = readableArray.getType(i);

            switch (type) {
                case Null:
                    jsonArray.put(i, null);
                    break;
                case Boolean:
                    jsonArray.put(i, readableArray.getBoolean(i));
                    break;
                case Number:
                    jsonArray.put(i, readableArray.getDouble(i));
                    break;
                case String:
                    jsonArray.put(i, readableArray.getString(i));
                    break;
                case Map:
                    jsonArray.put(i, toJSONObject(readableArray.getMap(i)));
                    break;
                case Array:
                    jsonArray.put(i, toJSONArray(readableArray.getArray(i)));
                    break;
            }
        }

        return jsonArray;
    }


}
