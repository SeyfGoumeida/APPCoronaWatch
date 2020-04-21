package com.example.coronawatch.Retrofit
import com.example.coronawatch.DataClases.Articles
import com.example.coronawatch.DataClases.Redactor
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface IAPI {

    @get:GET("article")
    val articles:Observable<Articles>

    @GET("users/redactor/{redactor_id}/")
    fun getRedactorDetails(@Path("redactor_id") id: Int): Observable<Redactor>


}