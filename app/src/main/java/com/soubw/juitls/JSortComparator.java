package com.soubw.juitls;

import java.util.Comparator;

/**
 * Created by WX_JIN on 2016/3/10.
 */
public class JSortComparator implements Comparator<JContacts> {
    public int compare(JContacts j1, JContacts j2) {
        return j1.getjName().compareToIgnoreCase(j2.getjName());
    }
}
