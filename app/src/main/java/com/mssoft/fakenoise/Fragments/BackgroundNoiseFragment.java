package com.mssoft.fakenoise.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableGridView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.mssoft.fakenoise.Adapters.NoiseAdapter;
import com.mssoft.fakenoise.Constants.Constants;
import com.mssoft.fakenoise.Utilities.NoiseSounds;
import com.mssoft.fakenoise.R;
import com.mssoft.fakenoise.Activities.RecordNoiseActivity;
import com.mssoft.fakenoise.Utilities.Permissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marius on 8/29/2015.
 */

public class BackgroundNoiseFragment extends android.app.Fragment implements ObservableScrollViewCallbacks {
    private Context mContext;
    private ArrayList<NoiseSounds> noises;
    private ObservableGridView gView;
    private NoiseAdapter adapter;
    private Map<Integer, Boolean> itemPressed;
    private SparseBooleanArray currentArray;
    private int itemCount;
    private TextView statusScreen;
    private FloatingActionButton addNoiseBtn;
    private boolean nonAudioFiles,isFirstLaunch = true;
    private static boolean fileIsDeteled;

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

    public ArrayList<NoiseSounds> getAllNoises() {

        ArrayList<NoiseSounds> list = new ArrayList<>();
        nonAudioFiles = false;
        File file = new File(Constants.AUDIO_PATH);
        File[] audioFiles = file.listFiles();


        if (audioFiles != null) {
            for (int i = 0; i < audioFiles.length; i++) {
                NoiseSounds sound = new NoiseSounds();
                try {
                    sound.setSound(MediaPlayer.create(mContext, Uri.parse(audioFiles[i].getAbsolutePath())));
                    sound.setName(audioFiles[i].getName());
                    sound.setPath(audioFiles[i].getAbsolutePath());
                } catch (Error e) {
                    e.printStackTrace();
                }

                if (sound.getSound() != null) {
                    list.add(sound);
                } else {
                    nonAudioFiles = true;
                }

            }

        }

        return list;

    }


    //show the message regarding sounds only when the noise fragment is visible to the user
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && nonAudioFiles && isFirstLaunch) {
            Snackbar.make(addNoiseBtn,
                    "Failed to load some files. Only audio permitted!"
                    , Snackbar.LENGTH_LONG).show();

            isFirstLaunch = false;
        }
    }


    public static void deleteNoiseFile(String path) {

        File fdelete = new File(path);
        if (fdelete.exists()) {
            fileIsDeteled = fdelete.delete();
        }
    }

    public void deleteNoise(SparseBooleanArray sparseBoolArray) {

        List<NoiseSounds> selectedNoises = new ArrayList<>();
        for (int i = 0; i < sparseBoolArray.size(); i++) {
            if (sparseBoolArray.valueAt(i)) {
                deleteNoiseFile(noises.get(sparseBoolArray.keyAt(i)).getPath());
                selectedNoises.add(noises.get(sparseBoolArray.keyAt(i)));
            }
        }
        noises.removeAll(selectedNoises);

        if (noises.size() == 0) {
            statusScreen.setVisibility(View.VISIBLE);
        }
    }

    public void startRecordActivity(){
        Intent intent = new Intent(mContext, RecordNoiseActivity.class);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {

            case Permissions.RECORD_AUDIO_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Permissions.recordAudio = true;
                    startRecordActivity();

                } else {
                    Permissions.recordAudio = false;
                }
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View bgNoise = inflater.inflate(R.layout.background_noise_picker_layout, container, false);

        noises = new ArrayList<>();
        itemPressed = new HashMap<>();
        gView = (ObservableGridView) bgNoise.findViewById(R.id.gridview);
        statusScreen = (TextView) bgNoise.findViewById(R.id.status_screen);
        addNoiseBtn = (FloatingActionButton) bgNoise.findViewById(R.id.add_noise_btn);


        addNoiseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Permissions.askForPermission(getActivity(),BackgroundNoiseFragment.this,2);

                if (Permissions.recordAudio) {
                   startRecordActivity();

                }

            }
        });

        gView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
//        checkedPos = new SparseBooleanArray();

        gView.setMultiChoiceModeListener(new GridView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                currentArray = gView.getCheckedItemPositions();
                itemCount = gView.getCheckedItemCount();

                switch (itemCount) {
                    case 1:
                        mode.setSubtitle("One item selected.");
                        break;
                    default:
                        mode.setSubtitle(itemCount + " items selected.");
                        break;
                }
                if (checked) {
                    gView.getChildAt(position).setBackground(ContextCompat
                            .getDrawable(mContext, R.drawable.grid_noise_item_shape_pressed));
                } else {
                    gView.getChildAt(position).setBackground(ContextCompat
                            .getDrawable(mContext, R.drawable.grid_noise_item_final));
                }


            }


            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.item_menu, menu);

                mode.setTitle("Select Items.");
                mode.setSubtitle("One item selected.");
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.delete_item:

                        deleteNoise(currentArray);
                        currentArray.clear();
                        adapter.notifyDataSetChanged();
                        gView.invalidateViews();

                        Snackbar.make(addNoiseBtn, itemCount + " Item(s) deleted .",
                                Snackbar.LENGTH_SHORT).show();
                        mode.finish(); //action picked ,so close cab

                        if(! fileIsDeteled){
                            Snackbar.make(addNoiseBtn, itemCount + " File deletion has failed .",
                                    Snackbar.LENGTH_SHORT).show();
                        }

                        return true;

                    default:
                        return true;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                gView.clearChoices(); //clearing the highlighted choices

                for (int i = 0; i < gView.getCount(); i++)
                    gView.setItemChecked(i, false);

                gView.setAdapter(adapter);//it's not the best practical way to do it but it works for all API's

            }
        });

        return bgNoise;
    }

    public void initializeMap(int size) {
        for (int i = 0; i < size; i++) {
            itemPressed.put(i, false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        noises = getAllNoises();

        if (noises.size() != 0) {
            statusScreen.setVisibility(View.GONE);
            adapter = new NoiseAdapter(mContext, noises);

            gView.setVerticalSpacing(20);
            gView.setAdapter(adapter);

            initializeMap(noises.size());

        } else {
            statusScreen.setVisibility(View.VISIBLE);
        }

        gView.setScrollViewCallbacks(this);
        gView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                view.setSelected(true);

                final ImageView image = (ImageView) view.findViewById(R.id.noise_image);

                if (!itemPressed.get(position)) {
                    image.setBackground(ContextCompat
                            .getDrawable(mContext, R.drawable.ic_pause_bg));
                    itemPressed.put(position, true);
                } else {
                    itemPressed.put(position, false);
                    image.setBackground(ContextCompat.getDrawable(mContext, R.drawable.ic_noise_bg));
                }

                MediaPlayer selectedSound = noises.get(position).getSound();
                selectedSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        image.setBackground(ContextCompat
                                .getDrawable(mContext, R.drawable.ic_noise_bg));
                        itemPressed.put(position, false);
                    }
                });
                String soundPath = noises.get(position).getPath();

                if (selectedSound.isPlaying()) {
                    selectedSound.stop();
                } else {

                    try {
                        selectedSound.reset();
                        selectedSound.setDataSource(mContext, Uri.parse(soundPath));
                        selectedSound.prepare();
                        selectedSound.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        if (noises.size() != 0)
            for (int i = 0; i < noises.size(); i++) {
                if (noises.get(i).getSound().isPlaying()) {
                    noises.get(i).getSound().stop();
                    noises.get(i).getSound().release();
                }
            }
    }


    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

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