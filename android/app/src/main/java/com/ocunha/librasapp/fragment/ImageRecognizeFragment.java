package com.ocunha.librasapp.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ocunha.librasapp.R;
import com.ocunha.librasapp.activity.MainActivity;
import com.ocunha.librasapp.utils.Constants;
import com.ocunha.librasapp.utils.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by osnircunha on 4/7/16.
 */
public class ImageRecognizeFragment extends Fragment {

    private ImageView imageView;
    private ProgressBar progressBar;
    private Bitmap bitmap;

    private FragmentActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity   = (FragmentActivity)context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_image_recognition_layout, container, false);

        imageView = (ImageView) view.findViewById(R.id.thumb);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        view.findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.isConnected(getContext())){
                    Snackbar.make(view, R.string.label_no_internet, Snackbar.LENGTH_LONG).show();
                    return;
                }
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 777);
                }
            }
        });

        view.findViewById(R.id.btn_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!MainActivity.isConnected(getContext())){
                    Snackbar.make(view, R.string.label_no_internet, Snackbar.LENGTH_LONG).show();
                    return;
                }
                Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                pickPhoto.setType("image/*");
                startActivityForResult(Intent.createChooser(pickPhoto, getString(R.string.label_select_picture)), 778);
            }
        });

        if(savedInstanceState != null){
            bitmap = savedInstanceState.getParcelable("bitmap");
            imageView.setImageBitmap(bitmap);
            if(bitmap != null){
                progressBar.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("bitmap", bitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dt) {
        if(resultCode == Activity.RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            switch (requestCode) {
                case 777:
                    Bundle extras = dt.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(bitmap);
                    classifyImage(Constants.IMAGE_RECOGNITION_CLASSIFY_URL, bitmap);

                    break;
                case 778:
                    Uri selectedImage = dt.getData();
                    try {
                        bitmap = decodeUri(selectedImage);
                        imageView.setImageBitmap(bitmap);

                        classifyImage(Constants.IMAGE_RECOGNITION_CLASSIFY_URL, bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(selectedImage), null, o2);
    }

    private void findWords(String words){
        RequestParams requestParams = new RequestParams();
        requestParams.put("word", words);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constants.LIBRAS_WORD_URL, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                WordListFragment wordListFragment = new WordListFragment();
                wordListFragment.setLibrasWords(JsonUtils.parseJsonListToLibrasWord(new String(responseBody)));

                progressBar.setVisibility(View.INVISIBLE);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, wordListFragment, "main").commit();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(getView(), "Error on search", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void classifyImage(String url, Bitmap bitmap){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos);
        byte[] data = bos.toByteArray();

        RequestParams requestParams = new RequestParams();
        requestParams.put("images_file", new ByteArrayInputStream(data), "image.jpg", "image/jpeg");
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(60000);
        client.setEnableRedirects(true);
        client.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                findWords(new String(responseBody));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                Snackbar.make(getView(), "Error on search", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }

        });
    }

}
