package riaz.chatrk.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "RkChat";
    private static Context mContext;
    private static final String FCM_TOKEN = "fcm_token";
    private static SharedPrefManager mInstance;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        mContext = context;
        preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }



    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SharedPrefManager(context);
        return mInstance;
    }
//
//    public void setNewOrders(int count){
//        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putInt(NEW_ORDERS, count);
//        editor.apply();
//    }
//
//    public int getNewOrder(){
//        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return preferences.getInt(NEW_ORDERS, 0);
//    }
//
//    public void isLogin(boolean status) {
//        if (status) {
//            SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean(KEY_LOGED, true).apply();
//        } else {
//            SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean(KEY_LOGED, false).apply();
//        }
//
//    }

//    public boolean getLogin() {
//        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return preferences.getBoolean(KEY_LOGED, false);
//    }
//
//    public boolean storeToken(String token) {
//        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.putString(KEY_TOKEN, token);
//        editor.apply();
//        return true;
//    }

    public boolean fcmToken(String token) {
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FCM_TOKEN, token);
        editor.apply();
        return true;
    }

    public String getFcmToken(){
        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(FCM_TOKEN, null);
    }

//    public void isLogin(boolean status) {
//        if (status) {
//            SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean(KEY_LOGED, true).apply();
//        } else {
//            SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.putBoolean(KEY_LOGED, false).apply();
//        }
//
//    }

    public SharedPreferences.Editor getEditor() {
        return preferences.edit();
    }


//    public String getToken() {
//        SharedPreferences preferences = mContext.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        return preferences.getString(KEY_TOKEN, null);
//    }

    public void setBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).commit();


    }

    public void setString(String key, String value) {
        getEditor().putString(key, value).commit();
    }

    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);

    }

    public String getString(String key) {
        return preferences.getString(key, "");
    }

    public void clearData(){
        getEditor().clear().apply();
    }
}
