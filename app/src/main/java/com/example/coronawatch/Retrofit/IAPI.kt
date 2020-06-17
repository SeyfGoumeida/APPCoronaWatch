package com.example.coronawatch.Retrofit
import com.example.coronawatch.DataClases.*
import io.reactivex.Observable
import retrofit2.http.*


interface IAPI {

    @get:GET("article")
    val articles:Observable<Articles>

    @get:GET("video")
    val videos:Observable<Videos>

    @GET("users/redactor/{redactor_id}/")
    fun getRedactorDetails(@Path("redactor_id") id: Int): Observable<Redactor>

    @GET("users/mobile/{user_id}/")
    fun getUserDetails(@Path("user_id") id: Int): Observable<User>

    @FormUrlEncoded
    @POST("users/login/")
    fun getUser(@Field("username") username: String, @Field("password") password: String): Observable<User>

    @FormUrlEncoded
    @POST("users/googlesign/")
    fun googleLogin(@Field("google_access_token") google_access_token: String): Observable<User>

    @FormUrlEncoded
    @POST("users/facebooksign/")
    fun facebbokLogin(@Field("facebook_access_token") facebook_access_token: String): Observable<User>

    @FormUrlEncoded
    @POST("users/emailsign/")
    fun signUser(@Field("username") username: String,
                 @Field("email") email: String,
                 @Field("password") password: String,
                 @Field("password2") password2: String,
                 @Field("first_name") first_name: String,
                 @Field("last_name") last_name: String
                 ): Observable<User>

    @GET("article/{article_id}/comments/")
    fun getComments(@Path("article_id") id: Int): Observable<Comments>

    @GET("video/{video_id}/comments/")
    fun getCommentsVideos(@Path("video_id") id: Int): Observable<VideoComments>



    @FormUrlEncoded
    @POST("article/detailComment/{comment_id}/")
    fun deleteComment(@Path("comment_id") id: Int , @Field("Authorization") Authorization: String) : Observable <String>


    @Multipart
    @POST("article/{article_id}/newComment/")
    fun addComment(@Path("article_id") id: Int ,
                    @Header("Authorization") Authorization: String,
                    @Part("content") content: String ) : Observable<CommentsItem>

    @Multipart
    @POST("video/")
    fun addVideo( @Header("Authorization") Authorization: String,
                   @Part("title") title: String  ,
                   @Part("video")  path: String ) : Observable<CommentsItem>


    @Multipart
    @POST("video/{video_id}/comments/")
    fun addCommentVideo(@Path("video_id") id: Int ,
                   @Header("Authorization") Authorization: String,
                   @Part("content") content: String ) : Observable<VideoCommentsItem>



    @FormUrlEncoded
    @POST("article/detailComment/{comment_id}/")
    fun editComment(@Path("comment_id") id: Int ,
                    @Field("Authorization") Authorization: String,
                    @Field("content") content: String ) : Observable<String>

    //MAP
    @GET("geo/country/{country_id}/infectedregions/")
    fun getCountryRigions(@Path("country_id") id: Int): Observable<Regions>

    @GET("geo/country/{country_id}/stats/")
    fun getCountryStatistics(@Path("country_id") id: Int): Observable<Stats>

    @GET("geo/country/")
    fun getcountries(): Observable<Countries>
    @GET("geo/worldstats/")
    fun getWorldStatistics(): Observable<Stats>





}