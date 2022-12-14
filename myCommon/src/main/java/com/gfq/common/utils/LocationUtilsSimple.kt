package com.gfq.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.gfq.common.helper.PermissionHelper
import com.gfq.common.helper.load.LoadingView
import com.gfq.common.system.loge
import com.gfq.common.toast.toast

/**
 *  2022/12/14 10:08
 * @author gaofuq
 * @description
 */
internal class LocationUtilsSimple:FragmentActivity() {
    private val loadingView by lazy { LoadingView(this) }

    private val onLocationChangeListener = LocationUtils.OnLocationChangeListener { location ->
        location?.let {
            val country = LocationUtils.getCountryName(it.latitude, it.longitude)
            val local = LocationUtils.getLocality(it.latitude, it.longitude)
            val address = LocationUtils.getAddress(it.latitude, it.longitude)
            val street = LocationUtils.getStreet(it.latitude, it.longitude)
            loge("country = $country")
            loge("local = $local")
            loge("address = $address")
            loge("street = $street")
//            actBinding.tvLocation.text = street
            loadingView.dismiss()
        }
    }

    @SuppressLint("MissingPermission")
    private val locationClickListener = View.OnClickListener {
        if (!LocationUtils.isGpsEnabled()) {
            toast("Gps不可用")
            return@OnClickListener
        }
        if (!LocationUtils.isLocationEnabled()) {
            toast("定位服务不可用")
            return@OnClickListener
        }
//        PermissionHelper.request(
//            this,
//            PermissionExplain.explainStorage,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            onAllGranted = {
//                loadingView.show("定位中...")
//                LocationUtils.register(1000 * 60 * 2L, 1000, onLocationChangeListener)
//            },
//            onDenied = {
//                loadingView.dismiss()
//            })
        //1.定位权限申请
        //2.发起定位
        //LocationUtils.register(1000 * 60 * 2L, 1000, onLocationChangeListener)

    }

    @SuppressLint("MissingPermission")
    override fun onDestroy() {
        super.onDestroy()
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationUtils.unregister()
        }
    }
}