package com.mzuriair.app.data.db;
import androidx.room.*; import java.util.List;
@Dao public interface TelemetryDao { @Insert void insert(TelemetryEntity e); @Query("SELECT * FROM telemetry ORDER BY ts DESC LIMIT :limit") List<TelemetryEntity> recent(int limit); @Query("DELETE FROM telemetry WHERE ts < :cutoff") void deleteOlder(long cutoff); }
