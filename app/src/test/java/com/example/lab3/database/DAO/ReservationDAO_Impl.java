package com.example.lab3.database.DAO;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.lab3.database.Converter;
import com.example.lab3.database.entity.MyReservation;
import com.example.lab3.database.entity.Reservation;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class ReservationDAO_Impl implements ReservationDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Reservation> __insertionAdapterOfReservation;

  private final Converter __converter = new Converter();

  private final EntityDeletionOrUpdateAdapter<Reservation> __updateAdapterOfReservation;

  private final SharedSQLiteStatement __preparedStmtOfDeleteReservation;

  public ReservationDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfReservation = new EntityInsertionAdapter<Reservation>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `reservation` (`courtTimeId`,`userId`,`status`,`date`,`description`,`resId`) VALUES (?,?,?,?,?,nullif(?, 0))";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Reservation value) {
        stmt.bindLong(1, value.getCourtTimeId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getStatus());
        final Long _tmp = __converter.LocalDate2Long(value.getDate());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.getDescription() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDescription());
        }
        stmt.bindLong(6, value.getResId());
      }
    };
    this.__updateAdapterOfReservation = new EntityDeletionOrUpdateAdapter<Reservation>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `reservation` SET `courtTimeId` = ?,`userId` = ?,`status` = ?,`date` = ?,`description` = ?,`resId` = ? WHERE `resId` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Reservation value) {
        stmt.bindLong(1, value.getCourtTimeId());
        stmt.bindLong(2, value.getUserId());
        stmt.bindLong(3, value.getStatus());
        final Long _tmp = __converter.LocalDate2Long(value.getDate());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.getDescription() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDescription());
        }
        stmt.bindLong(6, value.getResId());
        stmt.bindLong(7, value.getResId());
      }
    };
    this.__preparedStmtOfDeleteReservation = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE reservation SET status = 1 WHERE resId=?";
        return _query;
      }
    };
  }

  @Override
  public void addReservation(final Reservation reservation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfReservation.insert(reservation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final Reservation reservation) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfReservation.handle(reservation);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteReservation(final int id) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteReservation.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, id);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteReservation.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Reservation>> getAllReservations() {
    final String _sql = "SELECT * FROM reservation";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"reservation"}, false, new Callable<List<Reservation>>() {
      @Override
      public List<Reservation> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCourtTimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtTimeId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfResId = CursorUtil.getColumnIndexOrThrow(_cursor, "resId");
          final List<Reservation> _result = new ArrayList<Reservation>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Reservation _item;
            final int _tmpCourtTimeId;
            _tmpCourtTimeId = _cursor.getInt(_cursorIndexOfCourtTimeId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final int _tmpStatus;
            _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
            final LocalDate _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            _tmpDate = __converter.Long2LocalDate(_tmp);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            _item = new Reservation(_tmpCourtTimeId,_tmpUserId,_tmpStatus,_tmpDate,_tmpDescription);
            final int _tmpResId;
            _tmpResId = _cursor.getInt(_cursorIndexOfResId);
            _item.setResId(_tmpResId);
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
  public List<Reservation> getAllReservationsTest() {
    final String _sql = "SELECT * FROM reservation";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCourtTimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtTimeId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfResId = CursorUtil.getColumnIndexOrThrow(_cursor, "resId");
      final List<Reservation> _result = new ArrayList<Reservation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Reservation _item;
        final int _tmpCourtTimeId;
        _tmpCourtTimeId = _cursor.getInt(_cursorIndexOfCourtTimeId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final int _tmpStatus;
        _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        final LocalDate _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = __converter.Long2LocalDate(_tmp);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item = new Reservation(_tmpCourtTimeId,_tmpUserId,_tmpStatus,_tmpDate,_tmpDescription);
        final int _tmpResId;
        _tmpResId = _cursor.getInt(_cursorIndexOfResId);
        _item.setResId(_tmpResId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Reservation> getReservationsByUserIdTest(final int id) {
    final String _sql = "SELECT * FROM reservation WHERE userId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCourtTimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtTimeId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfResId = CursorUtil.getColumnIndexOrThrow(_cursor, "resId");
      final List<Reservation> _result = new ArrayList<Reservation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Reservation _item;
        final int _tmpCourtTimeId;
        _tmpCourtTimeId = _cursor.getInt(_cursorIndexOfCourtTimeId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final int _tmpStatus;
        _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        final LocalDate _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = __converter.Long2LocalDate(_tmp);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item = new Reservation(_tmpCourtTimeId,_tmpUserId,_tmpStatus,_tmpDate,_tmpDescription);
        final int _tmpResId;
        _tmpResId = _cursor.getInt(_cursorIndexOfResId);
        _item.setResId(_tmpResId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Reservation> getReservationsByCourtIdTest(final int id) {
    final String _sql = "SELECT *FROM reservation WHERE courtTimeId IN(SELECT courtTimeId FROM courtTime JOIN reservation ON courtTime.id = reservation.courtTimeId WHERE courtId=?)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCourtTimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtTimeId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfResId = CursorUtil.getColumnIndexOrThrow(_cursor, "resId");
      final List<Reservation> _result = new ArrayList<Reservation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Reservation _item;
        final int _tmpCourtTimeId;
        _tmpCourtTimeId = _cursor.getInt(_cursorIndexOfCourtTimeId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final int _tmpStatus;
        _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        final LocalDate _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = __converter.Long2LocalDate(_tmp);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item = new Reservation(_tmpCourtTimeId,_tmpUserId,_tmpStatus,_tmpDate,_tmpDescription);
        final int _tmpResId;
        _tmpResId = _cursor.getInt(_cursorIndexOfResId);
        _item.setResId(_tmpResId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public LiveData<List<Reservation>> getReservationsByCourtId(final int id) {
    final String _sql = "SELECT * FROM reservation,(SELECT * FROM courtTime WHERE courtId=?) AS cts WHERE reservation.courtTimeId = cts.id";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[]{"reservation","courtTime"}, false, new Callable<List<Reservation>>() {
      @Override
      public List<Reservation> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCourtTimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtTimeId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfResId = CursorUtil.getColumnIndexOrThrow(_cursor, "resId");
          final List<Reservation> _result = new ArrayList<Reservation>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Reservation _item;
            final int _tmpCourtTimeId;
            _tmpCourtTimeId = _cursor.getInt(_cursorIndexOfCourtTimeId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final int _tmpStatus;
            _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
            final LocalDate _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            _tmpDate = __converter.Long2LocalDate(_tmp);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            _item = new Reservation(_tmpCourtTimeId,_tmpUserId,_tmpStatus,_tmpDate,_tmpDescription);
            final int _tmpResId;
            _tmpResId = _cursor.getInt(_cursorIndexOfResId);
            _item.setResId(_tmpResId);
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
  public LiveData<List<Reservation>> getReservationsByUserId(final int id) {
    final String _sql = "SELECT * FROM reservation WHERE userId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    return __db.getInvalidationTracker().createLiveData(new String[]{"reservation"}, false, new Callable<List<Reservation>>() {
      @Override
      public List<Reservation> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfCourtTimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtTimeId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfResId = CursorUtil.getColumnIndexOrThrow(_cursor, "resId");
          final List<Reservation> _result = new ArrayList<Reservation>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Reservation _item;
            final int _tmpCourtTimeId;
            _tmpCourtTimeId = _cursor.getInt(_cursorIndexOfCourtTimeId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            final int _tmpStatus;
            _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
            final LocalDate _tmpDate;
            final Long _tmp;
            if (_cursor.isNull(_cursorIndexOfDate)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getLong(_cursorIndexOfDate);
            }
            _tmpDate = __converter.Long2LocalDate(_tmp);
            final String _tmpDescription;
            if (_cursor.isNull(_cursorIndexOfDescription)) {
              _tmpDescription = null;
            } else {
              _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            }
            _item = new Reservation(_tmpCourtTimeId,_tmpUserId,_tmpStatus,_tmpDate,_tmpDescription);
            final int _tmpResId;
            _tmpResId = _cursor.getInt(_cursorIndexOfResId);
            _item.setResId(_tmpResId);
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
  public Reservation getReservationById(final int id) {
    final String _sql = "SELECT * FROM reservation WHERE resId=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfCourtTimeId = CursorUtil.getColumnIndexOrThrow(_cursor, "courtTimeId");
      final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
      final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
      final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfResId = CursorUtil.getColumnIndexOrThrow(_cursor, "resId");
      final Reservation _result;
      if(_cursor.moveToFirst()) {
        final int _tmpCourtTimeId;
        _tmpCourtTimeId = _cursor.getInt(_cursorIndexOfCourtTimeId);
        final int _tmpUserId;
        _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
        final int _tmpStatus;
        _tmpStatus = _cursor.getInt(_cursorIndexOfStatus);
        final LocalDate _tmpDate;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = __converter.Long2LocalDate(_tmp);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _result = new Reservation(_tmpCourtTimeId,_tmpUserId,_tmpStatus,_tmpDate,_tmpDescription);
        final int _tmpResId;
        _tmpResId = _cursor.getInt(_cursorIndexOfResId);
        _result.setResId(_tmpResId);
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
  public List<MyReservation> getReservationByUserId(final int id) {
    final String _sql = "SELECT r.resId, name, address, sport, startTime, endTime, date, description\n"
            + "FROM reservation as r, court as c, courtTime as ct\n"
            + "WHERE r.courtTimeId=ct.id and ct.courtId=c.courtId and userId=? ";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfResId = 0;
      final int _cursorIndexOfName = 1;
      final int _cursorIndexOfAddress = 2;
      final int _cursorIndexOfSport = 3;
      final int _cursorIndexOfStartTime = 4;
      final int _cursorIndexOfEndTime = 5;
      final int _cursorIndexOfDate = 6;
      final int _cursorIndexOfDescription = 7;
      final List<MyReservation> _result = new ArrayList<MyReservation>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final MyReservation _item;
        final int _tmpResId;
        _tmpResId = _cursor.getInt(_cursorIndexOfResId);
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
        final LocalDate _tmpDate;
        final Long _tmp_2;
        if (_cursor.isNull(_cursorIndexOfDate)) {
          _tmp_2 = null;
        } else {
          _tmp_2 = _cursor.getLong(_cursorIndexOfDate);
        }
        _tmpDate = __converter.Long2LocalDate(_tmp_2);
        final String _tmpDescription;
        if (_cursor.isNull(_cursorIndexOfDescription)) {
          _tmpDescription = null;
        } else {
          _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        }
        _item = new MyReservation(_tmpResId,_tmpName,_tmpAddress,_tmpSport,_tmpStartTime,_tmpEndTime,_tmpDate,_tmpDescription);
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
