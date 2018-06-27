package com.ghostwording.hugsapp.analytics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class EventsDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "EventsDatabase";
    private static final int DATABASE_VERSION = 6;

    private static final String TABLE_EVENTS_NAME = "table_events";

    public interface TABLE_EVENTS {
        String ID = "_id";
        String ACTION_TYPE = "ActionType";
        String ACTION_LOCATION = "ActionLocation";
        String TARGET_TYPE = "TargetType";
        String TARGET_ID = "TargetId";
        String TARGET_PARAMETER = "TargetParameter";
        String CLIENT_TIME = "ClientTime";
        String CONTEXT = "Context";
        String RECIPIENT_TYPE = "RecipientType";
        String INTENTION_ID = "IntentionId";
        String RECIPIENT_ID = "RecipientId";
        String LAST = "IsLastSequence";
    }

    public EventsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String eventsTable = "CREATE TABLE " + TABLE_EVENTS_NAME
                + "("
                + TABLE_EVENTS.ID + " INTEGER PRIMARY KEY,"
                + TABLE_EVENTS.ACTION_TYPE + " TEXT,"
                + TABLE_EVENTS.ACTION_LOCATION + " TEXT,"
                + TABLE_EVENTS.TARGET_TYPE + " TEXT,"
                + TABLE_EVENTS.TARGET_ID + " TEXT,"
                + TABLE_EVENTS.TARGET_PARAMETER + " TEXT,"
                + TABLE_EVENTS.CLIENT_TIME + " TEXT,"
                + TABLE_EVENTS.RECIPIENT_TYPE + " TEXT,"
                + TABLE_EVENTS.INTENTION_ID + " TEXT,"
                + TABLE_EVENTS.RECIPIENT_ID + " TEXT,"
                + TABLE_EVENTS.CONTEXT + " TEXT,"
                + TABLE_EVENTS.LAST + " INTEGER)";
        db.execSQL(eventsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS_NAME);
        onCreate(db);
    }

    public List<EventModel> getEvents() {
        List<EventModel> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_EVENTS_NAME + " LIMIT 100";
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                result.add(new EventModel(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public void addEvent(EventModel eventModel) {
        ContentValues values = new ContentValues();
        values.put(TABLE_EVENTS.ACTION_TYPE, eventModel.getActionType());
        values.put(TABLE_EVENTS.ACTION_LOCATION, eventModel.getActionLocation());
        values.put(TABLE_EVENTS.TARGET_TYPE, eventModel.getTargetType());
        values.put(TABLE_EVENTS.TARGET_ID, eventModel.getTargetId());
        values.put(TABLE_EVENTS.TARGET_PARAMETER, eventModel.getTargetParameter());
        values.put(TABLE_EVENTS.CLIENT_TIME, eventModel.getClientTime());
        values.put(TABLE_EVENTS.CONTEXT, eventModel.getContext());
        values.put(TABLE_EVENTS.RECIPIENT_TYPE, eventModel.getRecipientType());
        values.put(TABLE_EVENTS.INTENTION_ID, eventModel.getIntentionId());
        values.put(TABLE_EVENTS.RECIPIENT_ID, eventModel.getRecipientId());
        values.put(TABLE_EVENTS.LAST, eventModel.isLast() ? 1 : 0);
        synchronized (this) {
            getWritableDatabase().insert(TABLE_EVENTS_NAME, null, values);
        }
    }

    public void removeEvents(List<EventModel> eventModels) {
        StringBuffer eventsParameter = new StringBuffer();
        String delimiter = "(";
        for (EventModel eventModel : eventModels) {
            eventsParameter.append(delimiter + eventModel.getId());
            delimiter = ", ";
        }
        eventsParameter.append(")");
        String deleteStatement = "DELETE FROM " + TABLE_EVENTS_NAME + " WHERE " + TABLE_EVENTS.ID + " IN " + eventsParameter;
        synchronized (this) {
            getWritableDatabase().execSQL(deleteStatement);
        }
    }

}




