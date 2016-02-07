package com.mssoft.fakenoise.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.mssoft.fakenoise.Fragments.BackgroundNoiseFragment;
import com.mssoft.fakenoise.Fragments.LogsFragment;
import com.mssoft.fakenoise.Fragments.SelectedContactsFragment;
import com.mssoft.fakenoise.R;

/**
 * Created by Marius on 8/29/2015.
 */
public class TabPageAdapter extends FragmentStatePagerAdapter{
    private static final int NUMBER_OF_TABS = 3;
    private Context context;
    private Drawable drawable;
    private static final String[] TAB_NAMES = {"CONTACTS","NOISE PICKER","LOGS"};
    public TabPageAdapter(FragmentManager fm , Context context) {
        super(fm); this.context = context;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0: {
                return new SelectedContactsFragment();
            }
            case 1:{
                return new BackgroundNoiseFragment();
            }
            case 2:{
                return new LogsFragment();
            }
        }
        return null;
    }
    @Override
    public int getCount() {
        return NUMBER_OF_TABS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0 :
                drawable = ContextCompat.getDrawable(context, R.drawable.agenda_icon);
                break;
            case 1:
                drawable =  ContextCompat.getDrawable(context, R.drawable.noise_icon);
                break;
            case 2:
                drawable =  ContextCompat.getDrawable(context, R.drawable.logs_icon);
                break;
        }
        drawable.setBounds(0, 0, 48, 48);
        ImageSpan imageSpan = new ImageSpan(drawable);

        SpannableString spannableString = new SpannableString(TAB_NAMES[position] + " ");
        spannableString.setSpan(imageSpan, spannableString.length()-1, spannableString.length(), 0);
        return spannableString;
    }
}
