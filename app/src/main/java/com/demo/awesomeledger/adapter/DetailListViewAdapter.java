package com.demo.awesomeledger.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.bean.Item;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DetailListViewAdapter extends BaseAdapter {

    private List<Item> itemList;
    private Context context;

    public DetailListViewAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //如果缓存convertView为空，则需要创建View
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.list_item, null);
            //根据自定义的Item布局加载布局
            holder.timeView = convertView.findViewById(R.id.item_time);
            holder.typeView = convertView.findViewById(R.id.item_type);
            holder.kindView = convertView.findViewById(R.id.item_kind);
            holder.amountView = convertView.findViewById(R.id.item_amont);
            //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        holder.timeView.setText(dateFormat.format(itemList.get(position).getDate()));
        holder.typeView.setText(itemList.get(position).getItemType().getType());
        holder.kindView.setText(itemList.get(position).getItemKind().getKind());
        holder.amountView.setText(String.valueOf(itemList.get(position).getMoney()));

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

}

class ViewHolder {
    TextView timeView;
    TextView typeView;
    TextView kindView;
    TextView amountView;
}
