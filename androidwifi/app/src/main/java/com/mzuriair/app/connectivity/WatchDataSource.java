package com.mzuriair.app.connectivity;
import com.mzuriair.app.data.model.Telemetry; import java.util.List;
public interface WatchDataSource { interface Callback { void onTelemetry(Telemetry t); void onError(String message); } void fetchLatest(Callback cb); void fetchHistory(long from,long to,CallbackHistory cb); void sendSos(Callback cb); interface CallbackHistory { void onHistory(List<Telemetry> items); void onError(String message); } }
