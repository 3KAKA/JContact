package com.soubw.jcontact;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.soubw.juitls.CharacterParser;
import com.soubw.juitls.JAdapter;
import com.soubw.juitls.JContacts;
import com.soubw.juitls.JIndexBarView;
import com.soubw.juitls.JListView;
import com.soubw.juitls.JLoadData;
import com.soubw.juitls.JSortComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity{

    private JListView lvList;

    private List<JContacts> jContactsList;
    private List<JContacts> jFilterList;

    ArrayList<Integer> mListSectionPos;

    ArrayList<JContacts> mListItems;

    JAdapter mAdaptor;

    ProgressBar mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.lvList = (JListView) findViewById(R.id.lvList);
        jContactsList = new ArrayList<>();
        jFilterList = new ArrayList<>();
        mListSectionPos = new ArrayList<Integer>();
        mListItems = new ArrayList<JContacts>();
        mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
        this.lvList.setJClearEditTextListener(new JListView.JClearEditTextListener() {
            @Override
            public void requestRefreshAdapter(CharSequence s) {
                String str = s.toString();
                if (mAdaptor != null && str != null){
                    // mAdaptor.getFilter().filter(str);
                    if(jContactsList != null){
                        filterData(str);
                    }
                }

            }
        });
        setListAdaptor();
        loadData();

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

    private void filterData(String s){
        setIndexBarViewVisibility(s);
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
        new Poplulate().execute(jFilterList);
    }

    private void setListAdaptor() {
        mAdaptor = new JAdapter(this, mListItems, mListSectionPos);
        lvList.setAdapter(mAdaptor);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        JIndexBarView jIndexBarView = (JIndexBarView) inflater.inflate(R.layout.index_bar_view, lvList, false);
        jIndexBarView.setData(lvList, mListItems, mListSectionPos);
        lvList.setJIndexBarView(jIndexBarView,mListSectionPos);

        View previewTextView = inflater.inflate(R.layout.preview_view, lvList, false);
        lvList.setPreviewView(previewTextView);

    }

    private void setIndexBarViewVisibility(String constraint) {
        if (constraint != null && constraint.length() > 0) {
            lvList.setIndexBarVisibility(false);
        } else {
            lvList.setIndexBarVisibility(true);
        }
    }

    private class Poplulate extends AsyncTask<List<JContacts>, Void, Void> {

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
                        item = new JContacts(current_section);
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
