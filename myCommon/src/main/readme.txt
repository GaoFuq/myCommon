说明：
一部分参考：
  https://github.com/like5188/Common


    apply plugin: 'kotlin-kapt'

  buildFeatures{
          dataBinding = true
  }

   implementation 'androidx.recyclerview:recyclerview:1.2.1'
      implementation "androidx.core:core-ktx:1.5.0"
      implementation 'androidx.appcompat:appcompat:1.3.0'
      implementation 'com.google.android.material:material:1.4.0'
      implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version"
      implementation 'androidx.constraintlayout:constraintlayout:2.1.2'

      //协程
      implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"
      implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3"

      //JetPack ktx
      implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
      implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
      implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.3.1"
      implementation 'androidx.fragment:fragment-ktx:1.3.4'
      implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
      implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
      implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.3.1"
      def nav_version = "2.3.5"
      implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
      implementation "androidx.navigation:navigation-ui-ktx:$nav_version"


      //网络
      implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
      implementation 'com.squareup.retrofit2:retrofit:2.9.0'
      implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
      implementation 'com.google.code.gson:gson:2.8.6'
      implementation 'com.squareup.okhttp3:okhttp:4.8.0'
      implementation 'com.alibaba:fastjson:1.1.72.android'
      implementation 'org.jetbrains.kotlin:kotlin-reflect:1.5.20'
  //    implementation "com.squareup.retrofit2:adapter-rxjava3:2.9.0"

      //--------------------- 常用第三方 -------------------
      implementation 'com.youth.banner:banner:2.1.0'
      implementation 'com.github.lihangleo2:ShadowLayout:3.2.0'
      implementation 'com.github.mmin18:realtimeblurview:1.2.1'
      implementation 'de.hdodenhof:circleimageview:3.1.0'
      implementation 'com.github.bumptech.glide:glide:4.11.0'
      annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
      implementation 'com.orhanobut:logger:2.2.0'
      //bugly
      implementation 'com.tencent.bugly:crashreport:3.3.9'
      implementation 'com.tencent.bugly:nativecrashreport:3.7.3'
      //AgentWeb
      implementation 'com.github.Justson.AgentWeb:agentweb-core:v4.1.9-androidx' // (必选)
      implementation 'com.github.Justson.AgentWeb:agentweb-filechooser:v4.1.9-androidx' // (可选)
      implementation 'com.github.Justson:Downloader:v4.1.9-androidx' // (可选)
      // 支付
      implementation 'com.xgr.easypay:EasyPay:2.0.5'   // 基类库，必选
      implementation 'com.xgr.easypay:wechatpay:2.0.5' // 微信支付，可选
      implementation 'com.xgr.easypay:alipay:2.0.5'    // 支付宝支付，可选
      implementation 'com.xgr.easypay:unionpay:2.0.5'  // 银联支付，可选
      // PictureSelector
      implementation 'com.github.LuckSiege.PictureSelector:picture_library:v2.6.1'
      // LiveDataBus
      implementation 'com.github.like5188.LiveDataBus:livedatabus:2.1.9'
      implementation 'com.github.like5188.LiveDataBus:livedatabus_annotations:2.1.9'
      kapt 'com.github.like5188.LiveDataBus:livedatabus_compiler:2.1.9'//apply plugin: 'kotlin-kapt'
      compileOnly 'com.squareup:javapoet:1.13.0'// 自动生成源码的库
      // 代码写drawable
      implementation 'com.github.forJrking:DrawableDsl:0.0.3'
      // exoplayer
  //    implementation 'com.google.android.exoplayer:exoplayer-core:2.15.1'