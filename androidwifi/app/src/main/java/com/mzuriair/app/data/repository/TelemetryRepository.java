package com.mzuriair.app.data.repository;
import android.content.Context; import androidx.room.Room; import com.mzuriair.app.connectivity.*; import com.mzuriair.app.data.db.*; import com.mzuriair.app.data.model.Telemetry; import java.util.*; import java.util.concurrent.*;
public class TelemetryRepository { private final WatchDataSource source; private final TelemetryDao dao; private final ExecutorService ex=Executors.newSingleThreadExecutor();
 public TelemetryRepository(Context c,WatchDataSource s){source=s;dao=Room.databaseBuilder(c,AppDatabase.class,"mzuriair.db").build().telemetryDao();}
 public void latest(WatchDataSource.Callback cb){source.fetchLatest(new WatchDataSource.Callback(){
  public void onTelemetry(Telemetry t){if(t!=null)ex.execute(()->dao.insert(map(t)));cb.onTelemetry(t);}
  public void onError(String m){cb.onError(m);}});}
 public void history(long f,long t,WatchDataSource.CallbackHistory cb){source.fetchHistory(f,t,cb);} public void sos(WatchDataSource.Callback cb){source.sendSos(cb);}
 public List<TelemetryEntity> cached(){try{return ex.submit(()->dao.recent(500)).get(3,TimeUnit.SECONDS);}catch(Exception e){return Collections.emptyList();}}
 private TelemetryEntity map(Telemetry t){TelemetryEntity e=new TelemetryEntity();e.device=t.device;e.ts=t.ts;e.seq=t.seq;e.simulated=t.simulated;if(t.risk!=null){e.risk= t.risk.level;e.aqi=t.risk.aqi;e.dominant=t.risk.dominant;e.reasons=t.risk.reasons;}if(t.metrics!=null){e.no2=t.metrics.get("NO2");e.co=t.metrics.get("CO");e.nh3=t.metrics.
         get("NH3");e.ethanol=t.metrics.get("Ethanol");e.benzene=t.metrics.get("Benzene");e.vocs=t.metrics.get("VOCs");e.pm25=t.metrics.get("PM2.5");e.temp=t.metrics.get("Temp");e.
         humidity=t.metrics.get("Humidity");}if(t.vitals!=null){e.hr=t.vitals.hr;e.wheeze=t.vitals.wheeze;e.wheezeDb=t.vitals.wheeze_db;e.activity=t.vitals.activity;}
  if(t.device_status!=null){e.battery=t.device_status.battery;e.charging=t.device_status.charging;e.rssi=t.device_status.rssi;}return e;}
}
