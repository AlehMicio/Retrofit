package android.retrofitdemo;

import android.app.Application;
import android.retrofitdemo.model.MovieApiResponse;
import android.retrofitdemo.model.Result;
import android.retrofitdemo.service.MovieApiService;
import android.retrofitdemo.service.RetrofitInstance;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Long, Result> {

    private MovieApiService movieApiService;
    private Application application;

    public MovieDataSource(MovieApiService movieApiService, Application application) {
        this.movieApiService = movieApiService;
        this.application = application;
    }

    @Override //Код для извлечения 1-ой страницы
    public void loadInitial(@NonNull LoadInitialParams<Long> loadInitialParams,
                            @NonNull LoadInitialCallback<Long, Result> loadInitialCallback) {

        movieApiService = RetrofitInstance.getService();
        Call<MovieApiResponse> call = movieApiService.
                getPopularMoviesWithPaging(application.getApplicationContext().getString(R.string.api_key), 1);
        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {
                MovieApiResponse movieApiResponse = response.body();
                ArrayList<Result> results =new ArrayList<>();

                if (movieApiResponse != null && movieApiResponse.getResults() != null){
                    results = (ArrayList<Result>) movieApiResponse.getResults();
                    loadInitialCallback.onResult(results, null, (long) 2);
                }
            }

            @Override
            public void onFailure(Call<MovieApiResponse> call, Throwable t) {

            }
        });
    }

    @Override //Код для извлечения последующих страниц
    public void loadAfter(@NonNull LoadParams<Long> loadParams,
                          @NonNull final LoadCallback<Long, Result> loadCallback) {

        movieApiService = RetrofitInstance.getService();
        Call<MovieApiResponse> call = movieApiService.
                getPopularMoviesWithPaging(application.getApplicationContext().getString(R.string.api_key),
                        loadParams.key);
        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {
                MovieApiResponse movieApiResponse = response.body();
                ArrayList<Result> results = new ArrayList<>();

                if (movieApiResponse != null && movieApiResponse.getResults() != null){
                    results = (ArrayList<Result>) movieApiResponse.getResults();
                    loadCallback.onResult(results,  loadParams.key + 1);
                }
            }

            @Override
            public void onFailure(Call<MovieApiResponse> call, Throwable t) {

            }
        });
    }

    @Override //Код для предыдущих страниц
    public void loadBefore(@NonNull LoadParams<Long> loadParams,
                           @NonNull LoadCallback<Long, Result> loadCallback) {

    }


}
