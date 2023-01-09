package android.retrofitdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.paging.PagedListKt;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.retrofitdemo.adapter.ResultAdapter;
import android.retrofitdemo.databinding.ActivityMainBinding;
import android.retrofitdemo.model.MovieApiResponse;
import android.retrofitdemo.model.Result;
import android.retrofitdemo.service.MovieApiService;
import android.retrofitdemo.service.RetrofitInstance;
import android.retrofitdemo.viewmodel.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //Вместо ArrayList используем PagedList

    //private ArrayList<Result> results;
    private PagedList<Result> results;
    private RecyclerView recyclerView;
    private ResultAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        swipeRefreshLayout = activityMainBinding.swiperefresh;
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularMovies();
            }
        });

        mainActivityViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).
                create(MainActivityViewModel.class);

        getPopularMovies();
    }
    //Метод без применения MVVM
    //Данный метод перенесён в Repository класс
   /* public void getPopularMovies(){
        MovieApiService movieApiService = RetrofitInstance.getService();
        //Api Key записан в XMl-файле string, поэтому вызываем его так
        Call<MovieApiResponse> call = movieApiService.getPopularMovies(getString(R.string.api_key));

        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Call<MovieApiResponse> call, Response<MovieApiResponse> response) {
                MovieApiResponse movieApiResponse = response.body();
                if (movieApiResponse != null && movieApiResponse.getResults() != null){
                    results = (ArrayList<Result>) movieApiResponse.getResults();
                    fillRecyclerView();
                    swipeRefreshLayout.setRefreshing(false); //Останаливаем показ
                }
            }

            @Override
            public void onFailure(Call<MovieApiResponse> call, Throwable t) {

            }
        });
    } */

    //Метод с применением MVVM
    private void getPopularMovies(){
       //Реализация с ArrayList
      /* mainActivityViewModel.getAllMovieData().observe(this, new Observer<List<Result>>() {
            @Override
            public void onChanged(List<Result> resultList) {
                results = (ArrayList<Result>) resultList;
                fillRecyclerView();
            }
        });
     */

        //Реализация с PagedList
        mainActivityViewModel.getPagedListLiveData().observe(this, new Observer<PagedList<Result>>() {
            @Override
            public void onChanged(PagedList<Result> resultList) {
                results = resultList;
                fillRecyclerView();
            }
        });
    }

    //Метод для заполнения RV
    private void fillRecyclerView(){
        recyclerView = activityMainBinding.recyclerView;
        adapter = new ResultAdapter(this);
        adapter.submitList(results); //ОТправляе PagedList в Адаптер

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            //Если ориентация телефона портретная, то:
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            //Если ландшафтная:
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}