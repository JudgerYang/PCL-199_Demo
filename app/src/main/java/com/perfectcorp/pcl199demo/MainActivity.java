package com.perfectcorp.pcl199demo;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.widget.SeekBar;

public class MainActivity extends Activity {
    private DemoVideoView mVideoView;
    private SeekBar mVideoProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVideoView = (DemoVideoView) findViewById(R.id.demo_video_view);
        mVideoView.setEventListener(mVideoEventListener);

        mVideoProgress = (SeekBar) findViewById(R.id.video_progress);
        mVideoProgress.setMax((int) (mVideoView.getVideoDuration() / 1000));
        mVideoProgress.setOnSeekBarChangeListener(mVideoProgressChangeListener);
    }

    @Override
    protected void onDestroy() {
        mVideoView.setEventListener(null);
        mVideoProgress.setOnSeekBarChangeListener(null);
        super.onDestroy();
    }

    private DemoVideoView.EventListener mVideoEventListener = new DemoVideoView.EventListener() {
        @Override
        public void onPlayTimeUpdated(long playTimeMs) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mVideoProgress.setProgress((int) (playTimeMs / 1000), true);
            } else {
                mVideoProgress.setProgress((int) (playTimeMs / 1000));
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener mVideoProgressChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mVideoView.setPlayTime(progress * 1000);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}
