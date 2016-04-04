package miltonsmind.pocketbook;

/**
 * Created by brendan on 22/02/2016.
 */

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditActivity extends FragmentActivity {

    private final String LOG_TAG = AddActivity.class.getSimpleName();
    private TextView mNameTextView, mEmailTextView, mPhoneTextView;
    private Button mButton;
    private ContentResolver mContentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Its possible to use editText or TextView as they inherit from a base class
        mNameTextView = (TextView) findViewById(R.id.friendName);
        mEmailTextView = (TextView) findViewById(R.id.friendEmail);
        mPhoneTextView = (TextView) findViewById(R.id.friendPhone);

        mContentResolver = EditActivity.this.getContentResolver();

        // Information parsed to activity
        Intent intent = getIntent();
        final String _id = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_ID);
        String name = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_NAME);
        String phone = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_PHONE);
        String email = intent.getStringExtra(FriendsContract.FriendsColumns.FRIENDS_EMAIL);

        // Populate on screen views
        mNameTextView.setText(name);
        mPhoneTextView.setText(phone);
        mEmailTextView.setText(email);

        // Save buttons
        mButton = (Button) findViewById(R.id.saveButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Saving info into text views retrieved from views
                ContentValues values = new ContentValues();
                values.put(FriendsContract.FriendsColumns.FRIENDS_NAME, mNameTextView.getText().toString());
                values.put(FriendsContract.FriendsColumns.FRIENDS_PHONE, mPhoneTextView.getText().toString());
                values.put(FriendsContract.FriendsColumns.FRIENDS_EMAIL, mEmailTextView.getText().toString());

                Uri uri = FriendsContract.Friends.buildFriendUri(_id); // To get correct field for update
                int recordsUpdated = mContentResolver.update(uri, values, null, null);
                Log.d(LOG_TAG, "number of records updated = " + recordsUpdated);

                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Should always call when an activity is finished processing

            }
        });
    }

    // If home pressed return to main menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                // Allows navigation back to parent method
                NavUtils.navigateUpFromSameTask(this);
            break;
        }
        return true;
    }
}
