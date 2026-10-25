package com.mzuriair.app.data;
import com.mzuriair.app.risk.RiskLevel;
/** The single source of truth. Every screen reads this object; UI never invents readings. */
public final class SensorData {
    public final long timestamp; public final RiskLevel riskLevel; public final int airQualityIndex;
    public final double temperature; public final int humidity, heartRate, spo2;
    public final boolean wheezingDetected; public final String wheezingLabel, wheezingLevel;
    public final double carbonMonoxide, nitrogenDioxide, ammonia, benzene, ethanol, alcohol, voc;
    public final String[] recommendations;
    public SensorData(long ts, RiskLevel r, int aqi, double t, int h, int hr, int sp, boolean w, String wLabel,
                      String wLevel, double co, double no2, double nh3, double bz, double et, double al,
                      double voc, String[] recs) {
        timestamp = ts; riskLevel = r; airQualityIndex = aqi; temperature = t; humidity = h; heartRate = hr;
        spo2 = sp; wheezingDetected = w; wheezingLabel = wLabel; wheezingLevel = wLevel; carbonMonoxide = co;
        nitrogenDioxide = no2; ammonia = nh3; benzene = bz; ethanol = et; alcohol = al; this.voc = voc;
        recommendations = recs;
    }
}
