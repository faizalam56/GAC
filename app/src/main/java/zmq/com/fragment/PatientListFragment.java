package zmq.com.fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zmq.com.activity.RecordVideo;
import zmq.com.adapter.PatientListAdapter;
import zmq.com.database.MyDatabaseAdapter;
import zmq.com.gac.R;
import zmq.com.model.PatientInfo;
import zmq.com.model.UploadStatus;
import zmq.com.utility.Utility;


public class PatientListFragment extends Fragment implements AdapterView.OnItemClickListener, PatientListAdapter.MyClickListener {

    ListView list;
    ArrayList<PatientInfo> patientList;
    private RecyclerView mRecyclerView;

    private Toolbar toolbar;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_list, container, false);
    }


    PatientListCallbacks comm;

    @Override
    public void onItemClick(int position, View v) {

        Toast.makeText(getActivity(), "Position "+position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClickCamera(int position, View v) {
        comm.cameraClicked(position, patientList.get(position));
    }

    @Override
    public void onItemClickUpload(int position, View v) {
        System.out.println("onItemClickUpload Fragment "+patientList.get(position));
        comm.uploadData(position, patientList.get(position),this);

    }

    public interface PatientListCallbacks{

        void onPatientItemClicked(int position);
        void cameraClicked(int position,PatientInfo info);
        void uploadData(int position,PatientInfo info, PatientListFragment frag);

    }

    public void setPatientListCallbacks(PatientListCallbacks comm) {
        this.comm = comm;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        toolbar = (Toolbar) getActivity().findViewById(R.id.patient_list_toolbar);
        toolbar.setTitle("List of Patient");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        patientList = (ArrayList<PatientInfo>) getArguments().getSerializable("patients");

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new PatientListAdapter(getActivity(),R.layout.patient_list_item, patientList);
        mRecyclerView.setAdapter(mAdapter);
        ((PatientListAdapter) mAdapter).setOnItemClickListener(this);

        System.out.println("onActivityCreated");
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {

        comm.onPatientItemClicked(position);
    }

    public void refressListOnResponse(int position){

        //to remove the given item from list of patient
        patientList.remove(position);
//        Utility.getUploadStatuses().remove(position);
        mAdapter.notifyDataSetChanged();
    }



}
