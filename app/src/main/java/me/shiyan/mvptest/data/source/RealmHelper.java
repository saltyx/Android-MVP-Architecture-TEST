package me.shiyan.mvptest.data.source;

import android.app.Application;
import android.support.annotation.NonNull;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import me.shiyan.mvptest.data.models.BooksClassify;
import me.shiyan.mvptest.data.models.BookDetail;

/**
 * Created by shiyan on 2016/7/26.
 */
public class RealmHelper {

    public static boolean DebugMode = true;

    private static RealmConfiguration CONFIG;

    public RealmHelper(@NonNull Application context){
        if (CONFIG == null){
            CONFIG = new RealmConfiguration.Builder(context)
                    .deleteRealmIfMigrationNeeded()
                    .build();
            Realm.setDefaultConfiguration(CONFIG);
        }
        InitialDB();//构造之后直接创建数据表
    }

    private void InitialDB(){
        Realm realm = getRealm();
        realm.beginTransaction();
        try{
            realm.deleteAll();
            realm.createObject(BooksClassify.class);
            realm.createObject(BookDetail.class);
            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
        }
    }

    public Realm getRealm(){
        return Realm.getDefaultInstance();
    }

}
