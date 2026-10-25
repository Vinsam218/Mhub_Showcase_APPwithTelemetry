package com.mzuriair.app.data.db;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile TelemetryDao _telemetryDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `telemetry` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `device` TEXT, `ts` INTEGER NOT NULL, `seq` INTEGER NOT NULL, `risk` TEXT, `aqi` INTEGER NOT NULL, `reasons` INTEGER NOT NULL, `dominant` TEXT, `no2` REAL, `co` REAL, `nh3` REAL, `ethanol` REAL, `benzene` REAL, `vocs` REAL, `pm25` REAL, `temp` REAL, `humidity` REAL, `hr` REAL, `wheezeDb` REAL, `wheeze` INTEGER, `activity` TEXT, `battery` INTEGER NOT NULL, `rssi` INTEGER NOT NULL, `charging` INTEGER NOT NULL, `simulated` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '01c0fff1a2f7f9e338fdfea799af0c3f')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `telemetry`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsTelemetry = new HashMap<String, TableInfo.Column>(25);
        _columnsTelemetry.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("device", new TableInfo.Column("device", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("ts", new TableInfo.Column("ts", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("seq", new TableInfo.Column("seq", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("risk", new TableInfo.Column("risk", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("aqi", new TableInfo.Column("aqi", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("reasons", new TableInfo.Column("reasons", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("dominant", new TableInfo.Column("dominant", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("no2", new TableInfo.Column("no2", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("co", new TableInfo.Column("co", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("nh3", new TableInfo.Column("nh3", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("ethanol", new TableInfo.Column("ethanol", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("benzene", new TableInfo.Column("benzene", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("vocs", new TableInfo.Column("vocs", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("pm25", new TableInfo.Column("pm25", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("temp", new TableInfo.Column("temp", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("humidity", new TableInfo.Column("humidity", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("hr", new TableInfo.Column("hr", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("wheezeDb", new TableInfo.Column("wheezeDb", "REAL", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("wheeze", new TableInfo.Column("wheeze", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("activity", new TableInfo.Column("activity", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("battery", new TableInfo.Column("battery", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("rssi", new TableInfo.Column("rssi", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("charging", new TableInfo.Column("charging", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTelemetry.put("simulated", new TableInfo.Column("simulated", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTelemetry = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTelemetry = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTelemetry = new TableInfo("telemetry", _columnsTelemetry, _foreignKeysTelemetry, _indicesTelemetry);
        final TableInfo _existingTelemetry = TableInfo.read(db, "telemetry");
        if (!_infoTelemetry.equals(_existingTelemetry)) {
          return new RoomOpenHelper.ValidationResult(false, "telemetry(com.mzuriair.app.data.db.TelemetryEntity).\n"
                  + " Expected:\n" + _infoTelemetry + "\n"
                  + " Found:\n" + _existingTelemetry);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "01c0fff1a2f7f9e338fdfea799af0c3f", "d384af9180f50c11dfd63df1af58eca6");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "telemetry");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `telemetry`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(TelemetryDao.class, TelemetryDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public TelemetryDao telemetryDao() {
    if (_telemetryDao != null) {
      return _telemetryDao;
    } else {
      synchronized(this) {
        if(_telemetryDao == null) {
          _telemetryDao = new TelemetryDao_Impl(this);
        }
        return _telemetryDao;
      }
    }
  }
}
