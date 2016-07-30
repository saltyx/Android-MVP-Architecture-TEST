package me.shiyan.mvptest.books;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import me.shiyan.mvptest.BasePresenter;
import me.shiyan.mvptest.BaseView;
import me.shiyan.mvptest.data.models.BookDetail;
import me.shiyan.mvptest.data.models.BooksClassify;

/**
 * Created by shiyan on 2016/7/26.
 */
public interface BooksContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);

        void showBooks(ArrayList<BookDetail> bookDetails);

        void showClassfies(ArrayList<BooksClassify> booksClassifies);

        void showNoClassfiy();

        void showNoBooks();

    }

    interface Presenter extends BasePresenter{

        void result(int requestCode, int resultCode);

        void loadBooksClassifies(boolean forceUpdate);

        void loadBooks(boolean forceUprdate);

        void loadBooksByClassifyName(String classifyName);

        void setCurrentClassify(@NonNull String classifyName);

        String getCurrentClassify();

    }

}
