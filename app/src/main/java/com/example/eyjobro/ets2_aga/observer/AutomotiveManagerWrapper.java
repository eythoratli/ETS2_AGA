package com.example.eyjobro.ets2_aga.observer;

import android.content.Context;
import android.swedspot.automotiveapi.AutomotiveSignal;
import android.swedspot.scs.data.Uint32;
import android.util.Log;

import com.swedspot.automotiveapi.AutomotiveFactory;
import com.swedspot.automotiveapi.AutomotiveListener;
import com.swedspot.automotiveapi.AutomotiveManager;
import com.swedspot.vil.distraction.DriverDistractionLevel;
import com.swedspot.vil.distraction.DriverDistractionListener;
import com.swedspot.vil.distraction.LightMode;
import com.swedspot.vil.distraction.StealthMode;
import com.swedspot.vil.policy.AutomotiveCertificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutomotiveManagerWrapper {

    public static final int DRIVER_DISTRACTION_SIGNAL_ID = 6666;
    private static final String TAG = AutomotiveManagerWrapper.class.getName();
    private static final Map<Integer, List<AbstractSignalListener>> listeners = new HashMap<Integer, List<AbstractSignalListener>>();
    private static final AutomotiveCertificate amc = new AutomotiveCertificate(new byte[0]);
    public static AutomotiveManagerWrapper instance = null;
    public static AutomotiveManager automotiveManager;
    public static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @SuppressWarnings("ResourceType")
    private AutomotiveManagerWrapper(final Context context) {
        AutomotiveListener aml = new AutomotiveListener() {
            @Override
            public void receive(AutomotiveSignal automotiveSignal) {
                if (!anyListener()) return;
                for (AbstractSignalListener listener : listeners.get(automotiveSignal.getSignalId())) {
                    if (listener == null) continue;
                    listener.onSignalReceived(automotiveSignal);
                }
            }
            @Override
            public void timeout(int i) {}
            @Override
            public void notAllowed(int i) {}
        };

        DriverDistractionListener ddl = new DriverDistractionListener() {
            @Override
            public void levelChanged(DriverDistractionLevel driverDistractionLevel) {
                int ddl = driverDistractionLevel.getLevel();
                if (!anyListener()) return;
                for (AbstractSignalListener listener : listeners.get(DRIVER_DISTRACTION_SIGNAL_ID)) {
                    if (listener == null) continue;
                    AutomotiveSignal distractionSignal = new AutomotiveSignal(DRIVER_DISTRACTION_SIGNAL_ID, new Uint32(ddl), null);
                    listener.onSignalReceived(distractionSignal);
                }
            }
            @Override
            public void lightModeChanged(LightMode lightMode) {
            }
            @Override
            public void stealthModeChanged(StealthMode stealthMode) {
            }
        };
        automotiveManager = AutomotiveFactory.createAutomotiveManagerInstance(amc, aml, ddl);
        automotiveManager.setListener(aml);
    }

    private boolean anyListener() {
        return listeners != null && !listeners.isEmpty();
    }

    public static AutomotiveManagerWrapper getInstance(Context context) {
        if (instance != null) return instance;
        if (context == null) return instance;
        synchronized (AutomotiveManagerWrapper.class) {
            if (instance != null) return instance;
            instance = new AutomotiveManagerWrapper(context);
            Log.i(TAG, "AGA Singleton instance created once and for all.");
        }
        return instance;
    }

    public void addListener(AbstractSignalListener listener) {
        List<AbstractSignalListener> signalListeners = listeners.get(listener.getSignalId());
        if (signalListeners == null) signalListeners = new ArrayList<AbstractSignalListener>();
        signalListeners.add(listener);
        listeners.put(listener.getSignalId(), signalListeners);
        doSubscribe(listener.getSignalId());
    }

    public void removeListener(AbstractSignalListener listener) {
        List<AbstractSignalListener> signalListeners = listeners.get(listener.getSignalId());
        removeListenerIfExists(listener, signalListeners);
        unSubscribeIfNoListenerExists(listener, signalListeners);
    }

    private void unSubscribeIfNoListenerExists(AbstractSignalListener listener, List<AbstractSignalListener> signalListeners) {
        if ((signalListeners != null && signalListeners.isEmpty()) || (signalListeners != null))
            doUnSubscribe(listener.getSignalId());
    }

    private void removeListenerIfExists(AbstractSignalListener listener, List<AbstractSignalListener> signalListeners) {
        if ((signalListeners != null) && (signalListeners.contains(listener)))
            signalListeners.remove(listener);
    }

    private void doSubscribe(final int signalID) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                automotiveManager.register(signalID);
            }
        });
    }

    private void doUnSubscribe(final int signalID) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                automotiveManager.unregister(signalID);
            }
        });
    }
}