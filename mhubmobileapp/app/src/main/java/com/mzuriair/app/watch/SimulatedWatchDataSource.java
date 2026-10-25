package com.mzuriair.app.watch;
import android.os.Handler;
import android.os.Looper;
import com.mzuriair.app.data.*;
import com.mzuriair.app.risk.*;
public class SimulatedWatchDataSource implements WatchDataSource {
    private final Handler h = new Handler(Looper.getMainLooper());
    private final RiskEngine engine = new DemoRiskEngine();
    private Listener l; private ConnectionState state = ConnectionState.DISCONNECTED;
    private DemoScenario scenario = DemoScenario.SAFE; private SensorData latest;
    private final Runnable tick = new Runnable() { @Override public void run() { emit(); h.postDelayed(this, 2000); } };
    private void setState(ConnectionState s) { state = s; if (l != null) l.onState(s); }
    private void emit() {
        if (state != ConnectionState.CONNECTED) return;
        latest = scenario.toData(engine, System.currentTimeMillis());
        if (l != null) l.onData(latest);
    }
    public void setScenario(DemoScenario s) { scenario = s; emit(); }
    @Override public void connect() {
        setState(ConnectionState.SYNCING);
        h.postDelayed(() -> { setState(ConnectionState.CONNECTED); emit(); }, 800);
    }
    @Override public void disconnect() { setState(ConnectionState.DISCONNECTED); }
    @Override public boolean isConnected() { return state == ConnectionState.CONNECTED; }
    @Override public SensorData getLatestSensorData() { return latest; }
    @Override public void startListening(Listener listener) { l = listener; h.removeCallbacks(tick); h.postDelayed(tick, 2000); }
    @Override public void stopListening() { h.removeCallbacksAndMessages(null); l = null; }
}
