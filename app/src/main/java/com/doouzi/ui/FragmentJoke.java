package com.doouzi.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.doouzi.R;
import com.doouzi.bean.Joke;
import com.doouzi.ui.adapter.JokeAdapter;
import com.doouzi.ui.widget.PullToRefreshListView;
import com.doouzi.util.JokeBudejieUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class FragmentJoke extends Fragment
{

	private PullToRefreshListView lv;
	private JokeAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.fragment_list, null);

		lv = (PullToRefreshListView) root.findViewById(R.id.lv_list);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			ViewCompat.setNestedScrollingEnabled(lv, true);
			lv.setNestedScrollingEnabled(true);
		}

//		lv.setOverScrollMode(View.OVER_SCROLL_NEVER);

		new workingThread(1,10).start();

		lv.setonRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new workingThread(1, 10).start();
			}
		});

		lv.setOnLoadMoreListener(new PullToRefreshListView.OnLoadMoreListener() {
			@Override
			public void OnLoadMore() {
				new workingThread(++page, 10).start();
			}
		});

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position>=2) {
//					Intent intent = new Intent(getActivity(), HouseDetailActivity.class);
//					intent.putExtra("id", ((HouseListInfo) adapter.getItem(position - 2)).getId());
//					startActivity(intent);
				}
			}
		});

		return root;
	}

	int page=0;

	private void initList(List<Joke> list)
	{
		if(lv.getAdapter()==null)
		{
			adapter=new JokeAdapter(getActivity(),list);
			lv.setAdapter(adapter);
		}else
		{
			adapter.clear();
			adapter.add(list);
			lv.onRefreshComplete(1);
		}
		lv.setSelection(0);
		lv.onLoadMoreComplete(1);
	}

	private void addToList(List<Joke> list)
	{
		adapter.add(list);
		lv.onLoadMoreComplete(1);
	}

	class workingThread extends Thread
	{
		int page,page_size;
		workingThread(int page,int page_size)
		{
			this.page=page;
			this.page_size=page_size;
		}
		@Override
		public void run()
		{
			super.run();
			get_joke_list(page, page_size);
		}
	}

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.arg1)
			{
				case 1:
					List<Joke> infos=(List<Joke>) msg.obj;
					if(page==1) {
						initList(infos);
					}
					else if(page>1)
						addToList(infos);
					break;
				case 0:
					lv.onRefreshComplete(0);
					lv.onLoadMoreComplete(0);
					break;
			}
		}
	};

	private void get_joke_list(int page,int page_size)
	{
		this.page=page;
		String jsonData = JokeBudejieUtil.getJokes(page);
		if (jsonData != null)
		{
			List<Joke> infos=null;
			try
			{
				Gson gson = new Gson();
				infos = gson.fromJson(jsonData, new TypeToken<List<Joke>>(){}.getType());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			Message msg = new Message();
			msg.obj=infos;
			if(infos==null||infos.size()==0)
				msg.arg1 = 0;
			else
				msg.arg1 = 1;
			handler.sendMessage(msg);
		}else
		{
			Message msg = new Message();
			msg.arg1 = 0;
			handler.sendMessage(msg);
		}
	}

}