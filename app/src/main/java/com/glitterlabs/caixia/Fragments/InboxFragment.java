package com.glitterlabs.caixia.Fragments;
/**
 * Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
 * All rights reserved. Patents pending.
 * Responsible: Abhay Bhusari
 *
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.glitterlabs.caixia.Adapters.InboxAdapter;
import com.glitterlabs.caixia.GIFPreviewActivity;
import com.glitterlabs.caixia.Models.InboxList;
import com.glitterlabs.caixia.PreviewPhotoActivity;
import com.glitterlabs.caixia.R;
import com.glitterlabs.caixia.UserProfileActivity;
import com.glitterlabs.caixia.util.Helper;
import com.mlapadula.gifencoder.GifEncoder;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;


public class InboxFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private ListView lvInbox;
    private  ImageView ivGifCamera,ivUser,ivPreview;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String currentUser = ParseUser.getCurrentUser().getObjectId();
    Context mContext;
    // for capture multiple photo
    private static final int RESULT_IMAGE_PICKER = 9000;
    private ArrayList<Bitmap> bitmapList = new ArrayList<Bitmap>();
    private  File pictureFile;

    public static InboxFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        InboxFragment fragment = new InboxFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inbox, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        lvInbox = (ListView) view.findViewById(R.id.lvInbox);
        this.mContext=getActivity();

        getInboxData();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.appBar);

        ivUser = (ImageView) toolbar.findViewById(R.id.ivUserProfile);
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserProfileActivity.class));

                sendPush();
            }
        });
     ivPreview = (ImageView) toolbar.findViewById(R.id.ivPreview);
        ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               startActivity(new Intent(getActivity(), PreviewPhotoActivity.class));
            }
        });

        ivGifCamera= (ImageView) toolbar.findViewById(R.id.iv_Gif_Camera);
        ivGifCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImages();
            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getInboxData();

            }
        });

        return view;
    }

    private void getImages() {
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.
                .setTabSelectionIndicatorColor(R.color.blue)
                .setCameraButtonColor(R.color.green)
                .setSelectionLimit(4)    // set photo selection limit. Default unlimited selection.
                .build();
        ImagePickerActivity.setConfig(config);
        startActivityForResult(intent, RESULT_IMAGE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_IMAGE_PICKER) {
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (parcelableUris == null) {
                    return;
                }

                // Java doesn't allow array casting, this is a little hack
                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);

                if (uris != null) {
                     bitmapList.clear();
                    for (int i=0; i<uris.length;i++) {
                        Log.i("TAG", " uri: " + uris[i]);
                        // mMedia.add(uri);
                        try {
                            Bitmap bitmap = BitmapFactory.decodeFile(uris[i].toString());
                            //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(UserProfileActivity.this.getContentResolver(), uris[i]);
                            bitmap = Bitmap.createScaledBitmap(bitmap,600, 600, false);
                            bitmapList.add(bitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // encode gif
                    pictureFile = getOutputMediaFile();
                    // convert images to gif animation image file
                    encodeGif(bitmapList, pictureFile);

                }else {
                    Helper.showDialog("Error","Please select photo first",getActivity());
                }
            }

        }

    }

    public void encodeGif(List<Bitmap> bitmaps, File outFile) {
        try {
            GifEncoder gifEncoder = new GifEncoder();
            gifEncoder.start(outFile.getCanonicalPath());
            gifEncoder.setDelay(400); // 300ms between frames
            // Grab frames and encode them
            Iterator<Bitmap> iter = bitmaps.iterator();
            while (iter.hasNext()) {

                Bitmap bitmap = iter.next();
                gifEncoder.addFrame(bitmap);
            }
            // Make the gif
            gifEncoder.finish();
            // Add to gallery
            Uri gifUri = Uri.fromFile(outFile);
            //  addToGallery(picUri);
            Intent in = new Intent(getActivity(), GIFPreviewActivity.class);
            in.putExtra("uri", gifUri.toString());
            startActivity(in);
        } catch (IOException err) {

        }
    }
    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CiaxiaApp");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("Camera Guide", "Required media storage does not exist");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".gif");

        return mediaFile;
    }

    public void sendPush() {
        // Find devices associated with these users
        ParseQuery pushQuery = ParseInstallation.getQuery();
        // pushQuery.whereEqualTo("userId", "opsSRJaQMz");
        pushQuery.whereEqualTo("deviceType", "android");
        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage("Abhay sent you friend request..!");
        push.sendInBackground
                (new SendCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {

                            Toast.makeText(getActivity(), "Success push", Toast.LENGTH_LONG).show();
                        } else {
                           // Toast.makeText(getActivity(), "fail push", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void getInboxData() {

        final ArrayList<InboxList> inboxData = new ArrayList<InboxList>();
       final ProgressDialog progressDialog = ProgressDialog.show(getActivity(), null,
                getString(R.string.alert_wait));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Inbox");
        query.whereEqualTo("userId", currentUser);
        query.whereExists("senderName");
        query.orderByDescending("senderName");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                progressDialog.dismiss();

                if (e == null) {

                    for (int i = 0; i < objects.size(); i++) {
                        InboxList inboxList = new InboxList();
                        inboxList.setItemId(objects.get(i).getObjectId());
                        inboxList.setSenderName(objects.get(i).getString("senderName"));
                        inboxList.setTextMessage(objects.get(i).getString("text"));
                        // inboxList.setCurrentTime(getCurrentTime());
                        inboxList.setStatus(objects.get(i).getInt("isSeen"));
                        Date date = objects.get(i).getCreatedAt();
                        inboxList.setCurrentTime(getMessageTime(date));
                        //inboxList.setCurrentTime(date.toGMTString());
                        inboxData.add(inboxList);

                    }

                } else {

                    e.printStackTrace();
                }

                InboxAdapter adapter = new InboxAdapter(getActivity(), inboxData);
                lvInbox.setAdapter(adapter);
                mSwipeRefreshLayout.setRefreshing(false);


            }


        });

    }

    private String getMessageTime(Date date) {
        long unixdate = date.getTime();
        DateFormat formatter = new SimpleDateFormat("hh:mm a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixdate * 1000);
        String strDate =formatter.format(calendar.getTime());
        return strDate;
    }
    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }



}
