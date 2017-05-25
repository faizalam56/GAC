package zmq.com.utility;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import zmq.com.model.UploadStatus;

/**
 * Created by zmq162 on 29/4/16.
 */
public class Utility {

//    public static ArrayList<UploadStatus>uploadStatuses;
    public static String FILE_NAME="NA";
    public static String Server_Date="NA";
    public static String EXCEPTION_STRING="NA";
    public static boolean isFileSaved=false;
    public static String patientID="NA";

//    public static ArrayList<UploadStatus> getUploadStatuses(){
//        if(uploadStatuses==null){
//            uploadStatuses=new ArrayList<UploadStatus>();
//        }
//        return  uploadStatuses;
//    }
//
//    public static void reInitialize(){
//        uploadStatuses=new ArrayList<UploadStatus>();
//    }

    public static String getDate(){
         Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
        System.out.println("Date " + simpleDateFormat.format(date));
        return  simpleDateFormat.format(date);
    }
}
