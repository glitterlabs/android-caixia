package com.glitterlabs.caixia;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.List;

public class PreviewPhotoActivity extends AppCompatActivity {
    private ProgressBar bar;
    private ImageView ivPhoto, ivCancelPreview;
    private TextView tvText, tvTimer;
    private ProgressDialog progressDialog;
    // for count down timer
    private CountDownTimer countDownTimer;
    private boolean timerHasStarted = true;

    private final long interval = 1 * 1000;
    String text, scheduldate;
    int startTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_photo);

        ivPhoto = (ImageView) findViewById(R.id.ivDisplayPhoto_PreviewPhoto);
        tvText = (TextView) findViewById(R.id.tvTextMessage_PreviewPhoto);
        tvText.setVisibility(View.GONE);
        bar = (ProgressBar) findViewById(R.id.ProgressBar_PreviewPhoto);
        tvTimer = (TextView) this.findViewById(R.id.tvTimer_PreviewPhoto);
        tvTimer.setVisibility(View.GONE);
        ivCancelPreview= (ImageView) findViewById(R.id.ivCancelPreviewEvent);
        ivCancelPreview.setVisibility(View.GONE);
        ivCancelPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();   // to stop preview event
            }
        });
        loadingPhoto();
    }

    public void loadingPhoto() {
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");

        final ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                "Inbox");

        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        query.whereExists("image");
       // query.whereEqualTo("createdAt",sdf.format(date));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for (int i = 0; i < list.size(); i++) {
                    scheduldate= list.get(i).getString("scheduledDate");
                    text = list.get(i).getString("text");
                    startTime = (int) list.get(i).getNumber("timeToDisplayImage");
                    // Locate the column named "ImageName" and set
                    // the string


                    ParseFile fileObject = (ParseFile) list.get(i)
                            .get("image");
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
                                        ivCancelPreview.setVisibility(View.VISIBLE);
                                        tvText.setVisibility(View.VISIBLE);
                                        tvText.setText(text);
                                        // Close progress dialog
                                        bar.setVisibility(View.GONE);
                                        timerEvent(startTime);

                                    } else {
                                        Log.d("test",
                                                "There was a problem downloading the data.");
                                    }
                                }
                            });
                    tvText.setVisibility(View.GONE);
                    tvTimer.setVisibility(View.GONE);

                }

            }
        });

    }

    public void imageRotate(Bitmap bmp) {
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
        countDownTimer = new MyCountDownTimer((startTime * 1000), interval);
        tvTimer.setText(tvTimer.getText() + String.valueOf((startTime * 1000) / 1000));
        countDownTimer.start();


    }

    public class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long startTime, long interval) {

            super(startTime, interval);

        }

        @Override
        public void onFinish() {


        }

        @Override
        public void onTick(long millisUntilFinished) {

            tvTimer.setText("" + millisUntilFinished / 1000);

        }

    }


}
