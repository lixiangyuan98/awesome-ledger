package com.demo.awesomeledger.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.demo.awesomeledger.type.ItemType;

public class OutgoingFragment extends BasePieDiagramFragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        type = ItemType.OUTGOING;
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
