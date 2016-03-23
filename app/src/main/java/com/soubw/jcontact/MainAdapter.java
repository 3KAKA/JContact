package com.soubw.jcontact;

import android.content.Context;
import android.view.View;

import com.soubw.jcontactlib.JAdapter;
import com.soubw.jcontactlib.JContacts;
import com.soubw.jcontactlib.JListView;
import com.soubw.jcontactlib.JViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WX_JIN on 2016/3/23.
 */
public class MainAdapter extends JAdapter {


    public MainAdapter(Context context, List<JContacts> jContactsList, JListView lvList, int indexBarViewId, int previewViewId, int itemLayoutId, int sectionLayoutId, View loadingView) {
        super(context, jContactsList, lvList, indexBarViewId, previewViewId, itemLayoutId, sectionLayoutId, loadingView);
    }

    @Override
    public void convert(JViewHolder holder, JContacts bean, int type) {
        switch (type) {
            case TYPE_ITEM:
                holder.setText(R.id.row_title,bean.getjName()+bean.getjPhoneNumber()+"aaaa");
                break;
            case TYPE_SECTION:
                holder.setText(R.id.row_title,bean.getjName());
                break;
        }
    }
}
