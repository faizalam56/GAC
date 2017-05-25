package zmq.com.model;

/**
 * Created by zmq162 on 30/12/15.
 */
public class DataObject {

    private String mText2;
    private int imgId;

    public DataObject(int img, String text2){
        imgId = img;
        mText2 = text2;
    }
    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
