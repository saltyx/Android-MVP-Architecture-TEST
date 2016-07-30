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

    public static boolean mFirstLoad = true;

    private String mCurrentClassify = "";

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
    public String getCurrentClassify() {
        return mCurrentClassify;
    }

    @Override
    public void setCurrentClassify(@NonNull String classifyName) {
        this.mCurrentClassify = classifyName;
        loadBooksByClassifyName(classifyName);
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
                mBooksView.showNoBooks();
            }
        });
    }

    @Override
    public void loadBooksClassifies(boolean forceUpdate) {

        Log.d(TAG,String.valueOf(mFirstLoad));
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
                mBooksView.showNoClassfiy();
            }
        });

        mFirstLoad = false;
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
            mBooksView.showBooks(bookDetails);
        }
    }

    @Override
    public void loadBooks(boolean forceUprdate) {

    }

}
