package me.shiyan.mvptest.books;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.shiyan.mvptest.R;
import me.shiyan.mvptest.data.models.BookDetail;
import me.shiyan.mvptest.data.models.BooksClassify;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment implements BooksContract.View, BooksContract.BooksClassifyOnItemClickListener{

    private String TAG = getClass().getName();

    private BooksContract.Presenter mPresenter;

    private BooksAdapter mListAdapter;

    private TextView mNoBooksViewMain;

    private ImageView mNoBooksIcon;

    private LinearLayout mBooksView;

    private View mNoBooksView;

    private ListView listView;

    public BooksFragment() {
        // Required empty public constructor

    }

    public static BooksFragment newInstance() {
        return new BooksFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showClassfies(ArrayList<BooksClassify> booksClassifies) {

        ((BooksActivity)getActivity()).setClassifyNav(booksClassifies);

    }

    @Override
    public void onClassifyItemClick(String title) {
        mPresenter.loadBooksByClassifyName(title);
        //Toast.makeText(getContext(),title,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoClassfiy() {

    }

    @Override
    public void showNoBooks() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_books, container, false);

        mListAdapter = new BooksAdapter(new ArrayList<BookDetail>(),new BooksItemListener(){
            @Override
            public void onBooksClick(BookDetail book) {
                Toast.makeText(getContext(),book.getName(),Toast.LENGTH_SHORT).show();
            }
        });

        listView = (ListView) root.findViewById(R.id.listview_books);
        listView.setAdapter(mListAdapter);

        mBooksView = (LinearLayout) root.findViewById(R.id.booksClassifyAll);

        //when no mBook
        mNoBooksIcon = (ImageView) root.findViewById(R.id.noBooksClassifyIcon);
        mNoBooksViewMain = (TextView) root.findViewById(R.id.noBooksClassifyMain);
        mNoBooksView = root.findViewById(R.id.booksClassifyAll);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.books_fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.fresh:
                mPresenter.loadBooksClassifies(false);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void setPresenter(@NonNull BooksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showBooks(ArrayList<BookDetail> bookDetails) {

        mListAdapter.replaceData(bookDetails);
        mBooksView.setVisibility(View.VISIBLE);
        mNoBooksView.setVisibility(View.VISIBLE);
        listView.setAdapter(mListAdapter);
    }


    //todo listItem的样式
    private static class BooksAdapter extends BaseAdapter{

        private ArrayList<BookDetail> mBook;
        private BooksItemListener mListener;

        public BooksAdapter(ArrayList<BookDetail> mBook, BooksItemListener mListener) {
            this.mBook = mBook;
            this.mListener = mListener;
        }

        @Override
        public int getCount() {
            return mBook.size();
        }

        @Override
        public BookDetail getItem(int position) {
            return mBook.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setArrayList(@NonNull ArrayList<BookDetail> array){
            mBook = array;
        }

        public void replaceData(ArrayList<BookDetail> array){
            setArrayList(array);
            this.notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            if (rowView == null){
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                rowView = inflater.inflate(R.layout.book_item,parent,false);
            }

            final BookDetail bookDetail = getItem(position);

            TextView title = (TextView) rowView.findViewById(R.id.bookName);
            title.setText(bookDetail.getName());
            TextView author = (TextView) rowView.findViewById(R.id.bookAuthor);
            author.setText(bookDetail.getAuthor());

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onBooksClick(bookDetail);
                }
            });

            return rowView;
        }
    }

    public interface BooksItemListener{

        public void onBooksClick(BookDetail book);

    }
}
