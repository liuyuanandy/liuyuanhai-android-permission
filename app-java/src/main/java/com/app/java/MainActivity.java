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
        findViewById(R.id.button_request_camera_permission).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean  isHavePermission = PermissionUtil.isHavePermission(MainActivity.this, PermissionUtil.TYPE.CAMERA);
                if(isHavePermission){
                    //添加有权限需要进行的操作
                    Toast.makeText(MainActivity.this,"已获得权限",Toast.LENGTH_SHORT).show();
                }else{
                    //请求权限--无论是否勾选“不再询问”或拒绝 都直接请求。
                    PermissionUtil.startRequestPermission(MainActivity.this,PermissionUtil.TYPE.CAMERA);
                }
            }
        });
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.PermissionGrantResultListener listener = new PermissionUtil.PermissionGrantResultListener() {
            public void granted(PermissionUtil.TYPE type) {
                if (type.equals(PermissionUtil.TYPE.CAMERA)) {
                    Toast.makeText(MainActivity.this,"权限申请成功",Toast.LENGTH_SHORT).show();
                    //正常继续执行需要的操作
                }
            }

            public void grantedFailed(PermissionUtil.TYPE type) {
                if (type.equals(PermissionUtil.TYPE.CAMERA)) {
                    Toast.makeText(MainActivity.this,"未获得授权",Toast.LENGTH_SHORT).show();
                    //最佳处理，弹窗提示，并引导用户进入设置页授权
                }
            }
        };
        PermissionUtil.onRequestPermissionsResult(this, requestCode, grantResults, PermissionUtil.TYPE.CAMERA, listener);
    }
}
