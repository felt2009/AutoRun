package com.wfx.autorunner;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wfx.autorunner.adapter.PlanInfoAdapter;
import com.wfx.autorunner.controller.ScriptRunning;
import com.wfx.autorunner.core.PlanInfo;
import com.wfx.autorunner.event.OnClickStartStopButton;
import com.wfx.autorunner.event.UpdateResolveInfo;
import com.wfx.autorunner.event.UpdateRunningInfo;
import com.wfx.autorunner.model.PlanInfoManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private RecyclerView planInfoRecyclerView;
    private int cardSpace;
    private PlanInfoAdapter planInfoAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // request su
        ScriptRunning.runSu();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        planInfoRecyclerView = (RecyclerView) findViewById(R.id.plan_list);
        setSupportActionBar(toolbar);

        planInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardSpace = getResources().getDimensionPixelSize(R.dimen.card_space);
        planInfoRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                       RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int pos = parent.getChildAdapterPosition(view);
                int count = parent.getAdapter().getItemCount();
                outRect.top = (pos == 0) ? 2 * cardSpace : cardSpace;
                outRect.bottom = (pos == (count - 1)) ? 2 * cardSpace : cardSpace;
                outRect.left = outRect.right = 2 * cardSpace;
            }
        });
        planInfoAdapter = new PlanInfoAdapter(PlanInfoManager.instance().getPlanInfoList());
        planInfoRecyclerView.setAdapter(planInfoAdapter);
        PlanInfoManager.instance().setActivity(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNewTaskActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Subscribe
    public void onEvent(OnClickStartStopButton onClickStartStopButton) {
        if (onClickStartStopButton.planInfo.getStatus() == PlanInfo.Status.running.value) {
            PlanInfoManager.instance().stopPlan(onClickStartStopButton.planInfo);
        } else {
            PlanInfoManager.instance().startPlan(onClickStartStopButton.planInfo);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateRunningInfo updateRunningInfo) {
        planInfoAdapter.updatePlans(PlanInfoManager.instance().getPlanInfoList());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UpdateResolveInfo updateResolveInfo) {
        planInfoAdapter.updatePlans(PlanInfoManager.instance().getPlanInfoList());
    }

    @Override
    public void onResume() {
        EventBus.getDefault().register(this);
        super.onResume();
        planInfoAdapter.updatePlans(PlanInfoManager.instance().getPlanInfoList());
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
