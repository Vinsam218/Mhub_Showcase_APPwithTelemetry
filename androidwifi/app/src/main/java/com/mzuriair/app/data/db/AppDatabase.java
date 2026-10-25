package com.mzuriair.app.data.db;
import androidx.room.Database; import androidx.room.RoomDatabase;
@Database(entities={TelemetryEntity.class},version=1,exportSchema=false) public abstract class AppDatabase extends RoomDatabase { public abstract TelemetryDao telemetryDao(); }
