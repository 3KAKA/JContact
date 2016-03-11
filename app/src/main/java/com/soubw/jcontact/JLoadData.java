package com.soubw.jcontact;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;

/**
 * Created by WX_JIN on 2016/3/10.
 */
public class JLoadData {

	private static class InnerInstance {
		private static final JLoadData instance = new JLoadData();
	}
	
	private JLoadData(){}
	
	public static JLoadData getIntance(){
		return InnerInstance.instance;
	}
	
    public synchronized void loadLocalContacts(final Context context, final JLoadDataListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<JContacts> listContacts = null;
                try {
                    ContentResolver resolver = context.getApplicationContext().getContentResolver();
                    Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, "sort_key"}, null, null, "sort_key COLLATE LOCALIZED ASC");
                    if (phoneCursor == null || phoneCursor.getCount() == 0) {
                        listener.doFailed("未获得读取联系人权限 或 未获得联系人数据");
                        return;
                    }
                    int PHONES_NUMBER_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int PHONES_DISPLAY_NAME_INDEX = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    if (phoneCursor.getCount() > 0) {
                        listContacts = new ArrayList<JContacts>();
                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
                            if (TextUtils.isEmpty(phoneNumber))
                                continue;
                            String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
                            JContacts wxjContact = new JContacts();
                            wxjContact.setjName(contactName);
                            wxjContact.setjPhoneNumber(phoneNumber);
                            listContacts.add(wxjContact);
                        }
                    }
                    listener.doSuccess(listContacts);
                    phoneCursor.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.doFailed(""+e.getStackTrace());
                }
            }
        }).start();
    }

 
    
    public interface JLoadDataListener {
        void doSuccess(List<JContacts> list);
        void doFailed(String error);
    }
}
