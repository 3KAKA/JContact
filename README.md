# JContact
Android 自定义通讯录（仿Ios反弹效果+模糊搜索+查看手机通讯录+拉伸导航条）
效果图如下：
![image](https://github.com/WX-JIN/JContact/blob/master/screenshots/1.png)

![image](https://github.com/WX-JIN/JContact/blob/master/screenshots/2.png)

![image](https://github.com/WX-JIN/JContact/blob/master/screenshots/3.png)

##	用法

* Android Studio

	引入依赖
	```
    compile 'com.soubw:jcontactlib:0.1.2'
	```	
	
##	调用示例

### 添加继承实体对象JContacts（可直接使用JContacts实体对象，根据自己是否需要扩展实体属性）

```java 实例为添加自己拓展实体属性
public class MainBean extends JContacts implements Serializable {

    private String wxj;

    public String getWxj() {
        return wxj;
    }

    public void setWxj(String wxj) {
        this.wxj = wxj;
    }

    public MainBean(){
        super();
    }
}

```	

### 添加继承JAdapter

```java
public class MainAdapter extends JAdapter {


    public MainAdapter(Context context, List<MainBean> jContactsList, JListView lvList, int indexBarViewId, int previewViewId, int itemLayoutId, int sectionLayoutId, View loadingView) {
        super(context, jContactsList, lvList, indexBarViewId, previewViewId, itemLayoutId, sectionLayoutId, loadingView);
    }

    @Override
    public void convert(JViewHolder holder, JContacts bean, int type) {
        MainBean b = (MainBean) bean;
        switch (type) {
            case TYPE_ITEM:
                holder.setText(R.id.row_title,bean.getjName()+b.getWxj());
                break;
            case TYPE_SECTION:
                holder.setText(R.id.row_title,bean.getjFirstWord());
                break;
        }
    }
}

### 调用

```java
	        mAdaptor = new MainAdapter(this,
                jContactsList,//联系人列表
                (JListView) findViewById(R.id.lvList),//JListView对象
                R.layout.jcontact_index_bar_view,//导航条视图
                R.layout.jcontact_preview_view,//预览字母背景图
                R.layout.jcontact_row_view,//列表内容view
                R.layout.jcontact_section_row_view,//列表字母view
                mLoadingView//加载LoadingView
        );

```	

##	0.1.0版本以后

### 新增可自己选择搜索框的背景框和图标，以及提示文字

```java
        <com.soubw.jcontactlib.JListView
            android:id="@+id/lvList"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:dividerHeight="0.1dp"
            android:divider="@android:color/black"
            jcontact:jClearEditTextBg="@drawable/jclearedittext_bg"
            jcontact:jClearEditTextCloseBg="@drawable/jclearedittext_close_bg"
            jcontact:jClearEditTextIconBg="@drawable/jclearedittext_icon_bg"
            jcontact:jClearEditTextNotice="请输入关键字"
            android:scrollbars="none"/>

```	


详细可以参考Demo
		
交流群：313870489
