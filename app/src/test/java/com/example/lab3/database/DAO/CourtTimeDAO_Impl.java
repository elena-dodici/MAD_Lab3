package com.example.lab3.database.DAO;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.lab3.database.Converter;
import com.example.lab3.database.entity.CourtTime;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class CourtTimeDAO_Impl implements CourtTimeDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CourtTime> __insertionAdapterOfCourtTime;

  private final Converter __converter = new Converter();

  private final EntityDeletionOrUpdateAdapter<CourtTime> __deletionAdapterOfCourtTime;

  private final EntityDeletionOrUpdateAdapter<CourtTime> __updateAdapterOfCourtTime;

  public CourtTimeDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCourtTime = new EntityInsertionAdapter<CourtTime>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `courtTime` (`courtId`,`startTime`,`endTime`,`id`) VALUES (?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CourtTime value) {
        stmt.bindLong(1, value.getCourtId());
        final Long _tmp = __converter.Time2Long(value.getStartTime());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        final Long _tmp_1 = __converter.Time2Long(value.getEndTime());
        if (_tmp_1 == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp_1);
        }
        stmt.bindLong(4, value.getId());
      }
    };
    this.__deletionAdapterOfCourtTime = new EntityDeletionOrUpdateAdapter<CourtTime>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `courtTime` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CourtTime value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfCourtTime = new EntityDeletionOrUpdateAdapter<CourtTime>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `courtTime` SET `courtId` = ?,`startTime` = ?,`endTime` = ?,`id` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, CourtTime value) {
        stmt.bindLong(1, value.getCourtId());
        final Long _tmp = __converter.Time2Long(value.getStartTime());
        if (_tmp == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindLong(2, _tmp);
        }
        final Long _tmp_1 = __converter.Time2Long(value.getEndTime());
        if (_tmp_1 == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindLong(3, _tmp_1);
        }
        stmt.bindLong(4, value.getId());
        stmt.bindLong(5, value.getId());
      }
    };
  }

  @Override
  public void addCourtTime(final CourtTime courtTime) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCourtTime.insert(courtTime);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteCourtTime(final CourtTime courtTime) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCourtTime.handle(courtTime);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final CourtTime courtTime) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCourtTime.handle(courtTime);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<CourtTime>> getAllTCourtTimesByCourtId(final int id) {
    final String _sql = "SELECT * FROM courtTime WHERE courtId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[]{"courtTime"}, false, new Callable<List<CourtTime>>() {
      @Override
      public List<CourtTime> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCourtId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtId");
          final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
          final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final List<CourtTime> _result = new ArrayList<CourtTime>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final CourtTime _item;
            final int _tmpCourtId;
            _tmpCourtId = _cursor.getInt(_cursorIndexOfCourtId);
            final Time _tmpStartTime;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfStartTime)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfStartTime);
            }
            _tmpStartTime = __converter.Long2Time(_tmp);
            final Time _tmpEndTime;
            final Long _tmp_1;
            if (_cursor.isNull(_cursorIndexOfEndTime)) {
              _tmp_1 = null;
            } else {
              _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
            }
            _tmpEndTime = __converter.Long2Time(_tmp_1);
            _item = new CourtTime(_tmpCourtId,_tmpStartTime,_tmpEndTime);
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public CourtTime getCourtTimeById(final int id) {
    final String _sql = "SELECT * FROM courtTime WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCourtId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtId");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final CourtTime _result;
      if(_cursor.moveToFirst()) {
        final int _tmpCourtId;
        _tmpCourtId = _cursor.getInt(_cursorIndexOfCourtId);
        final Time _tmpStartTime;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfStartTime)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfStartTime);
        }
        _tmpStartTime = __converter.Long2Time(_tmp);
        final Time _tmpEndTime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfEndTime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
        }
        _tmpEndTime = __converter.Long2Time(_tmp_1);
        _result = new CourtTime(_tmpCourtId,_tmpStartTime,_tmpEndTime);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<CourtTime> getAllTCourtTimesByCourtIdTest(final int id) {
    final String _sql = "SELECT * FROM courtTime WHERE courtId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCourtId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtId");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "startTime");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "endTime");
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final List<CourtTime> _result = new ArrayList<CourtTime>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final CourtTime _item;
        final int _tmpCourtId;
        _tmpCourtId = _cursor.getInt(_cursorIndexOfCourtId);
        final Time _tmpStartTime;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfStartTime)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfStartTime);
        }
        _tmpStartTime = __converter.Long2Time(_tmp);
        final Time _tmpEndTime;
        final Long _tmp_1;
        if (_cursor.isNull(_cursorIndexOfEndTime)) {
          _tmp_1 = null;
        } else {
          _tmp_1 = _cursor.getLong(_cursorIndexOfEndTime);
        }
        _tmpEndTime = __converter.Long2Time(_tmp_1);
        _item = new CourtTime(_tmpCourtId,_tmpStartTime,_tmpEndTime);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
