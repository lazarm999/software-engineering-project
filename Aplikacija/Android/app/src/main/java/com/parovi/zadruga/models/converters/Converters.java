package com.parovi.zadruga.models.converters;

import androidx.room.TypeConverter;

import com.parovi.zadruga.Utility;
import com.parovi.zadruga.models.entityModels.Chat;

import java.util.Date;

public class Converters {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Utility.ChatType toChatType(String value){
        if(value.equals(Utility.ChatType.PRIVATE.toString()))
            return Utility.ChatType.PRIVATE;
        if(value.equals(Utility.ChatType.GROUP.toString()))
            return Utility.ChatType.GROUP;
        return null;
    }

    @TypeConverter
    public static String fromChatType(Utility.ChatType chatType){
        return chatType == null ? null : chatType.toString();
    }
}
