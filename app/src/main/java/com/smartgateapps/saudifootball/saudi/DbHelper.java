package com.smartgateapps.saudifootball.saudi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.smartgateapps.saudifootball.model.LeaguNews;
import com.smartgateapps.saudifootball.model.Legue;
import com.smartgateapps.saudifootball.model.Match;
import com.smartgateapps.saudifootball.model.News;
import com.smartgateapps.saudifootball.model.NewsNews;
import com.smartgateapps.saudifootball.model.Stage;
import com.smartgateapps.saudifootball.model.Team;
import com.smartgateapps.saudifootball.model.TeamLeague;
import com.smartgateapps.saudifootball.model.TeamNews;

/**
 * Created by Raafat on 22/12/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "MY_DB";
    public static final int DB_VERSION = 2;
    private Context ctx;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.ctx = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Legue.getCreateSql());
        db.execSQL(Team.getCreateSql());
        db.execSQL(TeamLeague.getCreateSql());
        db.execSQL(News.getCreateSql());
        db.execSQL(LeaguNews.getCreateSql());
        db.execSQL(NewsNews.getCreateSql());
        db.execSQL(TeamNews.getCreateSql());
        db.execSQL(Stage.getCreateSql());
        db.execSQL(Match.getCreateSql());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+Legue.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Team.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TeamLeague.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+News.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+LeaguNews.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TeamNews.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Stage.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Match.TABLE_NAME);

        this.onCreate(db);
    }
}
