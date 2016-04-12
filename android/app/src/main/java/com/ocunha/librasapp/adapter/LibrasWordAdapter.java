package com.ocunha.librasapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ocunha.librasapp.R;
import com.ocunha.librasapp.domain.LibrasWord;

import java.util.List;

/**
 * Created by osnircunha on 4/7/16.
 */
public class LibrasWordAdapter extends RecyclerView.Adapter<LibrasWordAdapter.LibrasWordViewHolder> {

    private List<LibrasWord> librasWords;
    private final Context context;
    private final LibrasWordOnClickListener onClickListener;

    public interface LibrasWordOnClickListener {
        void onClickWord(LibrasWordViewHolder holder, int idx);
    }

    public LibrasWordAdapter(Context context, List<LibrasWord> librasWords, LibrasWordOnClickListener onClickListener) {
        this.context = context;
        this.librasWords = librasWords;
        this.onClickListener = onClickListener;
    }

    @Override
    public LibrasWordViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_list_layout, viewGroup, false);
        LibrasWordViewHolder holder = new LibrasWordViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final LibrasWordViewHolder holder, final int position) {
        LibrasWord c = librasWords.get(position);
        holder.tNome.setText(c.getWord());
        holder.tDesc.setText(c.getDescription());

        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickWord(holder, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.librasWords != null ? this.librasWords.size() : 0;
    }

    public void setLibrasWords(List<LibrasWord> librasWords) {
        this.librasWords = librasWords;
    }

    public LibrasWord removeItem(int position){
        LibrasWord librasWord = this.librasWords.remove(position);
        notifyItemRemoved(position);

        return librasWord;
    }

    public void addItem(int position, LibrasWord librasWord){
        this.librasWords.add(position, librasWord);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition){
        LibrasWord librasWord = this.librasWords.remove(fromPosition);
        this.librasWords.add(toPosition, librasWord);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<LibrasWord> librasWords){
        applyAndAnimateRemovals(librasWords);
        applyAndAnimateAdditions(librasWords);
        applyAndAnimateMovedItems(librasWords);
    }

    private void applyAndAnimateRemovals(List<LibrasWord> newModels) {
        for (int i = this.librasWords.size() - 1; i >= 0; i--) {
            LibrasWord model = this.librasWords.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }


    private void applyAndAnimateAdditions(List<LibrasWord> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final LibrasWord model = newModels.get(i);
            if (!librasWords.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<LibrasWord> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final LibrasWord model = newModels.get(toPosition);
            final int fromPosition = librasWords.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public static class LibrasWordViewHolder extends RecyclerView.ViewHolder {
        public TextView tNome;
        public TextView tDesc;
        public View view;

        public LibrasWordViewHolder(View view) {
            super(view);
            this.view = view;
            tNome = (TextView) view.findViewById(R.id.tNome);
            tDesc = (TextView) view.findViewById(R.id.tDesc);
        }
    }

}
