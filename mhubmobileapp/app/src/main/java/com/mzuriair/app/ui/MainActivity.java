package com.mzuriair.app.ui;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.mzuriair.app.R;
import com.mzuriair.app.data.*;
import com.mzuriair.app.databinding.ActivityMainBinding;
import com.mzuriair.app.databinding.ItemRowBinding;
import com.mzuriair.app.watch.ConnectionState;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/** Watch-style home screen. Renders ONLY what SensorRepository provides. */
public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding b;
    private final SensorRepository repo = SensorRepository.get();

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        repo.state().observe(this, this::renderState);
        repo.data().observe(this, this::render);
        b.btnSafe.setOnClickListener(v -> repo.setScenario(DemoScenario.SAFE));
        b.btnModerate.setOnClickListener(v -> repo.setScenario(DemoScenario.MODERATE));
        b.btnCritical.setOnClickListener(v -> repo.setScenario(DemoScenario.CRITICAL));
    }
    @Override protected void onStart() { super.onStart(); repo.start(); }
    @Override protected void onStop() { super.onStop(); if (isFinishing()) repo.stop(); }

    private int color(@ColorRes int id) { return ContextCompat.getColor(this, id); }

    private void renderState(ConnectionState st) {
        int c = st == ConnectionState.CONNECTED ? R.color.risk_safe
              : st == ConnectionState.SYNCING ? R.color.risk_moderate : R.color.risk_critical;
        b.dot.setBackgroundTintList(ColorStateList.valueOf(color(c)));
        b.tvConn.setText(st == ConnectionState.CONNECTED ? R.string.watch_connected
              : st == ConnectionState.SYNCING ? R.string.watch_syncing : R.string.watch_disconnected);
    }

    private void render(SensorData d) {
        int c = color(d.riskLevel == com.mzuriair.app.risk.RiskLevel.SAFE ? R.color.risk_safe
              : d.riskLevel == com.mzuriair.app.risk.RiskLevel.MODERATE ? R.color.risk_moderate : R.color.risk_critical);
        b.tvTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(d.timestamp)));
        b.watchFace.setStrokeColor(c);
        b.watchFace.setContentDescription(d.riskLevel.label + ", index " + d.airQualityIndex);
        b.tvRisk.setText(d.riskLevel.label); b.tvRisk.setTextColor(c);
        b.tvAqi.setText(String.valueOf(d.airQualityIndex));
        b.tvMessage.setText(d.recommendations[0]);
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < d.recommendations.length; i++) sb.append(i > 1 ? "\n" : "").append("• ").append(d.recommendations[i]);
        b.tvRecs.setText(sb);

        b.envList.removeAllViews();
        row(b.envList, "Temperature", String.format(Locale.US, "%.1f °C", d.temperature));
        row(b.envList, "Humidity", d.humidity + " %");
        row(b.envList, "CO", String.format(Locale.US, "%.1f ppm", d.carbonMonoxide));
        row(b.envList, "NO2", String.format(Locale.US, "%.3f ppm", d.nitrogenDioxide));
        row(b.envList, "NH3", String.format(Locale.US, "%.1f ppm", d.ammonia));
        row(b.envList, "Benzene", String.format(Locale.US, "%.4f ppm", d.benzene));
        row(b.envList, "Ethanol", String.format(Locale.US, "%.1f ppm", d.ethanol));
        row(b.envList, "Alcohol", String.format(Locale.US, "%.1f ppm", d.alcohol));
        row(b.envList, "VOC", String.format(Locale.US, "%.2f ppm", d.voc));
        b.vitalList.removeAllViews();
        row(b.vitalList, "Heart rate", d.heartRate + " BPM");
        row(b.vitalList, "SpO2", d.spo2 + " %");
        row(b.vitalList, "Wheezing", d.wheezingLabel);
        row(b.vitalList, "Wheezing level", d.wheezingLevel);
    }

    private void row(LinearLayout parent, String label, String value) {
        ItemRowBinding r = ItemRowBinding.inflate(getLayoutInflater(), parent, false);
        r.label.setText(label); r.value.setText(value);
        parent.addView(r.getRoot());
    }
}
