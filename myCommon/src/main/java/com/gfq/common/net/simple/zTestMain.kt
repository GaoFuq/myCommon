package com.gfq.common.net.simple

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gfq.common.utils.ApiDesc
import com.gfq.common.utils.HttpLogInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

/**
 *  2022/1/12 15:25
 * @auth gaofuq
 * @description
 */
//定义1
private interface Api {
    @ApiDesc("appVersion", "获取用户信息")
    @POST("api/getUserInfo")
    suspend fun getUserInfo(): BaseRespSimple<UserInfoRespSimple>
}

//定义2
private val okClient = OkHttpClient.Builder()
    .addInterceptor(HttpLogInterceptor(listOf(Api::class.java)))
    .build()

//定义3
private val retrofit: Retrofit = Retrofit.Builder()
    .client(okClient)
    .baseUrl("***********")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

//定义4
private val apiService: Api by lazy { retrofit.create(Api::class.java) }


//定义5
private class TestViewModel : BaseViewModelSimple() {
    fun getUserInfo() {
        request(
            request = { apiService.getUserInfo() },
            success = {

            },
            error = {},
            special = {},
        )
    }
}

//使用
private class SimpleActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vm by viewModels<TestViewModel>()
        vm.getUserInfo()
    }
}