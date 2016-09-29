package com.wfx.autorunner;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wfx.autorunner.fragments.ConfigRunningFragment;

/**
 * Created by sean on 9/22/16.
 */
public class AddNewTaskActivity extends AppCompatActivity {
    private final static String TAG = "AddNewTaskActivity";
    private ConfigRunningFragment configRunningFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.new_task_activity_title);
        }
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


    private void showConfigRunningFragment() {
        if (configRunningFragment == null) {
            configRunningFragment = ConfigRunningFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, configRunningFragment).commit();
    }
}
