package kz.itdamu.mallapp.helper;

/**
 * Created by Aibol on 08.03.2016.
 */
public class Helper {

    public static final String API_URL = "http://192.168.1.70/MallBackend/v1";

    public static boolean isEmpty(String text){
        boolean flag = true;
        if(text!=null && !text.equals("") && !text.equals("null")){
            flag = false;
        }
        return flag;
    }
}
