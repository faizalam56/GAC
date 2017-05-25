package zmq.com.fragment;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import zmq.com.gac.R;


public class SplashFragment extends Fragment implements OnClickListener{
	
	LinearLayout layout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view  = inflater.inflate(R.layout.splash_fragment, container, false);
		return view;
	}
	
	public interface SplashCallbacks{
		void splashTouch();
	}
	
	SplashCallbacks comm;
	
	public void setSplashCallbacks(SplashCallbacks comm) {
		this.comm = comm;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		comm = (SplashCallbacks) getActivity();
		layout  = (LinearLayout) getActivity().findViewById(R.id.linearLayoutbackground);
		layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		comm.splashTouch();
	}
}
