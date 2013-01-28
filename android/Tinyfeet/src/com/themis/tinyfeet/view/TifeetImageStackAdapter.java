package com.themis.tinyfeet.view;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.themis.tinyfeet.bo.TfeetStack;

public class TifeetImageStackAdapter extends BaseAdapter{
	private List<TfeetStack> stackList;
	private Context context;
	
	public TifeetImageStackAdapter(Context context,List<TfeetStack> tfeetStackList) {
		super();
		this.stackList = tfeetStackList;
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getCount() {
		if(stackList!=null){
			return stackList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return stackList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TfeetImageStackView tfeetImageStackView=new TfeetImageStackView(context);
		String urlStr = stackList.get(position).getTfeetList().get(0);
		try {
			tfeetImageStackView.bindUrl(urlStr);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tfeetImageStackView;
	}

}
