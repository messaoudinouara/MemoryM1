package com.example.admin.memorym1.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.admin.memorym1.data.MemonimoContract.GameCardEntry;
import com.example.admin.memorym1.data.MemonimoContract.GameEntry;
import com.example.admin.memorym1.data.MemonimoContract.PatternEntry;

import java.util.HashSet;

/**
 * Created by hackorder on 14/04/2015.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(MemonimoDbHelper.DATABASE_NAME);
    }

    public void setUp() {
        deleteTheDatabase();
    }

    // Tests sur la création de tables
    public void testCreateDb() throws Throwable {
        createDb();
    }
    // Tests sur l'insertion des données dans la table "game"
    public void testGameTable() throws Throwable {
        insertIntoGameTable();
    }
    // Tests sur l'insertion des données dans la table "game_card"
    public void testGameCardTable() throws Throwable {
        insertIntoGameCardTable();
    }
    // Tests sur l'insertion des données dans la table "pattern"
    public void testPatternTable() throws Throwable {
        insertIntoPatternTable();
    }

    public void createDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(GameEntry.TABLE_NAME);
        tableNameHashSet.add(GameCardEntry.TABLE_NAME);
        tableNameHashSet.add(PatternEntry.TABLE_NAME);

        // Récupération de la base SQLite
        SQLiteDatabase db = new MemonimoDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // Vérification de l'inexistence des tables
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: Database not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Database not contain all tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + GameEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(GameEntry._ID);
        locationColumnHashSet.add(GameEntry.COLUMN_FINISHED);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required columns",
                locationColumnHashSet.isEmpty());
        db.close();
    }

    public long insertIntoGameTable() {

        // Récupération de la base SQLite
        SQLiteDatabase db = new MemonimoDbHelper(this.mContext).getWritableDatabase();
        // Récupération d'un jeu de données pour le test
        ContentValues testValues = TestUtilities.createGameValues();

        // Vérification de l'insertion
        long idGenerated;
        idGenerated = db.insert(GameEntry.TABLE_NAME, null, testValues);
        assertTrue(idGenerated != -1);

        // Requête de récupération de données via la table
        Cursor cursor = db.query(
                GameEntry.TABLE_NAME, // Table
                null, // Colonnes interogées
                null, // Colonnes pour la condition WHERE
                null, // Valeurs pour la condition WHERE
                null, // Colonnes pour le GROUP BY
                null, // Colonnes pour le filtre
                null // Tri
        );
        // Vérification de la présence de données
        assertTrue("Error: No records returned", cursor.moveToFirst());
        // Vérification de l'exactitude des données
        TestUtilities.validateCurrentRecord("Error: Values from record not expected",
                cursor,
                testValues
        );
        // Vérification de la présence d'une seule ligne dans la table
        assertFalse("Error: More than one record returned", cursor.moveToNext());

        // Fermeture du curseur et de la base
        cursor.close();
        db.close();

        return idGenerated;
    }

    public long insertIntoPatternTable() {

        // Récupération de la base SQLite
        SQLiteDatabase db = new MemonimoDbHelper(this.mContext).getWritableDatabase();
        // Récupération d'un jeu de données pour le test
        ContentValues testValues = TestUtilities.createPatternValues();

        // Vérification de l'insertion
        long idGenerated;
        idGenerated = db.insert(PatternEntry.TABLE_NAME, null, testValues);
        assertTrue(idGenerated != -1);

        // Requête de récupération de données via la table
        Cursor cursor = db.query(
                PatternEntry.TABLE_NAME, // Table
                null, // Colonnes interogées
                null, // Colonnes pour la condition WHERE
                null, // Valeurs pour la condition WHERE
                null, // Colonnes pour le GROUP BY
                null, // Colonnes pour le filtre
                null // Tri
        );
        // Vérification de la présence de données
        assertTrue("Error: No records returned", cursor.moveToFirst());
        // Vérification de l'exactitude des données
        TestUtilities.validateCurrentRecord("Error: Values from record not expected",
                cursor,
                testValues
        );
        // Vérification de la présence d'une seule ligne dans la table
        assertFalse("Error: More than one record returned", cursor.moveToNext());

        // Fermeture du curseur et de la base
        cursor.close();
        db.close();

        return idGenerated;
    }


    /*
        Tests sur l'insertion des données dans la table "game_card"
     */
    public long insertIntoGameCardTable() {

        long idGame = insertIntoGameTable();

        // Récupération de la base SQLite
        SQLiteDatabase db = new MemonimoDbHelper(this.mContext).getWritableDatabase();
        // Récupération d'un jeu de données pour le test
        ContentValues testValues = TestUtilities.createGameCardValues(idGame);

        // Vérification de l'insertion
        long idGenerated;
        idGenerated = db.insert(GameCardEntry.TABLE_NAME, null, testValues);
        assertTrue(idGenerated != -1);

        // Requête de récupération de données via la table
        Cursor cursor = db.query(
                GameCardEntry.TABLE_NAME, // Table
                null, // Colonnes interogées
                null, // Colonnes pour la condition WHERE
                null, // Valeurs pour la condition WHERE
                null, // Colonnes pour le GROUP BY
                null, // Colonnes pour le filtre
                null // Tri
        );
        // Vérification de la présence de données
        assertTrue("Error: No records returned", cursor.moveToFirst());
        // Vérification de l'exactitude des données
        TestUtilities.validateCurrentRecord("Error: Values from record not expected",
                cursor,
                testValues
        );
        // Vérification de la présence d'une seule ligne dans la table
        assertFalse("Error: More than one record returned", cursor.moveToNext());

        // Fermeture du curseur et de la base
        cursor.close();
        db.close();

        return idGenerated;
    }
}
