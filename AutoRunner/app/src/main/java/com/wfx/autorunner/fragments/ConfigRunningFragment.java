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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wfx.autorunner.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by sean on 9/22/16.
 */
public class ConfigRunningFragment extends Fragment {
    private final static String TAG = "ConfigRunningFragment";
    private ProgressBar progressBar;
    private ListView listView;
    private List<ResolveInfo> apps;
    private PackageManager pm;
    public static ConfigRunningFragment newInstance() {
        ConfigRunningFragment fragment = new ConfigRunningFragment();
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

    private class AppAdapter extends ArrayAdapter<ResolveInfo> {
        AppAdapter(Context ctx, List<ResolveInfo> apps) {
            super(ctx, R.layout.app_item, apps);
        }

        @Override
        public View getView(int position, View convertView,
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
            AppViewHolder appViewHolder = (AppViewHolder) itemView.getTag();
            appViewHolder.appName.setText(getItem(position).loadLabel(pm));
            appViewHolder.appIcon.setImageDrawable(getItem(position).loadIcon(pm));
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
                Intent main=new Intent(Intent.ACTION_MAIN, null);

                main.addCategory(Intent.CATEGORY_LAUNCHER);
                apps = pm.queryIntentActivities(main, 0);
                Collections.sort(apps, new ResolveInfo.DisplayNameComparator(pm));
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
