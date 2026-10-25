package com.mzuriair.app;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.mzuriair.app.connectivity.*;
import com.mzuriair.app.data.api.MzuriApi; import com.mzuriair.app.data.model.Telemetry;
import com.mzuriair.app.data.repository.TelemetryRepository; import com.mzuriair.app.presentation.*;
import retrofit2.Retrofit; import retrofit2.converter.gson.GsonConverterFactory;
import java.text.*; import java.util.*;

public class MainActivity extends AppCompatActivity {
 static final int BG=Color.BLACK,SURFACE=Color.rgb(16,21,28),SURFACE2=Color.rgb(25,32,42),TEXT=Color.rgb(245,247,250),MUTED=Color.rgb(138,148,166),CYAN=Color.rgb(46,211,230),BLUE=Color.rgb(91,127,232),SAFE=Color.rgb(34,197,94),MOD=Color.rgb(245,158,11),CRIT=Color.rgb(239,68,68),HEART=Color.rgb(255,77,109),UNKNOWN=Color.rgb(100,116,139);
 LinearLayout root,content; MainViewModel vm; TextView sync;
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(BG);getWindow().setNavigationBarColor(BG); build();}
 TextView tv(String s,float sp,int c){TextView v=new TextView(this);v.setText(s);v.setTextSize(sp);v.setTextColor(c);v.setPadding(0,4,0,4);return v;}
 LinearLayout col(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(20,18,20,12);return l;}
 MaterialCardView card(){MaterialCardView c=new MaterialCardView(this);c.setCardBackgroundColor(SURFACE);c.setStrokeColor(Color.rgb(37,46,58));c.setStrokeWidth(1);c.setRadius(22);c.setContentPadding(18,16,18,16);return c;}
 void build(){root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(BG); content=col(); ScrollView sv=new ScrollView(this);sv.addView(content);root.addView(sv,new LinearLayout.LayoutParams(-1,0,1)); BottomNavigationView nav=new BottomNavigationView(this);nav.setBackgroundColor(BG);nav.getMenu().add("Home").setIcon(android.R.drawable.ic_menu_view);nav.getMenu().add("Live").setIcon(android.R.drawable.ic_menu_info_details);nav.getMenu().add("Trends").setIcon(android.R.drawable.ic_menu_sort_by_size);nav.getMenu().add("Insights").setIcon(android.R.drawable.ic_menu_help);nav.getMenu().add("Device").setIcon(android.R.drawable.ic_menu_manage);nav.setOnItemSelectedListener(i->{render(i.getTitle().toString());return true;});root.addView(nav,new LinearLayout.LayoutParams(-1,64));setContentView(root);
  Retrofit rt=new Retrofit.Builder().baseUrl("http://10.0.0.1:8000/").addConverterFactory(GsonConverterFactory.create()).build(); MzuriApi api=rt.create(MzuriApi.class);
  TelemetryRepository repo=new TelemetryRepository(this,new TelemetryApiDataSource(api,"mzuriair-001"));
  vm=new ViewModelProvider(this,new MainViewModelFactory(repo)).get(MainViewModel.class);
  vm.telemetry.observe(this,t->{
   if(t != null){
    homeCard(t);
   }
  });
  vm.error.observe(this,e->{if(e!=null) Toast.makeText(this,e,Toast.LENGTH_LONG).show();});
  render("Home"); }
 void header(String title,String sub){content.removeAllViews(); LinearLayout h=col();h.setPadding(20,22,20,4); TextView brand=tv("MZURI AIR",22,CYAN);brand.setTypeface(null,1);h.addView(brand);h.addView(tv(title,28,TEXT));sync=tv(sub,13,MUTED);h.addView(sync);content.addView(h);}
 void render(String page){if(page.equals("Home")){header("Good day","Watch risk status • API source");home();}else if(page.equals("Live")){header("Live Readings","Exact values received from the watch");live();}else if(page.equals("Trends")){header("Trends","Historical data from the telemetry API");trends();}else if(page.equals("Insights")){header("Insights","Why the watch classified your current state");insights();}else{header("Device","MzuriAIR watch and connection");device();}}
 void home(){ TextView loading=tv("Syncing with MzuriAIR…",15,MUTED);content.addView(loading);vm.refresh(); }
 void renderTelemetry(Telemetry t){ if(t==null)return; sync.setText((t.simulated?"DEMO DATA • ":"")+"Last synchronized "+new SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(new Date(t.ts*1000L))); }
 int riskColor(Telemetry t){String r=t.riskLabel();return r.equals("SAFE")?SAFE:r.equals("MODERATE")?MOD:r.equals("CRITICAL")?CRIT:UNKNOWN;}
 String metric(Telemetry t,String k,String unit){Double v=t.metrics==null?null:t.metrics.get(k);return v==null?"—":String.format(Locale.US,"%.2f %s",v,unit);}
 void homeCard(Telemetry t){content.removeAllViews();header("Good day",(t.simulated?"DEMO DATA • ":"")+"Last synchronized "+new SimpleDateFormat("HH:mm:ss",Locale.getDefault()).format(new Date(t.ts*1000L))); MaterialCardView c=card();LinearLayout l=col();TextView a=tv("AIR QUALITY",12,MUTED);l.addView(a);TextView n=tv(t.risk==null?"—":String.valueOf(t.risk.aqi),58,CYAN);n.setTypeface(null,1);l.addView(n);TextView rl=tv(t.riskLabel()+" RISK",20,riskColor(t));rl.setTypeface(null,1);l.addView(rl);l.addView(tv("Dominant pollutant: "+(t.risk==null?"—":t.risk.dominant),14,TEXT));c.addView(l);content.addView(c); LinearLayout row=new LinearLayout(this);row.setOrientation(LinearLayout.HORIZONTAL);row.addView(small("♥  "+(t.vitals==null||t.vitals.hr==null?"—":String.format(Locale.US,"%.0f BPM",t.vitals.hr)),HEART),new LinearLayout.LayoutParams(0,-2,1));row.addView(small("◉  "+(t.vitals==null||t.vitals.wheeze==null?"—":(t.vitals.wheeze?"Wheeze detected":"Wheeze not detected")),CYAN),new LinearLayout.LayoutParams(0,-2,1));content.addView(row);MaterialCardView rec=card();LinearLayout rl2=col();rl2.addView(tv("WATCH RECOMMENDATION",12,MUTED));for(String s:recommendations(t.riskLabel()))rl2.addView(tv("• "+s,15,TEXT));rec.addView(rl2);content.addView(rec);MaterialCardView why=card();LinearLayout wl=col();wl.addView(tv("WHY THIS MATTERS",12,MUTED));wl.addView(tv(whyText(t),15,TEXT));why.addView(wl);content.addView(why);Button sos=new Button(this);sos.setText("SOS");sos.setTextColor(TEXT);sos.setBackgroundColor(CRIT);sos.setOnClickListener(v->new AlertDialog.Builder(this).setTitle("Send SOS?").setMessage("This sends an SOS event to the configured MzuriAIR backend. It does not automatically contact emergency services.").setNegativeButton("Cancel",null).setPositiveButton("Send",(d,w)->vm.sos()).show());content.addView(sos); }
 LinearLayout small(String s,int c){LinearLayout l=col();l.setPadding(8,12,8,12);TextView v=tv(s,14,c);l.addView(v);return l;}
 String[] recommendations(String r){if(r.equals("SAFE"))return new String[]{"Air is clean. No need for alarm.","Keep your inhaler with you as usual."};if(r.equals("MODERATE"))return new String[]{"Consider wearing a well-fitting filtered mask.","Ventilate your space.","Monitor your breathing."};if(r.equals("CRITICAL"))return new String[]{"Move to cleaner air.","Avoid prolonged outdoor exposure.","Keep your prescribed inhaler accessible.","Have someone nearby if symptoms develop."};return new String[]{"Sensors warming up..."};}
 String whyText(Telemetry t){if(t.risk==null)return "The watch has not published a risk state.";StringBuilder s=new StringBuilder("The watch currently reports ").append(t.riskLabel()).append(". ");if((t.risk.reasons&1)!=0)s.append("AQI contributed. ");if((t.risk.reasons&2)!=0)s.append("Wheeze contributed. ");if((t.risk.reasons&4)!=0)s.append("Heart rate contributed. ");if((t.risk.reasons&8)!=0)s.append("Temperature contributed. ");if((t.risk.reasons&16)!=0)s.append("Humidity contributed. ");if((t.risk.reasons&32)!=0)s.append("There was insufficient data. ");return s.toString();}
 void live(){vm.refresh();TextView note=tv("Waiting for the latest telemetry…",15,MUTED);content.addView(note);vm.telemetry.observe(this,t->{if(t==null)return;content.removeView(note);String[][] ms={{"NO2","ppm"},{"CO","ppm"},{"NH3","ppm"},{"Ethanol","ppm"},{"Benzene","ppm"},{"VOCs","ppm"},{"PM2.5","µg/m³"},{"Temp","°C"},{"Humidity","%"}};for(String[] m:ms){MaterialCardView c=card();LinearLayout l=col();l.addView(tv(m[0],13,MUTED));l.addView(tv(metric(t,m[0],m[1]),25,TEXT));c.addView(l);content.addView(c);}if(t.vitals!=null){MaterialCardView c=card();LinearLayout l=col();l.addView(tv("VITALS",13,MUTED));l.addView(tv("HR: "+(t.vitals.hr==null?"—":t.vitals.hr+" BPM"),20,HEART));l.addView(tv("Wheeze: "+(t.vitals.wheeze==null?"—":t.vitals.wheeze),16,TEXT));l.addView(tv("Activity: "+t.vitals.activity,16,TEXT));c.addView(l);content.addView(c);}});}
 void trends(){MaterialCardView c=card();LinearLayout l=col();l.addView(tv("Historical trend framework",18,TEXT));l.addView(tv("The API contract supports 30 min, 1 h, 6 h, 24 h, 7 d and 30 d periods. The repository preserves timestamp and sequence numbers so a chart can never silently reorder samples.",15,MUTED));l.addView(tv("Live • 30 MIN • 1 HOUR • 6 HOURS • 24 HOURS • 7 DAYS • 30 DAYS",13,CYAN));c.addView(l);content.addView(c);}
 void insights(){vm.refresh();TextView n=tv("Loading watch explanation…",15,MUTED);content.addView(n);vm.telemetry.observe(this,t->{if(t==null)return;content.removeView(n);MaterialCardView c=card();LinearLayout l=col();l.addView(tv("CURRENT WATCH RISK",12,MUTED));l.addView(tv(t.riskLabel(),30,riskColor(t)));l.addView(tv(whyText(t),16,TEXT));if(t.risk!=null)l.addView(tv("AQI "+t.risk.aqi+" • Dominant "+t.risk.dominant,15,CYAN));c.addView(l);content.addView(c);});}
 void device(){MaterialCardView c=card();LinearLayout l=col();l.addView(tv("MzuriAIR Watch",24,TEXT));l.addView(tv("Device ID: mzuriair-001",15,MUTED));l.addView(tv("Transport: Wi-Fi / REST API",15,MUTED));l.addView(tv("BLE: future abstraction only",15,MUTED));l.addView(tv("Firmware risk engine remains authoritative.",15,CYAN));c.addView(l);content.addView(c);}
 @Override public void onBackPressed(){super.onBackPressed();}
}
