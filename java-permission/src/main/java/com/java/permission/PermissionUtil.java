package com.java.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * 动态申请权限辅助工具
 */
public final class PermissionUtil {
    /**
     * 动态申请所需的权限集合 9组权限
     */
    //调用相机权限
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};

    //调用读写sdCard权限
    private static final String[] STORAGE_PERMISSION = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //调用联系人权限
    private static final String[] CONTACTS_PERMISSION = new String[]{
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_CONTACTS
    };

    //调用拨打电话等权限
    private static final String[] PHONE_PERMISSION = new String[]{
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.USE_SIP
    };

    //调用Calendar时间读写权限
    private static final String[] CALENDAR_PERMISSION = new String[]{
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    //调用传感器权限
    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    private static final String[] SENSORS_PERMISSION = new String[]{Manifest.permission.BODY_SENSORS};

    //调用位置信息权限
    private static final String[] LOCATION_PERMISSION = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    //调用位置信息权限
    @RequiresApi(Build.VERSION_CODES.Q)
    private static final String[] LOCATION_BACK_PERMISSION =
            new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION};

    //调用录音录像权限
    public static final String[] RECORD_AUDIO_PERMISSION = new String[]{Manifest.permission.RECORD_AUDIO};

    //调用短信读写权限
    public static final String[] SMS_PERMISSION = new String[]{
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_WAP_PUSH,
            Manifest.permission.RECEIVE_MMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.SEND_SMS
    };

    //android 13 通知权限
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public static final String[] POST_NOTIFICATIONS =
            new String[]{Manifest.permission.POST_NOTIFICATIONS};

    //android 13 媒体权限
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public static final String[] ANDROID_13_MEDIA_IMAGES_AND_VIDEOS_PERMISSSION = new String[]{
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    public static final String[] ANDROID_13_MEDIA_AUDIOS_PERMISSSION =
            new String[]{Manifest.permission.READ_MEDIA_AUDIO};

    private PermissionUtil() {
    }

    /**
     * 判断是否已获取到权限
     */
    public static boolean isHavePermission(Context context, TYPE type) {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 检查该权限是否已经获取
            String[] permissions = getPerMissions(type);
            int result = ContextCompat.checkSelfPermission(context, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (result != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                return false;
            }
        }
        return true;
    }

    public static void startRequestPermission(Activity context, TYPE type) {
        String[] permissions = getPerMissions(type);
        ActivityCompat.requestPermissions(context, permissions, type.value);
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
    public static void onRequestPermissionsResult(
            Activity context,
            int requestCode,
            int[] grantResults,
            TYPE type,
            PermissionGrantResutListener listener) {
        if (requestCode == type.value && Build.VERSION.SDK_INT >= 23 && listener != null) {
            Log.e("--------------->","result length = "+grantResults.length);

            if (grantResults.length == 0) {
                return;
            }
            Log.e("--------------->","result = "+grantResults[0]);

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                listener.grantSuccess(type);
            } else {
                if(shouldShowRequestPermission(context,type)){
                    listener.grantFailedCanShowRequestPermission(type);
                }else{
                    listener.grantFailedCanNotShowRequestPermission(type);
                }

            }
        }
    }

    private static String[] getPerMissions(TYPE type) {
        String[] permissions = new String[0];
        switch (type.getValue()) {
            case 1:
                permissions = CAMERA_PERMISSION;
                break;
            case 2:
                permissions = STORAGE_PERMISSION;
                break;
            case 3:
                permissions = CONTACTS_PERMISSION;
                break;
            case 4:
                permissions = PHONE_PERMISSION;
                break;
            case 5:
                permissions = CALENDAR_PERMISSION;
                break;
            case 6:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                    permissions = SENSORS_PERMISSION;
                }
                break;
            case 7:
                permissions = LOCATION_PERMISSION;
                break;
            case 8:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
                    permissions = LOCATION_BACK_PERMISSION;
                }
                break;
            case 9:
                permissions = RECORD_AUDIO_PERMISSION;
                break;
            case 10:
                permissions = SMS_PERMISSION;
                break;
            case 11:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                    permissions = POST_NOTIFICATIONS;
                }
                break;
            case 12:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                    permissions = ANDROID_13_MEDIA_IMAGES_AND_VIDEOS_PERMISSSION;
                }
                break;
            case 13:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                    permissions = ANDROID_13_MEDIA_AUDIOS_PERMISSSION;
                }
                break;
            default:
                break;
        }
        return permissions;
    }

    /**
     * 是否应该显示请求权限判断
     * true：表示建议向用户说明权限用途
     * false：可能是首次请求、用户关闭弹窗或拒绝后不再询问等情况
     * 此返回值不能判断是否可以再次请求权限
     * 注意：此方法只能在onRequestPermissionsResult函数中判断才准确，在请求权限前判断不准确
     * 所以，应在调用请求权限后，在onRequestPermissionsResult进行判断
     */
    public static boolean shouldShowRequestPermission(Activity context, TYPE type) {
        String[] permissions = getPerMissions(type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.shouldShowRequestPermissionRationale(permissions[0]);
        }
        return false;
    }

    /**
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;
    }

    /**
     * 跳转到app设置 用户拒绝授权后跳转
     * @param context
     */
    public static void toAppSelfSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(intent);
    }

    //获取权限类型
    public enum TYPE {
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
        ANDROID_13_MEDIA_AUDIOS(13);

        private int value;

        TYPE(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    /**
     * 授权结果监听
     */
    public interface PermissionGrantResutListener {
        void grantSuccess(TYPE type);

        void grantFailedCanShowRequestPermission(TYPE type);

        void grantFailedCanNotShowRequestPermission(TYPE type);
    }
}
