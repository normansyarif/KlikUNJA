package id.ac.unja.klikunja.api;

import java.util.List;

import id.ac.unja.klikunja.models.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("wp-json/wp/v2/posts")
    Call<List<News>> getPostInfo(
            @Query("categories") String categories,
            @Query("_embed") String embed
    );

    @GET("wp-json/wp/v2/posts")
    Call<List<News>> getPostSearch(
            @Query("categories") String categories,
            @Query("_embed") String embed,
            @Query("search") String keyword
    );
}
