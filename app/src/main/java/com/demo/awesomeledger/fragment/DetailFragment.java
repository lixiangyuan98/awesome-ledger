package com.demo.awesomeledger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.demo.awesomeledger.R;
import com.demo.awesomeledger.util.ItemView;

public class DetailFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.item_container);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));//不要忘了。。

        List<ItemView> itemList = new ArrayList<>();
        for (int i = 0;i < 50;i++) {
            ItemView item = new ItemView();
            item.settime("6月6日");
            item.settype("支出");
            item.setkind("洗浴");
            item.setamont(400);
            itemList.add(item);
        }

        mItemAdapter = new ItemAdapter(itemList);
        mRecyclerView.setAdapter(mItemAdapter);


        return view;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private TextView timeView;
        private TextView typeView;
        private TextView kindView;
        private TextView amontView;
        private View root;

        public ItemHolder(View itemView) {
            super(itemView);
            this.root = itemView;
            timeView = (TextView) itemView.findViewById(R.id.item_time);
            typeView = (TextView) itemView.findViewById(R.id.item_type);
            kindView = (TextView) itemView.findViewById(R.id.item_kind);
            amontView = (TextView) itemView.findViewById(R.id.item_amont);
        }

    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder>  {

        private List<ItemView> mItems;

        public ItemAdapter(List<ItemView> items) {
            mItems = items;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item,
                    viewGroup,false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder viewHolder, int i) {
            ItemView item = mItems.get(i);
            //这里开始直接传数值，
            viewHolder.timeView.setText(item.gettime());
            viewHolder.typeView.setText(item.gettype());
            viewHolder.kindView.setText(item.getkind());
            viewHolder.amontView.setText(String.valueOf(item.getamont()));
            viewHolder.itemView.setTag(i);
        }

        @Override

        public int getItemCount() {
            return mItems.size();
        }
    }



}
