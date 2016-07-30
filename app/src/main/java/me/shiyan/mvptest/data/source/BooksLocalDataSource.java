package me.shiyan.mvptest.data.source;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import me.shiyan.mvptest.data.BooksDataSource;
import me.shiyan.mvptest.data.models.BookDetail;
import me.shiyan.mvptest.data.models.BooksClassify;

/**
 * Created by shiyan on 2016/7/26.
 */
public class BooksLocalDataSource implements me.shiyan.mvptest.data.BooksDataSource {

    private String TAG = getClass().getName();

    private static BooksLocalDataSource INSTANCE ;//存在与整个生命周期中

    private RealmHelper mRealmHelper;

    private BooksLocalDataSource(@NonNull Application context){
        mRealmHelper = new RealmHelper(context);
    }

    public static BooksLocalDataSource getInstance(@NonNull Application context){
        if (INSTANCE == null){
            INSTANCE = new BooksLocalDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getAllBooks(@NonNull LoadBooksCallBack callBack) {
        Realm realm = mRealmHelper.getRealm();
        RealmResults<BookDetail> results = realm.where(BookDetail.class)
                                                .findAll();

        if (results.isEmpty()){
            callBack.onDataNotAvailable();
        }else{
            callBack.onBooksLoaded(convertBookRealmResultToArrayList(results));
        }
        realm.close();
    }

    @Override
    public void getBooksByClassifyName(@NonNull String classifyName,LoadBooksCallBack callback) {
        Realm realm = mRealmHelper.getRealm();
        BooksClassify result = realm.where(BooksClassify.class)
                .equalTo("name",classifyName)
                .findFirst();
        if (result != null ){
            RealmResults<BookDetail> books = realm.where(BookDetail.class)
                    .equalTo("bookclass",result.getId())
                    .findAll();
            Log.d(TAG,String.valueOf(books.size()));
            if (books.size() == 0){
                callback.onDataNotAvailable();
            }else {
                callback.onBooksLoaded(convertBookRealmResultToArrayList(books));
            }
        }

        realm.close();
    }

    @Override
    /*根据ID查找书
    * */
    public void getBook(@NonNull int bookId, @NonNull GetBookCallBack callBack) {
        Realm realm = mRealmHelper.getRealm();
        BookDetail book = realm.where(BookDetail.class)
                    .equalTo("id",bookId)
                    .findFirst();
        if (book.getId() != bookId){
            callBack.onDataNotAvailable();
        }else {
            callBack.onBookLoaded(book);
        }

        realm.close();
    }

    @Override
    public void getBooksClassifies(@NonNull LoadBooksClassifiesCallBack callBack) {

    }

    @Override
    public void getBooksClassify(@NonNull int classId, @NonNull GetBooksClassifyCallBack callBack) {

    }

    @Override
    public void saveAllBooksClassifies(@NonNull JSONArray bookClassifies) {
        Realm realm = mRealmHelper.getRealm();
        realm.beginTransaction();
        try {
            realm.createOrUpdateAllFromJson(BooksClassify.class,bookClassifies);
            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
        }

        realm.close();
    }

    @Override
    public void saveBooks(@NonNull JSONArray books) {
        Realm realm = mRealmHelper.getRealm();
        realm.beginTransaction();
        try {
            realm.createOrUpdateAllFromJson(BookDetail.class,books);
            realm.commitTransaction();
        }catch (Exception e){
            e.printStackTrace();
            realm.cancelTransaction();
        }

        realm.close();
    }

    @Override
    public void freshBooks() {

    }

    @Override
    public void freshClassify() {

    }

    @Override
    public void deleteBooks(@NonNull int bookId) {
        Realm realm = mRealmHelper.getRealm();
        final BookDetail book = realm.where(BookDetail.class)
                                .findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm){
                book.deleteFromRealm();
            }
        });

        realm.close();
    }

    @Override
    public void deleteAllBooks() {

    }

    @Override
    public void deleteBooksClassify(@NonNull int classId) {

    }

    @Override
    public void deleteAllClassifies() {

    }

    private ArrayList convertBookRealmResultToArrayList(RealmResults<BookDetail> result){
        ArrayList array = new ArrayList();
        for (BookDetail detail: result
             ) {
            array.add(detail);
        }

        return array;
    }

    private ArrayList convertClassifyRealmResultToArrayList(RealmResults<BooksClassify> result){
        ArrayList array = new ArrayList();
        for (BooksClassify classify: result
                ) {
            array.add(classify);
        }
        return array;
    }
}
