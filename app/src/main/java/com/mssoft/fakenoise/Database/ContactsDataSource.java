package com.mssoft.fakenoise.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.mssoft.fakenoise.Constants.Constants;
import com.mssoft.fakenoise.Utilities.Contact;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Marius on 9/16/2015.
 */
public class ContactsDataSource {

    private Context mContext;
    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;
    private String[] allColumns = {ContactsDatabase.COLUMN_ID,
            ContactsDatabase.COLUMN_NAME,
            ContactsDatabase.COLUMN_PHONE,
            ContactsDatabase.COLUMN_NOISE,
            ContactsDatabase.COLUMN_PATH};

    public ContactsDataSource(Context context) {
        dbHelper = new ContactsDatabase(context);
        mContext = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    private void setNoiseAndPathToContact(Contact contact, String noise) {

        if (contact != null) {

            contact.setNoise(noise.substring(0, noise.length() - 3));
            contact.setPath(Constants.AUDIO_PATH + "/" + noise);

            Toast.makeText(mContext, "Sound attached !", Toast.LENGTH_LONG).show();
        }

        else{
            Toast.makeText(mContext, "Failed to attach sound!", Toast.LENGTH_LONG).show();
        }
    }

    public void update(String phone, ContentValues values) {

        database.update(ContactsDatabase.TABLE_CONTACTS, values,
                "phone=?", new String[]{phone});

        String path = (String) values.get("audio");
        Contact mContact = findContactByPhone(phone);

        setNoiseAndPathToContact(mContact, path);
    }

    private Contact findContactByPhone(String phone) {
        ArrayList<Contact> contacts = getAllContacts();
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getPhone().equals(phone)) {
                return contacts.get(i);
            }
        }
        return null;
    }


    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(cursor.getLong(0));
        contact.setName(cursor.getString(1));
        contact.setPhone(cursor.getString(2));
        contact.setNoise(cursor.getString(3));
        contact.setPath(cursor.getString(4));
        return contact;
    }

    public Contact createContact(String name, String phone) {
        ContentValues values = new ContentValues();
        values.put(ContactsDatabase.COLUMN_NAME, name);
        values.put(ContactsDatabase.COLUMN_PHONE, phone);
        values.put(ContactsDatabase.COLUMN_NOISE, "No sound");
        values.put(ContactsDatabase.COLUMN_PATH, "No path");
        long insertId = database.insert(ContactsDatabase.TABLE_CONTACTS, null,
                values);
        Cursor cursor = database.query(ContactsDatabase.TABLE_CONTACTS,
                allColumns, ContactsDatabase.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Contact returnedContact = cursorToContact(cursor);
        cursor.close();
        return returnedContact;
    }

    public void deleteContact(Contact contact) {
        long id = contact.getId();
        database.delete(ContactsDatabase.TABLE_CONTACTS, ContactsDatabase.COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> list = new ArrayList<>();
        Cursor cursor = database.query(ContactsDatabase.TABLE_CONTACTS,
                allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            list.add(contact);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return list;
    }

    public boolean isPhoneNumberAlreadyAdded(String phoneNumber) {

        ArrayList<Contact> myContacts = getAllContacts();

        for (int i = 0; i < myContacts.size(); i++) {
            if (myContacts.get(i).getPhone().equals(phoneNumber)) {
                return true;

            }
        }
        return false;

    }
}