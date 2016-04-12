package com.ocunha.librasapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ocunha.librasapp.R;
import com.ocunha.librasapp.utils.Constants;
import com.ocunha.librasapp.utils.JsonUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Created by osnircunha on 4/7/16.
 */
public class ImageRecognizeFragment extends Fragment {

    public static final String TAG = ImageRecognizeFragment.class.getSimpleName();

    private ImageView imageView;
    private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_recognition_layout, container, false);

        imageView = (ImageView) view.findViewById(R.id.thumb);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);

        view.findViewById(R.id.btn_capture).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, 777);
                }
            }
        });

        view.findViewById(R.id.btn_from_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK);
                pickPhoto.setType("image/*");
                startActivityForResult(Intent.createChooser(pickPhoto, getString(R.string.label_select_picture)), 778);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent dt) {
        if(resultCode == Activity.RESULT_OK) {
            progressBar.setVisibility(View.VISIBLE);
            switch (requestCode) {
                case 777:
                    Bundle extras = dt.getExtras();
                    final Bitmap imageBitmap = (Bitmap) extras.get("data");
                    imageView.setImageBitmap(imageBitmap);
                    classifyImage(Constants.IMAGE_RECOGNITION_CLASSIFY_URL, imageBitmap);

                    break;
                case 778:
                    Uri selectedImage = dt.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String mCurrentPhotoPath = cursor.getString(columnIndex);
                    cursor.close();

                    // Get the dimensions of the View
                    int targetW = imageView.getWidth();
                    int targetH = imageView.getHeight();

                    // Get the dimensions of the bitmap
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
                    int photoW = bmOptions.outWidth;
                    int photoH = bmOptions.outHeight;

                    // Determine how much to scale down the image
                    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                    // Decode the image file into a Bitmap sized to fill the View
                    bmOptions.inJustDecodeBounds = false;
                    bmOptions.inSampleSize = scaleFactor;
                    bmOptions.inPurgeable = true;

                    Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
                    imageView.setImageBitmap(bitmap);

                    classifyImage(Constants.IMAGE_RECOGNITION_CLASSIFY_URL, bitmap);

                    break;
            }
        }
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
                getFragmentManager().beginTransaction().replace(R.id.content_frame, wordListFragment).commit();
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
