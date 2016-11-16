package com.mssoft.fakenoise.Tasks;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.mssoft.fakenoise.Fragments.BackgroundNoiseFragment;
import com.mssoft.fakenoise.R;
import com.mssoft.fakenoise.Utilities.NoiseSounds;

import java.io.File;
import java.io.IOException;

/**
 * Created by Marius on 7/19/2016.
 */
public class RecordTask extends AsyncTask {
    private final static String AUDIO_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FakeNoises/";

    private TextView title;
    private String titleText;
    private String fileName;
    private ImageView recordStartImg;
    private Chronometer chronometer;
    private MediaRecorder recordNoise;
    private FloatingActionButton recordBtn;
    private Context mContext;

    public RecordTask(TextView title, String titleText, ImageView recordStartImg,
                      Chronometer chronometer, FloatingActionButton recordBtn, Context mContext,String fileName){
        this.title = title;
        this.titleText = titleText;
        this.recordStartImg = recordStartImg;
        this.chronometer = chronometer;
        this.recordBtn = recordBtn;
        this.mContext = mContext;
        this.fileName = fileName + ".3gp";

    }


    private MediaRecorder setUpRecorder() {

        MediaRecorder noise = new MediaRecorder();

        noise.setAudioSource(MediaRecorder.AudioSource.MIC);
        noise.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        noise.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        noise.setOutputFile(AUDIO_PATH + fileName);

        return noise;
    }

    public void stopCapture() {

        try {
            recordNoise.stop();

            NoiseSounds newSound = new NoiseSounds();
            newSound.setSound(MediaPlayer.create(mContext, Uri.parse(fileName)));
            newSound.setName(fileName);

            Snackbar.make(recordBtn,"Record saved.",Snackbar.LENGTH_LONG)
                    .show();

        }
        catch (RuntimeException e){
            e.printStackTrace();
            Snackbar.make(recordBtn,"Something went wrong! Please try again.",Snackbar.LENGTH_LONG)
                    .show();

            BackgroundNoiseFragment.deleteNoiseFile(AUDIO_PATH + fileName);
        }


        titleText = mContext.getString(R.string.record_default_title);
        title.setText(titleText);
        chronometer.stop();
        chronometer.setBase(SystemClock.elapsedRealtime());
        recordStartImg.setVisibility(View.GONE);
        recordBtn.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.record_icon));
    }


    private void setUpSoundsDirectory(){

        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"FakeNoises");

        if(!directory.exists() && !directory.isDirectory()) {
            directory.mkdirs();
        }
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        titleText = mContext.getString(R.string.record_start_title);
        title.setText(titleText);
        recordStartImg.setVisibility(View.VISIBLE);
        recordBtn.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.record_stop));
        recordNoise = setUpRecorder();
        setUpSoundsDirectory();
        chronometer.setBase(SystemClock.elapsedRealtime());

        try {
            recordNoise.prepare();
            recordNoise.start();
            chronometer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Object doInBackground(Object[] params) {

        return null;
    }


}
