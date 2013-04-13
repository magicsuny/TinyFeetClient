package com.themis.tinyfeet.db.model;

import android.content.ContentValues;
import com.themis.tinyfeet.proto.Tfeet;

/**
 * Created with IntelliJ IDEA.
 * User: sunharuka
 * Date: 13-4-11
 * Time: 下午9:42
 * To change this template use File | Settings | File Templates.
 */
public final class TfeetColumn {
    public static final String ID = "TFID";
    public static final String UID = "UID";
    public static final String PIC_URL = "PIC_URL";
    public static final String TEXT = "TEXT";
    public static final String SEASON = "SEASON";
    public static final String DAYNIGHT = "DAYNIGHT";
    public static final String LIKE_COUNT = "LIKE_COUNT";
    public static final String COMMENT_COUNT = "COMMENT_COUNT";
    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";
    public static final String CREATED_AT = "CREATE_AT";

    public static ContentValues fillValues(Tfeet.TFeet tfeet){
        ContentValues values = new ContentValues();
        values.clear();
        values.put(ID,String.valueOf(tfeet.getTfid()));
        values.put(UID,String.valueOf(tfeet.getUser().getUid()));
        values.put(PIC_URL,String.valueOf(tfeet.getPicUrl()));
        values.put(TEXT,String.valueOf(tfeet.getText()));
        values.put(SEASON,String.valueOf(tfeet.getSeason().getNumber()));
        values.put(DAYNIGHT,String.valueOf(tfeet.getDaynight().toString()));
        values.put(LIKE_COUNT,String.valueOf(tfeet.getLikedCount()));
        values.put(COMMENT_COUNT,String.valueOf(tfeet.getCommentCount()));
        values.put(LATITUDE,String.valueOf(tfeet.getGeo().getLat()));
        values.put(LONGITUDE,String.valueOf(tfeet.getGeo().getLng()));
        values.put(CREATED_AT,String.valueOf(tfeet.getCreatedAt()));
        return values;

    }

}
