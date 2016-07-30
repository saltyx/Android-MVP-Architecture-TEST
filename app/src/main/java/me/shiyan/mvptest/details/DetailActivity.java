package me.shiyan.mvptest.details;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import me.shiyan.mvptest.R;
import me.shiyan.mvptest.books.BooksPresenter;
import me.shiyan.mvptest.data.BooksRepo;
import me.shiyan.mvptest.data.source.BooksLocalDataSource;
import me.shiyan.mvptest.utils.ActivityUtils;

public class DetailActivity extends AppCompatActivity {

    private DetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(intent.getStringExtra("BOOK_NAME"));
        ab.setDisplayHomeAsUpEnabled(true);
        DetailFragment detailFragment =
                (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.bookDetail);

        if (detailFragment == null) {
            detailFragment = DetailFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),detailFragment,R.id.bookDetail);
        }

        //setup presenter
        mPresenter = new DetailPresenter(
                intent.getIntExtra("BOOK_ID",0)
                ,BooksRepo.getInstance(BooksLocalDataSource.getInstance(getApplication()))
                ,detailFragment);

    }

    @Override
    /*为了返回保存状态*/
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
