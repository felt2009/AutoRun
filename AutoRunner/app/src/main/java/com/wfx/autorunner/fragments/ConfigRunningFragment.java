package com.wfx.autorunner.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.wfx.autorunner.ChooseAppActivity;
import com.wfx.autorunner.R;
import com.wfx.autorunner.core.Script;
import com.wfx.autorunner.data.AppInfo;
import com.wfx.autorunner.network.ScriptResponse;
import com.wfx.autorunner.network.ServerApiManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sean on 9/22/16.
 */
public class ConfigRunningFragment extends Fragment {
    private final static String TAG = "ConfigRunningFragment";
    private ImageView appIcon;
    private TextView appName, errorMessage;
    private Spinner scriptSpinner;
    private AppCompatButton btnConfirm;
    private EditText repeatCountEdit;
    private View targetAppView;
    private List<Script> scripts;

    private static class OnGetScriptEvent {
        public OnGetScriptEvent(boolean succeed) {
            this.succeed = succeed;
        }
        public final boolean succeed;
    }

    private AppInfo chosenAppInfo;
    public static ConfigRunningFragment newInstance() {
        ConfigRunningFragment fragment = new ConfigRunningFragment();
        /*
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(OnGetScriptEvent onGetScriptEvent) {
        if (onGetScriptEvent.succeed) {
            String[] spinnerStrings = new String[scripts.size()];
            for(int index = 0; index < scripts.size(); ++index) {
                spinnerStrings[index] = scripts.get(index).getScriptName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, spinnerStrings);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            scriptSpinner.setAdapter(adapter);
            errorMessage.setText(null);
        } else {
            // TODO: show error message, retry
            scriptSpinner.setAdapter(null);
            errorMessage.setText(R.string.error_message);
        }
        updateConfirmButton();
    }

    @Subscribe
    public void onEvent(AppInfo appInfo) {
        Log.d(TAG, "appInfo !!!" + appInfo.packageName);
        chosenAppInfo = appInfo;
        //showConfigRunningFragment();
        appIcon.setImageDrawable(chosenAppInfo.appIcon);
        appName.setText(chosenAppInfo.appName);

        syncScriptInfo();
        updateConfirmButton();
    }

    private void syncScriptInfo() {
        if (chosenAppInfo == null) return;
        ServerApiManager.instance().fetchScriptInfo(new ServerApiManager.Listener() {
            @Override
            public void onStart() {
            }

            @Override
            public void onFinished(boolean success, JSONObject response) {
                Log.d(TAG, "onFinished success:" + success);
                if (success && response != null) {
                    try {
                        ScriptResponse scriptResponse = JSON.parseObject(response.toString(),
                                ScriptResponse.class);
                        Log.d(TAG, "code:" + scriptResponse.getCode() + ", msg:" + scriptResponse.getMsg().size());
                        for (Script script : scriptResponse.getMsg()) {
                            Log.d(TAG, "script:" + script.getScriptName() + ", time:" + script.getTime()
                                    + ", type:" + script.getType());
                        }
                        scripts = scriptResponse.getMsg();
                        if (scripts != null && scripts.size() > 0) {
                            EventBus.getDefault().post(new OnGetScriptEvent(true));
                        } else {
                            EventBus.getDefault().post(new OnGetScriptEvent(false));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "e:" + e.getMessage());
                        EventBus.getDefault().post(new OnGetScriptEvent(false));
                    }
                } else {
                    EventBus.getDefault().post(new OnGetScriptEvent(false));
                }
            }
        }, chosenAppInfo.packageName, ServerApiManager.ScriptType.TYPE_ALL,
                chosenAppInfo.packageName);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_config_running, container, false);
        appIcon = (ImageView) rootView.findViewById(R.id.icon);
        appName = (TextView) rootView.findViewById(R.id.app_name);
        errorMessage = (TextView) rootView.findViewById(R.id.error_message);
        targetAppView = rootView.findViewById(R.id.chosen_app);
        scriptSpinner = (Spinner) rootView.findViewById(R.id.scrip_spinner);
        rootView.findViewById(R.id.retry_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                syncScriptInfo();
            }
        });
        btnConfirm = (AppCompatButton) rootView.findViewById(R.id.confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: add new task
            }
        });
        repeatCountEdit = (EditText) rootView.findViewById(R.id.repeat_count);
        repeatCountEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d(TAG, "onTextChanged:" + charSequence.toString() +
                        ", repeatCountEdit:" + repeatCountEdit.getText());
                updateConfirmButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        targetAppView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChooseAppActivity.class));
            }
        });
        EventBus.getDefault().register(this);
        return rootView;
    }

    private void updateConfirmButton() {
        btnConfirm.setEnabled(chosenAppInfo != null && scripts != null && scripts.size() > 0 &&
                !TextUtils.isEmpty(repeatCountEdit.getText()) &&
                TextUtils.isDigitsOnly(repeatCountEdit.getText()));
    }
}
