package com.gfq.common.net.simple

/**
 *  2022/1/12 15:25
 * @auth gaofuq
 * @description
 */
//定义1
//private interface Api {
//    @Headers("${ApiDescInterceptor.MethodName}:getUserInfo")
//    @POST("api/getUserInfo")
//    suspend fun getUserInfo(): AbsResponseSimple<UserInfoRespSimple>
//}

////定义2
//private val okClient = OkHttpClient.Builder()
//    .addInterceptor(HttpLogInterceptor(listOf(Api::class.java)))
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
//

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