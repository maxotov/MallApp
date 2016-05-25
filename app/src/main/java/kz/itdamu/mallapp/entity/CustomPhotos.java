package kz.itdamu.mallapp.entity;

/**
 * Created by Берик on 12.04.2016.
 */
public class CustomPhotos {

    public String sdcardPath;
    public boolean isSeleted = false;

    public String getSdcardPath() {
        return sdcardPath;
    }

    public void setIsSeleted(boolean isSeleted) {
        this.isSeleted = isSeleted;
    }

    public boolean isSeleted() {
        return isSeleted;
    }
}
