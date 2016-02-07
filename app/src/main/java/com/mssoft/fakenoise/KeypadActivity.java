package com.mssoft.fakenoise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

/**
 * Created by Marius on 9/9/2015.
 */
public class KeypadActivity extends Activity  {
    private EditText mEdtPhone,mEdtName;
    private TextInputLayout til_name , til_phone;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.keypad_layout);

        mEdtName = (EditText) findViewById(R.id.name_edit_text);
        mEdtPhone = (EditText) findViewById(R.id.phone_edit_text);
        mEdtPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());   //number format text

        til_name = (TextInputLayout) findViewById(R.id.text_layout_first);
        til_name.setErrorEnabled(true);

        til_phone = (TextInputLayout) findViewById(R.id.text_layout_second);
        til_phone.setErrorEnabled(true);
        manageNameAndPhoneFieldsForContact();

    }

    private void manageNameAndPhoneFieldsForContact(){
        mEdtName.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    if(mEdtName.getText().toString().equals("")) {
                        til_name.setError("Please Insert a name");
                    }
                    else{
                        mEdtName.clearFocus();
                        mEdtPhone.requestFocus();
                    }
                    return true;
                }
                return false;
            }
        });


        mEdtPhone.setOnKeyListener(new View.OnKeyListener() {

            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    if(mEdtPhone.getText().toString().equals("")){
                        til_phone.setError("Please Insert a phone number !");
                    }
                    else {
                        Intent intent = new Intent();
                        intent.putExtra("name", getContactNameKeypad(mEdtName));
                        intent.putExtra("phone", getContactPhoneKeypad(mEdtPhone));
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                    // Perform action on Enter key press
                    return true;
                }
                return false;
            }
        });
    }

    private String getContactNameKeypad(EditText edt){
        return edt.getText().toString();
    }

    private String getContactPhoneKeypad(EditText edt){
        return edt.getText().toString();
    }

}
