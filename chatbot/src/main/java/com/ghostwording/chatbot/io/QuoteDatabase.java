package com.ghostwording.chatbot.io;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ghostwording.chatbot.model.texts.Quote;

public class QuoteDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuotesDatabase";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_QUOTES_NAME = "table_quotes";
    private static final String TABLE_TRANSLATIONS_NAME = "table_translations";

    public interface TABLE_QUOTES {
        String ID = "_id";
        String TEXT_ID = "textId";
        String INTENTION_ID = "intentionId";
        String PROTOTYPE_ID = "prototypeId";
        String CONTENT = "Content";
        String SENDER = "Sender";
        String TARGET = "Target";
        String POLITE_FORM = "PoliteForm";
        String CULTURE = "Culture";
    }

    public interface TABLE_TRANSLATIONS {
        String ID = "_id";
        String PROTOTYPE_ID = "PrototypeId";
    }

    public QuoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String favoritesTable = "CREATE TABLE " + TABLE_QUOTES_NAME
                + "("
                + TABLE_QUOTES.ID + " INTEGER PRIMARY KEY,"
                + TABLE_QUOTES.TEXT_ID + " TEXT,"
                + TABLE_QUOTES.INTENTION_ID + " TEXT,"
                + TABLE_QUOTES.PROTOTYPE_ID + " TEXT,"
                + TABLE_QUOTES.CONTENT + " TEXT,"
                + TABLE_QUOTES.SENDER + " TEXT,"
                + TABLE_QUOTES.TARGET + " TEXT,"
                + TABLE_QUOTES.POLITE_FORM + " TEXT,"
                + TABLE_QUOTES.CULTURE + " TEXT"
                + ")";
        String favoritesTextsTable = "CREATE TABLE " + TABLE_TRANSLATIONS_NAME
                + "("
                + TABLE_TRANSLATIONS.ID + " INTEGER PRIMARY KEY,"
                + TABLE_TRANSLATIONS.PROTOTYPE_ID + " TEXT)";
        db.execSQL(favoritesTable);
        db.execSQL(favoritesTextsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTES_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSLATIONS_NAME);
        onCreate(db);
    }

    public void saveQuote(Quote quote) {
        String query = "SELECT 1 FROM " + TABLE_QUOTES_NAME + " WHERE " + TABLE_QUOTES.TEXT_ID + "=?";
        Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{quote.getTextId()});
        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put(TABLE_QUOTES.TEXT_ID, quote.getTextId());
            values.put(TABLE_QUOTES.INTENTION_ID, quote.getIntentionId());
            values.put(TABLE_QUOTES.PROTOTYPE_ID, quote.getPrototypeId());
            values.put(TABLE_QUOTES.CONTENT, quote.getContent());
            values.put(TABLE_QUOTES.SENDER, quote.getSender());
            values.put(TABLE_QUOTES.TARGET, quote.getTarget());
            values.put(TABLE_QUOTES.POLITE_FORM, quote.getPoliteForm());
            values.put(TABLE_QUOTES.CULTURE, quote.getCulture());
            getWritableDatabase().insert(TABLE_QUOTES_NAME, null, values);
        }
        cursor.close();
    }

    public Quote getQuoteById(String textId) {
        Quote result = null;
        String query = "SELECT * FROM " + TABLE_QUOTES_NAME + " WHERE " + TABLE_QUOTES.TEXT_ID + "=?";
        Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{textId});
        if (cursor.moveToFirst()) {
            result = new Quote(cursor);
        }
        cursor.close();
        return result;
    }

}
