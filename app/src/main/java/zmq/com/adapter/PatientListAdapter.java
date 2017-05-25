package zmq.com.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import zmq.com.activity.RecordVideo;
import zmq.com.database.MyDatabaseAdapter;
import zmq.com.database.MySharedPreference;
import zmq.com.gac.R;
import zmq.com.model.DataObject;
import zmq.com.model.PatientInfo;
import zmq.com.model.UploadStatus;
import zmq.com.utility.Utility;

/**
 * Created by zmq162 on 30/12/15.
 */


public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.DataObjectHolder> {
    private static String LOG_TAG = "PatientListAdapter";
    private  ArrayList<PatientInfo> mDataset;
    private  MyClickListener myClickListener;
    private int RES_LAYOUT;
    Context mContext;
//    public static List<PatientInfo> uploadStatuses;

    private static HashMap<String,PatientInfo>uploadStatusHashMap;
//   static int inedxPos=0;


    public  class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView dateTime;
        ImageView one, two;

        public DataObjectHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.txt_p_name);
            dateTime = (TextView) itemView.findViewById(R.id.txt_p_id);
            one = (ImageView) itemView.findViewById(R.id.imageView);
            two = (ImageView) itemView.findViewById(R.id.imageView2);
            Log.i(LOG_TAG, "Adding Listener");
          //  itemView.setOnClickListener(this);
            one.setOnClickListener(this);
            two.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){

                case R.id.imageView:
                    PatientInfo uploadStatus=mDataset.get(getAdapterPosition());
                    if(!uploadStatus.getSaveStatus().equals("1")) {
                        myClickListener.onItemClickCamera(getAdapterPosition(), v);
                        Utility.patientID = mDataset.get(getAdapterPosition()).getpId();
                    }

                    break;
                case R.id.imageView2:
                    PatientInfo uploadStatus1=mDataset.get(getAdapterPosition());
                    Utility.patientID = mDataset.get(getAdapterPosition()).getpId();
                    Utility.FILE_NAME=uploadStatus1.getFileName();
                    myClickListener.onItemClickUpload(getAdapterPosition(), v);
                    break;
                default:
                    myClickListener.onItemClick(getAdapterPosition(), v);
                    break;
            }

        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
        System.out.println("myClickListener "+myClickListener);
    }

    public PatientListAdapter(Context context, int resLayout, ArrayList<PatientInfo> myDataset) {
        mDataset = myDataset;
        RES_LAYOUT = resLayout;
        mContext = context;
//        getRecords();

        System.out.println("PatientListAdapter Called....");
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(RES_LAYOUT, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

       //
          PatientInfo patientInfo=mDataset.get(position);
          System.out.println("POsition "+position +" mDataset "+mDataset.size()+" Patient Info.. "+patientInfo);

          if (patientInfo.getSaveStatus().equals("1") ) {

              holder.two.setImageResource(R.drawable.upload_red);

          }
        else if ( patientInfo.getSaveStatus().equals("0") ) {
              holder.two.setImageResource(R.drawable.upload_green);

          }


//        String patientId = MySharedPreference.getStringValue(mContext, "PatientInfo", "patientId");
//        File file = new File(Environment.getExternalStorageDirectory().getPath()+"/GAC","Compliance");

//        if(!patientId.equals("DEFAULT")){
//
//            if(mDataset.get(position).getpId().equals(patientId) && file.exists() && file.listFiles().length != 0){
//
//                holder.two.setImageResource(R.drawable.upload_red);
//            }
//        }



//        mDataset.get(position).getpId();

        holder.title.setText(patientInfo.getpName());
        holder.dateTime.setText(patientInfo.getpId());


//        holder.title.setText("Default");
//        holder.dateTime.setText(uploadStatuses.get(position).getPatientId());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
        public void onItemClickCamera(int position, View v);
        public void onItemClickUpload(int position, View v);
    }


    private void getRecords(){
//        MyDatabaseAdapter myDatabaseAdapter=new MyDatabaseAdapter(mContext);
//        myDatabaseAdapter.openToRead();
//        uploadStatuses=new ArrayList<>();
//        uploadStatusHashMap=new HashMap<>();
//        uploadStatuses=myDatabaseAdapter.getAllContacts(MyDatabaseAdapter.PATIENT_TABLE);
//        myDatabaseAdapter.close();

//        for(int i=0;i<uploadStatuses.size();i++){
//            UploadStatus uploadStatus=uploadStatuses.get(i);
//            uploadStatusHashMap.put(uploadStatus.getPatientId(),uploadStatus);
//        }

       // System.out.println("From PatientListAdapter "+uploadStatuses.size() +" apna List "+uploadStatuses +" Paraya List "+mDataset);
    }
}