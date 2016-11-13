package com.project.scafold.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


/**
 * Created by jayan on 11/6/2016.
 */

public class DaoHelper {

    private static SQLiteDatabase DB;
//    private static UsersDao DAO_USER;

    public static void initialize(Context context) {
//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "scafold-db-v1", null);
//        DB = helper.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(DB);
//        DaoSession daoSession = daoMaster.newSession();
//        DAO_USER = daoSession.getUsersDao();
    }


//    /********USER********/
//    public static boolean isUserExisting(long id) {
//        return DAO_USER.queryBuilder().where(UsersDao.Properties.Id
//                .eq(id)).limit(1).count() > 0;
//    }
//    public static void addUser(Users user) {
//        DAO_USER.insert(user);
//    }
//    public static void updateUser(Users user) {
//        DAO_USER.update(user);
//    }
//    public static void deleteUser(Users user) {
//        DAO_USER.delete(user);
//    }

}

