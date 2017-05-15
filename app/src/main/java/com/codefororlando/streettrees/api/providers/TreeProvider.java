// Copyright © 2017 Code for Orlando.
// 
// MIT License
// 
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.codefororlando.streettrees.api.providers;

import android.content.Context;

import com.codefororlando.streettrees.api.TreeAPIService;
import com.codefororlando.streettrees.api.deserializer.LatLngDeserializer;
import com.codefororlando.streettrees.api.models.Tree;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func0;

public class TreeProvider implements TreeProviderInterface {
    private static final String BASE_URL = "https://brigades.opendatanetwork.com/resource/";
    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 30;
    private static final int READ_TIMEOUT = 30;
    private static final long CACHE_SIZE = 4 * 1024 * 1024; // 10 MB

    private final TreeAPIService service;

    public TreeProvider(Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "cached_responses");
        Cache cache = new Cache(httpCacheDirectory, CACHE_SIZE);
        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .registerTypeAdapter(LatLng.class, new LatLngDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(client)
                .build();

        service = retrofit.create(TreeAPIService.class);
    }

    public void getTrees(final TreeProviderResultHandler<Tree> handler) {
        Call<List<Tree>> getTreesTransaction = service.getTrees();
        getTreesTransaction.enqueue(new Callback<List<Tree>>() {
            @Override
            public void onResponse(Call<List<Tree>> call, Response<List<Tree>> response) {
                List<Tree> trees = response.body();
                handler.onComplete(true, trees);
            }

            @Override
            public void onFailure(Call<List<Tree>> call, Throwable t) {
                handler.onComplete(false, null);
            }
        });
    }

    private List<Tree> getTrees() throws IOException {
        Call<List<Tree>> getTreesTransaction = service.getTrees();
        Response<List<Tree>> treeResponse = null;
        treeResponse = getTreesTransaction.execute();
        if (treeResponse.body() == null) {
            return null;
        }
        return treeResponse.body();
    }

    public Observable<List<Tree>> getTreesObservable() {
        return Observable.defer(new Func0<Observable<List<Tree>>>() {
            @Override
            public Observable<List<Tree>> call() {
                try {
                    return Observable.just(getTrees());
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }


        });
    }
}
