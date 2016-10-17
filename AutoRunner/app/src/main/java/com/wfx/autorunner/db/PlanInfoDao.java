package com.wfx.autorunner.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.wfx.autorunner.core.PlanInfo;
import com.wfx.autorunner.core.Script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class PlanInfoDao extends AbstractDao {
    private static final String TABLE_NAME = "PlanInfo";

    private static final String COL_NAME_ID = "_id";
    private static final String COL_NAME_NAME = "_name";
    private static final String COL_NAME_TS = "_ts";
    private static final String COL_NAME_STATUS = "_status";
    private static final String COL_NAME_SCRIPT_TOTAL_COUNT = "_total_count";
    private static final String COL_NAME_SCRIPT_COUNT = "_count";
    private static final String COL_NAME_SCRIPT_NAME = "_script_name";
    private static final String COL_NAME_SCRIPT_DURATION = "_script_duration";
    private static final String COL_NAME_SCRIPT_TYPE = "_script_type";
    public PlanInfoDao(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        StringBuilder createSqlBuilder = new StringBuilder();
        createSqlBuilder.append("create table ").append(TABLE_NAME)
                .append("(")
                .append(COL_NAME_ID).append(" integer primary key autoincrement, ")
                .append(COL_NAME_TS).append(" long, ")
                .append(COL_NAME_NAME).append(" text, ")
                .append(COL_NAME_STATUS).append(" integer, ")
                .append(COL_NAME_SCRIPT_TOTAL_COUNT).append(" integer, ")
                .append(COL_NAME_SCRIPT_COUNT).append(" integer, ")
                .append(COL_NAME_SCRIPT_NAME).append(" text, ")
                .append(COL_NAME_SCRIPT_TYPE).append(" integer, ")
                .append(COL_NAME_SCRIPT_DURATION).append(" integer)");
        db.execSQL(createSqlBuilder.toString());
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("drop table if exists " + TABLE_NAME);
    }

    public void insert(PlanInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder insertSqlBuilder = new StringBuilder();
        insertSqlBuilder.append("insert into ").append(TABLE_NAME)
                .append("(")
                .append(COL_NAME_TS).append(", ")
                .append(COL_NAME_NAME).append(", ")
                .append(COL_NAME_STATUS).append(", ")
                .append(COL_NAME_SCRIPT_TOTAL_COUNT).append(", ")
                .append(COL_NAME_SCRIPT_COUNT).append(", ")
                .append(COL_NAME_SCRIPT_NAME).append(", ")
                .append(COL_NAME_SCRIPT_TYPE).append(", ")
                .append(COL_NAME_SCRIPT_DURATION)
                .append(")")
                .append(" values(?, ?, ?, ?, ? ,?, ?, ?)");
        db.execSQL(insertSqlBuilder.toString(),
                new Object[] { info.getTs(), info.getName(), info.getStatus(),
                        info.getTotalCount(), info.getCount(), info.getScript().getScriptName(),
                        info.getScript().getType(), info.getScript().getTime()});
    }

    public void delete(PlanInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder deleteSqlBuilder = new StringBuilder();
        deleteSqlBuilder.append("delete from ").append(TABLE_NAME)
                .append(" where ")
                .append(COL_NAME_TS).append(" = ?");
        db.execSQL(deleteSqlBuilder.toString(),
                new Object[] { info.getTs() });
    }

    public void delete(long ts) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder deleteSqlBuilder = new StringBuilder();
        deleteSqlBuilder.append("delete from ").append(TABLE_NAME)
                .append(" where ")
                .append(COL_NAME_TS).append(" = ?");
        db.execSQL(deleteSqlBuilder.toString(),
                new Object[] { ts });
    }

    public void update(PlanInfo info) {
        SQLiteDatabase db = getWritableDatabase();
        StringBuilder updateSqlBuilder = new StringBuilder();
        updateSqlBuilder.append("update ").append(TABLE_NAME)
                .append(" set ")
                .append(COL_NAME_NAME).append(" = ?, ")
                .append(COL_NAME_STATUS).append(" = ?, ")
                .append(COL_NAME_SCRIPT_TOTAL_COUNT).append(" = ?, ")
                .append(COL_NAME_SCRIPT_COUNT).append(" = ?, ")
                .append(COL_NAME_SCRIPT_NAME).append(" = ?, ")
                .append(COL_NAME_SCRIPT_TYPE).append(" = ?, ")
                .append(COL_NAME_SCRIPT_DURATION).append(" = ?")
                .append(" where ")
                .append(COL_NAME_TS).append(" = ?");
        db.execSQL(updateSqlBuilder.toString(),
                new Object[] {
                        info.getName(), info.getStatus(),
                        info.getTotalCount(), info.getCount(), info.getScript().getScriptName(),
                        info.getScript().getType(), info.getScript().getTime(), info.getTs()
                });
    }

    public List<PlanInfo> getPlans() {
        List<PlanInfo> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, new String[]{});
        loadPlansFromCursor(list, cursor);
        Collections.sort(list);
        return list;
    }

    private void loadPlansFromCursor(List<PlanInfo> list, Cursor cursor) {
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COL_NAME_NAME));
            int count = cursor.getInt(cursor.getColumnIndex(COL_NAME_SCRIPT_COUNT));
            int total = cursor.getInt(cursor.getColumnIndex(COL_NAME_SCRIPT_TOTAL_COUNT));
            int status = cursor.getInt(cursor.getColumnIndex(COL_NAME_STATUS));
            long ts = cursor.getLong(cursor.getColumnIndex(COL_NAME_TS));
            int type = cursor.getInt(cursor.getColumnIndex(COL_NAME_SCRIPT_TYPE));
            String scriptName = cursor.getString(cursor.getColumnIndex(COL_NAME_SCRIPT_NAME));
            int duration = cursor.getInt(cursor.getColumnIndex(COL_NAME_SCRIPT_DURATION));
            Script script = new Script(scriptName, duration, type);
            list.add(new PlanInfo(name, ts, script, total, count, status));
        }
    }

}
