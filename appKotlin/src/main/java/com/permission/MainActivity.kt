package com.permission

import android.widget.Toast
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {
    protected override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById(R.id.button_request_camera_permission).setOnClickListener(object :
            android.view.View.OnClickListener {
            override fun onClick(view: android.view.View?) {
                val isHavePermission = com.java.permission.PermissionUtil.isHavePermission(
                    this@MainActivity,
                    com.java.permission.PermissionUtil.TYPE.CAMERA
                )
                if (isHavePermission) {
                    //添加有权限需要进行的操作
                    Toast.makeText(this@MainActivity, "已获得权限", Toast.LENGTH_SHORT).show()
                } else {
                    //请求系统权限--无论是否勾选“不再询问”或拒绝都直接请求，在请求结果中处理被拒绝不能再次请求或可再次请求的情况。
                    com.java.permission.PermissionUtil.startRequestPermission(
                        this@MainActivity,
                        com.java.permission.PermissionUtil.TYPE.CAMERA
                    )
                }
            }
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: kotlin.Int,
        permissions: kotlin.Array<kotlin.String?>?,
        grantResults: kotlin.IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val listener: com.java.permission.PermissionUtil.PermissionGrantResutListener =
            object : com.java.permission.PermissionUtil.PermissionGrantResutListener {
                override fun grantSuccess(type: com.java.permission.PermissionUtil.TYPE) {
                    if (type == com.java.permission.PermissionUtil.TYPE.CAMERA) {
                        Toast.makeText(this@MainActivity, "权限申请成功", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun grantFailed(type: com.java.permission.PermissionUtil.TYPE) {
                    if (type == com.java.permission.PermissionUtil.TYPE.CAMERA) {
                        Toast.makeText(
                            this@MainActivity,
                            "你可以再次申请系统权限",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun shouldNotShowRequestPermissoin(type: com.java.permission.PermissionUtil.TYPE) {
                    if (type == com.java.permission.PermissionUtil.TYPE.CAMERA) {
                        Toast.makeText(
                            this@MainActivity,
                            "你不可以再次申请系统权限",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        com.java.permission.PermissionUtil.onRequestPermissionsResult(
            this,
            requestCode,
            grantResults,
            com.java.permission.PermissionUtil.TYPE.CAMERA,
            listener
        )
    }
}
