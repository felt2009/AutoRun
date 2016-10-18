package com.wfx.autorunner.adapter;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfx.autorunner.R;
import com.wfx.autorunner.core.PlanInfo;

import java.util.ArrayList;
import java.util.List;

public class PlanInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static String TAG = "PlanInfoAdapter";
    private List<PlanInfo> planInfoList;

    public PlanInfoAdapter(List<PlanInfo> planInfos) {
        this.planInfoList = new ArrayList<>();
        planInfoList.addAll(planInfos);
    }

    public void updatePlans(List<PlanInfo> planInfos) {
        planInfoList.clear();
        planInfoList.addAll(planInfos);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.plan_info_item, parent, false);
        final PlanInfoItemHolder holder = new PlanInfoItemHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final PlanInfo planInfo = planInfoList.get(position);
        PlanInfoItemHolder planInfoItemHolder = (PlanInfoItemHolder) holder;
        planInfoItemHolder.appName.setText(planInfo.getName());
        planInfoItemHolder.scriptName.setText(planInfo.getScript().getScriptName());
        long now = System.currentTimeMillis();
        long difference = now - planInfo.getTs();
        String relativeDateString = (difference >= 0 && difference <= DateUtils.MINUTE_IN_MILLIS) ?
                planInfoItemHolder.appName.getContext().getString(R.string.create_date_just_now) :
                DateUtils.getRelativeTimeSpanString(
                        planInfo.getTs(),
                        now,
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        planInfoItemHolder.createDate.setText(relativeDateString);
    }

    @Override
    public int getItemCount() {
        return planInfoList.size();
    }

    private final class PlanInfoItemHolder extends RecyclerView.ViewHolder {
        public TextView appName, scriptName, createDate;
        public ImageView appIcon;
        public AppCompatButton startStopButton;
        public PlanInfoItemHolder(View itemView) {
            super(itemView);
        }
    }
}
