package com.boll.neworientalname.retrofit;


import android.util.Log;

import com.boll.neworientalname.response.CategoryBean;
import com.boll.neworientalname.response.ThreeChildBean;
import com.boll.neworientalname.response.TowChildBean;

import java.net.URI;
import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Url;
import rx.Observable;
import rx.functions.Func1;

/**
 * 网络请求
 *
 * @author Created by zoro on 2021/02/02.
 */
public class VideoDataLoader extends BaseLoader {

    private static final String TAG = "VideoDataLoader";

    private VideoDataService mVideoDataService;

    private static class SingletonHolder {
        private static final VideoDataLoader INSTANCE = new VideoDataLoader();
    }

    public static VideoDataLoader getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private VideoDataLoader() {
        mVideoDataService = RetrofitServiceManager.getInstance().create(VideoDataService.class);
    }

    /**
     * 获取所有类目接口地址
     *
     * @return
     */
    public Observable<List<CategoryBean>> getAllCategory() {
        return observe(mVideoDataService.getAllCategory()
                .map(new Func1<List<CategoryBean>, List<CategoryBean>>() {
                    @Override
                    public List<CategoryBean> call(List<CategoryBean> data) {
                        return data;
                    }
                }));
    }

    /**
     * 获取学前、分级、高中等类目的数据（两层数据结构）
     *
     * @return
     */
    public Observable<List<TowChildBean>> getTowChildBean(String url) {
        Log.e(TAG,"url==: "+ url);
        return observe(mVideoDataService.getTowChildBean(url)
                .map(new Func1<List<TowChildBean>, List<TowChildBean>>() {
                    @Override
                    public List<TowChildBean> call(List<TowChildBean> data) {
                        return data;
                    }
                })
        );
    }

    /**
     * 获取小学、初中等类目的数据（三层数据结构）
     *
     * @return
     */
    public Observable<List<ThreeChildBean>> getThreeChildBean(String url) {
        return observe(mVideoDataService.getThreeChildBean(url)
                .map(new Func1<List<ThreeChildBean>, List<ThreeChildBean>>() {
                    @Override
                    public List<ThreeChildBean> call(List<ThreeChildBean> data) {
                        return data;
                    }
                }));
    }

    public interface VideoDataService {

        /**
         * 获取所有类目接口地址
         *
         * @return
         */
        @GET("ytj/一体机_json.txt")
        Observable<List<CategoryBean>> getAllCategory();


        /**
         * 获取学前、分级、高中等类目的数据（两层数据结构）
         *
         * @return
         */
        @GET
        Observable<List<TowChildBean>> getTowChildBean(@Url String url);

        /**
         * 获取小学、初中等类目的数据（三层数据结构）
         *
         * @return
         */
        @GET
        Observable<List<ThreeChildBean>> getThreeChildBean(@Url String url);

    }

}