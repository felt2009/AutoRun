package com.wfx.autorunner.model;

import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

import com.wfx.autorunner.ContextHolder;
import com.wfx.autorunner.R;
import com.wfx.autorunner.controller.PhoneInfoHelper;
import com.wfx.autorunner.controller.PlanHelper;
import com.wfx.autorunner.controller.ScriptRunning;
import com.wfx.autorunner.core.PlanInfo;
import com.wfx.autorunner.core.Script;
import com.wfx.autorunner.core.TaskEntry;
import com.wfx.autorunner.db.DataBaseManager;
import com.wfx.autorunner.event.UpdateRunningInfo;
import com.wfx.autorunner.utils.PackageUtils;

import org.greenrobot.eventbus.EventBus;

import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by sean on 10/17/16.
 */

public class PlanInfoManager {
    private final static String TAG = "PlanInfoManager";
    private PlanInfoManager() {}
    private List<PlanInfo> planInfoList;
    private static PlanInfoManager sInstance;
    private PlanInfo running;
    private PlanInfoExecuteTask runningTask;
    public static PlanInfoManager instance() {
        if (sInstance == null) {
            sInstance = new PlanInfoManager();
        }
        return sInstance;
    }
    public void init() {
        planInfoList = DataBaseManager.instance().getPlans();
        new Thread() {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                PackageUtils.getAppInfoList();
            }
        }.start();
    }
    public List<PlanInfo> getPlanInfoList() {
        return planInfoList;
    }
    public PlanInfo createNewPlanInfo(String targetPackageName, Script script, int totalCount, int type) {
        PlanInfo planInfo = new PlanInfo(targetPackageName, System.currentTimeMillis(), script, totalCount);
        DataBaseManager.instance().insert(planInfo);
        planInfoList.add(planInfo);
        Collections.sort(planInfoList);
        return planInfo;
    }

    public void startPlan(PlanInfo planInfo) {
        Log.d(TAG, "startRunning plan xxxxxxxx");
        if (running != null && running.getTs() == planInfo.getTs()) {
            Log.d(TAG, "startRunning already running!");
            return;
        }
        if (!PackageUtils.scriptInstalled(planInfo.getScript().getScriptName())) {
            Toast.makeText(ContextHolder.getContext(),
                    ContextHolder.getContext().getString(R.string.no_script_package), Toast.LENGTH_LONG).show();
            return;
        }

        if (running != null) {
            if (runningTask != null) {
                runningTask.cancel();
                runningTask = null;
            }
            running.setStatus(PlanInfo.Status.stop);
            DataBaseManager.instance().update(running);
        }
        running = planInfo;
        running.setStatus(PlanInfo.Status.running);
        DataBaseManager.instance().update(running);

        runningTask = new PlanInfoExecuteTask(running);
        runningTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        EventBus.getDefault().post(new UpdateRunningInfo());
    }

    public void stopPlan(PlanInfo planInfo) {
        if (planInfo != running || (running != null && (running.getTs() != planInfo.getTs()))) {
            Log.d(TAG, "planInfo not running!");
            return;
        }
        if (running != null) {
            runningTask.cancel();
            runningTask = null;
            running.setStatus(PlanInfo.Status.stop);
            DataBaseManager.instance().update(running);
            EventBus.getDefault().post(new UpdateRunningInfo());
        }
    }

    private class PlanInfoExecuteTask extends AsyncTask<Void, Void, Void> {
        private PlanInfo planInfo;
        private boolean cancel;
        private PlanInfoExecuteTask(PlanInfo planInfo) {
            this.planInfo = planInfo;
            this.cancel = false;
        }

        public void cancel() {
            cancel = true;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "PlanInfoExecuteTask onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "PlanInfoExecuteTask doInBackground");
            ScriptRunning running = new ScriptRunning();
            TaskEntry entry = planInfo.getNextTaskEntry();
            while (entry != null && !cancel) {
                running.setScript(entry.getScript());
                running.setTargetPackageName(entry.getTargetPackageName());
                // TODO area need to be get;
                PhoneInfoHelper.getInstance().generatePhoneInfo(entry.getTargetPackageName(), "area", entry.getScript().getType());
                running.prepare();
                if (PhoneInfoHelper.getInstance().waitPhoneInfoValid(20)) {
                    running.runScript();
                } else {
                    Log.i(TAG, "FAILED, Phone info not valid.");
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                entry = planInfo.getNextTaskEntry();
                publishProgress();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Log.d(TAG, "PlanInfoExecuteTask onProgressUpdate");
            if (running != null) {
                running.increaseCount();
                DataBaseManager.instance().update(running);
                EventBus.getDefault().post(new UpdateRunningInfo());
            }
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "PlanInfoExecuteTask onPostExecute");
            if (running != null) {
                runningTask = null;
                running.setStatus(PlanInfo.Status.stop);
                DataBaseManager.instance().update(running);
                EventBus.getDefault().post(new UpdateRunningInfo());
            }
        }
    }
}
