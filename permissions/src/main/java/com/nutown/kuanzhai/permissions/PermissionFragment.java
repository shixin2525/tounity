package com.nutown.kuanzhai.permissions;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by LSX on 2018/2/23.
 */

public class PermissionFragment extends Fragment {
    public static final String PERMISSION_NAMES = "PermissionNames";
    private static final int PERMISSIONS_REQUEST_CODE = 15887;
    private final ToUnityPermissions.IPermissionRequestResult m_ResultCallbacks;

    public PermissionFragment(ToUnityPermissions.IPermissionRequestResult resultCallbacks) {
        this.m_ResultCallbacks = resultCallbacks;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.m_ResultCallbacks == null) {
            this.getFragmentManager().beginTransaction().remove(this).commit();
        } else {
            String[] permissionNames = this.getArguments().getStringArray("PermissionNames");
            this.requestPermissions(permissionNames, PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            for (int i = 0; i < permissions.length && i < grantResults.length; ++i) {
                if (grantResults[i] == 0) {
                    this.m_ResultCallbacks.OnPermissionGranted(permissions[i]);
                } else {
                    this.m_ResultCallbacks.OnPermissionDenied(permissions[i]);
                }
            }

            FragmentTransaction fragmentTransaction = this.getFragmentManager().beginTransaction();
            fragmentTransaction.remove(this);
            fragmentTransaction.commit();
        }
    }


}
