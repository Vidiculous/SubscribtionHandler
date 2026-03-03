package com.subhandler.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SubscriptionDao_Impl implements SubscriptionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SubscriptionEntity> __insertionAdapterOfSubscriptionEntity;

  private final EntityDeletionOrUpdateAdapter<SubscriptionEntity> __updateAdapterOfSubscriptionEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  public SubscriptionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSubscriptionEntity = new EntityInsertionAdapter<SubscriptionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `subscriptions` (`id`,`name`,`cost`,`currency`,`billingCycle`,`customCycleAmount`,`customCycleUnit`,`nextRenewalDate`,`autoRenew`,`reminderDays`,`color`,`notes`,`createdAt`,`updatedAt`,`notificationAlarmId`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubscriptionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getCost());
        statement.bindString(4, entity.getCurrency());
        statement.bindString(5, entity.getBillingCycle());
        if (entity.getCustomCycleAmount() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getCustomCycleAmount());
        }
        if (entity.getCustomCycleUnit() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCustomCycleUnit());
        }
        statement.bindString(8, entity.getNextRenewalDate());
        final int _tmp = entity.getAutoRenew() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getReminderDays());
        statement.bindString(11, entity.getColor());
        if (entity.getNotes() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getNotes());
        }
        statement.bindLong(13, entity.getCreatedAt());
        statement.bindLong(14, entity.getUpdatedAt());
        if (entity.getNotificationAlarmId() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getNotificationAlarmId());
        }
      }
    };
    this.__updateAdapterOfSubscriptionEntity = new EntityDeletionOrUpdateAdapter<SubscriptionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `subscriptions` SET `id` = ?,`name` = ?,`cost` = ?,`currency` = ?,`billingCycle` = ?,`customCycleAmount` = ?,`customCycleUnit` = ?,`nextRenewalDate` = ?,`autoRenew` = ?,`reminderDays` = ?,`color` = ?,`notes` = ?,`createdAt` = ?,`updatedAt` = ?,`notificationAlarmId` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SubscriptionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindDouble(3, entity.getCost());
        statement.bindString(4, entity.getCurrency());
        statement.bindString(5, entity.getBillingCycle());
        if (entity.getCustomCycleAmount() == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, entity.getCustomCycleAmount());
        }
        if (entity.getCustomCycleUnit() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getCustomCycleUnit());
        }
        statement.bindString(8, entity.getNextRenewalDate());
        final int _tmp = entity.getAutoRenew() ? 1 : 0;
        statement.bindLong(9, _tmp);
        statement.bindLong(10, entity.getReminderDays());
        statement.bindString(11, entity.getColor());
        if (entity.getNotes() == null) {
          statement.bindNull(12);
        } else {
          statement.bindString(12, entity.getNotes());
        }
        statement.bindLong(13, entity.getCreatedAt());
        statement.bindLong(14, entity.getUpdatedAt());
        if (entity.getNotificationAlarmId() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getNotificationAlarmId());
        }
        statement.bindLong(16, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM subscriptions WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final SubscriptionEntity entity,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSubscriptionEntity.insertAndReturnId(entity);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final SubscriptionEntity entity,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSubscriptionEntity.handle(entity);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SubscriptionEntity>> getAll() {
    final String _sql = "SELECT * FROM subscriptions ORDER BY nextRenewalDate ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"subscriptions"}, new Callable<List<SubscriptionEntity>>() {
      @Override
      @NonNull
      public List<SubscriptionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCost = CursorUtil.getColumnIndexOrThrow(_cursor, "cost");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfBillingCycle = CursorUtil.getColumnIndexOrThrow(_cursor, "billingCycle");
          final int _cursorIndexOfCustomCycleAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "customCycleAmount");
          final int _cursorIndexOfCustomCycleUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "customCycleUnit");
          final int _cursorIndexOfNextRenewalDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRenewalDate");
          final int _cursorIndexOfAutoRenew = CursorUtil.getColumnIndexOrThrow(_cursor, "autoRenew");
          final int _cursorIndexOfReminderDays = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderDays");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfNotificationAlarmId = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationAlarmId");
          final List<SubscriptionEntity> _result = new ArrayList<SubscriptionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubscriptionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpCost;
            _tmpCost = _cursor.getDouble(_cursorIndexOfCost);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpBillingCycle;
            _tmpBillingCycle = _cursor.getString(_cursorIndexOfBillingCycle);
            final Integer _tmpCustomCycleAmount;
            if (_cursor.isNull(_cursorIndexOfCustomCycleAmount)) {
              _tmpCustomCycleAmount = null;
            } else {
              _tmpCustomCycleAmount = _cursor.getInt(_cursorIndexOfCustomCycleAmount);
            }
            final String _tmpCustomCycleUnit;
            if (_cursor.isNull(_cursorIndexOfCustomCycleUnit)) {
              _tmpCustomCycleUnit = null;
            } else {
              _tmpCustomCycleUnit = _cursor.getString(_cursorIndexOfCustomCycleUnit);
            }
            final String _tmpNextRenewalDate;
            _tmpNextRenewalDate = _cursor.getString(_cursorIndexOfNextRenewalDate);
            final boolean _tmpAutoRenew;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAutoRenew);
            _tmpAutoRenew = _tmp != 0;
            final int _tmpReminderDays;
            _tmpReminderDays = _cursor.getInt(_cursorIndexOfReminderDays);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpNotificationAlarmId;
            if (_cursor.isNull(_cursorIndexOfNotificationAlarmId)) {
              _tmpNotificationAlarmId = null;
            } else {
              _tmpNotificationAlarmId = _cursor.getInt(_cursorIndexOfNotificationAlarmId);
            }
            _item = new SubscriptionEntity(_tmpId,_tmpName,_tmpCost,_tmpCurrency,_tmpBillingCycle,_tmpCustomCycleAmount,_tmpCustomCycleUnit,_tmpNextRenewalDate,_tmpAutoRenew,_tmpReminderDays,_tmpColor,_tmpNotes,_tmpCreatedAt,_tmpUpdatedAt,_tmpNotificationAlarmId);
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
  public Object getById(final long id, final Continuation<? super SubscriptionEntity> $completion) {
    final String _sql = "SELECT * FROM subscriptions WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SubscriptionEntity>() {
      @Override
      @Nullable
      public SubscriptionEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCost = CursorUtil.getColumnIndexOrThrow(_cursor, "cost");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfBillingCycle = CursorUtil.getColumnIndexOrThrow(_cursor, "billingCycle");
          final int _cursorIndexOfCustomCycleAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "customCycleAmount");
          final int _cursorIndexOfCustomCycleUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "customCycleUnit");
          final int _cursorIndexOfNextRenewalDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRenewalDate");
          final int _cursorIndexOfAutoRenew = CursorUtil.getColumnIndexOrThrow(_cursor, "autoRenew");
          final int _cursorIndexOfReminderDays = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderDays");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfNotificationAlarmId = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationAlarmId");
          final SubscriptionEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpCost;
            _tmpCost = _cursor.getDouble(_cursorIndexOfCost);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpBillingCycle;
            _tmpBillingCycle = _cursor.getString(_cursorIndexOfBillingCycle);
            final Integer _tmpCustomCycleAmount;
            if (_cursor.isNull(_cursorIndexOfCustomCycleAmount)) {
              _tmpCustomCycleAmount = null;
            } else {
              _tmpCustomCycleAmount = _cursor.getInt(_cursorIndexOfCustomCycleAmount);
            }
            final String _tmpCustomCycleUnit;
            if (_cursor.isNull(_cursorIndexOfCustomCycleUnit)) {
              _tmpCustomCycleUnit = null;
            } else {
              _tmpCustomCycleUnit = _cursor.getString(_cursorIndexOfCustomCycleUnit);
            }
            final String _tmpNextRenewalDate;
            _tmpNextRenewalDate = _cursor.getString(_cursorIndexOfNextRenewalDate);
            final boolean _tmpAutoRenew;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAutoRenew);
            _tmpAutoRenew = _tmp != 0;
            final int _tmpReminderDays;
            _tmpReminderDays = _cursor.getInt(_cursorIndexOfReminderDays);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpNotificationAlarmId;
            if (_cursor.isNull(_cursorIndexOfNotificationAlarmId)) {
              _tmpNotificationAlarmId = null;
            } else {
              _tmpNotificationAlarmId = _cursor.getInt(_cursorIndexOfNotificationAlarmId);
            }
            _result = new SubscriptionEntity(_tmpId,_tmpName,_tmpCost,_tmpCurrency,_tmpBillingCycle,_tmpCustomCycleAmount,_tmpCustomCycleUnit,_tmpNextRenewalDate,_tmpAutoRenew,_tmpReminderDays,_tmpColor,_tmpNotes,_tmpCreatedAt,_tmpUpdatedAt,_tmpNotificationAlarmId);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getAllOnce(final Continuation<? super List<SubscriptionEntity>> $completion) {
    final String _sql = "SELECT * FROM subscriptions";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SubscriptionEntity>>() {
      @Override
      @NonNull
      public List<SubscriptionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCost = CursorUtil.getColumnIndexOrThrow(_cursor, "cost");
          final int _cursorIndexOfCurrency = CursorUtil.getColumnIndexOrThrow(_cursor, "currency");
          final int _cursorIndexOfBillingCycle = CursorUtil.getColumnIndexOrThrow(_cursor, "billingCycle");
          final int _cursorIndexOfCustomCycleAmount = CursorUtil.getColumnIndexOrThrow(_cursor, "customCycleAmount");
          final int _cursorIndexOfCustomCycleUnit = CursorUtil.getColumnIndexOrThrow(_cursor, "customCycleUnit");
          final int _cursorIndexOfNextRenewalDate = CursorUtil.getColumnIndexOrThrow(_cursor, "nextRenewalDate");
          final int _cursorIndexOfAutoRenew = CursorUtil.getColumnIndexOrThrow(_cursor, "autoRenew");
          final int _cursorIndexOfReminderDays = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderDays");
          final int _cursorIndexOfColor = CursorUtil.getColumnIndexOrThrow(_cursor, "color");
          final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfNotificationAlarmId = CursorUtil.getColumnIndexOrThrow(_cursor, "notificationAlarmId");
          final List<SubscriptionEntity> _result = new ArrayList<SubscriptionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SubscriptionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final double _tmpCost;
            _tmpCost = _cursor.getDouble(_cursorIndexOfCost);
            final String _tmpCurrency;
            _tmpCurrency = _cursor.getString(_cursorIndexOfCurrency);
            final String _tmpBillingCycle;
            _tmpBillingCycle = _cursor.getString(_cursorIndexOfBillingCycle);
            final Integer _tmpCustomCycleAmount;
            if (_cursor.isNull(_cursorIndexOfCustomCycleAmount)) {
              _tmpCustomCycleAmount = null;
            } else {
              _tmpCustomCycleAmount = _cursor.getInt(_cursorIndexOfCustomCycleAmount);
            }
            final String _tmpCustomCycleUnit;
            if (_cursor.isNull(_cursorIndexOfCustomCycleUnit)) {
              _tmpCustomCycleUnit = null;
            } else {
              _tmpCustomCycleUnit = _cursor.getString(_cursorIndexOfCustomCycleUnit);
            }
            final String _tmpNextRenewalDate;
            _tmpNextRenewalDate = _cursor.getString(_cursorIndexOfNextRenewalDate);
            final boolean _tmpAutoRenew;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAutoRenew);
            _tmpAutoRenew = _tmp != 0;
            final int _tmpReminderDays;
            _tmpReminderDays = _cursor.getInt(_cursorIndexOfReminderDays);
            final String _tmpColor;
            _tmpColor = _cursor.getString(_cursorIndexOfColor);
            final String _tmpNotes;
            if (_cursor.isNull(_cursorIndexOfNotes)) {
              _tmpNotes = null;
            } else {
              _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Integer _tmpNotificationAlarmId;
            if (_cursor.isNull(_cursorIndexOfNotificationAlarmId)) {
              _tmpNotificationAlarmId = null;
            } else {
              _tmpNotificationAlarmId = _cursor.getInt(_cursorIndexOfNotificationAlarmId);
            }
            _item = new SubscriptionEntity(_tmpId,_tmpName,_tmpCost,_tmpCurrency,_tmpBillingCycle,_tmpCustomCycleAmount,_tmpCustomCycleUnit,_tmpNextRenewalDate,_tmpAutoRenew,_tmpReminderDays,_tmpColor,_tmpNotes,_tmpCreatedAt,_tmpUpdatedAt,_tmpNotificationAlarmId);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
