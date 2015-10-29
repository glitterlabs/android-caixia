package com.glitterlabs.caixia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.glitterlabs.caixia.Adapters.FrendsListAdapter;
import com.glitterlabs.caixia.Databases.DatabseHelper;
import com.glitterlabs.caixia.Models.Friend;
import com.glitterlabs.caixia.Models.MessageData;
import com.glitterlabs.caixia.util.Helper;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

public class MyFriendsActivity extends AppCompatActivity {

    DatabseHelper mdatabase = new DatabseHelper(this);
    private ListView lvFriends;
    private Button btnSend;
    private FrendsListAdapter mAdapter;
    String curentUsername = ParseUser.getCurrentUser().getString("Name");
    MessageData messageData;
    ProgressDialog progressDialog;
    ArrayList<Friend> mlist;
    String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        messageData = (MessageData) getIntent().getSerializableExtra("messageData");
        tag= getIntent().getStringExtra("tag");
        lvFriends = (ListView) findViewById(R.id.lvMyFrendList);
        mAdapter = new FrendsListAdapter(this, getAllFrends());
        lvFriends.setAdapter(mAdapter);
        btnSend = (Button) findViewById(R.id.btnSend_Myfriends);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToFriend(messageData);

            }
        });
/*lvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox check= (CheckBox)view;
        if(check.isChecked()){
            messageData.setUserId(list.get(position).getUserID());
        }
    }
});*/

    }

    private void sendMessageToFriend(MessageData messageData) {
        ArrayList list = mAdapter.checkedFrends;
        if (!list.isEmpty()) {
            progressDialog = ProgressDialog.show(MyFriendsActivity.this, null,
                    getString(R.string.alert_wait));
            for (int i = 0; i < list.size(); i++) {

                //ParseFile parseImgeFile = new ParseFile("image", messageData.getByteImage());
                ParseFile parseImgeFile = new  ParseFile(messageData.getImageFile(),tag);
              //  ParseFile parseImgeFile = new ParseFile(File file,"png")
                // Upload the image into Parse Cloud
                parseImgeFile.saveInBackground();
                ParseObject inbox = new ParseObject("Inbox");
                inbox.put("userId", mAdapter.checkedFrends.get(i));
                inbox.put("senderName", curentUsername);
                inbox.put("text", messageData.getTextMessage());
                inbox.put("timeToDisplayImage", messageData.getTimeForDisplay());
                inbox.put("scheduledDate", messageData.getScheduldDate());
                inbox.put("location", messageData.getGeoPoint());
                inbox.put("image", parseImgeFile);
                inbox.put("imageType",tag );
                inbox.put("isSeen",0);
                inbox.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        progressDialog.dismiss();
                        if (e == null) {
                            finish();

                        } else {
                            Helper.showDialog("Error", "Network error", MyFriendsActivity.this);
                        }
                    }
                });

            }

        } else {
            Toast.makeText(MyFriendsActivity.this, "Please select friend", Toast.LENGTH_LONG).show();
        }

    }

    public ArrayList<Friend> getAllFrends() {
        Cursor cursor = mdatabase.getAllData();
        mlist = new ArrayList<Friend>();
        if (cursor.getCount() == 0) {
            // no data found
            Toast.makeText(MyFriendsActivity.this, "Friend Not Found",
                    Toast.LENGTH_LONG).show();
            return mlist;
        } else {


            StringBuffer buffer = new StringBuffer();
            while (cursor.moveToNext()) {
                Friend friend = new Friend();
                /*buffer.append("ID : " + cursor.getString(0) + "\n");
                buffer.append("NAME : " + cursor.getString(1) + "\n");
                buffer.append("user name : " + cursor.getString(2) + "\n");
                buffer.append("userId : " + cursor.getString(3) + "\n");*/

                friend.setFriendName(cursor.getString(1));
                friend.setUserID(cursor.getString(3));
                mlist.add(friend);

            }
            return mlist;
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        mAdapter = new FrendsListAdapter(this, getAllFrends());
        lvFriends.setAdapter(mAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_friends, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_addFriennds) {
            startActivity(new Intent(this, AddFriendAvtivity.class));
        }
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
