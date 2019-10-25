package com.android.sb_audio_player;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;

public class AudioPlayerFragment extends Fragment {

    private MediaPlayer mediaPlayer = null;
    private int resumePosition;
    private String MP3URL = "https://www.nasa.gov/mp3/640149main_Computers%20are%20in%20Control.mp3";

    public View onCreateView(LayoutInflater inflater, ViewGroup conatiner,
                             Bundle saveInstanceState) {
        View v = inflater.inflate(R.layout.fragment_audio_player, conatiner, false);

        Button playButton = (Button) v.findViewById(R.id.play_button);
        playButton.setOnClickListener(playClickedListener);
        Button stopButton = (Button) v.findViewById(R.id.stop_button);
        stopButton.setOnClickListener(stopClickedListener);
        Button pauseButton = (Button) v.findViewById(R.id.pause_button);
        pauseButton.setOnClickListener(pauseClickedListener);
        Button resumeButton = (Button) v.findViewById(R.id.resume_button);
        resumeButton.setOnClickListener(resumeClickedListener);
        Button toggleButton = (Button) v.findViewById(R.id.toggle_button);
        toggleButton.setOnClickListener(toggleClickedListener);

        return v;
    }

    private void loadMP3URL(String path) {
        mediaPlayer = new MediaPlayer();
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        mediaPlayer.setAudioAttributes(audioAttributes);
        try {
            mediaPlayer.setDataSource(path);
        } catch (IOException e) {
            Log.d("APF", e.toString());
        }
        mediaPlayer.prepareAsync();

        mediaPlayer.setOnCompletionListener(
                new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.stop();
                        mediaPlayer.prepareAsync();
                    }
                }
        );

        mediaPlayer.setOnPreparedListener(
                new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                        mediaPlayer.setOnPreparedListener(null);
                    }
                }
        );

    }

    private void playMedia() {
        if (mediaPlayer == null) {
            loadMP3URL(MP3URL);
            //mediaPlayer = MediaPlayer.create((getActivity(), R.raw.one_small_step))
        } else if (!mediaPlayer.isPlaying()) {  //if mediaPlayer is not playing
            mediaPlayer.start();    //start from the beginning
        }
    }

    private View.OnClickListener playClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            playMedia();
        }
    };

    private View.OnClickListener stopClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying())  {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    playMedia();            // loop - play again
                } else if (!mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        }
    };

    private View.OnClickListener pauseClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mediaPlayer != null) {
                mediaPlayer.pause();
                resumePosition = mediaPlayer.getCurrentPosition();
            }
        }
    };

    private View.OnClickListener resumeClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (mediaPlayer != null) {
                mediaPlayer.seekTo(resumePosition);  // continue where left off
                mediaPlayer.start();
            }

        }
    };

    private View.OnClickListener toggleClickedListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // toggle URLs
            MP3URL = (MP3URL ==  "https://history.nasa.gov/afj/ap11fj/audio/1892800.mp3") ?
                    "https://www.nasa.gov/mp3/640149main_Computers%20are%20in%20Control.mp3":
                    "https://history.nasa.gov/afj/ap11fj/audio/1892800.mp3";
            // Load the URL
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            loadMP3URL(MP3URL);
        }
    };
}

