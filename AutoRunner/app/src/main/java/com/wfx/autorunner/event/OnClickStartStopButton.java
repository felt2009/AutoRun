package com.wfx.autorunner.event;

import com.wfx.autorunner.core.PlanInfo;

/**
 * Created by sean on 10/18/16.
 */

public class OnClickStartStopButton {
    public final PlanInfo planInfo;
    public OnClickStartStopButton(PlanInfo planInfo) {
        this.planInfo = planInfo;
    }
}
