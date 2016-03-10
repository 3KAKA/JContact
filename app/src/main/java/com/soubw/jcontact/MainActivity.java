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

    ArrayList<String> mListItems;

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
        mListItems = new ArrayList<String>();
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
                if (name.indexOf(s.toString()) != -1 || (new CharacterParser()).getSelling(name).startsWith(s.toString())) {
                    jFilterList.add(contact);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(jFilterList, new JSortComparator());
        new Poplulate().execute(jFilterList);
    }

    private void setListAdaptor() {
        mAdaptor = new JAdapter(this, mListItems, mListSectionPos);
        lvList.setAdapter(mAdaptor);

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // set index bar view
        JIndexBarView jIndexBarView = (JIndexBarView) inflater.inflate(R.layout.index_bar_view, lvList, false);
        jIndexBarView.setData(lvList, mListItems, mListSectionPos);
        lvList.setJIndexBarView(jIndexBarView,mListSectionPos);

        View previewTextView = inflater.inflate(R.layout.preview_view, lvList, false);
        lvList.setPreviewView(previewTextView);
       // lvList.setJClearEditTextFoucus();

    }

    private void setIndexBarViewVisibility(String constraint) {
        // hide index bar for search results
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
            // show loading indicator
            showLoading(lvList, mLoadingView);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(List<JContacts>... params) {
            mListItems.clear();
            mListSectionPos.clear();
            ArrayList<String> items = new ArrayList<>();
            List<JContacts> itemsList = params[0];
            for(JContacts contacts: itemsList){
                items.add(contacts.getjName());
            }
            if (items.size() > 0) {

                Collections.sort(items, new SortIgnoreCase());

                String prev_section = "";
                for (String current_item : items) {
                    String current_section = current_item.substring(0, 1).toUpperCase(Locale.getDefault());

                    if (!prev_section.equals(current_section)) {
                        mListItems.add(current_section);
                        mListItems.add(current_item);
                        // array list of section positions
                        mListSectionPos.add(mListItems.indexOf(current_section));
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
                //setListAdaptor();
                mAdaptor.setUpdateDate(mListItems,mListSectionPos);
                lvList.setJClearEditTextFoucus();
                showContent(lvList, mLoadingView);
            }
            super.onPostExecute(result);
        }
    }

    public class SortIgnoreCase implements Comparator<String> {
        public int compare(String s1, String s2) {
            return s1.compareToIgnoreCase(s2);
        }
    }


}
