package com.wfx.autorunner.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.wfx.autorunner.core.PlanInfo;

import java.util.LinkedList;
import java.util.List;

public class PlanInfoDao extends AbstractDao {
    private static final String TABLE_NAME = "PlanInfo";

    private static final String COL_NAME_ID = "_id";
    private static final String COL_NAME_NAME = "_name";
    private static final String COL_NAME_PATH = "_path";
    private static final String COL_NAME_TS = "_ts";
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
                .append(COL_NAME_PATH).append(" text)");
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
                .append(COL_NAME_PATH)
                .append(")")
                .append(" values(?, ?, ?)");
        db.execSQL(insertSqlBuilder.toString(),
                new Object[] { info.getTs(), info.getName(), info.getPath() });
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
                .append(COL_NAME_PATH).append(" = ?")
                .append(" where ")
                .append(COL_NAME_TS).append(" = ?");
        db.execSQL(updateSqlBuilder.toString(),
                new Object[] {
                        info.getName(), info.getPath(), info.getTs()
                });
    }

    public List<PlanInfo> getPlans() {
        List<PlanInfo> list = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, new String[]{});
        createDownloadTasks(list, cursor);
        return list;
    }

    private void createDownloadTasks(List<PlanInfo> list, Cursor cursor) {
        while (cursor.moveToNext()) {
            String name =  cursor.getString(cursor.getColumnIndex(COL_NAME_NAME));
            String path =  cursor.getString(cursor.getColumnIndex(COL_NAME_PATH));
            long ts = cursor.getLong(cursor.getColumnIndex(COL_NAME_PATH));
            list.add(new PlanInfo(name, path, ts));
        }
    }

}
