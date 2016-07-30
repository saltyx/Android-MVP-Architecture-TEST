package me.shiyan.mvptest.data;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import io.realm.Realm;
import me.shiyan.mvptest.data.models.BookDetail;
import me.shiyan.mvptest.data.models.BooksClassify;
import me.shiyan.mvptest.data.source.BooksLocalDataSource;
import me.shiyan.mvptest.utils.HttpUtils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by shiyan on 2016/7/26.
 * 数据仓库，Cache , Local,Remote部分
 */
public class BooksRepo implements BooksDataSource {

    private String TAG = getClass().getName();

    private static BooksRepo INSTANCE = null;

    private final BooksLocalDataSource  mbooksLocalDataSource;

    //cache
    Map<Integer, BookDetail> mBooksCache;

    Map<Integer, BooksClassify> mBooksClassifyCache;

    boolean mBooksCacheIsDirty = false;

    boolean mClassifyCacheIsDirty = false;

    private BooksRepo(@NonNull BooksLocalDataSource booksLocalDataSource) {
        this.mbooksLocalDataSource = booksLocalDataSource;
    }

    public static BooksRepo getInstance(@NonNull BooksLocalDataSource booksLocalDataSource){
        if (INSTANCE == null){
            INSTANCE = new BooksRepo(booksLocalDataSource);
        }
        return INSTANCE;
    }

    public void destroyBooksRepo(){
        INSTANCE = null;
    }

    @Override
    public void getAllBooks(@NonNull final LoadBooksCallBack callBack) {

        if (!mBooksCacheIsDirty &&  mBooksCache != null){

            callBack.onBooksLoaded(new ArrayList<BookDetail>(mBooksCache.values()));
        }
        if (mBooksCacheIsDirty){
            //从网络刷新数据,每一个种类获取一页二十条数据
            getAllDataFromRemote(null,callBack);
        }else{
            mbooksLocalDataSource.getAllBooks(new LoadBooksCallBack() {
                @Override
                public void onBooksLoaded(ArrayList<BookDetail> bookDetails) {
                    freshBooksCache(bookDetails);
                    callBack.onBooksLoaded(new ArrayList<BookDetail>(mBooksCache.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    //从网络刷新数据
                    getAllDataFromRemote(null,callBack);
                }
            });
        }

    }

    @Override
    public void getBook(@NonNull int bookId, @NonNull final GetBookCallBack callBack) {

        mbooksLocalDataSource.getBook(bookId, new GetBookCallBack() {
            @Override
            public void onBookLoaded(BookDetail bookDetail) {
                callBack.onBookLoaded(bookDetail);
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getBooksByClassifyName(@NonNull String classifyName, final LoadBooksCallBack callBack) {

        mbooksLocalDataSource.getBooksByClassifyName(classifyName, new LoadBooksCallBack() {
            @Override
            public void onBooksLoaded(ArrayList<BookDetail> bookDetails) {
                callBack.onBooksLoaded(bookDetails);
            }

            @Override
            public void onDataNotAvailable() {
                callBack.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getBooksClassifies(@NonNull final LoadBooksClassifiesCallBack callBack) {

        if (!mClassifyCacheIsDirty &&  mBooksClassifyCache != null){
            callBack.onBooksClassifiesLoaded(new ArrayList<BooksClassify>(mBooksClassifyCache.values()));
        }
        if (mClassifyCacheIsDirty){
            //从网络刷新数据
            getAllDataFromRemote(callBack, null);
        }else{
            mbooksLocalDataSource.getBooksClassifies(new LoadBooksClassifiesCallBack() {
                @Override
                public void onBooksClassifiesLoaded(ArrayList<BooksClassify> booksClassifies) {
                    freshBookClassifyCache(booksClassifies);
                    callBack.onBooksClassifiesLoaded(new ArrayList<BooksClassify>(mBooksClassifyCache.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    //从网络刷新数据
                    getAllDataFromRemote(callBack,null);
                }
            });
        }
    }

    @Override
    public void getBooksClassify(@NonNull int classId, @NonNull GetBooksClassifyCallBack callBack) {

    }

    @Override
    public void deleteBooks(@NonNull int bookId) {

    }

    @Override
    public void deleteAllBooks() {

    }

    @Override
    public void deleteBooksClassify(@NonNull int classId) {

    }

    @Override
    public void deleteAllClassifies() {

    }

    @Override
    public void freshBooks() {
        mBooksCacheIsDirty = true;
    }

    @Override
    public void freshClassify() {
        mClassifyCacheIsDirty = true;
    }

    @Override
    public void saveAllBooksClassifies(@NonNull JSONArray bookClassifies) {
        mbooksLocalDataSource.saveAllBooksClassifies(bookClassifies);
    }

    @Override
    public void saveBooks(@NonNull JSONArray books) {
        mbooksLocalDataSource.saveBooks(books);
    }

    private void getAllDataFromRemote(final LoadBooksClassifiesCallBack callBack,final LoadBooksCallBack callBack1){
        try{
            HttpUtils.getBooksClassify(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()){
                        try{
                            String result = response.body().string();
                            JSONArray booksClassifies = new JSONObject(result).getJSONArray("tngou");
                            saveAllBooksClassifies(booksClassifies);
                            freshBookClassifyCache(jsonArrayToClassifyArrayList(booksClassifies));
                            if (callBack!=null){
                                callBack.onBooksClassifiesLoaded(jsonArrayToClassifyArrayList(new JSONObject(result).getJSONArray("tngou")));
                            }
                            for(int i=0 ; i< booksClassifies.length(); i++){
                                JSONObject object = booksClassifies.getJSONObject(i);
                                HttpUtils.getBooksDetail(object.getInt("id"), 1, 20, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        e.printStackTrace();
                                    }
                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {

                                        if (response.isSuccessful()) {
                                            try{
                                                String result = response.body().string();
                                                appendBooksCache(jsonArrayToBookArrayList(new JSONObject(result).getJSONArray("list")));
                                                saveBooks(new JSONObject(result).getJSONArray("list"));
                                                if (callBack1 != null){
                                                    callBack1.onBooksLoaded(jsonArrayToBookArrayList(new JSONObject(result).getJSONArray("list")));
                                                }
                                            }catch (JSONException e){
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                });
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }catch (IOException ioException){
            Log.d(TAG, "从网络获取分类失败\nIO_EXCEPTION:"+ioException.toString());
        }catch (JSONException jsonException){
            Log.d(TAG, "从网络获取分类失败\nJSON_EXCEPTION:"+jsonException.toString());
        }
    }


    public void freshBooksCache(ArrayList<BookDetail> arrayList){
        if (mBooksCache == null){
            mBooksCache = new LinkedHashMap<>();
        }
        mBooksCache.clear();
        for (BookDetail book: arrayList
             ) {
            mBooksCache.put(book.getId(), book);
        }
        mBooksCacheIsDirty = false;
    }

    public void appendBooksCache(ArrayList<BookDetail> arrayList){
        if (mBooksCache == null){
            mBooksCache = new LinkedHashMap<>();
        }
        for (BookDetail book: arrayList
                ) {
            mBooksCache.put(book.getId(), book);
        }
        mBooksCacheIsDirty = false;
    }

    public void freshBookClassifyCache(ArrayList<BooksClassify> arrayList){
        if (mBooksClassifyCache == null){
            mBooksClassifyCache = new LinkedHashMap<>();
        }
        mBooksClassifyCache.clear();
        for (BooksClassify classify: arrayList
             ) {
            mBooksClassifyCache.put(classify.getId(), classify);
        }
        mClassifyCacheIsDirty = false;
    }

    private ArrayList<BooksClassify> jsonArrayToClassifyArrayList(JSONArray array) throws JSONException{
        ArrayList<BooksClassify> result = new ArrayList<>();
        for(int index = 0 ; index< array.length() ; index++){
            JSONObject object = array.getJSONObject(index);
            BooksClassify classify = new BooksClassify();
            classify.setId(object.getInt("id"));
            classify.setDescription(object.getString("description"));
            classify.setName(object.getString("name"));
            classify.setKeywords(object.getString("keywords"));
            classify.setSeq(object.getInt("seq"));
            classify.setTitle(object.getString("title"));
            result.add(classify);
        }
        return result;
    }
    private ArrayList<BookDetail> jsonArrayToBookArrayList(JSONArray array) throws JSONException{
        ArrayList<BookDetail> result = new ArrayList<>();
        for(int index = 0 ; index< array.length() ; index++){
            JSONObject object = array.getJSONObject(index);
            BookDetail book = new BookDetail();
            book.setId(object.getInt("id"));
            book.setAuthor(object.getString("author"));
            book.setName(object.getString("name"));
            book.setBookclass(object.getInt("bookclass"));
            book.setImg(object.getString("img"));
            book.setTime(object.getInt("time"));
            result.add(book);
        }
        return result;
    }
}
