package com.permission

import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.kotlin.permission.PermissionUtil

class MainActivity : FragmentActivity() {
    protected override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.button_request_camera_permission).setOnClickListener(object :
            android.view.View.OnClickListener {
            override fun onClick(view: android.view.View?) {
                val isHavePermission = PermissionUtil.isHavePermission(
                    this@MainActivity,
                    PermissionUtil.TYPE.CAMERA
                )
                if (isHavePermission) {
                    //添加有权限需要进行的操作
                    Toast.makeText(this@MainActivity, "已获得权限", Toast.LENGTH_SHORT).show()
                } else {
                    //请求系统权限--无论是否勾选“不再询问”或拒绝都直接请求，在请求结果中处理被拒绝不能再次请求或可再次请求的情况。
                    PermissionUtil.startRequestPermission(
                        this@MainActivity,
                        PermissionUtil.TYPE.CAMERA
                    )
                }
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: kotlin.Int,
        permissions: kotlin.Array<kotlin.String>,
        grantResults: kotlin.IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val listener: PermissionUtil.PermissionGrantResultListener =
            object : PermissionUtil.PermissionGrantResultListener {
                override fun granted(type: PermissionUtil.TYPE) {
                    if (type == PermissionUtil.TYPE.CAMERA) {
                        Toast.makeText(this@MainActivity, "权限申请成功", Toast.LENGTH_SHORT).show()
                        //正常继续执行-需要的操作
                    }
                }

                override fun grantedFailed(type: PermissionUtil.TYPE) {
                    if (type == PermissionUtil.TYPE.CAMERA) {
                        Toast.makeText(
                            this@MainActivity,
                            "你可以再次申请系统权限",
                            Toast.LENGTH_SHORT
                        ).show()
                        //最佳处理，弹窗提示，并引导用户进入设置页授权
                    }
                }
            }
        PermissionUtil.onRequestPermissionsResult(
            this,
            requestCode,
            grantResults,
            PermissionUtil.TYPE.CAMERA,
            listener
        )
    }
}
