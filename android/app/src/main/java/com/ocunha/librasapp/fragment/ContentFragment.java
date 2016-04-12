package com.ocunha.librasapp.fragment;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.ocunha.librasapp.R;
import com.ocunha.librasapp.domain.LibrasWord;
import com.ocunha.librasapp.utils.Constants;

/**
 * Created by osnircunha on 4/7/16.
 */
@SuppressLint("ValidFragment")
public class ContentFragment extends Fragment {

    private LibrasWord librasWord;
    private VideoView videoView;

    public void setLibrasWord(LibrasWord librasWord) {
        this.librasWord = librasWord;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_layout, container, false);


        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        if(savedInstanceState != null) {
            this.librasWord = (LibrasWord) savedInstanceState.getSerializable("word");
        }

        setupTextView(R.id.title_word, librasWord.getWord(), view);

        setupTextView(R.id.description_word, librasWord.getDescription(), view);
        setupTextView(R.id.libras_sample_word, librasWord.getLibrasSample(), view);
        setupTextView(R.id.sample_word, librasWord.getSample(), view);

        String url = String.format(Constants.LIBRAS_VIDEO_URL, librasWord.getVideoUrl());

        videoView = (VideoView) view.findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse(url));

        MediaController mc = new MediaController(getActivity());
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setMediaController(mc);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {

                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                        progressBar.setVisibility(View.INVISIBLE);
                        mp.start();
                    }
                });
            }
        });

        return view;
    }

    private void setupTextView(int id, String text, View view){
        TextView title = (TextView) view.findViewById(id);
        title.setText(text);
    }

    @Override
    public void onStart() {
        super.onStart();
        videoView.start();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("word", this.librasWord);
    }

}
