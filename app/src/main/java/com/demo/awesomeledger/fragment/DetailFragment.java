package com.demo.awesomeledger.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.*;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.demo.awesomeledger.R;
import com.demo.awesomeledger.util.ItemView;

public class DetailFragment extends Fragment implements AdapterView.OnItemClickListener,  AdapterView.OnItemLongClickListener, PopupMenu.OnMenuItemClickListener {
    private ListView lv;
    private MyAdapter adapter;
    private List<ItemView> itemList;
    public int position;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        lv = (ListView) view.findViewById(R.id.item_container);
        //单机监听
        lv.setOnItemClickListener(this);
        //长按监听
        lv.setOnItemLongClickListener(this);
        //获取数据
        itemList = getData();

        adapter = new MyAdapter(itemList,getActivity());
        lv.setAdapter(adapter);

        return view;
    }
    private List<ItemView> getData()
    {
        List<ItemView> List = new ArrayList<>();
        for (int i = 0;i < 50;i++) {
            ItemView item = new ItemView();
            item.settime("6月6日");
            item.settype("支出");
            item.setkind("洗浴");
            item.setamont(400);
            List.add(item);
        }
        return List;
    }


    private class MyAdapter extends BaseAdapter  {
        private List<ItemView> itemlist;
        private LayoutInflater inflater;
        private Context context;

        public MyAdapter(List<ItemView> list,Context context) {
            this.itemlist = list;
            this.context = context;
            this.inflater = LayoutInflater.from(getActivity());
        }

        public class ViewHolder
        {
            public TextView timeView;
            public TextView typeView;
            public TextView kindView;
            public TextView amontView;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.list_item, null);
                //根据自定义的Item布局加载布局
                holder.timeView = (TextView)convertView .findViewById(R.id.item_time);
                holder.typeView = (TextView)convertView .findViewById(R.id.item_type);
                holder.kindView = (TextView)convertView .findViewById(R.id.item_kind);
                holder.amontView = (TextView)convertView .findViewById(R.id.item_amont);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.timeView.setText(itemList.get(position).gettime());
            holder.typeView.setText(itemList.get(position).gettype());
            holder.kindView.setText(itemList.get(position).getkind());
            holder.amontView.setText(String.valueOf(itemList.get(position).getamont()));

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return itemlist.get(position);
        }


        @Override
        public int getCount() {
            return itemlist.size();
        }

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //单机事件，进入addItem
        Log.d("单击：",String.valueOf(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view,int position, long id) {
        //长按事件，删除操作
        Log.d("长按：",String.valueOf(position));
        this.position = position;
        PopupMenu popup = new PopupMenu(getContext(), view);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.delete_manu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(this);
        //显示(这一行代码不要忘记了)
        popup.show();
        return true;
    }
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                Log.d("单击：","删除"+String.valueOf(this.position));
                break;
            default:
                break;
        }
        return false;
    }

}
