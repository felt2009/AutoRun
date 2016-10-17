package com.wfx.autorunner.db;

import android.content.Context;

import com.wfx.autorunner.core.PlanInfo;

import java.util.List;

public class DataBaseManager {
    private static DataBaseManager sDataBaseManager;
    private PlanInfoDao mPlanInfoDao;
    private Context appCtx;

    public static DataBaseManager instance() {
        if (sDataBaseManager == null) {
            sDataBaseManager = new DataBaseManager();
        }
        return sDataBaseManager;
    }
    public void init(Context context) {
        appCtx = context.getApplicationContext();
        mPlanInfoDao = new PlanInfoDao(context);
    }

    private DataBaseManager() {
    }

    public synchronized void insert(PlanInfo planInfo) {
        mPlanInfoDao.insert(planInfo);
    }

    public synchronized void delete(PlanInfo planInfo) {
        mPlanInfoDao.delete(planInfo);
    }

    public synchronized void update(PlanInfo planInfo) {
        mPlanInfoDao.update(planInfo);
    }

    public List<PlanInfo> getPlans() {
        return mPlanInfoDao.getPlans();
    }
}
