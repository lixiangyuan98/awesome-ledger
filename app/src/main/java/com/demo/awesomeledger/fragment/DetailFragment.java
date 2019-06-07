package com.demo.awesomeledger.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.demo.awesomeledger.R;
import com.demo.awesomeledger.activity.AddItemActivity;
import com.demo.awesomeledger.adapter.DetailListViewAdapter;
import com.demo.awesomeledger.bean.Item;
import com.demo.awesomeledger.dao.ItemDao;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, PopupMenu.OnMenuItemClickListener {

    private int position;
    private ListView listView;
    private List<Item> itemList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        listView = view.findViewById(R.id.item_container);
        // 单击监听
        listView.setOnItemClickListener(this);
        // 长按监听
        listView.setOnItemLongClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //获取数据
        itemList = ItemDao.getInstance(getContext()).getAll();
//        for (Item item: itemList) {
//            ItemDao.getInstance(getContext()).delete(item);
//        }
        if (itemList != null) {
            for (Item item : itemList) {
                Log.i("Item", item.getItemType().getType() + " " + item.getMoney().toString());
            }
        }
        if(itemList == null){
            itemList = new ArrayList<>();
        }
        DetailListViewAdapter adapter = new DetailListViewAdapter(itemList, getContext());
        listView.setAdapter(adapter);
    }

    //单击事件，进入addItem
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), AddItemActivity.class);
        intent.putExtra("isNew", false);
        intent.putExtra("id", itemList.get(position).getId());
        startActivity(intent);
        Log.d("单击：", String.valueOf(position));
    }

    //长按事件，删除操作
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        this.position = position;
        PopupMenu popup = new PopupMenu(getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.delete_manu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();
        return true;
    }

    // 删除条目
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            ItemDao.getInstance(getContext()).delete(itemList.get(position));
            itemList.remove(position);
            DetailListViewAdapter adapter = new DetailListViewAdapter(itemList, getContext());
            listView.setAdapter(adapter);
        }
        return false;
    }
}
