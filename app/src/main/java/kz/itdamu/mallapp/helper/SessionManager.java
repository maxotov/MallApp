package kz.itdamu.mallapp.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * Created by Aibol on 13.03.2016.
 */
public class SessionManager {
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "MallAppPref";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_EXIST_DEVICE = "isExistDevice";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setDevice(String deviceId){
        editor.putString(KEY_EXIST_DEVICE, deviceId);
        editor.commit();
        Log.d(TAG, "User device id modified!");
    }

    public String getDeviceId(){
        return pref.getString(KEY_EXIST_DEVICE, "");
    }
}
