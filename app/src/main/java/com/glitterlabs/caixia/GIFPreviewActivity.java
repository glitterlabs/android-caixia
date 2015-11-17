package com.glitterlabs.caixia;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.glitterlabs.caixia.Adapters.TimerAdapter;
import com.glitterlabs.caixia.Models.MessageData;
import com.glitterlabs.caixia.util.GPSTracker;
import com.glitterlabs.caixia.util.GeoPointParse;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Calendar;

public class GIFPreviewActivity extends AppCompatActivity {

    private  ImageView gifPreviewImage, ivText,ivScheduldTime,ivScheduldDate,ivGeoPoint,ivSendMessage;
    private EditText etTextMessage;
    private Uri fileuri;
    MessageData messageData = new MessageData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif_preview);

         Intent extras=getIntent();
        fileuri = Uri.parse(extras.getStringExtra("uri"));

      //  File bitmapFile = new File(Environment.getExternalStorageDirectory() + "/" + PATH_TO_IMAGE);

        gifPreviewImage = (ImageView) findViewById(R.id.ivGif_Preview);
        Glide.with(this).load(fileuri).asGif().into(gifPreviewImage);
        ivText= (ImageView) findViewById(R.id.ivGif_Text);

        ivScheduldTime= (ImageView) findViewById(R.id.ivGif_Time);
        ivScheduldDate= (ImageView) findViewById(R.id.ivGif_DatePicker);
        ivGeoPoint= (ImageView) findViewById(R.id.ivGif_GPS);
        ivSendMessage= (ImageView) findViewById(R.id.ivGif_Send);
        etTextMessage= (EditText) findViewById(R.id.etGif_TextMessage);
        etTextMessage.setVisibility(View.GONE);
        ivText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               etTextMessage.setVisibility(View.VISIBLE);
            }
        });
        ivScheduldTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker();
            }
        });
        ivScheduldDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        ivGeoPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GeoPointPecker();
            }
        });
        ivSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resizeImage();
                messageData.setTextMessage(etTextMessage.getText().toString());
                Intent i = new Intent(GIFPreviewActivity.this, MyFriendsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("messageData", messageData);
                i.putExtras(bundle);
                i.putExtra("tag","gif");
                startActivity(i);
            }
        });

    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int mYear,mMonth,mDay;
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Toast.makeText(GIFPreviewActivity.this,dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year, Toast.LENGTH_LONG).show();
                        messageData.setScheduldDate(dayOfMonth + "-"
                                + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }
    private void showTimePicker() {
        String times[] = {"1 sec", "2 sec", "3 sec", "4 sec", "5 sec", "6 sec", "7 sec", "8 sec", " 9 sec", "10 sec"};
        final Dialog dialog = new Dialog(GIFPreviewActivity.this);
        dialog.setTitle("Select Time");
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);

        dialog.setContentView(R.layout.timer_list);
        ListView list = (ListView) dialog.findViewById(R.id.lvTimer);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int i = 0;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //set time
                messageData.setTimeForDisplay(position + 1);
                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        TimerAdapter adapter = new TimerAdapter(GIFPreviewActivity.this, times);
        list.setAdapter(adapter);
        dialog.show();


    }
private void GeoPointPecker(){
    GeoPointParse point = null;
    // create class object
    GPSTracker gpsTracker = new GPSTracker(GIFPreviewActivity.this);

    // check if GPS enabled
    if (gpsTracker.canGetLocation()) {

        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();
        point = new GeoPointParse(latitude, longitude);
        messageData.setGeoPoint(point);
        // \n is for new line
        Toast.makeText(GIFPreviewActivity.this, "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
    } else {
        // can't get location
        // GPS or Network is not enabled
        // Ask user to enable GPS/network in settings
        gpsTracker.showSettingsAlert();
    }
}
    private void resizeImage() {
        File imgFile = new File(fileuri.getPath());

        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            final int lnth=myBitmap.getByteCount();
            ByteBuffer dst= ByteBuffer.allocate(lnth);
            myBitmap.copyPixelsToBuffer( dst);
            byte[] byteData=dst.array();
          //  byte [] byteData = copyToBuffer(myBitmap);
            messageData.setByteImage(byteData);
            messageData.setImageFile(imgFile);
        }
    }


/*private void showTimePicker(){
    int mHour = 0,mMinute=0;
    TimePickerDialog tpd = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                   // txtTime.setText(hourOfDay + ":" + minute);
                }
            }, mHour, mMinute, false);
    tpd.show();
}*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gif_preview, menu);
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
