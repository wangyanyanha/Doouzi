package com.doouzi.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.doouzi.R;
import com.doouzi.bean.Joke;
import com.doouzi.ui.adapter.FeedAdapter;
import com.doouzi.ui.adapter.JokeAdapter;
import com.doouzi.ui.widget.PullRefreshLayout;
import com.doouzi.util.JokeBudejieUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class FragmentJoke extends Fragment
{

	private RecyclerView rvFeed;
	FeedAdapter feedAdapter;
	int lastVisibleItem=0;
	PullRefreshLayout mSwipeRefreshWidget;
	LinearLayoutManager linearLayoutManager;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View root = inflater.inflate(R.layout.fragment_list, null);

		mSwipeRefreshWidget = (PullRefreshLayout) root.findViewById(R.id.swipe_refresh_widget);
		rvFeed = (RecyclerView) root.findViewById(R.id.rvFeed);
		rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(RecyclerView recyclerView,
											 int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE
						&& lastVisibleItem + 1 == feedAdapter.getItemCount()) {
//					mSwipeRefreshWidget.setRefreshing(true);
					// 此处在现实项目中，请换成网络请求数据代码，sendRequest .....
//                    handler.sendEmptyMessageDelayed(0, 3000);
					page++;
					new GetJokesWorkThread().start();
				}
			}

			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
			}

		});

		mSwipeRefreshWidget.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				Log.e("test","refresh");
				page=1;
				new GetJokesWorkThread().start();
			}
		});

		new GetJokesWorkThread().start();

		return root;
	}

	int page=1;

	class GetJokesWorkThread extends Thread
	{
		@Override
		public void run()
		{
			super.run();
			GetJokes();
		}
	}

	private void GetJokes()
	{
		String jsonData = JokeBudejieUtil.getJokes(page);

//		Log.d("wy_",jsonData);

		if (jsonData != null)
		{
			List<Joke> info=null;
			try
			{
				Gson gson = new Gson();
				info = gson.fromJson(jsonData, new TypeToken<List<Joke>>(){}.getType());
				Message msg = new Message();
				msg.obj = info;
				if(info.size()>0)
					msg.arg1 = 1;
				else
					msg.arg1 = 0;
				get_jokes_handler.sendMessage(msg);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				Message msg = new Message();
				msg.arg1 = 0;
				get_jokes_handler.sendMessage(msg);
			}
		}else
		{
			Message msg = new Message();
			msg.arg1 = 0;
			get_jokes_handler.sendMessage(msg);
		}
	}

	private Handler get_jokes_handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			switch (msg.arg1)
			{
				case 1:
					List<Joke> info=(List<Joke>) msg.obj;
					if(page==1) {
						if(feedAdapter==null)
							setupFeed(info);
						else
						{
							feedAdapter.clear();
							addFeed(info);
						}
					}
					else if(page>1)
						addFeed(info);
					break;
				case 0:
					break;
			}
		}
	};

	private void setupFeed(List<Joke> jokes) {
		linearLayoutManager = new LinearLayoutManager(getActivity()) {
			@Override
			protected int getExtraLayoutSpace(RecyclerView.State state) {
				return 300;
			}
		};
		rvFeed.setLayoutManager(linearLayoutManager);

		feedAdapter = new FeedAdapter(getActivity(),jokes);
		rvFeed.setAdapter(feedAdapter);
		feedAdapter.updateItems();
	}

	private void addFeed(List<Joke> jokes)
	{
		if(jokes==null||jokes.size()==0)
		{

		}
		else {
			feedAdapter.add(jokes);
		}
		mSwipeRefreshWidget.setRefreshing(false);
	}


}