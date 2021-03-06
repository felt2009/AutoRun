package com.wfx.autorunner;

import android.app.Application;

import com.wfx.autorunner.db.DataBaseManager;
import com.wfx.autorunner.model.PlanInfoManager;
import com.wfx.autorunner.network.ServerApiManager;

/**
 * Created by n000058 on 2016/9/17.
 */
public class AutoRunApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.initial(this);
        ServerApiManager.instance().init(this);
        DataBaseManager.instance().init(this);
        PlanInfoManager.instance().init();
    }
}
