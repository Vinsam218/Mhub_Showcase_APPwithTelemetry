package com.mzuriair.app.data.db;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class TelemetryDao_Impl implements TelemetryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<TelemetryEntity> __insertionAdapterOfTelemetryEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOlder;

  public TelemetryDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTelemetryEntity = new EntityInsertionAdapter<TelemetryEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `telemetry` (`id`,`device`,`ts`,`seq`,`risk`,`aqi`,`reasons`,`dominant`,`no2`,`co`,`nh3`,`ethanol`,`benzene`,`vocs`,`pm25`,`temp`,`humidity`,`hr`,`wheezeDb`,`wheeze`,`activity`,`battery`,`rssi`,`charging`,`simulated`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          final TelemetryEntity entity) {
        statement.bindLong(1, entity.id);
        if (entity.device == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.device);
        }
        statement.bindLong(3, entity.ts);
        statement.bindLong(4, entity.seq);
        if (entity.risk == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.risk);
        }
        statement.bindLong(6, entity.aqi);
        statement.bindLong(7, entity.reasons);
        if (entity.dominant == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.dominant);
        }
        if (entity.no2 == null) {
          statement.bindNull(9);
        } else {
          statement.bindDouble(9, entity.no2);
        }
        if (entity.co == null) {
          statement.bindNull(10);
        } else {
          statement.bindDouble(10, entity.co);
        }
        if (entity.nh3 == null) {
          statement.bindNull(11);
        } else {
          statement.bindDouble(11, entity.nh3);
        }
        if (entity.ethanol == null) {
          statement.bindNull(12);
        } else {
          statement.bindDouble(12, entity.ethanol);
        }
        if (entity.benzene == null) {
          statement.bindNull(13);
        } else {
          statement.bindDouble(13, entity.benzene);
        }
        if (entity.vocs == null) {
          statement.bindNull(14);
        } else {
          statement.bindDouble(14, entity.vocs);
        }
        if (entity.pm25 == null) {
          statement.bindNull(15);
        } else {
          statement.bindDouble(15, entity.pm25);
        }
        if (entity.temp == null) {
          statement.bindNull(16);
        } else {
          statement.bindDouble(16, entity.temp);
        }
        if (entity.humidity == null) {
          statement.bindNull(17);
        } else {
          statement.bindDouble(17, entity.humidity);
        }
        if (entity.hr == null) {
          statement.bindNull(18);
        } else {
          statement.bindDouble(18, entity.hr);
        }
        if (entity.wheezeDb == null) {
          statement.bindNull(19);
        } else {
          statement.bindDouble(19, entity.wheezeDb);
        }
        final Integer _tmp = entity.wheeze == null ? null : (entity.wheeze ? 1 : 0);
        if (_tmp == null) {
          statement.bindNull(20);
        } else {
          statement.bindLong(20, _tmp);
        }
        if (entity.activity == null) {
          statement.bindNull(21);
        } else {
          statement.bindString(21, entity.activity);
        }
        statement.bindLong(22, entity.battery);
        statement.bindLong(23, entity.rssi);
        final int _tmp_1 = entity.charging ? 1 : 0;
        statement.bindLong(24, _tmp_1);
        final int _tmp_2 = entity.simulated ? 1 : 0;
        statement.bindLong(25, _tmp_2);
      }
    };
    this.__preparedStmtOfDeleteOlder = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM telemetry WHERE ts < ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(final TelemetryEntity e) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTelemetryEntity.insert(e);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteOlder(final long cutoff) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOlder.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, cutoff);
    try {
      __db.beginTransaction();
      try {
        _stmt.executeUpdateDelete();
        __db.setTransactionSuccessful();
      } finally {
        __db.endTransaction();
      }
    } finally {
      __preparedStmtOfDeleteOlder.release(_stmt);
    }
  }

  @Override
  public List<TelemetryEntity> recent(final int limit) {
    final String _sql = "SELECT * FROM telemetry ORDER BY ts DESC LIMIT ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, limit);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfDevice = CursorUtil.getColumnIndexOrThrow(_cursor, "device");
      final int _cursorIndexOfTs = CursorUtil.getColumnIndexOrThrow(_cursor, "ts");
      final int _cursorIndexOfSeq = CursorUtil.getColumnIndexOrThrow(_cursor, "seq");
      final int _cursorIndexOfRisk = CursorUtil.getColumnIndexOrThrow(_cursor, "risk");
      final int _cursorIndexOfAqi = CursorUtil.getColumnIndexOrThrow(_cursor, "aqi");
      final int _cursorIndexOfReasons = CursorUtil.getColumnIndexOrThrow(_cursor, "reasons");
      final int _cursorIndexOfDominant = CursorUtil.getColumnIndexOrThrow(_cursor, "dominant");
      final int _cursorIndexOfNo2 = CursorUtil.getColumnIndexOrThrow(_cursor, "no2");
      final int _cursorIndexOfCo = CursorUtil.getColumnIndexOrThrow(_cursor, "co");
      final int _cursorIndexOfNh3 = CursorUtil.getColumnIndexOrThrow(_cursor, "nh3");
      final int _cursorIndexOfEthanol = CursorUtil.getColumnIndexOrThrow(_cursor, "ethanol");
      final int _cursorIndexOfBenzene = CursorUtil.getColumnIndexOrThrow(_cursor, "benzene");
      final int _cursorIndexOfVocs = CursorUtil.getColumnIndexOrThrow(_cursor, "vocs");
      final int _cursorIndexOfPm25 = CursorUtil.getColumnIndexOrThrow(_cursor, "pm25");
      final int _cursorIndexOfTemp = CursorUtil.getColumnIndexOrThrow(_cursor, "temp");
      final int _cursorIndexOfHumidity = CursorUtil.getColumnIndexOrThrow(_cursor, "humidity");
      final int _cursorIndexOfHr = CursorUtil.getColumnIndexOrThrow(_cursor, "hr");
      final int _cursorIndexOfWheezeDb = CursorUtil.getColumnIndexOrThrow(_cursor, "wheezeDb");
      final int _cursorIndexOfWheeze = CursorUtil.getColumnIndexOrThrow(_cursor, "wheeze");
      final int _cursorIndexOfActivity = CursorUtil.getColumnIndexOrThrow(_cursor, "activity");
      final int _cursorIndexOfBattery = CursorUtil.getColumnIndexOrThrow(_cursor, "battery");
      final int _cursorIndexOfRssi = CursorUtil.getColumnIndexOrThrow(_cursor, "rssi");
      final int _cursorIndexOfCharging = CursorUtil.getColumnIndexOrThrow(_cursor, "charging");
      final int _cursorIndexOfSimulated = CursorUtil.getColumnIndexOrThrow(_cursor, "simulated");
      final List<TelemetryEntity> _result = new ArrayList<TelemetryEntity>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final TelemetryEntity _item;
        _item = new TelemetryEntity();
        _item.id = _cursor.getLong(_cursorIndexOfId);
        if (_cursor.isNull(_cursorIndexOfDevice)) {
          _item.device = null;
        } else {
          _item.device = _cursor.getString(_cursorIndexOfDevice);
        }
        _item.ts = _cursor.getLong(_cursorIndexOfTs);
        _item.seq = _cursor.getLong(_cursorIndexOfSeq);
        if (_cursor.isNull(_cursorIndexOfRisk)) {
          _item.risk = null;
        } else {
          _item.risk = _cursor.getString(_cursorIndexOfRisk);
        }
        _item.aqi = _cursor.getInt(_cursorIndexOfAqi);
        _item.reasons = _cursor.getInt(_cursorIndexOfReasons);
        if (_cursor.isNull(_cursorIndexOfDominant)) {
          _item.dominant = null;
        } else {
          _item.dominant = _cursor.getString(_cursorIndexOfDominant);
        }
        if (_cursor.isNull(_cursorIndexOfNo2)) {
          _item.no2 = null;
        } else {
          _item.no2 = _cursor.getDouble(_cursorIndexOfNo2);
        }
        if (_cursor.isNull(_cursorIndexOfCo)) {
          _item.co = null;
        } else {
          _item.co = _cursor.getDouble(_cursorIndexOfCo);
        }
        if (_cursor.isNull(_cursorIndexOfNh3)) {
          _item.nh3 = null;
        } else {
          _item.nh3 = _cursor.getDouble(_cursorIndexOfNh3);
        }
        if (_cursor.isNull(_cursorIndexOfEthanol)) {
          _item.ethanol = null;
        } else {
          _item.ethanol = _cursor.getDouble(_cursorIndexOfEthanol);
        }
        if (_cursor.isNull(_cursorIndexOfBenzene)) {
          _item.benzene = null;
        } else {
          _item.benzene = _cursor.getDouble(_cursorIndexOfBenzene);
        }
        if (_cursor.isNull(_cursorIndexOfVocs)) {
          _item.vocs = null;
        } else {
          _item.vocs = _cursor.getDouble(_cursorIndexOfVocs);
        }
        if (_cursor.isNull(_cursorIndexOfPm25)) {
          _item.pm25 = null;
        } else {
          _item.pm25 = _cursor.getDouble(_cursorIndexOfPm25);
        }
        if (_cursor.isNull(_cursorIndexOfTemp)) {
          _item.temp = null;
        } else {
          _item.temp = _cursor.getDouble(_cursorIndexOfTemp);
        }
        if (_cursor.isNull(_cursorIndexOfHumidity)) {
          _item.humidity = null;
        } else {
          _item.humidity = _cursor.getDouble(_cursorIndexOfHumidity);
        }
        if (_cursor.isNull(_cursorIndexOfHr)) {
          _item.hr = null;
        } else {
          _item.hr = _cursor.getDouble(_cursorIndexOfHr);
        }
        if (_cursor.isNull(_cursorIndexOfWheezeDb)) {
          _item.wheezeDb = null;
        } else {
          _item.wheezeDb = _cursor.getDouble(_cursorIndexOfWheezeDb);
        }
        final Integer _tmp;
        if (_cursor.isNull(_cursorIndexOfWheeze)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getInt(_cursorIndexOfWheeze);
        }
        _item.wheeze = _tmp == null ? null : _tmp != 0;
        if (_cursor.isNull(_cursorIndexOfActivity)) {
          _item.activity = null;
        } else {
          _item.activity = _cursor.getString(_cursorIndexOfActivity);
        }
        _item.battery = _cursor.getInt(_cursorIndexOfBattery);
        _item.rssi = _cursor.getInt(_cursorIndexOfRssi);
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfCharging);
        _item.charging = _tmp_1 != 0;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfSimulated);
        _item.simulated = _tmp_2 != 0;
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
