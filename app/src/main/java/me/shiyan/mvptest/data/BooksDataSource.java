package me.shiyan.mvptest.data;

import android.support.annotation.NonNull;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import me.shiyan.mvptest.data.models.BooksClassify;
import me.shiyan.mvptest.data.models.BookDetail;

/**
 * Created by shiyan on 2016/7/26.
 */
public interface BooksDataSource {

    interface LoadBooksCallBack{
        //加载图书后回调
        void onBooksLoaded(ArrayList<BookDetail> bookDetails);

        void onDataNotAvailable();
    }

    interface GetBookCallBack{

        void onBookLoaded(BookDetail bookDetail);

        void onDataNotAvailable();
    }

    interface LoadBooksClassifiesCallBack{
        //加载所有分类后回调
        void onBooksClassifiesLoaded(ArrayList<BooksClassify> booksClassifies);

        void onDataNotAvailable();
    }

    interface GetBooksClassifyCallBack{

        void onBooksLoaded(BooksClassify booksClassify);

        void onDataNotAvailable();
    }

    void getAllBooks(@NonNull LoadBooksCallBack callBack);

    void getBook(@NonNull int bookId, @NonNull GetBookCallBack callBack);

    void getBooksByClassifyName(@NonNull String classifyName,LoadBooksCallBack callBack);

    void getBooksClassifies(@NonNull LoadBooksClassifiesCallBack callBack);

    void getBooksClassify(@NonNull int classId, @NonNull GetBooksClassifyCallBack callBack);

    void deleteBooks(@NonNull int bookId);

    void deleteAllBooks();

    void deleteBooksClassify(@NonNull int classId);

    void deleteAllClassifies();

    void saveAllBooksClassifies(@NonNull JSONArray bookClassifies);

    void saveBooks(@NonNull JSONArray books);

    void freshBooks();

    void freshClassify();
}
