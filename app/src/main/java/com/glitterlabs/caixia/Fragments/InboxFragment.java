package com.glitterlabs.caixia.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.glitterlabs.caixia.Activities.LoginActivity;
import com.glitterlabs.caixia.Activities.UserProfileActivity;
import com.glitterlabs.caixia.Adapters.InboxAdapter;
import com.glitterlabs.caixia.Models.InboxList;
import com.glitterlabs.caixia.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
             //   showLogoutDilog("Logout", "Do you want to logout?", getActivity());

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

    public void showLogoutDilog(String title, String message, Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ParseUser.logOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(), LoginActivity.class));
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

    private String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
        String strDate = sdf.format(c.getTime());
        return strDate;
    }

}
