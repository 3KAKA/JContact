package com.soubw.jcontact;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
/**
 * Created by WX_JIN on 2016/3/10.
 */
public class MainActivity extends Activity{

    /**
     * 完整手机通讯录好友信息列表
     */
    private List<JContacts> jContactsList;
    /**
     * 包含字母信息和好友信息列表
     */
    private ArrayList<JContacts> mListItems;
    /**
     * 导航字母列表
     */
    private ArrayList<Integer> mListSectionPos;

    private JListView lvList;

    private JAdapter mAdaptor;

    private ProgressBar mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jContactsList = new ArrayList<>();
        mListSectionPos = new ArrayList<Integer>();
        mListItems = new ArrayList<JContacts>();
        initView();

        loadData();

    }

    private void initView(){
        mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
        mAdaptor = new JAdapter(this, mListItems, mListSectionPos);
        this.lvList = (JListView) findViewById(R.id.lvList);

        lvList.setAdapter(mAdaptor);
        this.lvList.setJClearEditTextListener(new JListView.JClearEditTextListener() {
            @Override
            public void requestRefreshAdapter(CharSequence s) {
                String str = s.toString();
                if (mAdaptor != null && str != null){
                    if(jContactsList != null){
                        filterData(str);
                    }
                }
            }
        });
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        JIndexBarView jIndexBarView = (JIndexBarView) inflater.inflate(R.layout.index_bar_view, lvList, false);
        jIndexBarView.setData(lvList, mListItems, mListSectionPos);
        lvList.setJIndexBarView(jIndexBarView,mListSectionPos);
        lvList.setPreviewView(inflater.inflate(R.layout.preview_view, lvList, false));
    }

    private void loadData(){
        JLoadData.getIntance().loadLocalContacts(MainActivity.this, new JLoadData.JLoadDataListener() {
            @Override
            public void doSuccess(List<JContacts> list) {
                jContactsList.clear();
                jContactsList = list;
                filterData(null);
            }
            @Override
            public void doFailed(String error) {
            }
        });
    }

    /**
     * 过滤列表数据
     * @param s
     */
    private void filterData(String s){
        setIndexBarViewVisibility(s);
        List<JContacts> jFilterList = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            jFilterList.clear();
            jFilterList.addAll(jContactsList);
        } else {
            jFilterList.clear();
            for (JContacts contact : jContactsList) {
                String name = contact.getjName();
                String num = contact.getjPhoneNumber();
                if (name.indexOf(s.toString()) != -1 || (new CharacterParser()).getSelling(name).contains(s.toString())
                        || (new CharacterParser()).getSelling(num).contains(s.toString())) {
                    jFilterList.add(contact);
                }
            }
        }
        new LoadFilter().execute(jFilterList);
    }

    private void setIndexBarViewVisibility(String constraint) {
        if (constraint != null && constraint.length() > 0) {
            lvList.setIndexBarVisibility(false);
        } else {
            lvList.setIndexBarVisibility(true);
        }
    }

    private class LoadFilter extends AsyncTask<List<JContacts>, Void, Void> {
        private void showLoading(View contentView, View loadingView) {
            contentView.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
        }

        private void showContent(View contentView, View loadingView) {
            contentView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
        }
        @Override
        protected void onPreExecute() {
            showLoading(lvList, mLoadingView);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(List<JContacts>... params) {
            mListItems.clear();
            mListSectionPos.clear();
            List<JContacts> itemsList = params[0];
            if (itemsList.size() > 0) {
                // 根据a-z进行排序
                Collections.sort(itemsList, new JSortComparator());
                String prev_section = "";
                JContacts item ;
                for (JContacts current_item : itemsList) {
                    String current_section = new CharacterParser().getSelling(current_item.getjName()).substring(0, 1).toUpperCase(Locale.getDefault());

                    if (!prev_section.equals(current_section)) {
                        item = new JContacts();
                        item.setjName(current_section);
                        mListItems.add(item);
                        mListItems.add(current_item);
                        mListSectionPos.add(mListItems.indexOf(item));
                        prev_section = current_section;
                    } else {
                        mListItems.add(current_item);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!isCancelled()) {
                mAdaptor.setUpdateDate(mListItems,mListSectionPos);
                lvList.setJClearEditTextFoucus();
                showContent(lvList, mLoadingView);
            }
            super.onPostExecute(result);
        }
    }
}
