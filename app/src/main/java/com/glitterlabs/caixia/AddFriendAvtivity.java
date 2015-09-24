package com.glitterlabs.caixia;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.glitterlabs.caixia.Adapters.SearchFriendAdapter;
import com.glitterlabs.caixia.Models.Friend;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import java.util.ArrayList;
import java.util.List;

public class AddFriendAvtivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private ListView lvFriends;
    private static String currentUser = ParseUser.getCurrentUser().getObjectId();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_AddFriend);
        progressBar.setVisibility(View.GONE);
        lvFriends = (ListView) findViewById(R.id.lv_searchedFriends);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_friend, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchFriend(query.trim());

                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;

            }

        });


        return true;
    }


    public void SearchFriend(String query) {
        final ArrayList<Friend> friendsList = new ArrayList<Friend>();
        progressBar.setVisibility(View.VISIBLE);
        ParseQuery<ParseUser> userquery = ParseUser.getQuery();
        userquery.whereEqualTo("username", query);
        userquery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                progressBar.setVisibility(View.GONE);
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        Friend friend = new Friend();
                        friend.setFriendName(list.get(i).getString("Name"));
                        friend.setUserName(list.get(i).getString("username"));
                        friend.setUserID(list.get(i).getObjectId());
                        friendsList.add(friend);
                    }
                    SearchFriendAdapter adapter = new SearchFriendAdapter(AddFriendAvtivity.this, friendsList);
                    lvFriends.setAdapter(adapter);

                } else {
                    Toast.makeText(AddFriendAvtivity.this, "Username Not Found",
                            Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public static void addFrendToServerSide(final String friendName, final boolean isconfirm, final String friendsUserID) {
       /* ParseQuery<ParseObject> frindQuery = new ParseQuery<ParseObject>("Friends");
        frindQuery.whereNotEqualTo("friendsName", friendName);
        frindQuery.whereNotEqualTo("userId", currentUser);
        frindQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {*/
                    ParseObject friend = new ParseObject("Friends");
                    friend.put("userId", currentUser);
                    friend.put("friendsName", friendName);
                    friend.put("isConfirm", isconfirm);
                    friend.saveInBackground();
                    sendFrendrequestToAddedFrend(friendsUserID);
              /*  } else {

                }
            }
        });*/

    }


    public static void sendFrendrequestToAddedFrend(final String userId) {
       /* ParseQuery<ParseObject> frindQuery = new ParseQuery<ParseObject>("Friends");
        frindQuery.whereNotEqualTo("friendsName", ParseUser.getCurrentUser().get("Name"));
        frindQuery.whereNotEqualTo("userId", userId);
        frindQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {*/
                    boolean isconfirm = false;
                    ParseObject friend = new ParseObject("Friends");
                    friend.put("userId", userId);
                    friend.put("friendsName", ParseUser.getCurrentUser().get("Name"));
                    friend.put("isConfirm", isconfirm);
                    friend.saveInBackground();
                    sendNotificationForFrendRequest(userId);
               /* } else {

                }
            }
        });*/
    }
    public static void sendNotificationForFrendRequest(String userId){


// Find devices associated with these users
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("userId",userId);

// Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage("Abhay sent you friend request..!");
        push.sendInBackground(new SendCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    //kjgjhf
                }else {
//jkgkjfjk
                }
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            // to go back to your MainActivity
            //NEED TO ADD  <meta-data android:name="android.support.PARENT_ACTIVITY" android:value=".MainActivity"></meta-data>
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
