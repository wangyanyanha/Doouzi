package com.doouzi.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.doouzi.R;
import com.doouzi.bean.Joke;

import java.util.List;


public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    List<Joke> jokes;

    public FeedAdapter(Context context, List<Joke> jokes) {
        this.context = context;
        this.jokes=jokes;
    }

    public void clear()
    {
        jokes.clear();
        notifyDataSetChanged();
    }

    public void add(List<Joke> list)
    {
        this.jokes.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.lv_item, parent, false);

        return new CellFeedViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final CellFeedViewHolder holder = (CellFeedViewHolder) viewHolder;
        bindDefaultFeedItem(position, holder);
    }

    private void bindDefaultFeedItem(int position, CellFeedViewHolder holder) {
        holder.tvJoke.setText(jokes.get(position).content);
        holder.tvJoke.setTag(holder);

    }

    public void updateItems() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @Override
    public int getItemCount() {
        return jokes.size();
    }

    public static class CellFeedViewHolder extends RecyclerView.ViewHolder {
        TextView tvJoke;

        public CellFeedViewHolder(View view) {
            super(view);

            tvJoke=(TextView) view.findViewById(R.id.tv_content);
        }
    }


}
