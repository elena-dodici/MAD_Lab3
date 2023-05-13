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
import com.example.lab3.database.entity.Court;
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
public final class CourtDao_Impl implements CourtDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Court> __insertionAdapterOfCourt;

  private final EntityDeletionOrUpdateAdapter<Court> __deletionAdapterOfCourt;

  private final EntityDeletionOrUpdateAdapter<Court> __updateAdapterOfCourt;

  private final Converter __converter = new Converter();

  public CourtDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCourt = new EntityInsertionAdapter<Court>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `court` (`name`,`address`,`sport`,`courtId`) VALUES (?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Court value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getAddress() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getAddress());
        }
        if (value.getSport() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSport());
        }
        stmt.bindLong(4, value.getCourtId());
      }
    };
    this.__deletionAdapterOfCourt = new EntityDeletionOrUpdateAdapter<Court>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `court` WHERE `courtId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Court value) {
        stmt.bindLong(1, value.getCourtId());
      }
    };
    this.__updateAdapterOfCourt = new EntityDeletionOrUpdateAdapter<Court>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `court` SET `name` = ?,`address` = ?,`sport` = ?,`courtId` = ? WHERE `courtId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Court value) {
        if (value.getName() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getName());
        }
        if (value.getAddress() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getAddress());
        }
        if (value.getSport() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSport());
        }
        stmt.bindLong(4, value.getCourtId());
        stmt.bindLong(5, value.getCourtId());
      }
    };
  }

  @Override
  public void addCourt(final Court court) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfCourt.insert(court);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteCourt(final Court court) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfCourt.handle(court);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Court court) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfCourt.handle(court);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public LiveData<List<Court>> getAllCourts() {
    final String _sql = "SELECT * FROM court";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"court"}, false, new Callable<List<Court>>() {
      @Override
      public List<Court> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
          final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
          final int _cursorIndexOfCourtId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtId");
          final List<Court> _result = new ArrayList<Court>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Court _item;
            final String _tmpName;
            if (_cursor.isNull(_cursorIndexOfName)) {
              _tmpName = null;
            } else {
              _tmpName = _cursor.getString(_cursorIndexOfName);
            }
            final String _tmpAddress;
            if (_cursor.isNull(_cursorIndexOfAddress)) {
              _tmpAddress = null;
            } else {
              _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
            }
            final String _tmpSport;
            if (_cursor.isNull(_cursorIndexOfSport)) {
              _tmpSport = null;
            } else {
              _tmpSport = _cursor.getString(_cursorIndexOfSport);
            }
            _item = new Court(_tmpName,_tmpAddress,_tmpSport);
            final int _tmpCourtId;
            _tmpCourtId = _cursor.getInt(_cursorIndexOfCourtId);
            _item.setCourtId(_tmpCourtId);
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
  public List<Court> getAllCourtsTest() {
    final String _sql = "SELECT * FROM court";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
      final int _cursorIndexOfCourtId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtId");
      final List<Court> _result = new ArrayList<Court>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Court _item;
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        final String _tmpSport;
        if (_cursor.isNull(_cursorIndexOfSport)) {
          _tmpSport = null;
        } else {
          _tmpSport = _cursor.getString(_cursorIndexOfSport);
        }
        _item = new Court(_tmpName,_tmpAddress,_tmpSport);
        final int _tmpCourtId;
        _tmpCourtId = _cursor.getInt(_cursorIndexOfCourtId);
        _item.setCourtId(_tmpCourtId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<CourtTime> getAllCourtFreeSlotsByCourtIdTest(final int id) {
    final String _sql = "SELECT * FROM courtTime WHERE id NOT IN (SELECT id FROM courtTime JOIN reservation ON courtTime.id = reservation.courtTimeId WHERE courtId=?)";
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

  @Override
  public LiveData<List<CourtTime>> getAllCourtFreeSlotsByCourtId(final int id) {
    final String _sql = "SELECT * FROM courtTime WHERE id NOT IN (SELECT id FROM courtTime JOIN reservation ON courtTime.id = reservation.courtTimeId WHERE courtId=?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[]{"courtTime","reservation"}, false, new Callable<List<CourtTime>>() {
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
  public Court getCourtById(final int id) {
    final String _sql = "SELECT * FROM court WHERE courtId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfAddress = CursorUtil.getColumnIndexOrThrow(_cursor, "address");
      final int _cursorIndexOfSport = CursorUtil.getColumnIndexOrThrow(_cursor, "sport");
      final int _cursorIndexOfCourtId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtId");
      final Court _result;
      if(_cursor.moveToFirst()) {
        final String _tmpName;
        if (_cursor.isNull(_cursorIndexOfName)) {
          _tmpName = null;
        } else {
          _tmpName = _cursor.getString(_cursorIndexOfName);
        }
        final String _tmpAddress;
        if (_cursor.isNull(_cursorIndexOfAddress)) {
          _tmpAddress = null;
        } else {
          _tmpAddress = _cursor.getString(_cursorIndexOfAddress);
        }
        final String _tmpSport;
        if (_cursor.isNull(_cursorIndexOfSport)) {
          _tmpSport = null;
        } else {
          _tmpSport = _cursor.getString(_cursorIndexOfSport);
        }
        _result = new Court(_tmpName,_tmpAddress,_tmpSport);
        final int _tmpCourtId;
        _tmpCourtId = _cursor.getInt(_cursorIndexOfCourtId);
        _result.setCourtId(_tmpCourtId);
      } else {
        _result = null;
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
