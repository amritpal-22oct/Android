package com.example.animdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class MainActivity extends Activity {

	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		listView = (ListView) findViewById(R.id.list);
		List<Entity> data = new ArrayList<Entity>();
		for (int i = 1; i < 1000; i++) {
			data.add(new Entity(i + ""));
		}
		LazyAdapter adapter = new LazyAdapter(data, this);
		listView.setAdapter(adapter);
	}

}
