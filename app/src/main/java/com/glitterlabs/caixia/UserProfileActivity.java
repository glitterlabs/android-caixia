package com.glitterlabs.caixia;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.glitterlabs.caixia.util.CustomTouchListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.parse.ParseUser;

public class UserProfileActivity extends AppCompatActivity {
   private RoundedImageView ivProfilePhoto;
    private TextView tvName,tvUserName,tvAddedMe,tvAddFriends,tvMyFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        /*ivProfilePhoto= (RoundedImageView) findViewById(R.id.ivUserProfilePhoto);

        ivProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animZoomin = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.image_animation);

                ivProfilePhoto.startAnimation(animZoomin);
            }
        });*/
        tvName = (TextView) findViewById(R.id.tvName_UserProfile);
        tvName.setText(ParseUser.getCurrentUser().getString("Name"));
        tvUserName= (TextView) findViewById(R.id.tvUsername_UserProfile);
        tvUserName.setText(ParseUser.getCurrentUser().getString("username"));
        tvAddFriends= (TextView) findViewById(R.id.tvAddFriends_UserProfile);
        tvAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAddFriends.setOnTouchListener(new CustomTouchListener());
                startActivity(new Intent(UserProfileActivity.this, AddFriendAvtivity.class));
            }
        });
        tvMyFriends= (TextView) findViewById(R.id.tvMyFriends_UserProfile);
        tvMyFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMyFriends.setOnTouchListener(new CustomTouchListener());
                startActivity(new Intent(UserProfileActivity.this, MyFriendsListActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }




    public void showLogoutDilog(String title, String message, Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ParseUser.logOut();
                finish();
                startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            showLogoutDilog("Logout", "Do you want to logout?",this);
        }
        if (id == android.R.id.home) {
            finish();
        }
        if(id== R.id.action_Share){
            String shareBody = "https://play.google.com/store/apps/details?id=com.glitterlabs.caixia";
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Caixia is memories application by which you can share your memories with your friends");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        }
        if(id== R.id.action_feedback){
           startActivity(new Intent(UserProfileActivity.this,FeedBackActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
