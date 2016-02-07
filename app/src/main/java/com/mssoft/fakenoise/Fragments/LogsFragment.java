package com.mssoft.fakenoise.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mssoft.fakenoise.R;

/**
 * Created by Marius on 8/29/2015.
 */
public class LogsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View logs = inflater.inflate(R.layout.log_layout,container,false);
        ((TextView)logs.findViewById(R.id.logs)).setText("Hello Log Frag");
    return logs;
    }
}

