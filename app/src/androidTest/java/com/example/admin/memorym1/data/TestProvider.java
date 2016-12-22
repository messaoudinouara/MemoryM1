package com.example.admin.memorym1.data;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.test.AndroidTestCase;

import com.example.admin.memorym1.data.MemonimoContract.GameCardEntry;
import com.example.admin.memorym1.data.MemonimoContract.GameEntry;
import com.example.admin.memorym1.data.MemonimoContract.PatternEntry;

/**
 * Created by hackorder on 14/04/2015.
 */
public class TestProvider extends AndroidTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecordsFromProvider();
    }

    public void deleteAllRecordsFromProvider() {

        Cursor cursor;

        // Suppression et vérification de la suppression dans la table "game_card"
        mContext.getContentResolver().delete(
                GameCardEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                GameCardEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from game_card table", 0, cursor.getCount());
        cursor.close();

        // Suppression et vérification de la suppression dans la table "pattern"
        mContext.getContentResolver().delete(
                PatternEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                PatternEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from pattern table", 0, cursor.getCount());
        cursor.close();


        // Suppression et vérification de la suppression dans la table "game"
        mContext.getContentResolver().delete(
                GameEntry.CONTENT_URI,
                null,
                null
        );
        cursor = mContext.getContentResolver().query(
                GameEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: Records not deleted from game table", 0, cursor.getCount());
        cursor.close();
    }


    /*
        Vérification sur le fait que le Provider est bien accessible
     */
    public void testProviderRegistry() {

        PackageManager pm = mContext.getPackageManager();

        ComponentName componentName =
                new ComponentName(mContext.getPackageName(), MemonimoProvider.class.getName());
        try {
            // Récupération des informations relatives au Content Provider
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Vérification sur la manière dont on s'adresse au Content Provider
            assertEquals("Error: MemonimoProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MemonimoContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MemonimoContract.CONTENT_AUTHORITY);

        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: MemonimoProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    /*
        Vérification sur les interfaces d'accès au Provider
     */
    public void testGetType() {

        String type;

        //
        type = mContext.getContentResolver().getType(GameEntry.CONTENT_URI);
        assertEquals("Error: the GameEntry CONTENT_URI should return GameEntry.CONTENT_TYPE",
                GameEntry.CONTENT_TYPE, type);

        //
        type = mContext.getContentResolver().getType(GameCardEntry.CONTENT_URI);
        assertEquals("Error: the TurnEntry CONTENT_URI should return GameCardEntry.CONTENT_TYPE",
                GameCardEntry.CONTENT_TYPE, type);

        //
        type = mContext.getContentResolver().getType(PatternEntry.CONTENT_URI);
        assertEquals("Error: the TurnEntry CONTENT_URI should return PatternEntry.CONTENT_TYPE",
                PatternEntry.CONTENT_TYPE, type);
    }

    /*
        Vérification du Provider pour une insertion sur la table "game"
     */
    public void testInsertGame() {

        // Récupération d'un jeu de données pour le test
        ContentValues testValues = TestUtilities.createGameValues();

        // Insertion via le Provider
        Uri uri = mContext.getContentResolver().insert(GameEntry.CONTENT_URI, testValues);
        // Récupération de l'identifiant généré
        long idGenerated = ContentUris.parseId(uri);

        // Vérification de l'insertion
        assertTrue("Error: Failure to insert game values with Provider", idGenerated != -1);
    }

    /*
        Vérification du Provider pour un accès sur la table "game"
     */
    public void testQueryGame() {

        // Récupération d'un jeu de données pour le test
        ContentValues testValues = TestUtilities.createGameValues();

        // Insertion des données directement en base
        long idGame = TestUtilities.insertGameValuesToDb(mContext, testValues);

        // Récupération des données via le Content Provider
        Cursor cursor = mContext.getContentResolver().query(
                GameEntry.CONTENT_URI, // URI
                null, // Colonnes interogées
                null, // Colonnes pour la condition WHERE
                null, // Valeurs pour la condition WHERE
                null // Tri
        );

        // Vérification en comparant les données
        TestUtilities.validateCursor("Error: the cursor from GameEntry should return the same values", cursor, testValues);
    }

    /*
        Vérification du Provider pour une insertion sur la table "pattern"
     */
    public void testInsertPattern() {

        // Récupération d'un jeu de données pour le test
        ContentValues testValues = TestUtilities.createPatternValues();

        // Insertion via le Provider
        Uri uri = mContext.getContentResolver().insert(PatternEntry.CONTENT_URI, testValues);
        // Récupération de l'identifiant généré
        long idGenerated = ContentUris.parseId(uri);

        // Vérification de l'insertion
        assertTrue("Error: Failure to insert pattern values with Provider", idGenerated != -1);
    }





    /*
        Vérification du Provider pour une modification sur la table "game"
     */
//    public void testUpdateGame() {
//
//        // Récupération d'un jeu de données pour le test
//        ContentValues testValues = TestUtilities.createGameValues();
//
//        // Insertion via le Provider
//        Uri uri = mContext.getContentResolver().insert(GameEntry.CONTENT_URI, testValues);
//        // Récupération de l'identifiant généré
//        long idGenerated = ContentUris.parseId(uri);
//
//        // Vérification de l'insertion
//        assertTrue("Error: Failure to insert game values with Provider", idGenerated != -1);
//
//        // Modification des valeurs
//        ContentValues updatedValues = new ContentValues(testValues);
//        updatedValues.put(GameEntry._ID, idGenerated);
//        updatedValues.put(GameEntry.COLUMN_FINISHED, "0");
//
//        // Create a cursor with observer to make sure that the content provider is notifying
//        // the observers as expected
//        Cursor locationCursor = mContext.getContentResolver().query(LocationEntry.CONTENT_URI, null, null, null, null);
//
//        TestUtilities.TestContentObserver tco = TestUtilities.getTestContentObserver();
//        locationCursor.registerContentObserver(tco);
//
//        int count = mContext.getContentResolver().update(
//                LocationEntry.CONTENT_URI, updatedValues, LocationEntry._ID + "= ?",
//                new String[] { Long.toString(locationRowId)});
//        assertEquals(count, 1);
//
//        // Test to make sure our observer is called.  If not, we throw an assertion.
//        //
//        // Students: If your code is failing here, it means that your content provider
//        // isn't calling getContext().getContentResolver().notifyChange(uri, null);
//        tco.waitForNotificationOrFail();
//
//        locationCursor.unregisterContentObserver(tco);
//        locationCursor.close();
//
//        // A cursor is your primary interface to the query results.
//        Cursor cursor = mContext.getContentResolver().query(
//                LocationEntry.CONTENT_URI,
//                null,   // projection
//                LocationEntry._ID + " = " + locationRowId,
//                null,   // Values for the "where" clause
//                null    // sort order
//        );
//
//        TestUtilities.validateCursor("testUpdateLocation.  Error validating location entry update.",
//                cursor, updatedValues);
//
//        cursor.close();
//    }

}
