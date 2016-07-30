package me.shiyan.mvptest.books;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import me.shiyan.mvptest.data.BooksDataSource;
import me.shiyan.mvptest.data.BooksRepo;
import me.shiyan.mvptest.data.models.BookDetail;
import me.shiyan.mvptest.data.models.BooksClassify;

/**
 * Created by shiyan on 2016/7/26.
 */
public class BooksPresenter implements BooksContract.Presenter {

    private String TAG = getClass().getName();

    private final BooksRepo mBooksRepo;

    private final BooksContract.View mBooksView;

    public boolean mFirstLoad = true;

    public BooksPresenter(@NonNull BooksRepo mBooksRepo,@NonNull BooksContract.View mBooksView) {
        this.mBooksRepo = mBooksRepo;
        this.mBooksView = mBooksView;

        mBooksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadBooksClassifies(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadBooksByClassifyName(String classifyName) {
        mBooksRepo.getBooksByClassifyName(classifyName, new BooksDataSource.LoadBooksCallBack() {
            @Override
            public void onBooksLoaded(ArrayList<BookDetail> bookDetails) {
                processBooks(bookDetails);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    @Override
    public void loadBooksClassifies(boolean forceUpdate) {
        if (forceUpdate || mFirstLoad){
            mBooksRepo.freshClassify();
            mFirstLoad = false;
        }
        mBooksRepo.getBooksClassifies(new BooksDataSource.LoadBooksClassifiesCallBack() {
            @Override
            public void onBooksClassifiesLoaded(ArrayList<BooksClassify> booksClassifies) {
                processBooksClassifies(booksClassifies);
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    private void processBooksClassifies(ArrayList<BooksClassify> booksClassifies){

        if (booksClassifies.isEmpty()){
            mBooksView.showNoClassfiy();
        }else{
            mBooksView.showClassfies(booksClassifies);
        }
    }

    private void processBooks(ArrayList<BookDetail> bookDetails){

        if (bookDetails.isEmpty()){
            mBooksView.showNoBooks();
        }else{
            Log.d(TAG,bookDetails.toString());
            mBooksView.showBooks(bookDetails);
        }
    }

    @Override
    public void loadBooks(boolean forceUprdate) {

    }

}
