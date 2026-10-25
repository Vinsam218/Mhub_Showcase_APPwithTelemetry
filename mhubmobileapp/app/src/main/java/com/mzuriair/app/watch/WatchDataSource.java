package com.mzuriair.app.watch;
import com.mzuriair.app.data.SensorData;
public interface WatchDataSource {
    interface Listener { void onData(SensorData d); void onState(ConnectionState s); }
    void connect(); void disconnect(); boolean isConnected(); SensorData getLatestSensorData();
    void startListening(Listener l); void stopListening();
}
