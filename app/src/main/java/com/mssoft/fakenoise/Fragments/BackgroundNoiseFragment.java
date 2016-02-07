package com.mssoft.fakenoise.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.mssoft.fakenoise.Adapters.NoiseAdapter;
import com.mssoft.fakenoise.Constants.Contants;
import com.mssoft.fakenoise.Database.NoiseDataSource;
import com.mssoft.fakenoise.NoiseSounds;
import com.mssoft.fakenoise.R;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Marius on 8/29/2015.
 */

public class BackgroundNoiseFragment extends Fragment implements ObservableScrollViewCallbacks {
    private Context mContext;
    private NoiseDataSource mSource;
    private AlertDialog mDialog;
    private boolean implicitSounds = false;
    private ArrayList<NoiseSounds> noises;
    private ObservableGridView gView;
    private com.mssoft.fakenoise.Constants.Contants sharedData;
    private NoiseAdapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View bgNoise = inflater.inflate(R.layout.background_noise_picker_layout, container, false);
        gView = (ObservableGridView) bgNoise.findViewById(R.id.gridview);
        FabNoiseFunctionality(bgNoise);

        mSource = new NoiseDataSource(mContext);
        sharedData = new Contants();
        try {
            mSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        noises = new ArrayList<>();

        try {
            noises = mSource.getAllNoises();
        } catch (IOException e) {
            e.printStackTrace();
        }
        adapter = new NoiseAdapter(mContext,noises);

        gView.setVerticalSpacing(1);
        gView.setAdapter(adapter);


        gView.setScrollViewCallbacks(this);
        return bgNoise;
    }


    @Override
    public void onResume() {
        super.onResume();
        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                if (!noises.get(position).getPrepared()) {
                    try {
                        noises.get(position).getSound().prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    noises.get(position).setPrepared(true);
                }
                noises.get(position).getSound().start();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(noises.size()!=0)
            for(int i = 0; i < noises.size();i++)
                noises.get(i).getSound().release();
    }

    private void FabNoiseFunctionality(View view){
        FloatingActionButton addNoiseBtn = (FloatingActionButton) view.findViewById(R.id.add_noise_btn);

        addNoiseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(sharedData.getBackgroundNoiseDialogTitle());
                builder.setSingleChoiceItems(sharedData.getBackgroundNoiseDialogSelectionList(), -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0 :
                                try {
                                    if(mSource.getAllNoises().size() == 0)
                                        implicitSounds = true;
                                    else
                                        Toast.makeText(mContext,"You already have the implicit sounds !",Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if(implicitSounds) {
                                    implicitSounds = false;

                                    String testPath = "android.resource://" + getActivity().getPackageName()+"/"  + R.raw.audio_1_applause;
                                    String testPath1 = "android.resource://" + getActivity().getPackageName() +"/" + R.raw.audio_2_night_storm;

                                    try {
                                        mSource.createNoise("First audio", testPath);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        mSource.createNoise("Second audio", testPath1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    ArrayList<NoiseSounds> noises = new ArrayList<>();

                                    try {
                                        noises = mSource.getAllNoises();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    adapter = new NoiseAdapter(mContext, noises);
                                    gView.setAdapter(adapter);
                                }
                                break;
                            case 1:
                                Toast.makeText(mContext,"Record Coming Soon",Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
                mDialog = builder.create();
                mDialog.show();
            }
        });
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar tb = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(scrollState == ScrollState.UP){
            if(tb.isShowing()){
                tb.hide();
            }
        }
        else if(scrollState == ScrollState.DOWN){
            if(!tb.isShowing()){
                tb.show();
            }
        }
    }
}