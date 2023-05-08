package com.example.lab3.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.lab3.database.DAO.CourtDao;
import com.example.lab3.database.DAO.CourtDao_Impl;
import com.example.lab3.database.DAO.CourtTimeDAO;
import com.example.lab3.database.DAO.CourtTimeDAO_Impl;
import com.example.lab3.database.DAO.ReservationDAO;
import com.example.lab3.database.DAO.ReservationDAO_Impl;
import com.example.lab3.database.DAO.UserDao;
import com.example.lab3.database.DAO.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile CourtDao _courtDao;

  private volatile ReservationDAO _reservationDAO;

  private volatile CourtTimeDAO _courtTimeDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `courtTime` (`courtId` INTEGER NOT NULL, `startTime` INTEGER NOT NULL, `endTime` INTEGER NOT NULL, `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `court` (`name` TEXT NOT NULL, `address` TEXT NOT NULL, `sport` TEXT NOT NULL, `courtId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `reservation` (`courtTimeId` INTEGER NOT NULL, `userId` INTEGER NOT NULL, `status` INTEGER NOT NULL, `date` INTEGER NOT NULL, `description` TEXT NOT NULL, `resId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `user` (`name` TEXT NOT NULL, `surname` TEXT NOT NULL, `tel` TEXT, `userId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'c0c1d797dfabb3da6ef8413e123392fe')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `courtTime`");
        _db.execSQL("DROP TABLE IF EXISTS `court`");
        _db.execSQL("DROP TABLE IF EXISTS `reservation`");
        _db.execSQL("DROP TABLE IF EXISTS `user`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsCourtTime = new HashMap<String, TableInfo.Column>(4);
        _columnsCourtTime.put("courtId", new TableInfo.Column("courtId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourtTime.put("startTime", new TableInfo.Column("startTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourtTime.put("endTime", new TableInfo.Column("endTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourtTime.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCourtTime = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCourtTime = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCourtTime = new TableInfo("courtTime", _columnsCourtTime, _foreignKeysCourtTime, _indicesCourtTime);
        final TableInfo _existingCourtTime = TableInfo.read(_db, "courtTime");
        if (! _infoCourtTime.equals(_existingCourtTime)) {
          return new RoomOpenHelper.ValidationResult(false, "courtTime(com.example.lab3.database.entity.CourtTime).\n"
                  + " Expected:\n" + _infoCourtTime + "\n"
                  + " Found:\n" + _existingCourtTime);
        }
        final HashMap<String, TableInfo.Column> _columnsCourt = new HashMap<String, TableInfo.Column>(4);
        _columnsCourt.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourt.put("address", new TableInfo.Column("address", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourt.put("sport", new TableInfo.Column("sport", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsCourt.put("courtId", new TableInfo.Column("courtId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysCourt = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesCourt = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoCourt = new TableInfo("court", _columnsCourt, _foreignKeysCourt, _indicesCourt);
        final TableInfo _existingCourt = TableInfo.read(_db, "court");
        if (! _infoCourt.equals(_existingCourt)) {
          return new RoomOpenHelper.ValidationResult(false, "court(com.example.lab3.database.entity.Court).\n"
                  + " Expected:\n" + _infoCourt + "\n"
                  + " Found:\n" + _existingCourt);
        }
        final HashMap<String, TableInfo.Column> _columnsReservation = new HashMap<String, TableInfo.Column>(6);
        _columnsReservation.put("courtTimeId", new TableInfo.Column("courtTimeId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservation.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservation.put("status", new TableInfo.Column("status", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservation.put("date", new TableInfo.Column("date", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservation.put("description", new TableInfo.Column("description", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsReservation.put("resId", new TableInfo.Column("resId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysReservation = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesReservation = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoReservation = new TableInfo("reservation", _columnsReservation, _foreignKeysReservation, _indicesReservation);
        final TableInfo _existingReservation = TableInfo.read(_db, "reservation");
        if (! _infoReservation.equals(_existingReservation)) {
          return new RoomOpenHelper.ValidationResult(false, "reservation(com.example.lab3.database.entity.Reservation).\n"
                  + " Expected:\n" + _infoReservation + "\n"
                  + " Found:\n" + _existingReservation);
        }
        final HashMap<String, TableInfo.Column> _columnsUser = new HashMap<String, TableInfo.Column>(4);
        _columnsUser.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("surname", new TableInfo.Column("surname", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("tel", new TableInfo.Column("tel", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUser.put("userId", new TableInfo.Column("userId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUser = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUser = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUser = new TableInfo("user", _columnsUser, _foreignKeysUser, _indicesUser);
        final TableInfo _existingUser = TableInfo.read(_db, "user");
        if (! _infoUser.equals(_existingUser)) {
          return new RoomOpenHelper.ValidationResult(false, "user(com.example.lab3.database.entity.User).\n"
                  + " Expected:\n" + _infoUser + "\n"
                  + " Found:\n" + _existingUser);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "c0c1d797dfabb3da6ef8413e123392fe", "fd1b0c4d8379517363ae549e49e143c4");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "courtTime","court","reservation","user");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `courtTime`");
      _db.execSQL("DELETE FROM `court`");
      _db.execSQL("DELETE FROM `reservation`");
      _db.execSQL("DELETE FROM `user`");
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
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(CourtDao.class, CourtDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(ReservationDAO.class, ReservationDAO_Impl.getRequiredConverters());
    _typeConvertersMap.put(CourtTimeDAO.class, CourtTimeDAO_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public CourtDao courtDao() {
    if (_courtDao != null) {
      return _courtDao;
    } else {
      synchronized(this) {
        if(_courtDao == null) {
          _courtDao = new CourtDao_Impl(this);
        }
        return _courtDao;
      }
    }
  }

  @Override
  public ReservationDAO reservationDao() {
    if (_reservationDAO != null) {
      return _reservationDAO;
    } else {
      synchronized(this) {
        if(_reservationDAO == null) {
          _reservationDAO = new ReservationDAO_Impl(this);
        }
        return _reservationDAO;
      }
    }
  }

  @Override
  public CourtTimeDAO courtTimeDao() {
    if (_courtTimeDAO != null) {
      return _courtTimeDAO;
    } else {
      synchronized(this) {
        if(_courtTimeDAO == null) {
          _courtTimeDAO = new CourtTimeDAO_Impl(this);
        }
        return _courtTimeDAO;
      }
    }
  }
}
