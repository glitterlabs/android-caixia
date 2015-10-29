package com.glitterlabs.caixia;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.glitterlabs.caixia.Adapters.MyFriendsListAdapter;
import com.glitterlabs.caixia.Databases.DatabseHelper;
import com.glitterlabs.caixia.Models.Friend;

import java.util.ArrayList;

public class MyFriendsListActivity extends AppCompatActivity {



    DatabseHelper mdatabase = new DatabseHelper(this);
    private ListView lvFriends;
    private MyFriendsListAdapter mAdapter;
    ArrayList<Friend> mlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lvFriends = (ListView) findViewById(R.id.lvMyFrendsListView);
        mAdapter = new MyFriendsListAdapter(this, getAllFrends());
        lvFriends.setAdapter(mAdapter);

    }
    public ArrayList<Friend> getAllFrends() {
        Cursor cursor = mdatabase.getAllData();
        mlist = new ArrayList<Friend>();
        if (cursor.getCount() == 0) {
            // no data found
            Toast.makeText(MyFriendsListActivity.this, "No friend found.",
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
    protected void onResume() {
        super.onResume();
        mAdapter = new MyFriendsListAdapter(this, getAllFrends());
        lvFriends.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_friends_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addFriend) {
            startActivity(new Intent(this, AddFriendAvtivity.class));
        }

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}