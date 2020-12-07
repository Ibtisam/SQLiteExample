package com.example.datamanagementsqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by MuhammadIbtisam on 20/05/2019.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    final private static String CREATE_CMD =
            "CREATE TABLE profile (email TEXT NOT NULL PRIMARY KEY, "
                    + "name TEXT NOT NULL, "
                    + "address TEXT NOT NULL, "
                    + "marital INT NOT NULL)";


    final private static String NAME = "profile_db";
    final private static Integer VERSION = 1;
    final private Context mContext;

    public DatabaseManager(Context context) {
        super(context, NAME, null, VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CMD);
        Toast.makeText(mContext, "profile table Created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void closeDatabase() {
        getWritableDatabase().close();
    }

    public void deleteDatabase() {
        mContext.deleteDatabase(NAME);
    }


    public int deleteTable(String tableName) {
        /*resCode = the number of rows affected if a whereClause is passed in, 0 otherwise.
        To remove all rows and get a count pass "1" as the whereClause.*/
        int resCode = getWritableDatabase().delete(tableName, "1", null);
        return resCode;
    }

    public long addRecord(String email, String name, String address, int maritalStatus) {
        //his class is used to store a set of values that the ContentResolver can process.
        ContentValues values = new ContentValues();

        values.put("email", email);
        values.put("name", name);
        values.put("address", address);
        values.put("marital", maritalStatus);
        //resCode = the row ID of the newly inserted row, or -1 if an error occurred
        long resCode = getWritableDatabase().insert("profile", null, values);

        values.clear();
        return resCode;
    }

    public int updateRecord(String email, String name, String address, int maritalStatus) {
        //his class is used to store a set of values that the ContentResolver can process.
        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("address", address);
        values.put("marital", maritalStatus);
        //the number of rows affected
        int resCode = getWritableDatabase().update("profile", values, "email=?", new String[]{email});
        values.clear();
        return resCode;
    }

    public int deleteRecord(String email) {
        /*resCode = the number of rows affected if a whereClause is passed in, 0 otherwise.
        To remove all rows and get a count pass "1" as the whereClause.*/
        int resCode = getWritableDatabase().delete("profile", "email=?", new String[]{email});
        return resCode;
    }

    public Cursor searchRecord(String email) {
        //A Cursor object, which is positioned before the first entry.
        // Note that Cursors are not synchronized, see the documentation for more details
        Cursor cursor = getWritableDatabase().query("profile", null, "email=?", new String[]{email}, null, null, null);
        return cursor;
    }
}

//HELP
/*delete

        int delete (String table,
        String whereClause,
        String[] whereArgs)

        Convenience method for deleting rows in the database.
        Parameters:
        table 	        String: the table to delete from
        whereClause 	String: the optional WHERE clause to apply when deleting. Passing null will delete all rows.
        whereArgs 	    String: You may include ?s in the where clause, which will be replaced by the values from whereArgs.
                                The values will be bound as Strings.
        Returns:
        int 	the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause. */

/*insert

        long insert (String table,
        String nullColumnHack,
        ContentValues values)

        Convenience method for inserting a row into the database.
        Parameters:
        table 	        String: the table to insert the row into
        nullColumnHack 	String: optional; may be null. SQL doesn't allow inserting a completely empty row without naming at least one column name. If your provided values is empty, no column names are known and an empty row can't be inserted. If not set to null, the nullColumnHack parameter provides the name of nullable column name to explicitly insert a NULL into in the case where your values is empty.
        values 	        ContentValues: this map contains the initial column values for the row. The keys should be the column names and the values the column values
        Returns:
        long 	the row ID of the newly inserted row, or -1 if an error occurred */

/*update

        int update (String table,
        ContentValues values,
        String whereClause,
        String[] whereArgs)

        Convenience method for updating rows in the database.
        Parameters:
        table 	        String: the table to update in
        values 	        ContentValues: a map from column names to new column values. null is a valid value that will be translated to NULL.
        whereClause 	String: the optional WHERE clause to apply when updating. Passing null will update all rows.
        whereArgs 	    String: You may include ?s in the where clause, which will be replaced by the values from whereArgs. The values will be bound as Strings.
        Returns:
        int 	the number of rows affected */

/*query
        added in API level 1

        Cursor query (String table,
        String[] columns,
        String selection,
        String[] selectionArgs,
        String groupBy,
        String having,
        String orderBy,
        String limit)

        Query the given table, returning a Cursor over the result set.
        Parameters:
        table 	        String: The table name to compile the query against.
        columns 	    String: A list of which columns to return. Passing null will return all columns, which is discouraged to prevent reading data from storage that isn't going to be used.
        selection 	    String: A filter declaring which rows to return, formatted as an SQL WHERE clause (excluding the WHERE itself). Passing null will return all rows for the given table.
        selectionArgs 	String: You may include ?s in selection, which will be replaced by the values from selectionArgs, in order that they appear in the selection. The values will be bound as Strings.
        groupBy 	    String: A filter declaring how to group rows, formatted as an SQL GROUP BY clause (excluding the GROUP BY itself). Passing null will cause the rows to not be grouped.
        having 	        String: A filter declare which row groups to include in the cursor, if row grouping is being used, formatted as an SQL HAVING clause (excluding the HAVING itself). Passing null will cause all row groups to be included, and is required when row grouping is not being used.
        orderBy 	    String: How to order the rows, formatted as an SQL ORDER BY clause (excluding the ORDER BY itself). Passing null will use the default sort order, which may be unordered.
        limit 	        String: Limits the number of rows returned by the query, formatted as LIMIT clause. Passing null denotes no LIMIT clause.
        Returns
        Cursor 	A Cursor object, which is positioned before the first entry. Note that Cursors are not synchronized, see the documentation for more details.*/
/*
    SQLiteOpenHelper (Context context,
                     String name,
                     SQLiteDatabase.CursorFactory factory,
                     int version)

    Create a helper object to create, open, and/or manage a database. This method always returns very quickly. The database is not actually created or opened until one of getWritableDatabase() or getReadableDatabase() is called.
       Parameters
       context 	    Context: to use to open or create the database
       name 	    String: of the database file, or null for an in-memory database
       factory 	    SQLiteDatabase.CursorFactory: to use for creating cursor objects, or null for the default
       version 	    int: number of the database (starting at 1); if the database is older,
                    onUpgrade(SQLiteDatabase, int, int) will be used to upgrade the database;
                    if the database is newer, onDowngrade(SQLiteDatabase, int, int) will be used to
                    downgrade the database*/