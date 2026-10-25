package com.mzuriair.app.data.api;
import com.mzuriair.app.data.model.Telemetry;
import retrofit2.Call; import retrofit2.http.*; import java.util.List;
public interface MzuriApi {
 @GET("api/devices/{deviceId}/latest") Call<Telemetry> latest(@Path("deviceId") String id);
 @GET("api/devices/{deviceId}/history") Call<List<Telemetry>> history(@Path("deviceId") String id,@Query("from") long from,@Query("to") long to);
 @GET("api/devices/{deviceId}/trends") Call<List<Telemetry>> trends(@Path("deviceId") String id,@Query("period") String period);
 @GET("api/devices/{deviceId}/status") Call<Telemetry.DeviceStatus> status(@Path("deviceId") String id);
 @POST("api/devices/{deviceId}/sos") Call<Void> sos(@Path("deviceId") String id);
}
