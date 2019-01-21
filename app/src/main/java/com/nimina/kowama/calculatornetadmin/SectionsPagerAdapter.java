package com.nimina.kowama.calculatornetadmin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.nimina.kowama.calculatornetadmin.fragments.MaskFragment;
import com.nimina.kowama.calculatornetadmin.fragments.SubnetFragment;
import com.nimina.kowama.calculatornetadmin.fragments.VLSMFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position){
            case 0:{
                return new SubnetFragment();
            }
            case 1:{
                return new VLSMFragment();
            }
            case 2:{
                return new MaskFragment();
            }
            default:{
                return null;
            }
        }
    }


    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }
}