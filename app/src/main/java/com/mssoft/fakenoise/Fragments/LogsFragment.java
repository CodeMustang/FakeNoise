package com.mssoft.fakenoise.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.mssoft.fakenoise.Activities.MainActivity;
import com.mssoft.fakenoise.Adapters.CallHistoryAdapter;
import com.mssoft.fakenoise.Database.ContactsDataSource;
import com.mssoft.fakenoise.Database.LogsDataSource;
import com.mssoft.fakenoise.R;
import com.mssoft.fakenoise.Utilities.ContactLog;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Marius on 8/29/2015.
 */
public class LogsFragment extends android.app.Fragment implements ObservableScrollViewCallbacks {
    private Context mContext;
    private TextView mWelcomeText;
    private ListView mListView;
    private FloatingActionButton deleteLogs;
    private CallHistoryAdapter logsAdapter;
    private ArrayList<ContactLog> mList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View logs = inflater.inflate(R.layout.log_layout, container, false);
        mWelcomeText = (TextView) logs.findViewById(R.id.logs);
        mListView = (ListView) logs.findViewById(R.id.log_lview);
        deleteLogs = (FloatingActionButton) logs.findViewById(R.id.delete_logs);

        deleteLogs.setOnClickListener(new FabListener());

        return logs;
    }


    private class FabListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            builder.setTitle("Delete Logs");
            builder.setMessage("Do you want to delete all logs ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    mList.clear();
                    MainActivity.mLogsDataSource.deleteAllLogs();
                    logsAdapter.notifyDataSetChanged();
                    mWelcomeText.setVisibility(View.VISIBLE);
                    deleteLogs.setVisibility(View.GONE);

                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            builder.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mList = MainActivity.mLogsDataSource.getAllLogs();

        if (mList.size() != 0) {

            logsAdapter = new CallHistoryAdapter(mContext, mList);
            mWelcomeText.setVisibility(View.GONE);
            mListView.setAdapter(logsAdapter);
            deleteLogs.setVisibility(View.VISIBLE);
        }
        else{
            mWelcomeText.setVisibility(View.VISIBLE);
            deleteLogs.setVisibility(View.GONE);
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        ActionBar tb = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (tb.isShowing()) {
                tb.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!tb.isShowing()) {
                tb.show();
            }
        }
    }


}

