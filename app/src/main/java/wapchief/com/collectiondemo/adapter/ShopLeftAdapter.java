package wapchief.com.collectiondemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wapchief.com.collectiondemo.R;


public class ShopLeftAdapter extends BaseAdapter {
	private Context context;
	private List<String> list;

	public ShopLeftAdapter(Context context, List<String> newList) {
		super();
		this.context = context;
		if (null != list && list.size() > 0) {
			this.list = newList;
		} else {
			list = new ArrayList<String>();
		}
	}

	public void addList(List<String> newList){
		if(null != newList && newList.size()>0){
			list.addAll(newList);
			notifyDataSetChanged();
		}
	}
	/* 
	 * 
	 */
	@Override
	public int getCount() {
		if(null != list && list.size()>0){
			return list.size();
		}
		return 0;
	}

	/* 
	 * 
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/* 
	 * 
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/* 
	 * 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if(convertView == null){
			convertView = View.inflate(context, R.layout.item_textview, null);
			vh = new ViewHolder();
			vh.tv1 = (TextView) convertView.findViewById(R.id.item_title);
			vh.tv1.setVisibility(View.GONE);
			vh.tv2 = (TextView) convertView.findViewById(R.id.item_textview);

			convertView.setTag(vh);
		}else{
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tv1.setText(list.get(position));
		vh.tv1.setTag(position);
		return convertView;
	}
	public class ViewHolder{
		TextView tv1,tv2;
	}

}
