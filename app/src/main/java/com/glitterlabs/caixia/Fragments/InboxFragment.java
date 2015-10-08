package com.glitterlabs.caixia.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.glitterlabs.caixia.Adapters.InboxAdapter;
import com.glitterlabs.caixia.Models.InboxList;
import com.glitterlabs.caixia.PreviewPhotoActivity;
import com.glitterlabs.caixia.UserProfileActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SendCallback;
import com.glitterlabs.caixia.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/*
    Copyright (c) 2015-2020 Glitter Technology Ventures, LLC.
    All rights reserved. Patents pending.

    Responsible: Abhay Bhusari
 */
public class InboxFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;
    private ListView lvInbox;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    String currentUser = ParseUser.getCurrentUser().getObjectId();


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

        getInboxData();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.appBar);

        ImageView ivUser = (ImageView) toolbar.findViewById(R.id.ivUserProfile);
        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserProfileActivity.class));

                sendPush();
            }
        });
        ImageView ivPreview = (ImageView) toolbar.findViewById(R.id.ivPreview);
        ivPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PreviewPhotoActivity.class));
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
                            Toast.makeText(getActivity(), "fail push", Toast.LENGTH_LONG).show();
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
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {

                progressDialog.dismiss();

                if (e == null) {

                    for (int i = 0; i < objects.size(); i++) {
                        InboxList inboxList = new InboxList();
                        inboxList.setItemId(objects.get(i).getObjectId());
                        inboxList.setSenderName(objects.get(i).getString("senderName"));
                        inboxList.setTextMessage(objects.get(i).getString("text"));
                        inboxList.setCurrentTime(getCurrentTime());
                        inboxList.setStatus(objects.get(i).getInt("isSeen"));
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




    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

}
