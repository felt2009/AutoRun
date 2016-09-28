package com.wfx.autorunner.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wfx.autorunner.R;
import com.wfx.autorunner.data.AppInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by sean on 9/22/16.
 */
public class ChooseAppFragment extends Fragment {
    private final static String TAG = "ChooseAppFragment";
    private ProgressBar progressBar;
    private ListView listView;
    private List<AppInfo> apps;
    private PackageManager pm;
    public static ChooseAppFragment newInstance() {
        ChooseAppFragment fragment = new ChooseAppFragment();
        /*
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_choose_app, container, false);
        listView = (ListView) rootView.findViewById(R.id.app_list);
        progressBar = (ProgressBar) rootView.findViewById(R.id.loading_progress);
        pm = getContext().getPackageManager();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EventBus.getDefault().post(apps.get(i));
                getActivity().finish();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        listView.setVisibility(View.INVISIBLE);
        updateInstallApps();
    }

    private void updateInstallApps() {
        new GetInstalledAppTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class AppViewHolder {
        public ImageView appIcon;
        public TextView appName;
        public AppViewHolder(View itemView) {
            appName = (TextView) itemView.findViewById(R.id.app_name);
            appIcon = (ImageView) itemView.findViewById(R.id.icon);
        }
    }

    private class AppAdapter extends ArrayAdapter<AppInfo> {
        AppAdapter(Context ctx, List<AppInfo> apps) {
            super(ctx, R.layout.app_item, apps);
        }

        @Override
        public android.view.View getView(int position, View convertView,
                                         ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(
                        R.layout.app_item, parent, false);
                convertView.setTag(new AppViewHolder(convertView));
            }
            bindView(position, convertView);
            return(convertView);
        }

        private void bindView(int position, View itemView) {
            AppInfo info = getItem(position);
            AppViewHolder appViewHolder = (AppViewHolder) itemView.getTag();
            appViewHolder.appName.setText(info.appName);
            appViewHolder.appIcon.setImageDrawable(info.appIcon);
        }
    }

    private class GetInstalledAppTask extends AsyncTask<Void, Void, Void> {
        private GetInstalledAppTask() {}
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "GetInstalledAppTask onPreExecute");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            Log.d(TAG, "GetInstalledAppTask doInBackground");
            try {
                PackageManager pm = getContext().getPackageManager();
                Intent main = new Intent(Intent.ACTION_MAIN, null);
                main.addCategory(Intent.CATEGORY_LAUNCHER);
                List<ResolveInfo> resolveInfoList = pm.queryIntentActivities(main, 0);
                Collections.sort(resolveInfoList, new ResolveInfo.DisplayNameComparator(pm));
                apps = new ArrayList<>();
                for (ResolveInfo resolveInfo : resolveInfoList) {
                    Log.d(TAG, "GetInstalledAppTask doInBackground packageName:" + resolveInfo.resolvePackageName);
                    apps.add(new AppInfo(resolveInfo.loadLabel(pm).toString(),
                            resolveInfo.resolvePackageName, resolveInfo.loadIcon(pm)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.d(TAG, "GetInstalledAppTask onPostExecute result:" + result);
            progressBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            listView.setAdapter(new AppAdapter(getContext(), apps));
        }
    }
}
