package com.soubw.jcontact;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.soubw.jcontactlib.JAdapter;
import com.soubw.jcontactlib.JContacts;
import com.soubw.jcontactlib.JListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WX_JIN on 2016/3/10.
 */
public class MainActivity extends Activity{

    /**
     * 完整手机通讯录好友信息列表
     */
    private List<MainBean> jContactsList;

    private JAdapter mAdaptor;

    private ProgressBar mLoadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jContactsList = new ArrayList<>();
        initView();
        loadData();

    }

    private void initView(){
        mLoadingView = (ProgressBar) findViewById(R.id.loading_view);
        mAdaptor = new MainAdapter(this,
                jContactsList,//联系人列表
                (JListView) findViewById(R.id.lvList),//JListView对象
                R.layout.jcontact_index_bar_view,//导航条视图
                R.layout.jcontact_preview_view,//预览字母背景图
                R.layout.jcontact_row_view,//列表内容view
                R.layout.jcontact_section_row_view,//列表字母view
                mLoadingView//加载View
        );
        JListView jListView = (JListView) findViewById(R.id.lvList);
        jListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainBean bean = (MainBean)parent.getAdapter().getItem(position);
                Toast.makeText(MainActivity.this,bean.getjName(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * 加载通讯录，这个也可以自己去查询，只要把对象赋值给jContactsList，并重新mAdaptor.setjContactsList(jContactsList);;
     */
    private void loadData(){
        JLoadData.getIntance().loadLocalContacts(MainActivity.this, new JLoadData.JLoadDataListener() {
            @Override
            public void doSuccess(List<MainBean> list) {
                jContactsList.clear();
                jContactsList = list;
                mAdaptor.setjContactsList(jContactsList);
            }
            @Override
            public void doFailed(String error) {
            }
        });
    }



}
