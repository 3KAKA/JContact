package com.soubw.jcontact;

import com.soubw.jcontactlib.JContacts;

import java.io.Serializable;

/**
 * Created by WX_JIN on 2016/3/23.
 */
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
