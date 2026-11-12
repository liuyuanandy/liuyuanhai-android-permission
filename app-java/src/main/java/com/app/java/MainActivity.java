package com.app.java;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.java.permission.PermissionUtil;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("MainActivity", "Back pressed");
                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
                setEnabled(true);
            }
        });
        findViewById(R.id.button_request_camera_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean  isHavePermission = PermissionUtil.isHavePermission(MainActivity.this, PermissionUtil.TYPE.CAMERA);
                if(isHavePermission){
                    //添加有权限需要进行的操作
                    Toast.makeText(MainActivity.this,"已获得权限",Toast.LENGTH_SHORT).show();
                }else{
                    //请求系统权限--无论是否勾选“不再询问”或拒绝都直接请求，在请求结果中处理被拒绝不能再次请求或可再次请求的情况。
                    PermissionUtil.startRequestPermission(MainActivity.this,PermissionUtil.TYPE.CAMERA);
                }
            }
        });
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.PermissionGrantResutListener listener = new PermissionUtil.PermissionGrantResutListener() {
            public void grantSuccess(PermissionUtil.TYPE type) {
                if (type.equals(PermissionUtil.TYPE.CAMERA)) {
                    Toast.makeText(MainActivity.this,"权限申请成功",Toast.LENGTH_SHORT).show();

                }
            }

            public void grantFailedCanShowRequestPermission(PermissionUtil.TYPE type) {
                if (type.equals(PermissionUtil.TYPE.CAMERA)) {
                    Toast.makeText(MainActivity.this,"可以再次申请系统权限",Toast.LENGTH_SHORT).show();
                }

            }

            public void grantFailedCanNotShowRequestPermission(PermissionUtil.TYPE type) {
                if (type.equals(PermissionUtil.TYPE.CAMERA)) {
                    Toast.makeText(MainActivity.this,"不可以再次申请系统权限",Toast.LENGTH_SHORT).show();
                }
            }
        };
        PermissionUtil.onRequestPermissionsResult(this, requestCode, grantResults, PermissionUtil.TYPE.CAMERA, listener);
    }
}
