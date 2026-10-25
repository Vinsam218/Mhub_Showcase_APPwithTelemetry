package com.mzuriair.app.data.model;
import com.google.gson.annotations.SerializedName;
import java.util.Map;
public class Telemetry {
 public String device; public long ts; public long seq; public Risk risk; public Map<String,Double> metrics; public Vitals vitals; public DeviceStatus device_status; public boolean simulated; public String event;
 public static class Risk { public String level; public int aqi; public String dominant; public int reasons; }
 public static class Vitals { public Double hr; public Boolean wheeze; public Double wheeze_db; public String activity; }
 public static class DeviceStatus { public int battery; public boolean charging; public int rssi; }
 public boolean isCritical(){ return risk!=null && "critical".equalsIgnoreCase(risk.level); }
 public String riskLabel(){ if(risk==null||risk.level==null)return "UNKNOWN"; return risk.level.toUpperCase(); }
}
