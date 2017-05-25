package zmq.com.fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.andexert.library.RippleView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;

import zmq.com.database.MyDatabaseAdapter;
import zmq.com.gac.R;
import zmq.com.model.PatientInfo;
import zmq.com.model.UploadStatus;


/**
 * Created by zmq162 on 28/12/15.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {


    private Toolbar toolbar;
    private EditText inputName, inputPassword; //inputEmail,
    private TextInputLayout inputLayoutName, inputLayoutPassword;//inputLayoutEmail,
    private Button btnSignUp;
    RippleView rippleView;
    CoordinatorLayout coordinatorLayout;
    HashMap<String,String> paramsValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.login_frag, container, false);
        return view;
    }

    LoginCallbacks comm;




    public interface LoginCallbacks{

         void loginClicked(ArrayList<NameValuePair> paramsValue);
    }

    public void setLoginCallbacks(LoginCallbacks comm) {
        this.comm = comm;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Supervisor");
        paramsValue = new HashMap<String,String>();

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        rippleView = (RippleView) getActivity().findViewById(R.id.more);
        coordinatorLayout = (CoordinatorLayout) getActivity().findViewById(R.id.mCordinatoLayout);




        inputLayoutName = (TextInputLayout) getActivity().findViewById(R.id.input_layout_name);
        inputLayoutPassword = (TextInputLayout) getActivity().findViewById(R.id.input_layout_password);
        inputName = (EditText) getActivity().findViewById(R.id.input_name);
        inputPassword = (EditText) getActivity().findViewById(R.id.input_password);
        btnSignUp = (Button) getActivity().findViewById(R.id.btn_signup);

        getRecords();
        if(inputName.getText().toString().length()==0){
            System.out.println("Enabled EditText..............");
            inputName.addTextChangedListener(new MyTextWatcher(inputName));
            inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        }else{
            System.out.println("Disabled EditText..............");
            inputName.setEnabled(false);
            inputPassword.setEnabled(false);

        }


        btnSignUp.setOnClickListener(this);

    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputName);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputPassword);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_signup:
                paramsValue.put("username", inputName.getText().toString());
                paramsValue.put("password", inputPassword.getText().toString());

                ArrayList<NameValuePair> paramsValue = new ArrayList<NameValuePair>();

                paramsValue.add(new BasicNameValuePair("username", inputName.getText().toString()));
                paramsValue.add(new BasicNameValuePair("password", inputPassword.getText().toString()));

                comm.loginClicked(paramsValue);
                break;

            default:
                break;
        }
    }

    private void getRecords(){
        MyDatabaseAdapter myDatabaseAdapter=new MyDatabaseAdapter(getActivity());
        myDatabaseAdapter.openToRead();

        if(myDatabaseAdapter.getCount(MyDatabaseAdapter.PATIENT_TABLE)>0){
            myDatabaseAdapter.close();
            setRecord();
        }



    }
    private void setRecord(){
        MyDatabaseAdapter myDatabaseAdapter=new MyDatabaseAdapter(getActivity());
        myDatabaseAdapter.openToRead();

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
        inputName.setText(uploadStatusn.getUserId());
        inputPassword.setText(uploadStatusn.getPassword());

        myDatabaseAdapter.close();
    }
}
