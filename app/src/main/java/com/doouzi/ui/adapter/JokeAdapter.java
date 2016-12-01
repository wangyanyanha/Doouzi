package com.doouzi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.doouzi.R;
import com.doouzi.bean.Joke;

import java.util.List;

/**
 * Created by wy on 2015/9/14.
 * Class for house.adapter
 */

public class JokeAdapter extends BaseAdapter {
    private Context mContext;
    private List<Joke> infos;
    private LayoutInflater mLayoutInflater;

    public JokeAdapter(Context c) {
        this.mContext = c;
    }

    public JokeAdapter(Context c, List<Joke> infos)
    {
        super();
        mLayoutInflater = LayoutInflater.from(c);
        this.mContext = c;
        this.infos = infos;
    }

    public void clear()
    {
        infos.clear();
        notifyDataSetChanged();
    }

    public void add(List<Joke> list)
    {
        this.infos.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return infos.size();
    }

    @Override
    public Object getItem(int position) {
        return infos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(infos == null)
            return  null;
        ViewHolder vh;
        if(convertView == null)
        {
            convertView = mLayoutInflater.inflate(R.layout.item_joke, parent, false);
            vh = new ViewHolder();
            vh.tv_content = (TextView) convertView.findViewById(R.id.tv_joke);

            convertView.setTag(vh);
        }else
        {
            vh = (ViewHolder) convertView.getTag();
        }

        Joke joke=infos.get(position);
        vh.tv_content.setText(joke.content);


        return convertView;
    }

    static class ViewHolder {
        private TextView tv_name, tv_content, tv_time;
    }

}
