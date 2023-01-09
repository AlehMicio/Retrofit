package android.retrofitdemo.viewmodel;

import android.app.Application;
import android.retrofitdemo.MovieDataSource;
import android.retrofitdemo.MovieDataSourceFactory;
import android.retrofitdemo.model.MovieRepository;
import android.retrofitdemo.model.Result;
import android.retrofitdemo.service.MovieApiService;
import android.retrofitdemo.service.RetrofitInstance;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivityViewModel extends AndroidViewModel {

    private MovieRepository movieRepository;
    private LiveData<MovieDataSource> movieDataSourceLiveData;
    private Executor executor; //Данный класс позволяет автоматически управлять потоками, поэтому несколько потоков запустять параллельно
    private LiveData<PagedList<Result>> pagedListLiveData; //Нужен ещё getter

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);

        MovieApiService movieApiService = RetrofitInstance.getService();
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory(application, movieApiService);
        movieDataSourceLiveData = movieDataSourceFactory.getMutableLiveData();

        //PageList - некая обёртка при использовании Padding LIb
        PagedList.Config config = new PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10) //Кол-во элементов, которые загружаются изначально
                .setPageSize(20) //Кол-во элементов, загружаемых в PageList
                .setPrefetchDistance(3) //Кол-во страниц, которые изанчально загрузятся
                .build();

        executor = Executors.newCachedThreadPool();

        pagedListLiveData = new LivePagedListBuilder<Long, Result>(movieDataSourceFactory, config)
                .setFetchExecutor(executor)
                .build();
    }

    //Метод для получение всех данных из Result
    public LiveData<List<Result>> getAllMovieData(){


        return movieRepository.getMutableLiveData();
    }

    public LiveData<PagedList<Result>> getPagedListLiveData() {
        return pagedListLiveData;
    }
}
