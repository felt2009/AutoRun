package com.wfx.autorunner.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wfx.autorunner.R;

/**
 * Created by sean on 9/22/16.
 */
public class ConfigRunningFragment extends Fragment {
    private final static String TAG = "ConfigRunningFragment";
    public static ConfigRunningFragment newInstance() {
        ConfigRunningFragment fragment = new ConfigRunningFragment();
        /*
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_config_running, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
