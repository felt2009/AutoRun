package com.wfx.autorunner.model;

import com.wfx.autorunner.core.PlanInfo;
import com.wfx.autorunner.core.Script;
import com.wfx.autorunner.db.DataBaseManager;

import java.util.Collections;
import java.util.List;

/**
 * Created by sean on 10/17/16.
 */

public class PlanInfoManager {
    private final static String TAG = "PlanInfoManager";
    private PlanInfoManager() {}
    private List<PlanInfo> planInfos;
    private static PlanInfoManager sInstance;
    public static PlanInfoManager instance() {
        if (sInstance == null) {
            sInstance = new PlanInfoManager();
        }
        return sInstance;
    }
    public void init() {
        planInfos = DataBaseManager.instance().getPlans();
    }
    public List<PlanInfo> getPlanInfos() {
        return planInfos;
    }
    public PlanInfo createNewPlanInfo(String targetPackageName, Script script, int totalCount, int type) {
        PlanInfo planInfo = new PlanInfo(targetPackageName, System.currentTimeMillis(), script, totalCount);
        DataBaseManager.instance().insert(planInfo);
        planInfos.add(planInfo);
        Collections.sort(planInfos);
        return planInfo;
    }
}
