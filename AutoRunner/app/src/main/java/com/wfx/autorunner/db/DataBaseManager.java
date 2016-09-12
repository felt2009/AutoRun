package com.wfx.autorunner.db;

import android.content.Context;

import com.wfx.autorunner.core.PlanInfo;

import java.util.List;

public class DataBaseManager {
    private static DataBaseManager sDataBaseManager;
    private final PlanInfoDao mPlanInfoDao;

    public static DataBaseManager instance(Context context) {
        if (sDataBaseManager == null) {
            sDataBaseManager = new DataBaseManager(context);
        }
        return sDataBaseManager;
    }

    private DataBaseManager(Context context) {
        mPlanInfoDao = new PlanInfoDao(context);
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
