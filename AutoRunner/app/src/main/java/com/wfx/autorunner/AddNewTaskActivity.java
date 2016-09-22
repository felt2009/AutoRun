package com.wfx.autorunner;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfx.autorunner.data.AppInfo;
import com.wfx.autorunner.fragments.ChooseAppFragment;
import com.wfx.autorunner.fragments.ConfigRunningFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by sean on 9/22/16.
 */
public class AddNewTaskActivity extends AppCompatActivity {
    private final static String TAG = "AddNewTaskActivity";
    private ChooseAppFragment chooseAppFragment;
    private ConfigRunningFragment configRunningFragment;
    private View chosenApp, hintView;
    private ImageView appIcon;
    private TextView appName;
    private AppInfo chosenAppInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        chosenApp = findViewById(R.id.chosen_app);
        hintView = findViewById(R.id.hint);
        appName = (TextView) findViewById(R.id.app_name);
        appIcon = (ImageView) findViewById(R.id.icon);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        showChooseAppFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(AppInfo appInfo) {
        Log.d(TAG, "OnBatteryStatsUpdate !!!");
        chosenAppInfo = appInfo;
        appIcon.setImageDrawable(appInfo.appIcon);
        appName.setText(appInfo.appName);
        chosenApp.setVisibility(View.VISIBLE);
        hintView.setVisibility(View.INVISIBLE);
        showConfigRunningFragment();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showChooseAppFragment() {
        if (chooseAppFragment == null) {
            chooseAppFragment = ChooseAppFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, chooseAppFragment).commit();
    }

    private void showConfigRunningFragment() {
        if (configRunningFragment == null) {
            configRunningFragment = ConfigRunningFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragment_container, configRunningFragment).commit();
    }
}
