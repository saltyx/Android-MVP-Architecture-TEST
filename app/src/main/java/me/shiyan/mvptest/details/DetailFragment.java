package me.shiyan.mvptest.details;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import me.shiyan.mvptest.R;
import me.shiyan.mvptest.books.BooksContract;
import me.shiyan.mvptest.data.models.BookDetail;
import me.shiyan.mvptest.utils.HttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment implements DetailContract.View {

    private String TAG = getClass().getName();

    private DetailContract.Presenter mPresenter;

    private TextView mBookName;

    private TextView mBookAuthor;

    private ImageView mBookImg;

    private TextView mBookSummary;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(){
        return new DetailFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showBookDetail(@NonNull BookDetail bookDetail) {
        mBookName.setText(bookDetail.getName());
        mBookAuthor.setText(bookDetail.getAuthor());
        mBookSummary.setText(bookDetail.getSummary());
        getBookImg(bookDetail.getImg());
    }

    @Override
    public void showNoBookDetail() {

    }

    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_detail, container, false);

        mBookName = (TextView) root.findViewById(R.id.bookName);
        mBookAuthor = (TextView) root.findViewById(R.id.bookAuthor);
        mBookSummary = (TextView) root.findViewById(R.id.bookSummary);

        mBookImg = (ImageView) root.findViewById(R.id.bookImg);
        showNoBookImg();

        return root;
    }

    private void showNoBookImg(){

        mBookImg.setImageResource(R.drawable.ic_error_outline_black_48dp);

    }

    private void getBookImg(String subUrl){

        HttpUtils.getBookImg(subUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                showNoBookImg();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()){
                    InputStream inputStream = response.body().byteStream();
                    final Bitmap img = BitmapFactory.decodeStream(inputStream);
                    mBookImg.post(new Runnable() {
                        @Override
                        public void run() {
                            mBookImg.setImageBitmap(img);
                        }
                    });

                }else{
                    showNoBookImg();
                }
            }
        });
    }

}
