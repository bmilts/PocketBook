package miltonsmind.pocketbook;

/**
 * Created by brendan on 21/02/2016.
 */

import android.net.Uri;
import android.provider.BaseColumns;


// Class containing constant definitions for Uri (Connector)
// Also contains column names, fixed for contact provider
// Separated for best practice and viability
public class FriendsContract {

    interface FriendsColumns{

        String FRIENDS_ID = "_id";
        String FRIENDS_NAME = "friends_name";
        String FRIENDS_EMAIL ="friends_email";
        String FRIENDS_PHONE = "friends_phone";

    }

    // Could be used by other apps as well
    public static final String CONTENT_AUTHORITY = "miltonsmind.pocketbook.provider";

    // Basics for content provider lookup
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_FRIENDS = "friends";

    // Shortcut to accessing Uri without accessing main class
    public static final Uri URI_TABLE = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_FRIENDS);

    public static final String[] TOP_LEVEL_PATHS = {
        PATH_FRIENDS
    };

    // Defining path by creating string
    public static class Friends implements FriendsColumns, BaseColumns {

        public static final Uri CONTENT_URI =
                // Creates access for friends provider
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_FRIENDS).build();

        // Mime types are labels for Data to be decoded
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + ".friends"; // Group of data to access whole directory
        // Mime type for single item
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + ".friends";

        public static Uri buildFriendUri(String friendId) {

            return CONTENT_URI.buildUpon().appendEncodedPath(friendId).build();
        }

        // Extracting a particular ID that has been parsed
        public static String getFriendId(Uri uri){

            return uri.getPathSegments().get(1);
        }
    }
}

