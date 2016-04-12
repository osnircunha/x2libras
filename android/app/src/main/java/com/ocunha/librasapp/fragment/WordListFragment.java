package com.ocunha.librasapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ocunha.librasapp.R;
import com.ocunha.librasapp.adapter.LibrasWordAdapter;
import com.ocunha.librasapp.domain.LibrasWord;
import com.ocunha.librasapp.utils.Constants;
import com.ocunha.librasapp.utils.JsonUtils;
import com.ocunha.librasapp.utils.Preferences;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by osnircunha on 4/7/16.
 */
public class WordListFragment extends Fragment {

    public static final String TAG = WordListFragment.class.getSimpleName();

    private ArrayList<LibrasWord> librasWords = new ArrayList<>();
    private LibrasWordAdapter librasWordAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_search_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(savedInstanceState != null){
            librasWords = (ArrayList<LibrasWord>) savedInstanceState.getSerializable("list");
        }

        final EditText queryTextView = (EditText) view.findViewById(R.id.text_query);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);

        view.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String query = queryTextView.getText().toString();
                if(!"".equals(query.trim())){
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("word", query);

                    AsyncHttpClient client = new AsyncHttpClient();
                    client.get(Constants.LIBRAS_WORD_URL, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            hideSoftKeyboard();

                            librasWordAdapter.animateTo(JsonUtils.parseJsonListToLibrasWord(new String(responseBody)));

                            progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Snackbar.make(view, "Error on search", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                }
            }
        });

        this.librasWordAdapter = new LibrasWordAdapter(getContext(), this.librasWords, new LibrasWordAdapter.LibrasWordOnClickListener() {

            @Override
            public void onClickWord(LibrasWordAdapter.LibrasWordViewHolder holder, int idx) {
                ContentFragment contentFragment = new ContentFragment();
                contentFragment.setLibrasWord(librasWords.get(idx));

                Preferences.getInstance().logSearchHistory(librasWords.get(idx));

                getFragmentManager().beginTransaction().replace(R.id.content_frame, contentFragment).addToBackStack(WordListFragment.TAG).commit();
            }
        });

        recyclerView.setAdapter(librasWordAdapter);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable("list", this.librasWords);
    }

    public void setLibrasWords(ArrayList<LibrasWord> librasWords) {
        this.librasWords = librasWords;
    }

    private void hideSoftKeyboard() {
        InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        input.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
