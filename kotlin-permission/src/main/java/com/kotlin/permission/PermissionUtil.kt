package com.kotlin.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES.Q
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * 动态申请权限辅助工具
 */
object PermissionUtil {
    /**
     * 动态申请所需的权限集合 9组权限
     */
    //调用相机权限
    val CAMERA_PERMISSION: Array<String> = arrayOf<String>(Manifest.permission.CAMERA)

    //调用读写sdCard权限
    val STORAGE_PERMISSION: Array<String> = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    //调用联系人权限
    val CONTACTS_PERMISSION: Array<String> = arrayOf<String>(
        Manifest.permission.WRITE_CONTACTS,
        Manifest.permission.GET_ACCOUNTS,
        Manifest.permission.READ_CONTACTS
    )

    //调用拨打电话等权限
    val PHONE_PERMISSION: Array<String> = arrayOf<String>(
        Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.WRITE_CALL_LOG,
        Manifest.permission.USE_SIP
    )

    //调用Calendar时间读写权限
    val CALENDAR_PERMISSION: Array<String> =
        arrayOf<String>(Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR)

    //调用传感器权限
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    val SENSORS_PERMISSION: Array<String> = arrayOf<String>(Manifest.permission.BODY_SENSORS)

    //调用位置信息权限
    val LOCATION_PERMISSION: Array<String> = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    //调用位置信息权限
    @RequiresApi(Build.VERSION_CODES.Q)
    val LOCATION_BACK_PERMISSION: Array<String> =
        arrayOf<String>(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    //调用录音录像权限
    val RECORD_AUDIO_PERMISSION: Array<String> = arrayOf<String>(Manifest.permission.RECORD_AUDIO)

    //调用短信读写权限
    val SMS_PERMISSION: Array<String> = arrayOf<String>(
        Manifest.permission.READ_SMS,
        Manifest.permission.RECEIVE_WAP_PUSH,
        Manifest.permission.RECEIVE_MMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.SEND_SMS
    )

    //android 13 通知权限
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val POST_NOTIFICATIONS: Array<String> = arrayOf<String>(Manifest.permission.POST_NOTIFICATIONS)

    //android 13 媒体权限
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val ANDROID_13_MEDIA_IMAGES_AND_VIDEOS_PERMISSSION: Array<String> =
        arrayOf<String>(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val ANDROID_13_MEDIA_AUDIOS_PERMISSSION: Array<String> =
        arrayOf<String>(Manifest.permission.READ_MEDIA_AUDIO)
    /**
     * 判断是否已获取到权限
     */
    @JvmStatic
    fun isHavePermission(context: Context, type: TYPE): Boolean {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            val permissions = getPerMissions(type)
            val i = ContextCompat.checkSelfPermission(context, permissions[0])
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                return false
            }
        }
        return true
    }

    fun startRequestPermission(context: Activity, type: TYPE) {
        val permissions = getPerMissions(type)
        ActivityCompat.requestPermissions(context, permissions, type.value)
    }

    /***
     * 权限
     * @param requestCode
     * @param grantResults
     * // 用户权限 申请 的回调方法
     * @Override
     * public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
     * super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     */
    fun onRequestPermissionsResult(
        context: Activity,
        requestCode: Int,
        grantResults: IntArray,
        type: TYPE,
        listener: PermissionGrantResutListener?
    ) {
        if (requestCode == type.value && Build.VERSION.SDK_INT >= 23 && listener != null) {
            if (grantResults.isEmpty()) {
                listener.grantFailed(type)
                return
            }

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listener.grantSuccess(type)
            } else {
                // Permission results cannot distinguish a dismissed prompt from a permanent denial.
                listener.grantFailed(type)
            }
        }
    }

    private fun getPerMissions(type: TYPE): Array<String> {
        var permissions: Array<String> = emptyArray()
        when (type.value) {
            1 -> permissions = CAMERA_PERMISSION
            2 -> permissions = STORAGE_PERMISSION
            3 -> permissions = CONTACTS_PERMISSION
            4 -> permissions = PHONE_PERMISSION
            5 -> permissions = CALENDAR_PERMISSION
            6 -> if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    permissions = SENSORS_PERMISSION
                }
            7 -> permissions = LOCATION_PERMISSION
            8 -> if(Build.VERSION.SDK_INT>Build.VERSION_CODES.Q) {
                    permissions =  LOCATION_BACK_PERMISSION
                }
            9 -> permissions = RECORD_AUDIO_PERMISSION
            10 -> permissions = SMS_PERMISSION
            11 -> if(Build.VERSION.SDK_INT>Build.VERSION_CODES.TIRAMISU){
                permissions = POST_NOTIFICATIONS
            }
            12 -> if(Build.VERSION.SDK_INT>Build.VERSION_CODES.TIRAMISU){
                permissions = ANDROID_13_MEDIA_IMAGES_AND_VIDEOS_PERMISSSION
            }
            13 -> if(Build.VERSION.SDK_INT>Build.VERSION_CODES.TIRAMISU){
                permissions = ANDROID_13_MEDIA_AUDIOS_PERMISSSION
            }
        }
        return permissions
    }
    /**
     * 是否应该显示请求权限判断
     * true：表示建议向用户说明权限用途
     * false：可能是首次请求、用户关闭弹窗或拒绝后不再询问等情况
     * 此返回值不能判断是否可以再次请求权限
     * 注意：此方法只能在onRequestPermissionsResult函数中判断才准确，在请求权限前判断不准确
     * 所以，应在调用请求权限后，在onRequestPermissionsResult进行判断
     */
    fun shouldShowRequestPermission(context: Activity, type: TYPE): Boolean {
        val permissions = getPerMissions(type)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val i = ContextCompat.checkSelfPermission(context, permissions[0])
            return context.shouldShowRequestPermissionRationale(permissions[0])
        }
        return false
    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    fun isLocServiceEnable(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (gps || network) {
            return true
        }
        return false
    }

    /**
     * 跳转到app设置 用户拒绝授权后跳转
     * @param context
     */
    fun toAppSelfSetting(context: Context) {
        val mIntent = Intent()
        mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS")
            mIntent.setData(Uri.fromParts("package", context.getPackageName(), null))
        } else if (Build.VERSION.SDK_INT <= 8) {
            mIntent.setAction(Intent.ACTION_VIEW)
            mIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails")
            mIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName())
        }
        context.startActivity(mIntent)
    }

    //获取权限类型
    //获取权限类型
    enum class TYPE(var value: Int) {
        CAMERA(1),  //调用相机权限
        STORAGE(2),  //调用读写sdCard权限
        CONTACTS(3),  //调用联系人权限
        PHONE(4),  //调用拨打电话等权限
        CALENDAR(5),  //调用Calendar时间读写权限
        SENSORS(6),  //调用传感器权限
        LOCATION(7),  //调用位置信息权限
        LOCATION_BACK(8),  //后台调用位置信息权限
        RECORD_AUDIO(9),  //调用录音录像权限
        SMS(10),  //调用短信读写权限
        POST_NOTIFICATIONS(11),
        ANDROID_13_MEDIA_IMAGES_AND_VIDEOS(12),
        ANDROID_13_MEDIA_AUDIOS(13)
    }

    /**
     * 授权结果监听
     */
    interface PermissionGrantResutListener {
        fun grantSuccess(var1: TYPE?)

        fun grantFailed(var1: TYPE?)

        fun shouldNotShowRequestPermissoin(var1: TYPE?)
    }
}
