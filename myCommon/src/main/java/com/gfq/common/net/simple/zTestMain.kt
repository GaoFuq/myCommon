package com.gfq.common.net.simple

//import androidx.fragment.app.Fragment
//import androidx.lifecycle.lifecycleScope
//import com.gfq.common.net.AbsResponse
//import okhttp3.OkHttpClient
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import retrofit2.http.POST

/**
 *  2022/1/12 15:25
 * @auth gaofuq
 * @description
 */
//定义1
//private interface Api {
//    @POST("api/getUserInfo")
//    suspend fun getUserInfo(): AbsResponseSimple<UserInfoRespSimple>
//}
//
////定义2
//private val okClient = OkHttpClient.Builder()
//    .build()
//
////定义3
//private val retrofit: Retrofit = Retrofit.Builder()
//    .client(okClient)
//    .baseUrl("***********")
//    .addConverterFactory(GsonConverterFactory.create())
//    .build()
//
////定义4
//private val apiService: Api by lazy { retrofit.create(Api::class.java) }
//fun x() {
//    Fragment().lifecycleScope.request(
//        api = {apiService.getUserInfo()},
//    )
//}

////定义5
//private class TestViewModelWithStateDialog : BaseRequestViewModelWithStateDialog() {
//    fun getUserInfo() {
//        request(
//            api = { apiService.getUserInfo() },
//            success = {
//
//            },
//            error = {},
//            special = {code: Int?, message: String?, data: UserInfoRespSimple? ->
//
//            },
//        )
//    }
//}

//使用
//private  class SimpleActivity:AppCompatActivity(){
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
////        val vm by viewModels<TestViewModelWithStateDialog>()
////        vm.getUserInfo()
//    }
//}