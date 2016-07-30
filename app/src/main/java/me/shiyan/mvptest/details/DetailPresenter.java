package me.shiyan.mvptest.details;

import android.support.annotation.NonNull;
import android.util.Log;

import me.shiyan.mvptest.data.BooksDataSource;
import me.shiyan.mvptest.data.BooksRepo;
import me.shiyan.mvptest.data.models.BookDetail;

/**
 * Created by shiyan on 2016/7/30.
 */
public class DetailPresenter implements DetailContract.Presenter {

    private String TAG = getClass().getName();

    private final BooksRepo mBooksRepo;

    private final DetailContract.View mBooksView;

    private final int bookId;

    public DetailPresenter(int bookId,BooksRepo mBooksRepo, DetailContract.View mBooksView) {
        this.bookId = bookId;
        this.mBooksRepo = mBooksRepo;
        this.mBooksView = mBooksView;

        mBooksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadBookById();
    }

    @Override
    public void loadBookById() {
        mBooksRepo.getBook(bookId, new BooksDataSource.GetBookCallBack() {
            @Override
            public void onBookLoaded(BookDetail bookDetail) {
                processBookDetail(bookDetail);
            }

            @Override
            public void onDataNotAvailable() {
                mBooksView.showNoBookDetail();
            }
        });
    }

    private void processBookDetail(BookDetail bookDetail){

        if (bookDetail != null) {
            mBooksView.showBookDetail(bookDetail);
        }else{
            mBooksView.showNoBookDetail();
        }
    }
}
