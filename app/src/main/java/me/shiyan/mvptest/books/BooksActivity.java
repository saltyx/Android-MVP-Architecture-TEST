package me.shiyan.mvptest.books;

import android.content.ClipData;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.shiyan.mvptest.R;
import me.shiyan.mvptest.data.BooksRepo;
import me.shiyan.mvptest.data.models.BooksClassify;
import me.shiyan.mvptest.data.source.BooksLocalDataSource;
import me.shiyan.mvptest.utils.ActivityUtils;

public class BooksActivity extends AppCompatActivity {

    private static final String CURRENT_BOOK_CLASSIFY = "CURRENT_BOOK_CLASSIFY";

    private DrawerLayout mDrawerLayout;

    private BooksPresenter mPresenter;

    private ListView mClassifyListView;

    private BooksContract.BooksClassifyOnItemClickListener mOnItemClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.app_name);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        BooksFragment booksFragment =
                (BooksFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (booksFragment == null){
            booksFragment = BooksFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), booksFragment,R.id.contentFrame);
            mOnItemClick = (BooksContract.BooksClassifyOnItemClickListener)booksFragment;
        }
        //setup presenter
        mPresenter = new BooksPresenter(
                BooksRepo.getInstance(BooksLocalDataSource.getInstance(getApplication()))
                ,booksFragment);

        // Set up the navigation drawer.

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        if (savedInstanceState != null){

        }
    }

    public void setupDrawerContent(NavigationView navigationView){
        mPresenter.loadBooksClassifies(false);
    }

    public void setClassifyNav(final ArrayList<BooksClassify> booksClassifies) {
        BooksActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mClassifyListView = (ListView)findViewById(R.id.classifies);
                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(BooksActivity.this,android.R.layout.simple_list_item_activated_1);
                for (BooksClassify classify: booksClassifies) {
                    adapter.add(classify.getName());
                }
                mClassifyListView.setAdapter(adapter);
                mClassifyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mOnItemClick.onClassifyItemClick(adapter.getItem(position));
                        //view.setChecked(true);
                        mDrawerLayout.closeDrawers();
                    }
                });
            }
        });

    }

    @Override
    //目前无想法
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_BOOK_CLASSIFY,"");

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
