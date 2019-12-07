package com.codebosses.flicks.adapters.genre;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.eventbus.EventBusMovieClick;
import com.codebosses.flicks.pojo.moviespojo.MoviesResult;
import com.codebosses.flicks.utils.FontUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.GenreHolder> {
    //    Android fields....
    private Context context;
    private LayoutInflater layoutInflater;

    //    Font fields....
    private FontUtils fontUtils;

    //    Instance fields....
    private List<MoviesResult> moviesResultArrayList = new ArrayList<>();
    private String movieType;

    public GenreAdapter(Context context, List<MoviesResult> moviesResultArrayList, String movieType) {
        this.context = context;
        fontUtils = FontUtils.getFontUtils(context);
        this.moviesResultArrayList = moviesResultArrayList;
        layoutInflater = LayoutInflater.from(context);
        this.movieType = movieType;
    }

    @NonNull
    @Override
    public GenreAdapter.GenreHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.row_genre, parent, false);
        return new GenreAdapter.GenreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.GenreHolder holder, int position) {
        MoviesResult moviesResult = moviesResultArrayList.get(position);
        if (moviesResult != null) {
            if (moviesResult.getPoster_path() != null && !moviesResult.getPoster_path().equals(""))
                Glide.with(context)
                        .load(EndpointUrl.POSTER_BASE_URL + "/" + moviesResult.getPoster_path())
                        .apply(new RequestOptions().placeholder(R.drawable.placeholder))
                        .thumbnail(0.1f)
                        .into(holder.imageViewThumbnail);
            String title = moviesResult.getTitle();
            if (title != null) {
                holder.textViewMovieTitle.setText(title);
            }
            double movieRating = moviesResult.getVote_average();
            holder.materialRatingBar.setRating((float) movieRating / 2);
        }
    }

    @Override
    public int getItemCount() {
        return moviesResultArrayList.size();
    }

    class GenreHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.imageViewThumbnailSimilarMoviesRow)
        ImageView imageViewThumbnail;
        @BindView(R.id.textViewMoviesNameSimilarMoviesRow)
        TextView textViewMovieTitle;
        @BindView(R.id.ratingBarSimilarMoviesRow)
        MaterialRatingBar materialRatingBar;

        GenreHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            fontUtils.setTextViewRegularFont(textViewMovieTitle);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            EventBus.getDefault().post(new EventBusMovieClick(getAdapterPosition(), movieType));
        }

    }
}
