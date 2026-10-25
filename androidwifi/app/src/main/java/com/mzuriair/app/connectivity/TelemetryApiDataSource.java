package com.mzuriair.app.connectivity;

import com.mzuriair.app.data.api.MzuriApi;
import com.mzuriair.app.data.model.Telemetry;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class TelemetryApiDataSource implements WatchDataSource {

 private final MzuriApi api;
 private final String id;

 public TelemetryApiDataSource(MzuriApi api, String id) {
  this.api = api;
  this.id = id;
 }

 @Override
 public void fetchLatest(Callback cb) {

  api.latest(id).enqueue(new retrofit2.Callback<Telemetry>() {

   @Override
   public void onResponse(
           Call<Telemetry> c,
           Response<Telemetry> r
   ) {
    if (r.isSuccessful() && r.body() != null) {
     cb.onTelemetry(r.body());
    } else {
     cb.onError("Server returned " + r.code());
    }
   }

   @Override
   public void onFailure(
           Call<Telemetry> c,
           Throwable t
   ) {
    cb.onError(t.getMessage());
   }
  });
 }

 @Override
 public void fetchHistory(
         long f,
         long t,
         CallbackHistory cb
 ) {

  api.history(id, f, t).enqueue(
          new retrofit2.Callback<List<Telemetry>>() {

           @Override
           public void onResponse(
                   Call<List<Telemetry>> c,
                   Response<List<Telemetry>> r
           ) {
            if (r.isSuccessful() && r.body() != null) {
             cb.onHistory(r.body());
            } else {
             cb.onError(
                     "Server returned " + r.code()
             );
            }
           }

           @Override
           public void onFailure(
                   Call<List<Telemetry>> c,
                   Throwable t
           ) {
            cb.onError(t.getMessage());
           }
          }
  );
 }

 @Override
 public void sendSos(Callback cb) {

  api.sos(id).enqueue(
          new retrofit2.Callback<Void>() {

           @Override
           public void onResponse(
                   Call<Void> c,
                   Response<Void> r
           ) {
            if (r.isSuccessful()) {
             cb.onTelemetry(null);
            } else {
             cb.onError(
                     "SOS failed: " + r.code()
             );
            }
           }

           @Override
           public void onFailure(
                   Call<Void> c,
                   Throwable t
           ) {
            cb.onError(t.getMessage());
           }
          }
  );
 }
}
