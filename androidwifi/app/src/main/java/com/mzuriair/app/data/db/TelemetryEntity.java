package com.mzuriair.app.data.db;
import androidx.room.Entity; import androidx.room.PrimaryKey;
@Entity(tableName="telemetry") public class TelemetryEntity {
 @PrimaryKey(autoGenerate=true) public long id; public String device; public long ts,seq; public String risk; public int aqi,reasons; public String dominant; public Double no2,co,nh3,ethanol,benzene,vocs,pm25,temp,humidity,hr,wheezeDb; public Boolean wheeze; public String activity; public int battery,rssi; public boolean charging,simulated;
}
