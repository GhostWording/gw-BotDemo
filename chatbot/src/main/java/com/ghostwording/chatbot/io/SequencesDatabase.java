package com.ghostwording.chatbot.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ghostwording.chatbot.chatbot.model.BotSequence;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SequencesDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SequenceDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SEQUENCES_NAME = "table_sequences";

    public interface TABLE_SEQUENCES {
        String ID = "_id";
        String SEQUENCE_ID = "sequenceId";
        String SEQUENCE_JSON = "sequenceJson";
    }

    public SequencesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sequencesTable = "CREATE TABLE " + TABLE_SEQUENCES_NAME
                + "("
                + TABLE_SEQUENCES.ID + " INTEGER PRIMARY KEY,"
                + TABLE_SEQUENCES.SEQUENCE_ID + " TEXT,"
                + TABLE_SEQUENCES.SEQUENCE_JSON + " TEXT)";
        db.execSQL(sequencesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEQUENCES_NAME);
        onCreate(db);
    }

    public void saveSequence(BotSequence sequence) {
        String query = "SELECT 1 FROM " + TABLE_SEQUENCES_NAME + " WHERE " + TABLE_SEQUENCES.SEQUENCE_ID + "=?";
        Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{sequence.getId()});
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(TABLE_SEQUENCES.SEQUENCE_ID, sequence.getId());
            values.put(TABLE_SEQUENCES.SEQUENCE_JSON, new Gson().toJson(sequence));
            getWritableDatabase().insert(TABLE_SEQUENCES_NAME, null, values);
        }
        cursor.close();
    }

    public BotSequence getSequenceById(String sequenceId) {
        BotSequence result = null;
        String query = "SELECT * FROM " + TABLE_SEQUENCES_NAME + " WHERE " + TABLE_SEQUENCES.SEQUENCE_ID + "=?";
        Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{sequenceId});
        if (cursor.moveToFirst()) {
            String sequenceJson = cursor.getString(cursor.getColumnIndex(TABLE_SEQUENCES.SEQUENCE_JSON));
            result = new Gson().fromJson(sequenceJson, BotSequence.class);
        }
        cursor.close();
        return result;
    }

    public List<BotSequence> getAllSequences() {
        List<BotSequence> result = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_SEQUENCES_NAME;
        Cursor cursor = getReadableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String sequenceJson = cursor.getString(cursor.getColumnIndex(TABLE_SEQUENCES.SEQUENCE_JSON));
                result.add(new Gson().fromJson(sequenceJson, BotSequence.class));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return result;
    }

    public void deleteAllSequences() {
        getWritableDatabase().execSQL("DELETE FROM " + TABLE_SEQUENCES_NAME);
    }

    public void deleteSequence(BotSequence botSequence) {
        getWritableDatabase().execSQL("DELETE FROM " + TABLE_SEQUENCES_NAME + " WHERE " + TABLE_SEQUENCES.SEQUENCE_ID + "=?", new String[]{botSequence.getId()});
    }

}
