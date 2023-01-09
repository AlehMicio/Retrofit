package android.retrofitdemo.adapter;

import android.content.Context;
import android.retrofitdemo.R;
import android.retrofitdemo.databinding.ResultListItemBinding;
import android.retrofitdemo.model.Result;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//Т.к. используем PageList, то вместо RecyclerView.Adapter<ResultAdapter.ResultViewHolder> наследуемся от:
public class ResultAdapter extends PagedListAdapter<Result, ResultAdapter.ResultViewHolder> {

    //ArrayList уже будет не нужен, т.к. используем PagedList

    private Context context;
   // private ArrayList<Result> results;

    public ResultAdapter(Context context) {
        super(Result.CALLBACK);
        this.context = context;
        //this.results = results;
    }

    public class ResultViewHolder extends RecyclerView.ViewHolder{

        // public TextView titleTextView, popularTextView; //Используется Data Binding
        // public ImageView movieImageView;

        private ResultListItemBinding resultListItemBinding; //Также юзаем эту переменную и в конcтрукторе ниже

        public ResultViewHolder(@NonNull ResultListItemBinding resultListItemBinding) {
            super(resultListItemBinding.getRoot());
           // titleTextView = itemView.findViewById(R.id.titleTextView);
           // popularTextView = itemView.findViewById(R.id.popularTextView);
           // movieImageView = itemView.findViewById(R.id.movieImageView);

            this.resultListItemBinding = resultListItemBinding;
        }
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_list_item, parent, false);
        //return new ResultViewHolder(view);

        ResultListItemBinding resultListItemBinding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.result_list_item, parent, false);
        return new ResultViewHolder(resultListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {
        /*
        holder.titleTextView.setText(results.get(position).getOriginalTitle());
        holder.popularTextView.setText(Double.toString(results.get(position).getPopularity()));
        //Для image:
        //Вначле нужен путь на сайте: общий каталог + определённая картинка
        String imagePath = "https://image.tmdb.org/t/p/w500/" + results.get(position).getPosterPath();
        Glide.with(context).load(imagePath)
                .placeholder(R.drawable.progress_circle) //Пока будут подгружатсья фото из инета, то вместо них будет эта картинка
                .into(holder.movieImageView); //Устанавливаем с помощью библиотеки Glide
        */

        //Result result = results.get(position);
        Result result = getItem(position);
        String imagePath = "https://image.tmdb.org/t/p/w500/" + result.getPosterPath();
        holder.resultListItemBinding.setResult(result);

    }

//     @Override
//    public int getItemCount() {
//        return results.size();
//    }

}
