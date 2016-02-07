package com.mssoft.fakenoise.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mssoft.fakenoise.Contact;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Marius on 9/16/2015.
 */
public class ContactsDataSource {

    private SQLiteDatabase database;
    private SQLiteOpenHelper dbHelper;
    private String[] allColumns = {ContactsDatabase.COLUMN_ID,
            ContactsDatabase.COLUMN_NAME, ContactsDatabase.COLUMN_PHONE};

    public ContactsDataSource(Context context) {
        dbHelper = new ContactsDatabase(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    private Contact cursortToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.setId(cursor.getLong(0));
        contact.setName(cursor.getString(1));
        contact.setPhone(cursor.getString(2));
        return contact;
    }

    public Contact createContact(String name, String phone) {
        ContentValues values = new ContentValues();
        values.put(ContactsDatabase.COLUMN_NAME, name);
        values.put(ContactsDatabase.COLUMN_PHONE, phone);
        long insertId = database.insert(ContactsDatabase.TABLE_CONTACTS, null,
                values);
        Cursor cursor = database.query(ContactsDatabase.TABLE_CONTACTS,
                allColumns, ContactsDatabase.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Contact returnedContact = cursortToContact(cursor);
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
            Contact contact  = cursortToContact(cursor);
            list.add(contact);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return list;
    }
}