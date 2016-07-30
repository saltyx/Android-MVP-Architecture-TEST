package me.shiyan.mvptest.books;


import android.content.Context;
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
import me.shiyan.mvptest.base.BaseDialog;
import me.shiyan.mvptest.data.models.BookDetail;
import me.shiyan.mvptest.data.models.BooksClassify;
import me.shiyan.mvptest.details.DetailActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BooksFragment extends Fragment implements BooksContract.View {

    private String TAG = getClass().getName();

    private BooksContract.Presenter mPresenter;

    private BooksAdapter mListAdapter;

    private LinearLayout mBooksView;

    private LinearLayout mNoBooksView;

    private ListView listView;

    public BooksFragment() {

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

        if (getActivity() != null)
            ((BooksActivity)getActivity()).setClassifyNav(booksClassifies);

    }


    @Override
    public void showNoClassfiy() {

        if (getActivity() != null){
            ((BooksActivity)getActivity()).setNoClassifyNav();
        }
    }

    @Override
    public void showNoBooks() {

        mNoBooksView.setVisibility(View.VISIBLE);
        mBooksView.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_books, container, false);

        mListAdapter = new BooksAdapter(new ArrayList<BookDetail>(),new BooksItemListener(){
            @Override
            public void onBooksClick(BookDetail book) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("BOOK_ID",book.getId());
                intent.putExtra("BOOK_NAME",book.getName());
                startActivity(intent);
            }
        });

        listView = (ListView) root.findViewById(R.id.listview_books);
        listView.setAdapter(mListAdapter);

        mBooksView = (LinearLayout) root.findViewById(R.id.booksClassifyAll);

        //when no mBook
        mNoBooksView = (LinearLayout) root.findViewById(R.id.noBooksClassify);

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
                Toast.makeText(getContext(),"Cache刷新",Toast.LENGTH_SHORT).show();
                break;
            case R.id.about:
                showAbout();
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

    private void showAbout(){
        BaseDialog about = new BaseDialog(getContext(),R.style.BaseDailog);
        about.setLayoutContent(R.layout.about_layout);
        about.show();
    }

    @Override
    public void showBooks(ArrayList<BookDetail> bookDetails) {

        mListAdapter.replaceData(bookDetails);
        mBooksView.setVisibility(View.VISIBLE);
        mNoBooksView.setVisibility(View.GONE);
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
