package com.ocunha.librasapp.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ocunha.librasapp.R;
import com.ocunha.librasapp.adapter.LibrasWordAdapter;
import com.ocunha.librasapp.domain.LibrasWord;
import com.ocunha.librasapp.utils.Preferences;

import java.util.ArrayList;

/**
 * Created by osnircunha on 4/7/16.
 */
public class HistoryFragment extends Fragment implements SearchView.OnQueryTextListener{

    public static final String TAG = HistoryFragment.class.getSimpleName();

    private ArrayList<LibrasWord> librasWords;
    private LibrasWordAdapter librasWordAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(savedInstanceState != null){
            this.librasWords = (ArrayList<LibrasWord>) savedInstanceState.getSerializable("list");
        } else {
            this.librasWords = Preferences.getInstance().getSearchHistory();
        }

        this.librasWordAdapter = new LibrasWordAdapter(getContext(), this.librasWords, new LibrasWordAdapter.LibrasWordOnClickListener() {

            @Override
            public void onClickWord(LibrasWordAdapter.LibrasWordViewHolder holder, int idx) {
                ContentFragment contentFragment = new ContentFragment();
                contentFragment.setLibrasWord(librasWords.get(idx));
                Preferences.getInstance().logSearchHistory(librasWords.get(idx));

                getFragmentManager().beginTransaction().replace(R.id.content_frame, contentFragment).commit();
            }
        });

        recyclerView.setAdapter(librasWordAdapter);

        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("list", this.librasWords);
    }

    public void setWordList(ArrayList<LibrasWord> librasWords) {
        this.librasWords = librasWords;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.isEmpty()){
            setWordList(Preferences.getInstance().getSearchHistory());
            this.librasWordAdapter.animateTo(this.librasWords);
        } else {
            ArrayList<LibrasWord> librasWords = filter(newText, this.librasWords);
            this.librasWordAdapter.animateTo(librasWords);
        }
        return true;
    }

    private ArrayList<LibrasWord> filter(String query,ArrayList<LibrasWord> words ){
        ArrayList<LibrasWord> librasWords = new ArrayList<>();
        for(LibrasWord word : words){
            if(word.getWord().toLowerCase().contains(query.toLowerCase())){
                librasWords.add(word);
            }
        }
        return librasWords;
    }
}
