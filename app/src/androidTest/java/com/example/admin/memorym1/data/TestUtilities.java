package com.example.admin.memorym1.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.admin.memorym1.data.MemonimoContract.GameCardEntry;
import com.example.admin.memorym1.data.MemonimoContract.GameEntry;
import com.example.admin.memorym1.data.MemonimoContract.PatternEntry;

import java.util.Map;
import java.util.Set;

/**
 * Created by hackorder on 14/04/2015.
 */
public class TestUtilities extends AndroidTestCase {

    private static final String BOOLEAN_FALSE = "0";
    private static final String BOOLEAN_TRUE = "1";

    private static final String TEST_GAME_FINISHED = BOOLEAN_TRUE;
    private static final String TEST_GAME_FIRST_POSITION_CHOOSEN = "3";
    private static final String TEST_GAME_SECOND_POSITION_CHOOSEN = "5";
    private static final String TEST_GAME_NUM_ATTEMPT = "9";
    private static final String TEST_GAME_DIFFICULTY = "1";

    private static final String TEST_PATTERN_IMG_ENCODED = "jygrfjfhgvjdfhgvje";

    private static final String TEST_GAME_CARD_POSITION = "7";
    private static final String TEST_GAME_CARD_FOUND = BOOLEAN_FALSE;
    private static final String TEST_GAME_CARD_CODE_ANIMAL = "3";
    private static final String TEST_GAME_CARD_ATTEMPT = BOOLEAN_FALSE;

    /*
        Vérification des données d'un curseur
    */
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Vérification d'un curseur et des données qu'il contient
    */
    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    /*
        Création d'un jeu de données pour tester la table "game"
     */
    static ContentValues createGameValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(GameEntry.COLUMN_FINISHED, TEST_GAME_FINISHED);
        testValues.put(GameEntry.COLUMN_FIRST_POSITION_CHOOSEN, TEST_GAME_FIRST_POSITION_CHOOSEN);
        testValues.put(GameEntry.COLUMN_SECOND_POSITION_CHOOSEN, TEST_GAME_SECOND_POSITION_CHOOSEN);
        testValues.put(GameEntry.COLUMN_NUM_ATTEMPT, TEST_GAME_NUM_ATTEMPT);
        testValues.put(GameEntry.COLUMN_DIFFICULTY, TEST_GAME_DIFFICULTY);
        return testValues;
    }

    /*
        Création d'un jeu de données pour tester la table "pattern"
     */
    static ContentValues createPatternValues() {
        ContentValues testValues = new ContentValues();
        testValues.put(PatternEntry.COLUMN_IMG_ENCODED, TEST_PATTERN_IMG_ENCODED);
        return testValues;
    }

    /*
        Création d'un jeu de données pour tester la table "game_card"
     */
    static ContentValues createGameCardValues(long _idGame) {
        ContentValues testValues = new ContentValues();
        testValues.put(GameCardEntry.COLUMN_POSITION, TEST_GAME_CARD_POSITION);
        testValues.put(GameCardEntry.COLUMN_ID_GAME, _idGame);
        testValues.put(GameCardEntry.COLUMN_CODE_ANIMAL, TEST_GAME_CARD_CODE_ANIMAL);
        testValues.put(GameCardEntry.COLUMN_FOUND, TEST_GAME_CARD_FOUND);
        testValues.put(GameCardEntry.COLUMN_ATTEMPT, TEST_GAME_CARD_ATTEMPT);
        return testValues;
    }

    /*
        Insertion d'un jeu de données pour tester la table "game"
     */
    static long insertGameValuesToDb(Context _context, ContentValues _testValues) {

        // Récupération de la base SQLite
        SQLiteDatabase db = new MemonimoDbHelper(_context).getWritableDatabase();

        // Vérification de l'insertion
        long idGenerated;
        idGenerated = db.insert(GameEntry.TABLE_NAME, null, _testValues);
        assertTrue("Error: Failure to insert game values", idGenerated != -1);

        return idGenerated;
    }

    /*
        Insertion d'un jeu de données pour tester la table "pattern"
     */
    static long insertPatternValuesToDb(Context _context, ContentValues _testValues) {

        // Récupération de la base SQLite
        SQLiteDatabase db = new MemonimoDbHelper(_context).getWritableDatabase();

        // Vérification de l'insertion
        long idGenerated;
        idGenerated = db.insert(PatternEntry.TABLE_NAME, null, _testValues);
        assertTrue("Error: Failure to insert pattern values", idGenerated != -1);

        return idGenerated;
    }
}
