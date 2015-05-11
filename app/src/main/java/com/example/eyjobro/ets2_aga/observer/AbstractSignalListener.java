package com.example.eyjobro.ets2_aga.observer;

import android.app.Fragment;
import android.swedspot.automotiveapi.AutomotiveSignal;
import android.swedspot.scs.data.SCSData;

public abstract class AbstractSignalListener<T extends SCSData>{

    private Fragment fragment;


    public abstract void onSignalDeliveredOnUiThread(T data);
    public abstract int getSignalId();

	public AbstractSignalListener(Fragment fragment) {
        this.fragment = fragment;
	}

	public void onSignalReceived(AutomotiveSignal automotiveSignal) {
		final SCSData data = automotiveSignal.getData();
        fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSignalDeliveredOnUiThread((T)data);
            }
        });
	}
}