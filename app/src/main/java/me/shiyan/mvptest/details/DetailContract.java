package me.shiyan.mvptest.details;

import android.support.annotation.NonNull;

import me.shiyan.mvptest.BasePresenter;
import me.shiyan.mvptest.BaseView;
import me.shiyan.mvptest.books.BooksContract;
import me.shiyan.mvptest.data.models.BookDetail;

/**
 * Created by shiyan on 2016/7/30.
 */
public interface DetailContract {

    interface View extends BaseView<DetailContract.Presenter>{

        void showBookDetail(@NonNull BookDetail bookDetail);

        void showNoBookDetail();
    }

    interface Presenter extends BasePresenter{

        void loadBookById();
    }
}
