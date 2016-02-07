package com.mssoft.fakenoise.Constants;

/**
 * Created by Marius on 10/4/2015.
 */
public class Contants {

    private static final int REQUEST_CONTACT = 1;
    private static final int REQUEST_KEYPAD = 2;
    private static final int RESULT_OK = -1;
    private static final int RESULT_CANCELED = 0;

    private static final CharSequence[] LIST = {"Agenda","Keypad"};
    private static final String DIALOG_TITLE = "Add From ...";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private final String error ="Some error occured , please choose a different number";

    private final CharSequence[] BGNOISE_LIST = {"Implicit Sound ","Record"};
    private final String BGNOISE_DIALOG_TITLE = "Choose noise from ...";

    /******** SELECTED CONTACTS FRAGMENT ***********************/



    public int getRequestContact(){
        return REQUEST_CONTACT;
    }

    public int getRequestKeypad(){
        return REQUEST_KEYPAD;
    }

    public int getResultOk(){
        return RESULT_OK;
    }

    public int getResultCanceled(){
        return RESULT_CANCELED;
    }

    public CharSequence[] getSelectedContactsDialogSelectionList(){
        return LIST;
    }

    public String getSelectedContactsDialogTitle(){
        return DIALOG_TITLE;
    }
    public String getSelectedContactsColumnName(){
        return COLUMN_NAME;
    }
    public String getSelectedContactsColumnPhone(){
        return COLUMN_PHONE;
    }
    public String getContactError(){return error;};



    /*************************************************/

   /**************** BACKGROUND NOISE FRAGMENT *************/

    public CharSequence[] getBackgroundNoiseDialogSelectionList(){
        return BGNOISE_LIST;
    }

    public String getBackgroundNoiseDialogTitle(){
        return BGNOISE_DIALOG_TITLE;
    }


    /****************** BACKGROUND NOISE FRAGMENT *****************/
}
