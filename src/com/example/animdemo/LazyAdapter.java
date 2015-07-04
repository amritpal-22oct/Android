package com.example.animdemo;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

	List<Entity> data;
	Context ctx;
	Map<Integer, HeavyTask> taskStatus;

	public LazyAdapter(List<Entity> data, Context ctx) {
		this.data = data;
		this.ctx = ctx;
		taskStatus = new WeakHashMap<Integer, HeavyTask>();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = ((LayoutInflater) ctx
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.list_item, parent, false);
			holder.text = (TextView) view.findViewById(R.id.tv);
			holder.result = (TextView) view.findViewById(R.id.result);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		//To check if view is still visible after Async task.
		holder.position = position;
		holder.text.setText(data.get(position).text);

		if (data.get(position).result == null) {
			holder.result.setText("");
			if (taskStatus.get(position) != null) {
				// This is one option(commented) but not a good one. It creates
				// a different
				// object for every getView call in case task is already
				// running.
				// ((HeavyTask) taskStatus.get(position)).cancel(true);

				// Check if task is running and update the holder
				if (taskStatus.get(position).getStatus() == AsyncTask.Status.RUNNING) {
					taskStatus.get(position).updateHolder(holder);
				}
			} else {
				// Create a new Async task.
				taskStatus.put(position, new HeavyTask(data.get(position),
						holder, position));
				taskStatus.get(position).execute();
			}
		} else {
			holder.result.setText(data.get(position).result);
		}
		return view;
	}

	class HeavyTask extends AsyncTask<Void, Void, String> {

		Entity entity;
		ViewHolder holder;
		int position;

		public HeavyTask(Entity entity, ViewHolder holder, int position) {
			this.entity = entity;
			this.holder = holder;
			this.position = position;
			Log.d("Position : ", position + "");
		}

		public void updateHolder(ViewHolder holder) {
			this.holder = holder;
		}

		@Override
		public String doInBackground(Void... params) {
			if (entity.result == null) {
				try {
					Thread.sleep(50);
					entity.result = Integer.toString(Integer
							.parseInt(entity.text) * 100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
			if (!isCancelled() && (holder.position == position)) {
				holder.result.setText(entity.result);
			}
			// Keep removing cached AsyncTask objects
			taskStatus.remove(position);

		}
	}

	static class ViewHolder {
		TextView text;
		TextView result;
		int position;
	}

}
