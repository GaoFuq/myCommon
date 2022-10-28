package com.gfq.common.helper

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.gfq.common.R
import com.gfq.common.databinding.PermCustomDialogLayoutBinding
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.dialog.RationaleDialog

/**
 *  2022/10/8 16:19
 * @auth gaofuq
 * @description
 */
object PermissionHelper {
    @JvmStatic
    fun request(
        context: FragmentActivity,
        explainMessage: String,
        vararg permissions: String,
        onAllGranted: () -> Unit,
        onDenied: () -> Unit,
    ) {
        PermissionX.init(context)
            .permissions(*permissions)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
//                //beforeRequest：当第一次触发onExplainRequestReason时为true，否则为false
                if (beforeRequest) {
                    val dialog = CustomDialog(context, explainMessage, deniedList)
                    scope.showRequestReasonDialog(dialog)
                } else {
                    val dialog = CustomDialog(context, "您拒绝了下列权限，是否在设置中手动开启", deniedList)
                    scope.showRequestReasonDialog(dialog)
                }
            }
            .onForwardToSettings { scope, deniedList ->
                val message = "将前往设置界面手动开启"
                val dialog = CustomDialog(context, message, deniedList)
                dialog.negativeButton.text="取消"
                dialog.positiveButton.text="确定"
                scope.showForwardToSettingsDialog(dialog)
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    onAllGranted()
                } else {
                    onDenied()
//                    Toast.makeText(context, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
            }
    }

    @JvmStatic
    fun request(
        fragment:  Fragment,
        explainMessage: String,
        vararg permissions: String,
        onAllGranted: () -> Unit,
        onDenied: () -> Unit,
    ) {
        PermissionX.init(fragment)
            .permissions(*permissions)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
                val context = fragment.context
                context?:return@onExplainRequestReason
//                //beforeRequest：当第一次触发onExplainRequestReason时为true，否则为false
                if (beforeRequest) {
                    val dialog = CustomDialog(context, explainMessage, deniedList)
                    scope.showRequestReasonDialog(dialog)
                } else {
                    val dialog = CustomDialog(context, "您拒绝了下列权限，是否在设置中手动开启", deniedList)
                    scope.showRequestReasonDialog(dialog)
                }
            }
            .onForwardToSettings { scope, deniedList ->
                val context = fragment.context
                context?:return@onForwardToSettings
                val message = "将前往设置界面手动开启"
                val dialog = CustomDialog(context, message, deniedList)
                dialog.negativeButton.text="取消"
                dialog.positiveButton.text="确定"
                scope.showForwardToSettingsDialog(dialog)
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    onAllGranted()
                } else {
                    onDenied()
//                    Toast.makeText(context, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
            }
    }

    @JvmStatic
    fun requestPhoneState(activity: FragmentActivity,next:()->Unit){
        request(activity,
            "该权限用于\n1.在移动运营商双卡情况下，更精准的获取数据流量卡的运营商类型。\n2.更精准的广告推送。\n3.反作弊。\n建议您开启该权限，优化使用体验。",
            Manifest.permission.READ_PHONE_STATE,
            onAllGranted = next,
            onDenied ={
//                CacheManager.isDeniedPhoneStatePerm = true
                next()
            }
        )
    }

    @JvmStatic
    fun requestStorageForModifyUserHead(activity: FragmentActivity,next:()->Unit){
        request(activity,
            "修改用户头像功能需要下列权限。建议您开启该权限。",
            Manifest.permission.READ_EXTERNAL_STORAGE,
            onAllGranted =next,
            onDenied = {})
    }

    @JvmStatic
    fun requestStorageForSaveImage(fragment:  Fragment, next:()->Unit){
        request(fragment,
            "保存图片功能需要下列权限。建议您开启该权限。",
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            onAllGranted =next,
            onDenied = {})
    }

    @JvmStatic
    fun requestCallPhoneForService(activity: FragmentActivity,next:()->Unit){
        request(activity,
            "拨打电话功能需要下列权限。建议您开启该权限。",
            Manifest.permission.CALL_PHONE,
            onAllGranted =next,
            onDenied = {})
    }
}

@TargetApi(30)
open class CustomDialog(context: Context, val message: String, val permissions: List<String>,style:Int = R.style.CustomDialog) :
    RationaleDialog(context, style) {

    private val permissionMap =
        mapOf(Manifest.permission.READ_CALENDAR to Manifest.permission_group.CALENDAR,
            Manifest.permission.WRITE_CALENDAR to Manifest.permission_group.CALENDAR,
            Manifest.permission.READ_CALL_LOG to Manifest.permission_group.CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG to Manifest.permission_group.CALL_LOG,
            Manifest.permission.PROCESS_OUTGOING_CALLS to Manifest.permission_group.CALL_LOG,
            Manifest.permission.CAMERA to Manifest.permission_group.CAMERA,
            Manifest.permission.READ_CONTACTS to Manifest.permission_group.CONTACTS,
            Manifest.permission.WRITE_CONTACTS to Manifest.permission_group.CONTACTS,
            Manifest.permission.GET_ACCOUNTS to Manifest.permission_group.CONTACTS,
            Manifest.permission.ACCESS_FINE_LOCATION to Manifest.permission_group.LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION to Manifest.permission_group.LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION to Manifest.permission_group.LOCATION,
            Manifest.permission.RECORD_AUDIO to Manifest.permission_group.MICROPHONE,
            Manifest.permission.READ_PHONE_STATE to Manifest.permission_group.PHONE,
            Manifest.permission.READ_PHONE_NUMBERS to Manifest.permission_group.PHONE,
            Manifest.permission.CALL_PHONE to Manifest.permission_group.PHONE,
            Manifest.permission.ANSWER_PHONE_CALLS to Manifest.permission_group.PHONE,
            Manifest.permission.ADD_VOICEMAIL to Manifest.permission_group.PHONE,
            Manifest.permission.USE_SIP to Manifest.permission_group.PHONE,
            Manifest.permission.ACCEPT_HANDOVER to Manifest.permission_group.PHONE,
            Manifest.permission.BODY_SENSORS to Manifest.permission_group.SENSORS,
            Manifest.permission.ACTIVITY_RECOGNITION to Manifest.permission_group.ACTIVITY_RECOGNITION,
            Manifest.permission.SEND_SMS to Manifest.permission_group.SMS,
            Manifest.permission.RECEIVE_SMS to Manifest.permission_group.SMS,
            Manifest.permission.READ_SMS to Manifest.permission_group.SMS,
            Manifest.permission.RECEIVE_WAP_PUSH to Manifest.permission_group.SMS,
            Manifest.permission.RECEIVE_MMS to Manifest.permission_group.SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE to Manifest.permission_group.STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE to Manifest.permission_group.STORAGE,
            Manifest.permission.ACCESS_MEDIA_LOCATION to Manifest.permission_group.STORAGE
        )

    private val groupSet = HashSet<String>()

    private val dialogBinding =
        DataBindingUtil.inflate<PermCustomDialogLayoutBinding>(
            LayoutInflater.from(context),
            R.layout.perm_custom_dialog_layout,
            null,
            false
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dialogBinding.root)
        dialogBinding.messageText.text = message
        buildPermissionsLayout()
        window?.let {
            val param = it.attributes
            val width = (context.resources.displayMetrics.widthPixels * 0.8).toInt()
            val height = param.height
            it.setLayout(width, height)
        }
    }

    override fun getNegativeButton(): TextView {
        return dialogBinding.negativeBtn
    }

    override fun getPositiveButton(): TextView {
        return dialogBinding.positiveBtn
    }

    override fun getPermissionsToRequest(): List<String> {
        return permissions;
    }

    private fun buildPermissionsLayout() {
        for (permission in permissions) {
            val permissionGroup = permissionMap[permission]
            if (permissionGroup != null && !groupSet.contains(permissionGroup)) {
                val textView = LayoutInflater.from(context)
                    .inflate(R.layout.perm_item, dialogBinding.permissionsLayout, false) as TextView
                textView.text = context.packageManager.getPermissionGroupInfo(permissionGroup, 0)
                    .loadLabel(context.packageManager)
                dialogBinding.permissionsLayout.addView(textView)
                groupSet.add(permissionGroup)
            }
        }
    }

}
