package com.nutown.kuanzhai.permissions;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by LSX on 2018/2/23.
 */

public class ToUnityPermissions {

    public ToUnityPermissions() {
    }

    public boolean IsPermissionGranted(Context activity, String permissionName) {
        return Build.VERSION.SDK_INT < 23 ? true : (activity == null ? false : activity.checkSelfPermission(permissionName) == 0);
    }

    public void RequestPermissionAsync(Context activity, String[] permissionNames, IPermissionRequestResult resultCallbacks) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity != null && permissionNames != null && resultCallbacks != null) {
                Fragment request = new PermissionFragment(resultCallbacks);
                Bundle bundle = new Bundle();
                bundle.putStringArray("PermissionNames", permissionNames);
                request.setArguments(bundle);
                FragmentTransaction fragmentTransaction = ((Activity) activity).getFragmentManager().beginTransaction();
                fragmentTransaction.add(0, request);
                fragmentTransaction.commit();
            }
        }
    }

    public void showToast(Context context,String txt){
        Toast.makeText(context,txt,Toast.LENGTH_SHORT).show();
    }

    interface IPermissionRequestResult {
        void OnPermissionGranted(String var1);

        void OnPermissionDenied(String var1);
    }

    //跳转设置界面
    public void goToSettingUi(final Context context) {
//        String sdk = android.os.Build.VERSION.SDK; // SDK号
//
//        String model = android.os.Build.MODEL; // 手机型号
//
//        String release = android.os.Build.VERSION.RELEASE; // android系统版本号
//        String brand = Build.BRAND;//手机厂商
//        if(!TextUtils.isEmpty(brand)){
//            if ("redmi".equals(brand.toLowerCase()) || "xiaomi".equals(brand.toLowerCase())) {
//                gotoMiuiPermission(context);//小米
//            } else if ("meizu".equals(brand.toLowerCase())) {
//                gotoMeizuPermission(context);
//            } else if ("huawei".equals(brand.toLowerCase()) || "honor".equals(brand.toLowerCase())) {
//                gotoHuaweiPermission(context);
//            } else {
//                context.startActivity(getAppDetailSettingIntent(context));
//            }
//        }
        context.startActivity(getAppDetailSettingIntent(context));
    }

//    private void gotoMiuiPermission(Context context) {
//        try { // MIUI 8
//            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
//            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
//            localIntent.putExtra("extra_pkgname", context.getPackageName());
//            context.startActivity(localIntent);
//        } catch (Exception e) {
//            try { // MIUI 5/6/7
//                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
//                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
//                localIntent.putExtra("extra_pkgname", context.getPackageName());
//                context.startActivity(localIntent);
//            } catch (Exception e1) { // 否则跳转到应用详情
//                context.startActivity(getAppDetailSettingIntent(context));
//            }
//        }
//    }
//
//    /**
//     * 跳转到魅族的权限管理系统
//     */
//    private void gotoMeizuPermission(Context context) {
//        try {
//            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
//            context.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            context.startActivity(getAppDetailSettingIntent(context));
//        }
//    }
//
//    /**
//     * 华为的权限管理页面
//     */
//    private void gotoHuaweiPermission(Context context) {
//        try {
//            Intent intent = new Intent();
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
//            intent.setComponent(comp);
//            context.startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//            context.startActivity(getAppDetailSettingIntent(context));
//        }
//
//    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面</span>）
     *
     * @return
     */
    private Intent getAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }



}
