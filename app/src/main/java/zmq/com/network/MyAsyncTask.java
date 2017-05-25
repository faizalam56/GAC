package zmq.com.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;



import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAsyncTask extends AsyncTask<Void, Void, JSONObject>{

	Context context;
	String serviceName;
	ArrayList  param;
	int parmChecker,multiChecker;
	Handler mHandler;
//	ProgressDialog progressDialog;
	
	
	public MyAsyncTask(Context context, String serviceName, ArrayList param, int parmChecker, int multiChecker, Handler mHandler) {
		this.context = context;
		this.serviceName = serviceName;
		this.param = param;
		this.parmChecker = parmChecker;
		this.mHandler = mHandler;
		this.multiChecker=multiChecker;
	}
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
//		progressDialog = new ProgressDialog(context);
//		progressDialog.setMessage("Connecting...");
//		progressDialog.setCancelable(false);
//		progressDialog.show();
	}
	@Override
	protected JSONObject doInBackground(Void... params) {
		// TODO Auto-generated method stub
		NetworkJSON mNetworkJSON = new NetworkJSON();
		JSONObject jObject = mNetworkJSON.getJsonObject(serviceName, param, parmChecker,multiChecker);
		
		Message message = Message.obtain();
		message.obj = jObject;
		mHandler.sendMessage(message);
		
		System.out.println("res"+jObject);
		return jObject;
	}
	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
//		progressDialog.dismiss();
		
//		if(result != null){
//			progressDialog.dismiss();			
//		}
		
	}

}
