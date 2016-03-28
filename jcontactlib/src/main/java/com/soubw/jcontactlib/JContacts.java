package com.soubw.jcontactlib;

import java.io.Serializable;
import java.util.Locale;

/**
 * Created by WX_JIN on 2016/3/10.
 */
public class JContacts implements Serializable {

    protected String jName;
    protected String jPhoneNumber;
    protected String jFirstWord;

    public JContacts(){}

    public String getjName() {
        return jName;
    }

    public void setjName(String jName) {
        this.jName = jName;
        if (jName !=null && !jName.isEmpty()){
            setjFirstWord(CharacterParser.getInstance().getSelling(jName).substring(0, 1).toUpperCase(Locale.getDefault()));
        }
    }

    public String getjPhoneNumber() {
        return jPhoneNumber;
    }

    public void setjPhoneNumber(String jPhoneNumber) {
        this.jPhoneNumber = jPhoneNumber;
    }

    public String getjFirstWord() {
        return jFirstWord;
    }

    private void setjFirstWord(String jFirstWord) {
        this.jFirstWord = jFirstWord;
    }
}
