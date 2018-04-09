package com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.service

import com.zhuinden.mvvmaacrxjavaretrofitroom.data.remote.api.CatsBO
import io.reactivex.Single
import retrofit2.http.GET

interface CatService {
    @GET("api/images/get?format=xml&results_per_page=20")
    fun getCats(): Single<CatsBO>
}