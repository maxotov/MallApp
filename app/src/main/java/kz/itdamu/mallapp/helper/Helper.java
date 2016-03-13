package kz.itdamu.mallapp.helper;

/**
 * Created by Aibol on 08.03.2016.
 */
public class Helper {

    public static final String API_URL = "http://192.168.1.70/MallBackend/v1";
    // Google Project Number
    public static final String GOOGLE_PROJ_ID = "811945841836"; //mall-app-project
    public static final String MSG_KEY = "m";

    public static boolean isEmpty(String text){
        boolean flag = true;
        if(text!=null && !text.equals("") && !text.equals("null")){
            flag = false;
        }
        return flag;
    }
}
