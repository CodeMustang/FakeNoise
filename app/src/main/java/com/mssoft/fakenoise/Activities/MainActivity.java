package com.mssoft.fakenoise.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mssoft.fakenoise.Adapters.TabPageAdapter;
import com.mssoft.fakenoise.Database.ContactsDataSource;
import com.mssoft.fakenoise.Database.LogsDataSource;
import com.mssoft.fakenoise.R;
import com.mssoft.fakenoise.Services.PhoneListenerService;
import com.mssoft.fakenoise.Utilities.Permissions;
import com.mssoft.fakenoise.Utilities.SlidingTabLayout;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private SlidingTabLayout tabs;
    public static ContactsDataSource mContactsSource;
    public static LogsDataSource mLogsDataSource;


    private void startService() {

        Intent intent = new Intent(this, PhoneListenerService.class);
        startService(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tlb);
        setSupportActionBar(toolbar);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new TabPageAdapter(getFragmentManager(), this));
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setCustomTabView(R.layout.customtab_view, R.id.tv);
        tabs.setBackgroundColor(ContextCompat.getColor(this, R.color.fragTabsColor));
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return ContextCompat.getColor(getApplicationContext(), R.color.tabColor);
            }
        });

        tabs.setDistributeEvenly(true); //Fitting each tab equally for CENTERING
        tabs.setViewPager(viewPager);

        Permissions.askForPermission(this, null, 4);

        if (Permissions.readPhoneState) {
            startService();

        }


        mContactsSource = new ContactsDataSource(this);

        try {
            mContactsSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load contacts!", Toast.LENGTH_LONG).show();
        }

        mLogsDataSource = new LogsDataSource(this);

        try {
            mLogsDataSource.open();
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to load logs!", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {

            case Permissions.PHONE_STATE_REQUEST_CODE:

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Permissions.readPhoneState = true;
                    startService();
                } else {
                    Permissions.readPhoneState = false;
                }

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.help:
                Toast.makeText(getApplicationContext(), "Coming soon Help featurette", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        mContactsSource.close();
        mLogsDataSource.close();

        if (PhoneListenerService.isRunning) {
            Intent intent = new Intent(this, PhoneListenerService.class);
            stopService(intent);
        }

        super.onDestroy();
    }
}
