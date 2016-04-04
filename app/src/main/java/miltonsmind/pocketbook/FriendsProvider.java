package miltonsmind.pocketbook;

/**
 * Created by brendan on 21/02/2016.
 */

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;


public class FriendsProvider extends ContentProvider{

    private FriendsDatabase mOpenHelper;

    private static String TAG = FriendsProvider.class.getSimpleName();
    private static UriMatcher sUriMatcher = buildUriMatcher();

    private static final int FRIENDS = 100;
    private static final int FRIENDS_ID = 101;

    private static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FriendsContract.CONTENT_AUTHORITY;

        // Add relevant processes to work on
        matcher.addURI(authority, "friends", FRIENDS);
        matcher.addURI(authority, "friends/*", FRIENDS_ID);
        return matcher;

    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new FriendsDatabase(getContext());
        return true;
    }

    public void deleteDatabase() {
        mOpenHelper.close();
        FriendsDatabase.deleteDatabase(getContext());
        mOpenHelper = new FriendsDatabase(getContext());
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch(match){

            case FRIENDS:
                return FriendsContract.Friends.CONTENT_TYPE;
            case FRIENDS_ID:
                return FriendsContract.Friends.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);

        }

    }

    // Code will automatically grab data based on selection
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        // Instance of database
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        // What was passed to method
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(FriendsDatabase.Tables.FRIENDS);

        switch (match) {

            // When query sent either list of all friends or just retrieve one friend
            case FRIENDS:
                // do nothing
                break;
            case FRIENDS_ID:

                // if id extracted is = to id pased in
                String id = FriendsContract.Friends.getFriendId(uri);
                queryBuilder.appendWhere(BaseColumns._ID + "=" + id);
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);

            }

            // Projection = list columns, selection = where clause
            Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            return cursor;

        }

        // Dictionary that contains contents names and values asociates
        @Override
        public Uri insert(Uri uri, ContentValues values) {
            Log.v(TAG, "insert(uri=" + uri + ", values=" + values.toString());

            // Instance of database
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

            // What was passed to method
            final int match = sUriMatcher.match(uri);

            switch (match){
                case FRIENDS:
                    long recordId = db.insertOrThrow(FriendsDatabase.Tables.FRIENDS, null, values);
                    return FriendsContract.Friends.buildFriendUri(String.valueOf(recordId));
                default:
                    throw new IllegalArgumentException("Unknown Uri: " + uri);

            }
        }

    // By calling this function without a valid ID it may update all records by accident
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        Log.v(TAG, "update(uri=" + uri + ", values=" + values.toString());

        // Instance of database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // What was passed to method
        final int match = sUriMatcher.match(uri);

        // Selection needs to be recorded in both instances
        String selectionCriteria = selection;
        switch (match) {
            case FRIENDS:
                // Do nothing
                break;
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                    break;

                default:
                    throw new IllegalArgumentException("Unknown Uri: " + uri);

        }

        // Returns number of records updated
        return db.update(FriendsDatabase.Tables.FRIENDS, values, selectionCriteria, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        Log.v(TAG, "delete(uri=" + uri);

        if(uri.equals(FriendsContract.URI_TABLE)){
            deleteDatabase();
            return 0;

        }

        // Instance of database
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        // What was passed to method
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case FRIENDS_ID:
                String id = FriendsContract.Friends.getFriendId(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : "");
                return db.delete(FriendsDatabase.Tables.FRIENDS, selectionCriteria, selectionArgs);

            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);

        }
    }
}

