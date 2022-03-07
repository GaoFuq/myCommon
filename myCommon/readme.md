# 通用类库
常用依赖
```groovy
  implementation 'androidx.core:core-ktx:1.5.0'
    implementation 'androidx.appcompat:appcompat:1.3.0'
    implementation 'com.google.android.material:material:1.4.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.2'
    
    // JetPack livedata viewModel
    implementation "androidx.lifecycle:lifecycle-extensions:2.2.0"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.3.1"
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
    
    // JetPack navigation
    implementation "androidx.navigation:navigation-fragment-ktx:2.4.1"
    implementation "androidx.navigation:navigation-ui-ktx:2.4.1"
    implementation 'androidx.fragment:fragment-ktx:1.3.5'
    
    // JetPack coroutines 协程
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3"
    implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3"
    
    // kotlin 反射
    implementation 'org.jetbrains.kotlin:kotlin-reflect:1.5.20'

    implementation 'com.github.GaoFuq:BaseRvAdapter:1.6.2'
    implementation 'com.github.GaoFuq:myCommon:1.5.7'

    

    implementation  'com.scwang.smart:refresh-layout-kernel:2.0.3'
    implementation  'com.scwang.smart:refresh-footer-classics:2.0.3'    //经典加载
    implementation  'com.scwang.smart:refresh-header-classics:2.0.3'    //经典刷新头
    implementation  'com.scwang.smart:refresh-header-radar:2.0.3'       //雷达刷新头
    implementation  'com.scwang.smart:refresh-header-falsify:2.0.3'     //虚拟刷新头
    implementation  'com.scwang.smart:refresh-header-material:2.0.3'    //谷歌刷新头
    implementation  'com.scwang.smart:refresh-header-two-level:2.0.3'   //二级刷新头
    implementation  'com.scwang.smart:refresh-footer-ball:2.0.3'        //球脉冲加载
    implementation  'com.scwang.smart:refresh-footer-classics:2.0.3'    //经典加载


    // 网络
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:okhttp:4.8.0'
//    implementation "com.squareup.retrofit2:adapter-rxjava3:2.9.0"
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
    implementation 'com.alibaba:fastjson:1.1.72.android'
    implementation 'com.google.code.gson:gson:2.8.6'


    //图片加载
    implementation 'com.github.bumptech.glide:glide:4.11.0'
//    annotationProcessor 'com.github.bumptech.glide:compiler:4.11.0'
    kapt 'com.github.bumptech.glide:compiler:4.11.0' // gradle 7+ 使用 kapt
    
```