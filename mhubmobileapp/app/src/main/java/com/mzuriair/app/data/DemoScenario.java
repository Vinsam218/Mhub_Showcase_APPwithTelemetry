package com.mzuriair.app.data;
import com.mzuriair.app.risk.RiskEngine;
/** DEMONSTRATION DATA ONLY - not medically validated. Also holds the recommendation text. */
public enum DemoScenario {
    SAFE(12, 21.0, 45, 72, 98, false, "Not detected", "0 dB demo value", 2.0, 0.020, 2.0, 0.005, 1.5, 0.8, 0.05,
        "No need for alarm", "Continue monitoring your environment.",
        "Your current readings are within the demonstration SAFE profile."),
    MODERATE(48, 23.0, 58, 82, 96, true, "Detected intermittently", "moderate demo value", 7.0, 0.060, 8.0, 0.015, 5.0, 3.0, 0.18,
        "Consider reducing exposure to the current environment.", "Ventilate your space if it is safe to do so.",
        "Monitor your breathing and follow your personal asthma action plan.",
        "Consider using appropriate respiratory protection if advised for your environment.",
        "Move to cleaner air if symptoms begin or exposure continues."),
    CRITICAL(92, 25.0, 70, 105, 91, true, "Detected", "high demo value", 39.7, 0.18, 19.5, 0.0397, 19.1, 3.3, 0.45,
        "Move away from the suspected exposure and seek cleaner air.", "Follow your personal asthma action plan.",
        "Keep your prescribed reliever medication accessible.", "Have someone nearby if symptoms are worsening.",
        "If severe breathing difficulty or other emergency symptoms occur, seek emergency medical care.");

    private final int aqi, hum, hr, spo2; private final double temp, co, no2, nh3, bz, et, al, voc;
    private final boolean wheeze; private final String wLabel, wLevel; private final String[] recs;
    DemoScenario(int aqi, double temp, int hum, int hr, int spo2, boolean wheeze, String wLabel, String wLevel,
                 double co, double no2, double nh3, double bz, double et, double al, double voc, String... recs) {
        this.aqi = aqi; this.temp = temp; this.hum = hum; this.hr = hr; this.spo2 = spo2; this.wheeze = wheeze;
        this.wLabel = wLabel; this.wLevel = wLevel; this.co = co; this.no2 = no2; this.nh3 = nh3; this.bz = bz;
        this.et = et; this.al = al; this.voc = voc; this.recs = recs;
    }
    public SensorData toData(RiskEngine engine, long ts) {
        return new SensorData(ts, engine.classify(aqi), aqi, temp, hum, hr, spo2, wheeze, wLabel, wLevel,
                co, no2, nh3, bz, et, al, voc, recs);
    }
}
