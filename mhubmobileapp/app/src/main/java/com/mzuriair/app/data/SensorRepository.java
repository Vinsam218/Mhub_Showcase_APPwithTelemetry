package com.mzuriair.app.data;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.mzuriair.app.watch.*;
/** WatchDataSource -> SensorRepository -> UI. Swap the source for BLE later. */
public final class SensorRepository {
    private static SensorRepository instance;
    public static synchronized SensorRepository get() { if (instance == null) instance = new SensorRepository(); return instance; }
    private final SimulatedWatchDataSource sim = new SimulatedWatchDataSource();
    private final WatchDataSource source = sim; // later: new BluetoothWatchDataSource(...)
    private final MutableLiveData<SensorData> data = new MutableLiveData<>();
    private final MutableLiveData<ConnectionState> state = new MutableLiveData<>(ConnectionState.DISCONNECTED);
    private boolean started;
    public LiveData<SensorData> data() { return data; }
    public LiveData<ConnectionState> state() { return state; }
    public void setScenario(DemoScenario s) { sim.setScenario(s); }
    public void start() {
        if (started) return; started = true;
        source.startListening(new WatchDataSource.Listener() {
            @Override public void onData(SensorData d) { data.setValue(d); }
            @Override public void onState(ConnectionState s) { state.setValue(s); }
        });
        source.connect();
    }
    public void stop() { if (!started) return; started = false; source.stopListening(); source.disconnect(); }
}
