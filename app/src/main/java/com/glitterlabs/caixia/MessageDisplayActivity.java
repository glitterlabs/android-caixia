package com.glitterlabs.caixia;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;


public class MessageDisplayActivity extends AppCompatActivity {
    private String id, textMessage;
    private ProgressBar bar;
    private ImageView ivPhoto;
    private TextView tvText, tvTimer;
    private ProgressDialog progressDialog;
    // for count down timer
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = true;

    private final long interval = 1 * 1000;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_display);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        textMessage = intent.getStringExtra("text");
        ivPhoto = (ImageView) findViewById(R.id.ivDisplayPhoto);
        tvText = (TextView) findViewById(R.id.tvTextMessage);
        tvText.setVisibility(View.GONE);
        bar = (ProgressBar) findViewById(R.id.ProgressBar);
        tvTimer = (TextView) this.findViewById(R.id.tvTimer);
        tvTimer.setVisibility(View.GONE);
        loadingPhoto(id);


    }

    public void loadingPhoto(String id) {


        // progressDialog = ProgressDialog.sh

        // Locate the class table named "Inbox" in Parse.com
        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Inbox");

        // Locate the objectId from the class
        query.getInBackground(id,
                new GetCallback<ParseObject>() {

                    public void done(ParseObject object,
                                     ParseException e) {
                        // now chang photo seen status
                        object.put("isSeen", 1);
                        object.saveInBackground();
                        final String text = object.getString("text");
                        final int startTime = (int) object.getNumber("timeToDisplayImage");
                        // Locate the column named "ImageName" and set
                        // the string
                        ParseFile fileObject = (ParseFile) object
                                .get("image");
                        String imagetype = object.getString("imageType");
                        if (imagetype.equals("gif")) {
                            try {
                                File imageFile = fileObject.getFile();
                                loadGif(startTime,imageFile);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                        }
                        else{
                            fileObject
                                    .getDataInBackground(new GetDataCallback() {

                                        public void done(byte[] data,
                                                         ParseException e) {
                                            if (e == null) {
                                                Log.d("test",
                                                        "We've got data in data.");
                                                // Decode the Byte[] into
                                                // Bitmap

                                                Bitmap bmp = BitmapFactory
                                                        .decodeByteArray(
                                                                data, 0,
                                                                data.length);

                                                imageRotate(bmp);

                                                tvText.setVisibility(View.VISIBLE);
                                                tvText.setText(text);
                                                // Close progress dialog
                                                bar.setVisibility(View.GONE);
                                                timerEvent(startTime);

                                            } else {
                                                Log.d("test",
                                                        "There is a problem downloading the data.");
                                            }
                                        }
                                    });
                        }



                    }
                });

    }
public void loadGif(int startTime,File gifFile ){


    try {
        Glide.with(MessageDisplayActivity.this).load(gifFile).asGif().into(ivPhoto);
    } catch (Exception e1) {
        e1.printStackTrace();
    }
    bar.setVisibility(View.GONE);
    timerEvent(startTime);
}
    public void imageRotate(Bitmap bmp)
    {
// Getting width & height of the given image.
        int w = bmp.getWidth();
        int h = bmp.getHeight();
// Setting pre rotate to 90
        Matrix mtx = new Matrix();
        mtx.postRotate(-90); // anti-clockwise by 90 degrees
// Rotating Bitmap
       Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
       BitmapDrawable bmd = new BitmapDrawable(rotatedBMP);
       ivPhoto.setImageBitmap(rotatedBMP);


    }

    public void timerEvent(int startTime) {
        tvTimer.setVisibility(View.VISIBLE);
        countDownTimer = new MDCountDownTimer((startTime * 1000), interval);
        tvTimer.setText(tvTimer.getText() + String.valueOf((startTime * 1000) / 1000));
        countDownTimer.start();
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!timerHasStarted) {
                    countDownTimer.start();
                    timerHasStarted = true;
                    //   Toast.makeText(MessageDisplayActivity.this,"Stop Timer",Toast.LENGTH_LONG).show();

                } else {
                    countDownTimer.cancel();
                    timerHasStarted = false;
                    //  Toast.makeText(MessageDisplayActivity.this, "Restart Timer", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public class MDCountDownTimer extends CountDownTimer {

        public MDCountDownTimer(long startTime, long interval) {

            super(startTime, interval);

        }

        @Override
        public void onFinish() {
            finish();

        }

        @Override
        public void onTick(long millisUntilFinished) {

            tvTimer.setText("" + millisUntilFinished / 1000);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
