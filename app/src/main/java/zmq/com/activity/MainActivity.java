package zmq.com.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import zmq.com.database.MyDatabaseAdapter;
import zmq.com.database.MySharedPreference;
import zmq.com.fragment.LoginFragment;
import zmq.com.fragment.PatientListFragment;
import zmq.com.fragment.ProgressFragment;
import zmq.com.fragment.SplashFragment;
import zmq.com.gac.R;
import zmq.com.model.PatientInfo;
import zmq.com.model.UploadStatus;
import zmq.com.network.MyAsyncTask;
import zmq.com.network.NetworkJSON;
import zmq.com.utility.Utility;


public class MainActivity extends AppCompatActivity implements LoginFragment.LoginCallbacks, PatientListFragment.PatientListCallbacks, SplashFragment.SplashCallbacks {

    private FragmentManager manager;
    Handler mHandler;
    JSONObject jObject;
    ArrayList<PatientInfo> patientInfo;
    String ServerDate="NA";
    int fileIndex=0,listItemPos=0;

    private HashMap<String,PatientInfo>hashMapServer=new HashMap<>();
    private HashMap<String,PatientInfo>hashMapDatabase;
    private String screenTag="NA";

    PowerManager.WakeLock mWakeLock;
//    ArrayList<UploadStatus> uploadStatuses;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = getSupportFragmentManager();

        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MainActivity");
        this.mWakeLock.acquire();

        if(savedInstanceState == null){

            FragmentTransaction transaction = manager.beginTransaction();
            SplashFragment mSplashFragment = new SplashFragment();
            transaction.add(R.id.main_container, mSplashFragment, "SplashFragment");
            transaction.commit();
            mSplashFragment.setSplashCallbacks(this);

        }

//        if(savedInstanceState == null){
//
//            FragmentTransaction transaction = manager.beginTransaction();
//            LoginFragment mLoginFragment = new LoginFragment();
//            transaction.add(R.id.main_container, mLoginFragment, "LoginFragment");
//            transaction.commit();
//            mLoginFragment.setLoginCallbacks(this);
//        }
    }

    @Override
    public void loginClicked(final ArrayList<NameValuePair> paramsValue) {

        loadingFragment(getResources().getString(R.string.progress_txt_1));
        String serviceName = "get_GroupActiveCompliance_patientNew";//"get_GroupActiveCompliance_patientNew"
                                                                 //get_GroupActiveCompliance_patient
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                jObject = (JSONObject) msg.obj;

                try {

                    System.out.println(jObject.toString()+" bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbdffffff");
                    if(jObject != null ){

                        if(jObject.getInt("error") == 0){

                            Toast.makeText(MainActivity.this, "User or password not exist", Toast.LENGTH_SHORT).show();
                            manager.popBackStack();
                        }else {
                         String str1=   jObject.getString("data");
                            System.out.println(str1+"   dgsdgsgsggsgsgsgsgsgsgsg");
                            if(str1.equals("2") ) {
                                System.out.println(str1 + "  ffffffffffffffff");
                             //   Toast.makeText(MainActivity.this, "There is no more patient", Toast.LENGTH_SHORT).show();
                                AlertDialog.Builder dialog = myAlertDialog(1,"ALERT","There are no more patients" );
                                dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {


                                        manager.popBackStack("LoginFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                                        manager.popBackStack();
                                    }
                                });
                                dialog.show();

                            }
                            else {

                                        // here we parse the data of jObject

                                        patientInfo = new ArrayList<>();
//                                        uploadStatuses=new ArrayList<>();

                               // Utility.reInitialize();
                                        JSONArray jArray = jObject.getJSONArray("data");

                                        for (int i = 0; i < jArray.length(); i++) {

                                            JSONObject obj2 = (JSONObject) jArray.get(i);
                                            PatientInfo info = new PatientInfo();

                                            info.setpId(obj2.getString("tbPatientId"));
                                            info.setpName(obj2.getString("name"));
                                            info.setpAsha(obj2.getString("AshaName"));
                                            info.setpRegime(obj2.getString("patientRegime"));
                                            info.setpDoseMode(obj2.getString("nextComplianceMode"));
                                            info.setDoseDate(obj2.getString("patientDoseTakenDate"));

                                            info.setSaveStatus("0");
                                            info.setUploadStatus("0");
                                            info.setServerDate(obj2.getString("ServerDate"));
                                            info.setUserId(paramsValue.get(0).getValue());
                                            info.setPassword(paramsValue.get(1).getValue());

                                            System.out.println(obj2.getString("patientDoseTakenDate")+ "Date from arif bhaii");

                                            patientInfo.add(info);

                                           ////// File saved in database By Fz/////////////////////////
                                          /*  UploadStatus uploadStatus=new UploadStatus();
                                            uploadStatus.setPatientId(obj2.getString("tbPatientId"));
                                            uploadStatus.setSaveStatus("0");
                                            uploadStatus.setUploadStatus("0");
                                            uploadStatus.setDate(obj2.getString("ServerDate"));

                                            uploadStatus.setUserId(paramsValue.get(0).getValue());
                                            uploadStatus.setPassword(paramsValue.get(1).getValue());

//                                            uploadStatus.setDate("2016-04-29");
                                            Utility.getUploadStatuses().add(uploadStatus);*/

                                            hashMapServer.put(info.getpId(), info);
                                            ServerDate=obj2.getString("ServerDate");
                                            Utility.Server_Date=ServerDate;//"2016-04-29"
                                        }

                                        saveInDB();

                                        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/GAC","Compliance");

                                        if(file.exists() && file.listFiles().length != 0) {
                                            final File[] allFiles = file.listFiles();
                                            String filePath = allFiles[0].getAbsolutePath();

                                            System.out.println("Total space "+allFiles[0].getTotalSpace()+" / "+"Total getUsableSpace "+allFiles[0].getUsableSpace()+" / "+"Total getFreeSpace "+allFiles[0].getFreeSpace());
                                        }

                                        FragmentTransaction transaction = manager.beginTransaction();
                                        PatientListFragment mPatientListFragment = new PatientListFragment();
                                        Bundle args = new Bundle();
                                        args.putSerializable("patients", patientInfo);
                                        mPatientListFragment.setArguments(args);
                                        transaction.replace(R.id.main_container, mPatientListFragment, "PatientListFragment");
                                        transaction.addToBackStack("PatientListScreen");
                                        transaction.commit();
                                        mPatientListFragment.setPatientListCallbacks(MainActivity.this);
                            }
                        }
                    }
                    else{

                        AlertDialog.Builder dialog = myAlertDialog(1,"ALERT",Utility.EXCEPTION_STRING );
                        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                manager.popBackStack("LoginFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                                manager.popBackStack();
                            }
                        });
                        dialog.show();

                        System.out.println("From Else Part.....");
                        Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utility.EXCEPTION_STRING=e.getMessage();

                    AlertDialog.Builder dialog = myAlertDialog(1,"ALERT",Utility.EXCEPTION_STRING );
                    dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            manager.popBackStack("LoginFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

                            manager.popBackStack();
                        }
                    });
                    dialog.show();

                    System.out.println("MainActivity * loginClicked * JSONException " +Utility.EXCEPTION_STRING  +" "+ NetworkJSON.jObject);
                    e.printStackTrace();
                }
            }
        };

        new MyAsyncTask(this, serviceName, paramsValue, 1,0, mHandler).execute();

    }

    public void loadingFragment(String progressMsg){

        FragmentTransaction transaction = manager.beginTransaction();
        ProgressFragment mProgressFragment = new ProgressFragment();
        Bundle args = new Bundle();
        args.putString("myProgText", progressMsg);
        mProgressFragment.setArguments(args);
        transaction.replace(R.id.main_container, mProgressFragment, "ProgressFragment");
        transaction.addToBackStack("ProgressScreen");
        transaction.commit();
    }

    @Override
    public void onPatientItemClicked(int position) {

//        Intent intentVideo = new Intent(this, RecordVideo.class);
//        startActivityForResult(intentVideo, 200);

    }

    @Override
    public void cameraClicked(int position, PatientInfo info) {
        listItemPos=position;

        Utility.isFileSaved=true;
        Intent intentVideo = new Intent(this, RecordVideo.class);
        intentVideo.putExtra("patientId",info.getpId());
        System.out.println("ID on cameraClicked "+info.getpId());
        startActivityForResult(intentVideo, 200);

    }

    @Override
    public void uploadData(final int position, PatientInfo info, final PatientListFragment frag) {

        String filePath="NA";

//        String patientId = MySharedPreference.getStringValue(this, "PatientInfo", "patientId");

//        if(patientId.equals(info.getpId())){
          if(!Utility.FILE_NAME.equalsIgnoreCase("NA")){

            final File file = new File(Environment.getExternalStorageDirectory().getPath()+"/GAC","Compliance");
            final File[] allFiles = file.listFiles();

            for(int i=0;i<allFiles.length;i++){
                System.out.println("Files at [ "+i+"]" +allFiles[i].getName() +" "+Utility.FILE_NAME);
                if(allFiles[i].getName().equalsIgnoreCase(Utility.FILE_NAME)){
                    filePath = allFiles[i].getAbsolutePath();
                    fileIndex=i;
                   break;
                }
            }


            //String filePath = allFiles[0].getAbsolutePath();

            String serviceName = "submitPatientCompliance";
              if(!filePath.equalsIgnoreCase("NA")) loadingFragment(getResources().getString(R.string.progress_txt_1));
              else{
                  AlertDialog.Builder dialog = myAlertDialog(1,"ALERT","You have already deleted your file !");
                  dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int which) {
                             dialog.cancel();

//                          manager.popBackStack("PatientListFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
//
//                          manager.popBackStack();
                      }
                  });
                  dialog.show();
              }

            ArrayList<NameValuePair> paramsValue = new ArrayList<NameValuePair>();

            paramsValue.add(new BasicNameValuePair("filePath", filePath));
            paramsValue.add(new BasicNameValuePair("complianceMode", info.getpDoseMode()));
            paramsValue.add(new BasicNameValuePair("tbPatientId", info.getpId()));
            paramsValue.add(new BasicNameValuePair("patientRegime", info.getpRegime()));
            paramsValue.add(new BasicNameValuePair("appStatus", "4"));
            paramsValue.add(new BasicNameValuePair("patientDoseTakenDate",info.getDoseDate().toString()));

              System.out.println(info.getDoseDate()+"without tostringgg");
              System.out.println(info.getDoseDate().toString()+"with tostringgg");

            mHandler = new Handler(){


                @Override
                public void handleMessage(Message msg) {

                    jObject = (JSONObject) msg.obj;

                    try {

                        if(jObject != null ){

                            if(jObject.getBoolean("success") == true) {
                                frag.refressListOnResponse(position);
                                manager.popBackStack();
                                Toast.makeText(MainActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                MySharedPreference.clearPrefernce(MainActivity.this, "PatientInfo");
                                allFiles[fileIndex].delete();
                                deleteRecord();
                            }else{
                                screenTag="PatientListFragment";
                                showErrorDialog();
                               // Toast.makeText(MainActivity.this, "Network Error", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{

                            screenTag="PatientListFragment";
                            showErrorDialog();

                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        Utility.EXCEPTION_STRING=e.getMessage();
                        screenTag="PatientListFragment";
                        showErrorDialog();

                        System.out.println("StartActivity * uploadDataToServer * JSONException ");
                        e.printStackTrace();
                    }
                }
            };
          if(!filePath.equalsIgnoreCase("NA"))  new MyAsyncTask(this,  serviceName, paramsValue, 1,1, mHandler).execute();

        }else{

            if(Utility.FILE_NAME.equals("NA")){
                Toast.makeText(MainActivity.this, "please record the video first", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, "please choose the Correct Patient", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showErrorDialog(){
        AlertDialog.Builder dialog = myAlertDialog(1,"ALERT", Utility.EXCEPTION_STRING );
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {


                manager.popBackStack(screenTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                manager.popBackStack();
            }
        });
        dialog.show();
    }

    private  void deleteRecord(){
        MyDatabaseAdapter myDatabaseAdapter=new MyDatabaseAdapter(this);
        myDatabaseAdapter.openToRead();

        String where= MyDatabaseAdapter.PATIENT_ID+"=?";
//        ContentValues values=new ContentValues();
//        values.put(MyDatabaseAdapter.UPLOADSTATUS, "1");
//        values.put(MyDatabaseAdapter.FILE_NAME, "NA");
//        myDatabaseAdapter.updateData(MyDatabaseAdapter.UPLOAD_TABLE, where, values, new String[]{RecordVideo.patientID});
        myDatabaseAdapter.deleteData(MyDatabaseAdapter.PATIENT_TABLE,where,new String[]{Utility.patientID});
        myDatabaseAdapter.close();

        Utility.FILE_NAME="NA";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("requestCode "+requestCode +" resultCode "+resultCode);
        switch (requestCode) {

            case 200:

                if (resultCode == 2) {

                    if( Utility.isFileSaved){

                        Utility.isFileSaved=false;
                        //String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
                        Utility.FILE_NAME=Utility.patientID+"_"+Utility.Server_Date + "_TBC" + ".mp4";
                        updateTable();
                    }

                    MySharedPreference.saveStringInPreference(this, "PatientInfo", "patientId", data.getStringExtra("patientId"));


                    FragmentTransaction transaction = manager.beginTransaction();
                    PatientListFragment mPatientListFragment = new PatientListFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("patients", patientInfo);
                    mPatientListFragment.setArguments(args);
                    transaction.replace(R.id.main_container, mPatientListFragment, "PatientListFragment");
                    transaction.addToBackStack("PatientListScreen");
                    transaction.commit();
                    mPatientListFragment.setPatientListCallbacks(MainActivity.this);

                    //manager.popBackStack("ComplianceFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    Toast.makeText(this, "Video Recorded Successfully",
                            Toast.LENGTH_LONG).show();
                } else {

                   // Toast.makeText(this, "onUserLeaveHintASIF "+resultCode, Toast.LENGTH_SHORT).show();
                    File file = new File(Environment.getExternalStorageDirectory()
                            .getPath() + "/GAC", "Compliance");
                    final File[] allFiles = file.listFiles();

                    if (allFiles.length != 0) {
                        allFiles[0].delete();
                    }
                }
                break;
            default:
                break;
        }

    }
    private  void updateTable(){
        MyDatabaseAdapter myDatabaseAdapter=new MyDatabaseAdapter(this);
        myDatabaseAdapter.openToWrite();

        String where= MyDatabaseAdapter.PATIENT_ID+"=?";
        ContentValues values=new ContentValues();
        values.put(MyDatabaseAdapter.SAVE_STATUS, "1");
        values.put(MyDatabaseAdapter.FILE_NAME, Utility.FILE_NAME);
        myDatabaseAdapter.updateData(MyDatabaseAdapter.PATIENT_TABLE, where, values, new String[]{Utility.patientID});

        patientInfo=myDatabaseAdapter.getAllContacts(MyDatabaseAdapter.PATIENT_TABLE) ;
        myDatabaseAdapter.close();


//        Utility.getUploadStatuses().get(listItemPos).setSaveStatus("1");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        manager.popBackStack("ProgressScreen", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    @Override
    public void splashTouch() {

        FragmentTransaction transaction = manager.beginTransaction();
        LoginFragment mLoginFragment = new LoginFragment();
        transaction.replace(R.id.main_container, mLoginFragment, "LoginFragment");
        transaction.commit();
        mLoginFragment.setLoginCallbacks(this);
    }

    public  AlertDialog.Builder  myAlertDialog(int cases,String title,String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

        switch (cases){

            case 1:
                dialog.setTitle(title);
                dialog.setMessage(message);
              //  dialog.setIcon(R.drawable.ic_dialog_alert);
                break;

            default:
                break;
        }

        return dialog;
    }

    private void saveInDB(){
        MyDatabaseAdapter myDatabaseAdapter=new MyDatabaseAdapter(this);
        myDatabaseAdapter.openToWrite();


        int tableCount=myDatabaseAdapter.getCount(MyDatabaseAdapter.PATIENT_TABLE);

        if(tableCount>0 ) {
            String whereClause = MyDatabaseAdapter.UPLOADSTATUS + "=0";
            String keys[]={
                    MyDatabaseAdapter.ROW_ID,
                    MyDatabaseAdapter.SERVER_DATE,
                    MyDatabaseAdapter.PATIENT_ID,
                    MyDatabaseAdapter.FILE_NAME,
                    MyDatabaseAdapter.SAVE_STATUS,
                    MyDatabaseAdapter.UPLOADSTATUS,

                    MyDatabaseAdapter.USER_ID,
                    MyDatabaseAdapter.PASSWORD,

                    MyDatabaseAdapter.PATIENT_NAME,
                    MyDatabaseAdapter.ASHA_NAME,
                    MyDatabaseAdapter.PATIENT_REGIME,
                    MyDatabaseAdapter.NEXTCOMPLIANCEMODE,
                    MyDatabaseAdapter.PATIENTDOSETAKENDATE,



            };
            PatientInfo uploadStatusn = myDatabaseAdapter.selectSpecificData(MyDatabaseAdapter.PATIENT_TABLE, keys, whereClause);

            if (!uploadStatusn.getServerDate().equalsIgnoreCase(ServerDate) ) {
                myDatabaseAdapter.dropTable(MyDatabaseAdapter.PATIENT_TABLE);

                for (int i = 0; i < patientInfo.size(); i++) {

                    PatientInfo uploadStatus = patientInfo.get(i);

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MyDatabaseAdapter.PATIENT_ID, uploadStatus.getpId());
                    contentValues.put(MyDatabaseAdapter.FILE_NAME, "NA");
                    contentValues.put(MyDatabaseAdapter.SERVER_DATE, uploadStatus.getServerDate());
                    contentValues.put(MyDatabaseAdapter.SAVE_STATUS, uploadStatus.getSaveStatus());
                    contentValues.put(MyDatabaseAdapter.UPLOADSTATUS, uploadStatus.getUploadStatus());

                    contentValues.put(MyDatabaseAdapter.PATIENT_NAME, uploadStatus.getpName());
                    contentValues.put(MyDatabaseAdapter.ASHA_NAME, uploadStatus.getpAsha());
                    contentValues.put(MyDatabaseAdapter.PATIENT_REGIME, uploadStatus.getpRegime());
                    contentValues.put(MyDatabaseAdapter.NEXTCOMPLIANCEMODE, uploadStatus.getpDoseMode());
                    contentValues.put(MyDatabaseAdapter.PATIENTDOSETAKENDATE, uploadStatus.getDoseDate());

                    contentValues.put(MyDatabaseAdapter.USER_ID, uploadStatus.getUserId());
                    contentValues.put(MyDatabaseAdapter.PASSWORD, uploadStatus.getPassword());

                    myDatabaseAdapter.insertData(MyDatabaseAdapter.PATIENT_TABLE, contentValues);
                }

            }
            else if(tableCount<patientInfo.size()){
                 hashMapDatabase=myDatabaseAdapter.getAllContactsInMap(MyDatabaseAdapter.PATIENT_TABLE);
                 ArrayList<PatientInfo>al=chkNewRecords();

                if(al.size()>0){
                    for(int k=0;k<al.size();k++){
                        PatientInfo uploadStatus=al.get(k);

                        ContentValues contentValues = new ContentValues();
                        contentValues.put(MyDatabaseAdapter.PATIENT_ID, uploadStatus.getpId());
                        contentValues.put(MyDatabaseAdapter.FILE_NAME, "NA");
                        contentValues.put(MyDatabaseAdapter.SERVER_DATE, uploadStatus.getServerDate());
                        contentValues.put(MyDatabaseAdapter.SAVE_STATUS, uploadStatus.getSaveStatus());
                        contentValues.put(MyDatabaseAdapter.UPLOADSTATUS, uploadStatus.getUploadStatus());

                        contentValues.put(MyDatabaseAdapter.PATIENT_NAME, uploadStatus.getpName());
                        contentValues.put(MyDatabaseAdapter.ASHA_NAME, uploadStatus.getpAsha());
                        contentValues.put(MyDatabaseAdapter.PATIENT_REGIME, uploadStatus.getpRegime());
                        contentValues.put(MyDatabaseAdapter.NEXTCOMPLIANCEMODE, uploadStatus.getpDoseMode());
                        contentValues.put(MyDatabaseAdapter.PATIENTDOSETAKENDATE, uploadStatus.getDoseDate());

                        contentValues.put(MyDatabaseAdapter.USER_ID, uploadStatus.getUserId());
                        contentValues.put(MyDatabaseAdapter.PASSWORD, uploadStatus.getPassword());

                        myDatabaseAdapter.insertData(MyDatabaseAdapter.PATIENT_TABLE, contentValues);
                    }

                }

            }

            patientInfo=myDatabaseAdapter.getAllContacts(MyDatabaseAdapter.PATIENT_TABLE);
        }
        else{
            for (int i = 0; i < patientInfo.size(); i++) {

                PatientInfo uploadStatus = patientInfo.get(i);

                ContentValues contentValues = new ContentValues();
                contentValues.put(MyDatabaseAdapter.PATIENT_ID, uploadStatus.getpId());
                contentValues.put(MyDatabaseAdapter.FILE_NAME, "NA");
                contentValues.put(MyDatabaseAdapter.SERVER_DATE, uploadStatus.getServerDate());
                contentValues.put(MyDatabaseAdapter.SAVE_STATUS, uploadStatus.getSaveStatus());
                contentValues.put(MyDatabaseAdapter.UPLOADSTATUS, uploadStatus.getUploadStatus());

                contentValues.put(MyDatabaseAdapter.PATIENT_NAME, uploadStatus.getpName());
                contentValues.put(MyDatabaseAdapter.ASHA_NAME, uploadStatus.getpAsha());
                contentValues.put(MyDatabaseAdapter.PATIENT_REGIME, uploadStatus.getpRegime());
                contentValues.put(MyDatabaseAdapter.NEXTCOMPLIANCEMODE, uploadStatus.getpDoseMode());
                contentValues.put(MyDatabaseAdapter.PATIENTDOSETAKENDATE, uploadStatus.getDoseDate());

                contentValues.put(MyDatabaseAdapter.USER_ID, uploadStatus.getUserId());
                contentValues.put(MyDatabaseAdapter.PASSWORD, uploadStatus.getPassword());

                myDatabaseAdapter.insertData(MyDatabaseAdapter.PATIENT_TABLE, contentValues);
            }
        }
        patientInfo=myDatabaseAdapter.getAllContacts(MyDatabaseAdapter.PATIENT_TABLE);
        myDatabaseAdapter.close();

    }

    private ArrayList<PatientInfo> chkNewRecords(){
          ArrayList<PatientInfo>al=new ArrayList<>();

        for(int i=0;i<hashMapServer.size();i++){
            PatientInfo uploadStatus=patientInfo.get(i);

            String p_id=hashMapServer.get(uploadStatus.getpId()).getpId();

                if(!hashMapDatabase.containsKey(p_id)){
                    al.add(uploadStatus);
                    System.out.println("New Record..."+al);
                }

        }
        return al;
    }
}
