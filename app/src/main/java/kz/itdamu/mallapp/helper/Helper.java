package kz.itdamu.mallapp.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

import kz.itdamu.mallapp.R;

/**
 * Created by Aibol on 08.03.2016.
 */
public class Helper {
    public static final String API_URL = "http://itdamu.kz/MallBackend/v1";
    public static final String API_URL2 = "http://192.168.1.70/MallBackend/v1/goodsRest.php";
    // Google Project Number
    public static final String ACTION_MULTIPLE_PICK = "mallapp.ACTION_MULTIPLE_PICK";
    public static final String GOOGLE_PROJ_ID = "811945841836"; //mall-app-project
    public static final String MSG_KEY = "m";

    public static boolean isEmpty(String text){
        boolean flag = true;
        if(text!=null && !text.equals("")){
            flag = false;
        }
        return flag;
    }

    public static boolean validateParams(String... params){
        for(String s: params){
            if(s == null && s.trim().equals("")) return false;
        }
        return true;
    }

    public static void showSettingsGPS(final Activity activity){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle(R.string.GPSAlertDialogTitle);
        alertDialog.setMessage(R.string.GPSAlertDialogMessage);
        alertDialog.setPositiveButton(R.string.settings, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
