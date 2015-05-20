package com.androidmagic.sqlite;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * @Author : rohit Jun 15, 2013 5:48:21 PM
 */
public class ResourceBasedSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = ResourceBasedSQLiteOpenHelper.class.getSimpleName();

    private final Context context;
    private final String mResourceName;

    public  ResourceBasedSQLiteOpenHelper(Context context, String name, int version, String mResourceName) {
        super(context, name, /*factory*/null, version);
        this.context = context;
        this.mResourceName = mResourceName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating DB " + db.getPath());

        char[] buf = new char[1024];
        try {
            InputStreamReader ir  = new InputStreamReader(context.getAssets().open(mResourceName));
            int read = ir.read(buf);
            StringBuilder sb = new StringBuilder();
            while (read != -1) {
                if (read != 0) {
                    sb.append(buf, 0, read);
                }
                read = ir.read(buf);
            }
            StringTokenizer st = new StringTokenizer(sb.toString(), ";");
            while (st.hasMoreTokens()) {
                String sql = st.nextToken();
                if (sql.contains("BEGIN")) {
                    while (st.hasMoreTokens() && !sql.contains("END")) {
                        sql += ';' + st.nextToken();
                    }
                }
                if (!sql.matches(";\\s*$")) {
                    sql += ";";
                }
                if (!sql.matches("^\\s*;")) {
                    db.execSQL(sql);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Error creating database " + db.getPath() + ":" + e.getMessage());
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}