package com.demo.awesomeledger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.adapter.DetailListViewAdapter;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.dao.ItemDao;

import java.util.List;

public class DetailFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, PopupMenu.OnMenuItemClickListener {

    private int position;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        listView = view.findViewById(R.id.item_container);
        //单机监听
        listView.setOnItemClickListener(this);
        //长按监听
        listView.setOnItemLongClickListener(this);
        //获取数据
        List<Item> itemList = ItemDao.getInstance(getContext()).getAll();
        DetailListViewAdapter adapter = new DetailListViewAdapter(itemList, getContext());
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        List<Item> itemList = ItemDao.getInstance(getContext()).getAll();
        DetailListViewAdapter adapter = new DetailListViewAdapter(itemList, getContext());
        listView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //单机事件，进入addItem
        Log.d("单击：", String.valueOf(position));
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view, int position, long id) {
        //长按事件，删除操作
        Log.d("长按：", String.valueOf(position));
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
        if (item.getItemId() == R.id.delete) {
            Log.d("单击：", "删除" + String.valueOf(this.position));
        }
        return false;
    }
}
