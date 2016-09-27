package com.wfx.autorunner.network;

import com.wfx.autorunner.core.Script;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sean on 9/27/16.
 */
public class ScriptResponse {
    private int code;
    private List<Script> msg = new ArrayList<>();
    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
    public List<Script> getMsg() {
        return msg;
    }

    public void setMsg(List<Script> msg) {
        this.msg = msg;
    }

    public void addScript(Script script) {
        msg.add(script);
    }
}
