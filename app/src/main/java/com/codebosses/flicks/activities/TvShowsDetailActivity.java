package com.codebosses.flicks.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codebosses.flicks.R;
import com.codebosses.flicks.adapters.castandcrewadapter.CastAdapter;
import com.codebosses.flicks.adapters.castandcrewadapter.CrewAdapter;
import com.codebosses.flicks.adapters.moviesdetail.MoviesGenreAdapter;
import com.codebosses.flicks.adapters.moviesdetail.VideosAdapter;
import com.codebosses.flicks.adapters.reviewsadapter.ReviewsAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.EpisodePhotosAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.SimilarTvShowsAdapter;
import com.codebosses.flicks.adapters.tvshowsdetail.TvShowSeasonsAdapter;
import com.codebosses.flicks.api.Api;
import com.codebosses.flicks.api.ApiClient;
import com.codebosses.flicks.endpoints.EndpointKeys;
import com.codebosses.flicks.endpoints.EndpointUrl;
import com.codebosses.flicks.pojo.castandcrew.CastAndCrewMainObject;
import com.codebosses.flicks.pojo.castandcrew.CastData;
import com.codebosses.flicks.pojo.castandcrew.CrewData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosData;
import com.codebosses.flicks.pojo.episodephotos.EpisodePhotosMainObject;
import com.codebosses.flicks.pojo.eventbus.EventBusCastAndCrewClick;
import com.codebosses.flicks.pojo.eventbus.EventBusPlayVideo;
import com.codebosses.flicks.pojo.eventbus.EventBusTvShowsClick;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerMainObject;
import com.codebosses.flicks.pojo.moviespojo.moviestrailer.MoviesTrailerResult;
import com.codebosses.flicks.pojo.reviews.ReviewsData;
import com.codebosses.flicks.pojo.reviews.ReviewsMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvMainObject;
import com.codebosses.flicks.pojo.tvpojo.TvResult;
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.Season;
import com.codebosses.flicks.pojo.tvpojo.tvshowsdetail.TvShowsDetailMainObject;
import com.codebosses.flicks.utils.FontUtils;
import com.codebosses.flicks.utils.ValidUtils;
import com.codebosses.flicks.utils.customviews.CustomNestedScrollView;
import com.codebosses.flicks.utils.customviews.curve_image_view.CrescentoImageView;
import com.dd.ShadowLayout;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.player.listeners.YouTubePlayerInitListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class TvShowsDetailActivity extends AppCompatActivity {

    //    Android fields....
    @BindView(R.id.textViewVideosHeader)
    TextView textViewVideosHeader;
    @BindView(R.id.recyclerViewVideosTvShowsDetail)
    RecyclerView recyclerViewVideos;
    @BindView(R.id.circularProgressBarTvShowsDetail)
    CircularProgressBar circularProgressBarMoviesDetail;
    //    @BindView(R.id.viewBlurTvShowsDetail)
//    View viewBlur;
    @BindView(R.id.imageViewCoverTvShowsDetail)
    CrescentoImageView imageViewCover;
    @BindView(R.id.shadowPlayButtonTvShowsDetail)
    ShadowLayout shadowLayoutPlayButton;
    @BindView(R.id.imageButtonPlayTvShowsDetail)
    AppCompatImageButton imageButtonPlay;
    @BindView(R.id.textViewTitleTvShowsDetail)
    TextView textViewTitle;
    @BindView(R.id.ratingBarTvShowsDetail)
    MaterialRatingBar ratingBar;
    @BindView(R.id.imageViewThumbnailTvShowsDetail)
    ImageView imageViewThumbnail;
    @BindView(R.id.textViewVotesTvShowsDetail)
    TextView textViewVoteCount;
    @BindView(R.id.textViewReleaseDateHeaderTvShowsDetail)
    TextView textViewReleaseDateHeader;
    @BindView(R.id.textViewYearTvShowsDetail)
    TextView textViewReleaseDate;
    @BindView(R.id.textViewOverviewTvShowsDetail)
    TextView textViewOverview;
    @BindView(R.id.cardViewThumbnailContainerTvShowsDetail)
    CardView cardViewThumbnail;
    @BindView(R.id.recyclerViewGenreTvShowsDetail)
    RecyclerView recyclerViewGenre;
    @BindView(R.id.recyclerViewCastTvShowsDetail)
    RecyclerView recyclerViewCast;
    @BindView(R.id.textViewCrewHeader)
    TextView textViewCrewHeader;
    @BindView(R.id.recyclerViewCrewTvShowsDetail)
    RecyclerView recyclerViewCrew;
    @BindView(R.id.textViewGenreHeader)
    TextView textViewGenreHeader;
    @BindView(R.id.textViewCastHeader)
    TextView textViewCastHeader;
    @BindView(R.id.textViewViewMoreSimilarTvShows)
    TextView textViewViewMoreSimilarTvShows;
    @BindView(R.id.textViewSimilarTvShowsHeader)
    TextView textViewSimilarTvShowsHeader;
    @BindView(R.id.recyclerViewSimilarTvShowsDetail)
    RecyclerView recyclerViewSimilarTvShows;
    @BindView(R.id.textViewSuggestionTvShowsHeader)
    TextView textViewSuggestionHeader;
    @BindView(R.id.textViewViewMoreSuggestionTvShows)
    TextView textViewViewMoreSuggestion;
    @BindView(R.id.recyclerViewSuggestionTvShowsDetail)
    RecyclerView recyclerViewSuggestedTvShows;
    @BindView(R.id.nestedScrollViewTvShowsDetail)
    CustomNestedScrollView nestedScrollViewTvShowsDetail;
    @BindView(R.id.textViewStoryLineHeader)
    TextView textViewOverViewHeader;
    @BindView(R.id.textViewRatingTvShowsDetail)
    TextView textViewTvShowsRating;
    @BindView(R.id.textViewAudienceTvShowsDetail)
    TextView textViewAudienceRating;
    @BindView(R.id.textViewSeasonsHeader)
    TextView textViewSeasonsHeader;
    @BindView(R.id.textViewSeasonsNumber)
    TextView textViewSeasonsNumber;
    @BindView(R.id.recyclerViewSeasonsTvShowsDetail)
    RecyclerView recyclerViewSeasons;
    @BindView(R.id.toolbarTvShowsDetail)
    Toolbar toolbarTvShowsDetail;
    @BindView(R.id.textViewViewReviewsHeaderTvShowsDetail)
    TextView textViewReviewsHeader;
    @BindView(R.id.recyclerViewReviewsTvShowsDetail)
    RecyclerView recyclerViewReviews;
    @BindView(R.id.textViewVideosCountTvShowsDetail)
    TextView textViewVideosCount;
    @BindView(R.id.textViewImagesHeader)
    TextView textViewImageHeader;
    @BindView(R.id.recyclerViewImagesTvShowsDetail)
    RecyclerView recyclerViewImages;
    @BindView(R.id.textViewImagesCountTvShowsDetail)
    TextView textViewImagesCounter;

    //    Retrofit calls....
    private Call<MoviesTrailerMainObject> moviesTrailerMainObjectCall;
    private Call<TvShowsDetailMainObject> tvShowsDetailMainObjectCall;
    private Call<TvMainObject> similarTvShowsCall;
    private Call<TvMainObject> suggestedTvShowsCall;
    private Call<CastAndCrewMainObject> castAndCrewMainObjectCall;
    private Call<ReviewsMainObject> reviewsMainObjectCall;
    private Call<EpisodePhotosMainObject> moviesImagesCall;

    //    Instance fields....
    private List<MoviesTrailerResult> moviesTrailerResultList = new ArrayList<>();
    private List<TvResult> similarTvResultList = new ArrayList<>();
    private List<TvResult> suggestedTvResultList = new ArrayList<>();
    private List<CastData> castDataList = new ArrayList<>();
    private List<CrewData> crewDataList = new ArrayList<>();
    private List<Season> seasonList = new ArrayList<>();
    private List<ReviewsData> reviewsDataList = new ArrayList<>();
    private List<EpisodePhotosData> imagesPhotoList = new ArrayList<>();
    private String tvShowId, tvShowTitle;
    private double rating;
    private int scrollingCounter = 0;

    //    Adapter fields....
    private SimilarTvShowsAdapter similarTvShowsAdapter;
    private SimilarTvShowsAdapter suggestedTvShowsAdapter;
    private CastAdapter castAdapter;
    private CrewAdapter crewAdapter;
    private TvShowSeasonsAdapter tvShowSeasonsAdapter;
    private ReviewsAdapter reviewsAdapter;
    private VideosAdapter videosAdapter;
    private EpisodePhotosAdapter imagesAdapter;

    //    Font fields....
    private FontUtils fontUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_shows_detail);
        TvShowsDetailActivity.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

//        Setting custom fonts....
        fontUtils = FontUtils.getFontUtils(this);
        fontUtils.setTextViewRegularFont(textViewTitle);
        fontUtils.setTextViewLightFont(textViewVoteCount);
        fontUtils.setTextViewRegularFont(textViewReleaseDateHeader);
        fontUtils.setTextViewLightFont(textViewReleaseDate);
        fontUtils.setTextViewRegularFont(textViewGenreHeader);
        fontUtils.setTextViewRegularFont(textViewCastHeader);
        fontUtils.setTextViewRegularFont(textViewCrewHeader);
        fontUtils.setTextViewRegularFont(textViewSimilarTvShowsHeader);
        fontUtils.setTextViewRegularFont(textViewViewMoreSimilarTvShows);
        fontUtils.setTextViewRegularFont(textViewSuggestionHeader);
        fontUtils.setTextViewRegularFont(textViewViewMoreSuggestion);
        fontUtils.setTextViewRegularFont(textViewOverViewHeader);
        fontUtils.setTextViewLightFont(textViewOverview);
        fontUtils.setTextViewRegularFont(textViewTvShowsRating);
        fontUtils.setTextViewLightFont(textViewAudienceRating);
        fontUtils.setTextViewRegularFont(textViewSeasonsHeader);
        fontUtils.setTextViewLightFont(textViewSeasonsNumber);
        fontUtils.setTextViewRegularFont(textViewReviewsHeader);
        fontUtils.setTextViewLightFont(textViewAudienceRating);
        fontUtils.setTextViewRegularFont(textViewReviewsHeader);
        fontUtils.setTextViewLightFont(textViewVideosCount);
        fontUtils.setTextViewRegularFont(textViewVideosHeader);
        fontUtils.setTextViewRegularFont(textViewImageHeader);
        fontUtils.setTextViewLightFont(textViewImagesCounter);

        //        Setting layout managers for recycler view....
        recyclerViewCast.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewCrew.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSimilarTvShows.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSuggestedTvShows.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSeasons.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewImages.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(TvShowsDetailActivity.this));

//        Creating empty list adapter objects...
        similarTvShowsAdapter = new SimilarTvShowsAdapter(this, similarTvResultList, EndpointKeys.SIMILAR_TV_SHOWS_DETAIL);
        suggestedTvShowsAdapter = new SimilarTvShowsAdapter(this, suggestedTvResultList, EndpointKeys.SUGGESTED_TV_SHOWS_DETAIL);
        castAdapter = new CastAdapter(this, castDataList);
        crewAdapter = new CrewAdapter(this, crewDataList);
        tvShowSeasonsAdapter = new TvShowSeasonsAdapter(this, seasonList, EndpointKeys.SEASON);
        reviewsAdapter = new ReviewsAdapter(this, reviewsDataList);
        videosAdapter = new VideosAdapter(this, moviesTrailerResultList);
        imagesAdapter = new EpisodePhotosAdapter(this, imagesPhotoList,EndpointKeys.TV_SHOW_IMAGES);

//        Setting item animator for recycler views....
        recyclerViewSimilarTvShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSuggestedTvShows.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCrew.setItemAnimator(new DefaultItemAnimator());
        recyclerViewCast.setItemAnimator(new DefaultItemAnimator());
        recyclerViewSeasons.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());
        recyclerViewVideos.setItemAnimator(new DefaultItemAnimator());
        recyclerViewImages.setItemAnimator(new DefaultItemAnimator());

//        Setting empty list adapter to recycler views....
        recyclerViewSimilarTvShows.setAdapter(similarTvShowsAdapter);
        recyclerViewSuggestedTvShows.setAdapter(suggestedTvShowsAdapter);
        recyclerViewCast.setAdapter(castAdapter);
        recyclerViewCrew.setAdapter(crewAdapter);
        recyclerViewSeasons.setAdapter(tvShowSeasonsAdapter);
        recyclerViewReviews.setAdapter(reviewsAdapter);
        recyclerViewVideos.setAdapter(videosAdapter);
        recyclerViewImages.setAdapter(imagesAdapter);

        setSupportActionBar(toolbarTvShowsDetail);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null) {
            tvShowId = String.valueOf(getIntent().getIntExtra(EndpointKeys.TV_ID, -1));
            tvShowTitle = getIntent().getStringExtra(EndpointKeys.TV_NAME);
            rating = getIntent().getDoubleExtra(EndpointKeys.RATING, 0.0);
            ratingBar.setRating((float) rating / 2);
            getTvTrailers("en-US", tvShowId);
            getTvDetail("en-US", tvShowId);
            getSimilarTvShows(tvShowId, "en-US", 1);
            getSuggestedTvShows(tvShowId, "en-US", 1);
            getTvShowCredits(tvShowId);
            getTvShowReviews(tvShowId, "en-US", 1);
            getTvShowImages(tvShowId, "en");
        }

//        Checking if user scroll nested scroll view so that youtube player can pause/resume....
//        if (nestedScrollViewTvShowsDetail != null) {
//
//            nestedScrollViewTvShowsDetail.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
//
//                if (scrollY > oldScrollY) {
//                    if (scrollingCounter == 0) {
//                        if (youTubePlayer != null)
//                            youTubePlayer.pause();
//                    }
//                    scrollingCounter++;
//                }
//                if (scrollY < oldScrollY) {
//                }
//
//                if (scrollY == 0) {
//                    if (youTubePlayer != null) {
//                        youTubePlayer.play();
//                    }
//                    scrollingCounter = 0;
//                }
//
//                if (scrollY == (v.getMeasuredHeight() - v.getChildAt(0).getMeasuredHeight())) {
//                }
//            });
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (youTubePlayer != null) {
//            youTubePlayer.play();
//        }
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (youTubePlayer != null) {
//            youTubePlayer.pause();
//        }
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (moviesTrailerMainObjectCall != null && moviesTrailerMainObjectCall.isExecuted()) {
            moviesTrailerMainObjectCall.cancel();
        }
        if (tvShowsDetailMainObjectCall != null && tvShowsDetailMainObjectCall.isExecuted()) {
            tvShowsDetailMainObjectCall.cancel();
        }
        if (similarTvShowsCall != null && similarTvShowsCall.isExecuted()) {
            similarTvShowsCall.cancel();
        }
        if (suggestedTvShowsCall != null && suggestedTvShowsCall.isExecuted()) {
            suggestedTvShowsCall.cancel();
        }
        if (castAndCrewMainObjectCall != null && castAndCrewMainObjectCall.isExecuted()) {
            castAndCrewMainObjectCall.cancel();
        }
        if (reviewsMainObjectCall != null && reviewsMainObjectCall.isExecuted()) {
            reviewsMainObjectCall.cancel();
        }
        if (moviesImagesCall != null && moviesImagesCall.isExecuted()) {
            moviesImagesCall.cancel();
        }
//        youTubePlayerView.release();
    }

    @OnClick(R.id.textViewViewMoreSimilarTvShows)
    public void onViewMoreSimilarTvShowsClick(TextView textViewSimilar) {
        Intent intent = new Intent(this, SimilarTvShowsActivity.class);
        intent.putExtra(EndpointKeys.TV_ID, tvShowId);
        startActivity(intent);
    }

    @OnClick(R.id.textViewViewMoreSuggestionTvShows)
    public void onViewMoreSuggestedTvShowsClick(TextView textViewSuggestion) {
        Intent intent = new Intent(this, SuggestedTvShowsActivity.class);
        intent.putExtra(EndpointKeys.TV_ID, tvShowId);
        startActivity(intent);
    }

    private void getTvTrailers(String language, String tvId) {
        moviesTrailerMainObjectCall = ApiClient.getClient().create(Api.class).getTvTrailer(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        moviesTrailerMainObjectCall.enqueue(new Callback<MoviesTrailerMainObject>() {
            @Override
            public void onResponse(Call<MoviesTrailerMainObject> call, retrofit2.Response<MoviesTrailerMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    MoviesTrailerMainObject moviesTrailerMainObject = response.body();
                    if (moviesTrailerMainObject != null) {
                        if (moviesTrailerMainObject.getResults().size() > 0) {
                            recyclerViewVideos.setVisibility(View.VISIBLE);
                            textViewVideosHeader.setVisibility(View.VISIBLE);
                            textViewVideosCount.setVisibility(View.VISIBLE);
                            Glide.with(TvShowsDetailActivity.this)
                                    .load(EndpointUrl.YOUTUBE_THUMBNAIL_BASE_URL + response.body().getResults().get(0).getKey() + "/mqdefault.jpg")
                                    .apply(new RequestOptions().centerCrop())
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .into(imageViewCover);
                            for (int i = 0; i < moviesTrailerMainObject.getResults().size(); i++) {
                                moviesTrailerResultList.add(moviesTrailerMainObject.getResults().get(i));
                                videosAdapter.notifyItemInserted(i);
                            }
                            textViewVideosCount.setText("(" + moviesTrailerResultList.size() + ")");
//                            toolbarTvShowsDetail.setVisibility(View.GONE);
//                            youTubePlayerView.initialize(new YouTubePlayerInitListener() {
//                                @Override
//                                public void onInitSuccess(@NonNull YouTubePlayer youTubePlayer) {
//                                    youTubePlayer.addListener(new AbstractYouTubePlayerListener() {
//                                        @Override
//                                        public void onReady() {
//                                            super.onReady();
//                                            TvShowsDetailActivity.this.youTubePlayer = youTubePlayer;
//                                            youTubePlayer.loadVideo(moviesTrailerResultList.get(0).getKey(), 0);
//                                        }
//                                    });
//                                }
//                            }, true);
                        } else {
                            textViewVideosHeader.setVisibility(View.GONE);
                            textViewVideosCount.setVisibility(View.GONE);
                            imageButtonPlay.setVisibility(View.GONE);
                            recyclerViewVideos.setVisibility(View.GONE);
//                            youTubePlayerView.setVisibility(View.GONE);
//                            toolbarTvShowsDetail.setVisibility(View.VISIBLE);
//                            setSupportActionBar(toolbarTvShowsDetail);
//                            if (getSupportActionBar() != null) {
//                                getSupportActionBar().setTitle(tvShowTitle);
//                                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                                ValidUtils.changeToolbarFont(toolbarTvShowsDetail, TvShowsDetailActivity.this);
//                            }
//                            Toast.makeText(TvShowsDetailActivity.this, "Could not found trailer of this movie.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesTrailerMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
//                        textViewError.setText(internetProblem);
                    } else {
//                        textViewError.setText(error.getMessage());
                    }
                } else {
//                    textViewError.setText(couldNotGetCelebrities);
                }
            }
        });
    }

    private void getTvShowImages(String tvShowId, String language) {
        moviesImagesCall = ApiClient.getClient().create(Api.class).getTvImages(tvShowId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, "");
        moviesImagesCall.enqueue(new Callback<EpisodePhotosMainObject>() {
            @Override
            public void onResponse(Call<EpisodePhotosMainObject> call, retrofit2.Response<EpisodePhotosMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    EpisodePhotosMainObject imagesMainObject = response.body();
                    if (imagesMainObject != null) {
                        if (imagesMainObject.getBackdrops() != null && imagesMainObject.getBackdrops().size() > 0) {
                            textViewImageHeader.setVisibility(View.VISIBLE);
                            textViewImagesCounter.setVisibility(View.VISIBLE);
                            recyclerViewImages.setVisibility(View.VISIBLE);
                            for (int i = 0; i < imagesMainObject.getBackdrops().size(); i++) {
                                imagesPhotoList.add(imagesMainObject.getBackdrops().get(i));
                                imagesAdapter.notifyItemInserted(i);
                            }
                            textViewImagesCounter.setText("(" + imagesPhotoList.size() + ")");
                        } else {
                            textViewImageHeader.setVisibility(View.GONE);
                            textViewImagesCounter.setVisibility(View.GONE);
                            recyclerViewImages.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<EpisodePhotosMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {

                    } else {

                    }
                } else {

                }
            }
        });
    }

    private void getTvDetail(String language, String tvId) {
        circularProgressBarMoviesDetail.setVisibility(View.VISIBLE);
        tvShowsDetailMainObjectCall = ApiClient.getClient().create(Api.class).getTvDetail(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY, language);
        tvShowsDetailMainObjectCall.enqueue(new Callback<TvShowsDetailMainObject>() {
            @Override
            public void onResponse(Call<TvShowsDetailMainObject> call, retrofit2.Response<TvShowsDetailMainObject> response) {
                circularProgressBarMoviesDetail.setVisibility(View.GONE);
                if (response != null && response.isSuccessful()) {
                    TvShowsDetailMainObject tvShowsDetailMainObject = response.body();
                    if (tvShowsDetailMainObject != null) {

                        String originalName = tvShowsDetailMainObject.getOriginal_name();
                        String overview = tvShowsDetailMainObject.getOverview();
                        String firstAirDate = tvShowsDetailMainObject.getFirst_air_date();
                        String tvShowPosterPath = tvShowsDetailMainObject.getPoster_path();

//                        viewBlur.setVisibility(View.VISIBLE);
                        shadowLayoutPlayButton.setVisibility(View.VISIBLE);
                        cardViewThumbnail.setVisibility(View.VISIBLE);
                        textViewReleaseDateHeader.setVisibility(View.VISIBLE);
                        ratingBar.setVisibility(View.VISIBLE);
                        textViewGenreHeader.setVisibility(View.VISIBLE);
                        textViewAudienceRating.setVisibility(View.VISIBLE);
                        textViewTvShowsRating.setVisibility(View.VISIBLE);
                        textViewOverViewHeader.setVisibility(View.VISIBLE);
                        textViewVoteCount.setVisibility(View.VISIBLE);

                        textViewSeasonsNumber.setText("(" + tvShowsDetailMainObject.getSeasons().size() + ")");
                        textViewTitle.setText(originalName);
                        textViewReleaseDate.setText(firstAirDate);
                        textViewTvShowsRating.setText(String.valueOf(rating));
                        textViewOverview.setText(overview);
                        Glide.with(TvShowsDetailActivity.this)
                                .load(EndpointUrl.POSTER_BASE_URL + "/" + tvShowPosterPath)
                                .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                .apply(new RequestOptions().fitCenter())
                                .into(imageViewThumbnail);
                        if (moviesTrailerResultList.size() > 0) {
                            Glide.with(TvShowsDetailActivity.this)
                                    .load(EndpointUrl.YOUTUBE_THUMBNAIL_BASE_URL + moviesTrailerResultList.get(0).getKey() + "/mqdefault.jpg")
                                    .apply(new RequestOptions().centerCrop())
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .into(imageViewCover);
                        } else {
                            Glide.with(TvShowsDetailActivity.this)
                                    .load(EndpointUrl.POSTER_BASE_URL + "/" + tvShowPosterPath)
                                    .apply(new RequestOptions().placeholder(R.drawable.zootopia_thumbnail))
                                    .apply(new RequestOptions().fitCenter())
                                    .into(imageViewCover);
                        }
                        if (tvShowsDetailMainObject.getGenres().size() > 0) {
                            recyclerViewGenre.setVisibility(View.VISIBLE);
                            recyclerViewGenre.setAdapter(new MoviesGenreAdapter(TvShowsDetailActivity.this, tvShowsDetailMainObject.getGenres()));
                        } else {
                            textViewGenreHeader.setVisibility(View.GONE);
                            recyclerViewGenre.setVisibility(View.GONE);
                        }
                        if (tvShowsDetailMainObject.getSeasons().size() > 0) {
                            textViewSeasonsHeader.setVisibility(View.VISIBLE);
                            textViewSeasonsNumber.setVisibility(View.VISIBLE);
                            recyclerViewSeasons.setVisibility(View.VISIBLE);
                            for (int i = 0; i < tvShowsDetailMainObject.getSeasons().size(); i++) {
                                seasonList.add(tvShowsDetailMainObject.getSeasons().get(i));
                                tvShowSeasonsAdapter.notifyItemInserted(seasonList.size() - 1);
                            }
                        }
                        if (TextUtils.isEmpty(overview)) {
                            textViewOverview.setVisibility(View.GONE);
                            textViewOverViewHeader.setVisibility(View.GONE);
                        }
                        if (tvShowsDetailMainObject.getGenres().size() > 0) {
                            textViewGenreHeader.setVisibility(View.VISIBLE);
                            textViewGenreHeader.setVisibility(View.VISIBLE);
                        } else {
                            recyclerViewGenre.setVisibility(View.GONE);
                            textViewGenreHeader.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TvShowsDetailMainObject> call, Throwable error) {
                circularProgressBarMoviesDetail.setVisibility(View.GONE);
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
//                        textViewError.setText(internetProblem);
                    } else {
//                        textViewError.setText(error.getMessage());
                    }
                } else {
//                    textViewError.setText(couldNotGetCelebrities);
                }
            }
        });
    }

    private void getSimilarTvShows(String tvId, String language, int pageNumber) {
        similarTvShowsCall = ApiClient.getClient().create(Api.class).getSimilarTvShows(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        similarTvShowsCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    TvMainObject tvMainObject = response.body();
                    if (tvMainObject != null) {
                        if (tvMainObject.getTotal_results() > 0) {
                            textViewSimilarTvShowsHeader.setVisibility(View.VISIBLE);
                            textViewViewMoreSimilarTvShows.setVisibility(View.VISIBLE);
                            recyclerViewSimilarTvShows.setVisibility(View.VISIBLE);
                            for (int i = 0; i < tvMainObject.getResults().size(); i++) {
                                similarTvResultList.add(tvMainObject.getResults().get(i));
                                similarTvShowsAdapter.notifyItemInserted(similarTvResultList.size() - 1);
                            }
                        } else {
                            textViewSimilarTvShowsHeader.setVisibility(View.GONE);
                            textViewViewMoreSimilarTvShows.setVisibility(View.VISIBLE);
                            recyclerViewSimilarTvShows.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TvMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
//                        textViewError.setText(internetProblem);
                    } else {
//                        textViewError.setText(error.getMessage());
                    }
                } else {
//                    textViewError.setText(couldNotGetMovies);
                }
            }
        });
    }

    private void getSuggestedTvShows(String tvId, String language, int pageNumber) {
        suggestedTvShowsCall = ApiClient.getClient().create(Api.class).getSuggestedTvShows(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        suggestedTvShowsCall.enqueue(new Callback<TvMainObject>() {
            @Override
            public void onResponse(Call<TvMainObject> call, retrofit2.Response<TvMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    TvMainObject tvMainObject = response.body();
                    if (tvMainObject != null) {
                        if (tvMainObject.getTotal_results() > 0) {
                            textViewSuggestionHeader.setVisibility(View.VISIBLE);
                            textViewViewMoreSuggestion.setVisibility(View.VISIBLE);
                            recyclerViewSuggestedTvShows.setVisibility(View.VISIBLE);
                            for (int i = 0; i < tvMainObject.getResults().size(); i++) {
                                suggestedTvResultList.add(tvMainObject.getResults().get(i));
                                suggestedTvShowsAdapter.notifyItemInserted(suggestedTvResultList.size() - 1);
                            }
                        } else {
                            textViewSuggestionHeader.setVisibility(View.GONE);
                            textViewViewMoreSuggestion.setVisibility(View.GONE);
                            recyclerViewSuggestedTvShows.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TvMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }

                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
//                        textViewError.setText(internetProblem);
                    } else {
//                        textViewError.setText(error.getMessage());
                    }
                } else {
//                    textViewError.setText(couldNotGetMovies);
                }
            }
        });
    }

    private void getTvShowCredits(String tvId) {
        castAndCrewMainObjectCall = ApiClient.getClient().create(Api.class).getTvCredits(tvId, EndpointKeys.THE_MOVIE_DB_API_KEY);
        castAndCrewMainObjectCall.enqueue(new Callback<CastAndCrewMainObject>() {
            @Override
            public void onResponse(Call<CastAndCrewMainObject> call, retrofit2.Response<CastAndCrewMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    CastAndCrewMainObject castAndCrewMainObject = response.body();
                    if (castAndCrewMainObject != null) {
                        if (castAndCrewMainObject.getCast().size() > 0) {
                            textViewCastHeader.setVisibility(View.VISIBLE);
                            recyclerViewCast.setVisibility(View.VISIBLE);
                            for (int i = 0; i < castAndCrewMainObject.getCast().size(); i++) {
                                castDataList.add(castAndCrewMainObject.getCast().get(i));
                                castAdapter.notifyItemInserted(i);
                            }
                        } else {
                            textViewCastHeader.setVisibility(View.GONE);
                            recyclerViewCast.setVisibility(View.GONE);
                        }
                        if (castAndCrewMainObject.getCrew().size() > 0) {
                            textViewCrewHeader.setVisibility(View.VISIBLE);
                            recyclerViewCrew.setVisibility(View.VISIBLE);
                            for (int i = 0; i < castAndCrewMainObject.getCrew().size(); i++) {
                                crewDataList.add(castAndCrewMainObject.getCrew().get(i));
                                crewAdapter.notifyItemInserted(i);
                            }
                        } else {
                            textViewCrewHeader.setVisibility(View.GONE);
                            recyclerViewCrew.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<CastAndCrewMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {
                    } else {
                    }
                } else {
                }
            }
        });
    }

    private void getTvShowReviews(String movieId, String language, int pageNumber) {
        reviewsMainObjectCall = ApiClient.getClient().create(Api.class).getTvReviews(movieId, EndpointKeys.THE_MOVIE_DB_API_KEY, language, pageNumber);
        reviewsMainObjectCall.enqueue(new Callback<ReviewsMainObject>() {
            @Override
            public void onResponse(Call<ReviewsMainObject> call, retrofit2.Response<ReviewsMainObject> response) {
                if (response != null && response.isSuccessful()) {
                    ReviewsMainObject reviewsMainObject = response.body();
                    if (reviewsMainObject != null) {
                        if (reviewsMainObject.getTotal_results() > 0) {
                            textViewReviewsHeader.setVisibility(View.VISIBLE);
                            recyclerViewReviews.setVisibility(View.VISIBLE);
                            for (int i = 0; i < reviewsMainObject.getResults().size(); i++) {
                                reviewsDataList.add(reviewsMainObject.getResults().get(i));
                                reviewsAdapter.notifyItemInserted(reviewsDataList.size() - 1);
                            }
                        } else {
                            textViewReviewsHeader.setVisibility(View.GONE);
                            recyclerViewReviews.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ReviewsMainObject> call, Throwable error) {
                if (call.isCanceled() || "Canceled".equals(error.getMessage())) {
                    return;
                }
                if (error != null) {
                    if (error.getMessage().contains("No address associated with hostname")) {

                    } else {

                    }
                } else {

                }
            }
        });
    }

    @OnClick({R.id.imageButtonPlayTvShowsDetail, R.id.imageViewCoverTvShowsDetail})
    public void onPlayButtonClick(View view) {
        if (moviesTrailerResultList.size() > 0) {
            startTrailerActivity(moviesTrailerResultList.get(0).getKey());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusPlayVideo(EventBusPlayVideo eventBusPlayVideo) {
        if (moviesTrailerResultList.size() > 0)
            startTrailerActivity(moviesTrailerResultList.get(eventBusPlayVideo.getPosition()).getKey());
    }

    private void startTrailerActivity(String key) {
        Intent intent = new Intent(this, TrailerActivity.class);
        intent.putExtra(EndpointKeys.YOUTUBE_KEY, key);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusSimilarTvShowClick(EventBusTvShowsClick eventBusTvShowsClick) {
        String tvTitle = "";
        int tvId = 0;
        double rating = 0.0;
        if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.SIMILAR_TV_SHOWS_DETAIL)) {
            tvId = similarTvResultList.get(eventBusTvShowsClick.getPosition()).getId();
            tvTitle = similarTvResultList.get(eventBusTvShowsClick.getPosition()).getName();
            rating = similarTvResultList.get(eventBusTvShowsClick.getPosition()).getVote_average();
        } else if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.SUGGESTED_TV_SHOWS_DETAIL)) {
            tvId = suggestedTvResultList.get(eventBusTvShowsClick.getPosition()).getId();
            tvTitle = suggestedTvResultList.get(eventBusTvShowsClick.getPosition()).getName();
            rating = suggestedTvResultList.get(eventBusTvShowsClick.getPosition()).getVote_average();
        } else if (eventBusTvShowsClick.getTvShowType().equals(EndpointKeys.SEASON)) {
            Intent intent = new Intent(this, TvSeasonDetailActivity.class);
            intent.putExtra(EndpointKeys.TV_ID, tvShowId);
            intent.putExtra(EndpointKeys.SEASON_NUMBER, seasonList.get(eventBusTvShowsClick.getPosition()).getSeason_number());
            intent.putExtra(EndpointKeys.TV_NAME, seasonList.get(eventBusTvShowsClick.getPosition()).getName());
            startActivity(intent);
            return;
        }
        Intent intent = new Intent(this, TvShowsDetailActivity.class);
        intent.putExtra(EndpointKeys.TV_ID, tvId);
        intent.putExtra(EndpointKeys.TV_NAME, tvTitle);
        intent.putExtra(EndpointKeys.RATING, rating);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusCastAndCrewClick(EventBusCastAndCrewClick eventBusCastAndCrewClick) {
        int castId = -1;
        String name = "", image = "";
        if (eventBusCastAndCrewClick.getClickType().equals(EndpointKeys.CAST)) {
            castId = castDataList.get(eventBusCastAndCrewClick.getPosition()).getId();
            name = castDataList.get(eventBusCastAndCrewClick.getPosition()).getName();
            image = castDataList.get(eventBusCastAndCrewClick.getPosition()).getProfile_path();
        } else {
            castId = crewDataList.get(eventBusCastAndCrewClick.getPosition()).getId();
            name = crewDataList.get(eventBusCastAndCrewClick.getPosition()).getName();
            image = crewDataList.get(eventBusCastAndCrewClick.getPosition()).getProfile_path();
        }
        Intent intent = new Intent(this, CelebrityDetailActivity.class);
        intent.putExtra(EndpointKeys.CELEBRITY_ID, castId);
        intent.putExtra(EndpointKeys.CELEB_NAME, name);
        intent.putExtra(EndpointKeys.CELEB_IMAGE, image);
        startActivity(intent);
    }


}