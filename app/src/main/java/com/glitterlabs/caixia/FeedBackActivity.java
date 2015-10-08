package com.glitterlabs.caixia;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FeedBackActivity extends AppCompatActivity {
private  EditText etfeedback;
    private ImageView ivSendFedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        etfeedback= (EditText) findViewById(R.id.etFeedback);
        ivSendFedback= (ImageView) findViewById(R.id.ivSend_feedback);
        ivSendFedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });

    }
private  void sendFeedback(){
    String feedbackMessage= etfeedback.getText().toString();
    ParseObject feedback= new ParseObject("Feedback");
    feedback.put("userId", ParseUser.getCurrentUser().getObjectId());
    feedback.put("userName", ParseUser.getCurrentUser().get("username"));
    feedback.put("senderName", ParseUser.getCurrentUser().get("Name"));
    feedback.put("feedbackMessage", feedbackMessage);
    feedback.saveInBackground(new SaveCallback() {
        @Override
        public void done(ParseException e) {
            if (e==null){
                Toast.makeText(FeedBackActivity.this,"Feedback sent",Toast.LENGTH_LONG).show();
                etfeedback.setText("");
                finish();
            }else {
                Toast.makeText(FeedBackActivity.this,"Error",Toast.LENGTH_LONG).show();
            }
        }
    });
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feed_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
