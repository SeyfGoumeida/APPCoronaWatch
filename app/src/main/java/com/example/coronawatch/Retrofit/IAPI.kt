package com.example.coronawatch.Retrofit
import com.example.coronawatch.DataClases.Articles
import com.example.coronawatch.DataClases.Comments
import com.example.coronawatch.DataClases.Redactor
import com.example.coronawatch.DataClases.User
import io.reactivex.Observable
import retrofit2.http.*


interface IAPI {

    @get:GET("article")
    val articles:Observable<Articles>

    @GET("users/redactor/{redactor_id}/")
    fun getRedactorDetails(@Path("redactor_id") id: Int): Observable<Redactor>

    @FormUrlEncoded
    @POST("users/login/")
    fun getUser(@Field("username") username: String, @Field("password") password: String): Observable<User>

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

    @FormUrlEncoded
    @POST("article/detailComment/{comment_id}/")
    fun deleteComment(@Path("comment_id") id: Int , @Field("Authorization") Authorization: String) : String

    @FormUrlEncoded
    @POST("article/detailComment/{comment_id}/")
    fun editComment(@Path("comment_id") id: Int ,
                    @Field("Authorization") Authorization: String,
                    @Field("content") content: String ) : String


}