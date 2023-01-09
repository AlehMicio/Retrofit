package android.retrofitdemo.service;

import android.retrofitdemo.model.MovieApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApiService {

    //Получаем фильмы в RV с сайта
    @GET("movie/popular")
    Call<MovieApiResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/popular") //Тоже самое, только с помощью Paging Lib
    Call<MovieApiResponse> getPopularMoviesWithPaging(@Query("api_key") String apiKey,
                                                      @Query("page") long page);

}
