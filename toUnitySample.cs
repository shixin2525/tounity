using LitJson;
using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class PlatformAndroid : PlatformFormat
{
    private AndroidJavaClass mAndroidJavaClass;
    private static AndroidJavaObject mAndroidJavaObject;
    private AndroidJavaObject mGaoDeLocation;
    private AndroidJavaObject mGaoDeNavigation;
    private AndroidJavaObject mPermissions;
    private static AndroidJavaObject mwegi;
    private static AndroidJavaObject myApp;
    public override string getPath()
    {
        return Application.persistentDataPath;
    }
    void Awake()
    {
        mAndroidJavaClass = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
        mAndroidJavaObject = mAndroidJavaClass.GetStatic<AndroidJavaObject>("currentActivity");
        mGaoDeLocation = new AndroidJavaObject("com.nutown.kuanzhai.gaode.GaoDeLocation");
        mGaoDeNavigation = new AndroidJavaObject("com.nutown.kuanzhai.gaode.GaoDeNavigation");
        mPermissions = new AndroidJavaObject("com.nutown.kuanzhai.permissions.ToUnityPermissions");
        mwegi = new AndroidJavaObject("cn.nutown.widget.SystemWidget");
        myApp = new AndroidJavaObject("cn.nutown.kuanzhai.MainActivity");
    }
    /*
     * 高德操作
     ***/
    public override void startLocation(Action<LocationBean> location)
    {
        Debug.Log("获取经纬度");
        string shk1 = mGaoDeLocation.Call<string>("getSHA1", mAndroidJavaObject);
        mGaoDeLocation.Call("startGetLocation", mAndroidJavaObject, new LocationCallBack(location));
    }

    public override void stopLocation()
    {
        mGaoDeLocation.Call("stopLocation");
    }


    public override void startNavi(double startLatitude, double startLongitude, double endLatitude, double endLongitude, Action Arrive, Action Successful, Action Failure, Action<string, LocationBean, LocationBean> IonInfo)
    {
        Debug.Log("开启导航");
        mGaoDeNavigation.Call("startNavi", mAndroidJavaObject, startLatitude, startLongitude, endLatitude, endLongitude, new ListenerNavigation(Arrive, Successful, Failure, IonInfo));

    }
    public override void stopNavi()
    {
        if (mGaoDeNavigation != null)
        {
            mGaoDeNavigation.Call("stopNavi");
        }
    }

    //得到两个经纬度之间的距离
    public override double checkDisparity(double startLatitude, double startLongitude, double endLatitude, double endLongitude)
    {
        return mGaoDeLocation.CallStatic<double>("checkDisparity", mAndroidJavaObject, startLatitude, startLongitude, endLatitude, endLongitude);
    }

    /*
     * 权限处理
     * **/
    //照相机权限
    public override void requestCamera(Action permissionGranted, Action permissionDenied)
    {
        if (mPermissions.Call<bool>("IsPermissionGranted", mAndroidJavaObject, PermissionsConst.CAMERA))
        {
            if (permissionGranted != null)
            {
                permissionGranted();
            }
        }
        else
        {
            mPermissions.Call("RequestPermissionAsync", mAndroidJavaObject, new string[] { PermissionsConst.CAMERA }, new IPermissionRequestResult(permissionGranted, permissionDenied));

        }
    }
    public override void requestPhotoLibary(Action permissionGranted, Action permissionDenied)
    {

    }
    //定位权限
    public override void requestLocation(Action permissionGranted, Action permissionDenied)
    {
        if (mPermissions.Call<bool>("IsPermissionGranted", mAndroidJavaObject, PermissionsConst.ACCESS_FINE_LOCATION))
        {
            if (permissionGranted != null)
            {
                permissionGranted();
            }
        }
        else
        {
            mPermissions.Call("RequestPermissionAsync", mAndroidJavaObject, new string[] { PermissionsConst.ACCESS_FINE_LOCATION, }, new IPermissionRequestResult(permissionGranted, permissionDenied));

        }

    }
    //SDK操作权限
    public override void requestSDK(Action permissionGranted, Action permissionDenied)
    {
        if (mPermissions.Call<bool>("IsPermissionGranted", mAndroidJavaObject, PermissionsConst.WRITE_EXTERNAL_STORAGE))
        {
            if (permissionGranted != null)
            {
                permissionGranted();
            }
        }
        else
        {
            mPermissions.Call("RequestPermissionAsync", mAndroidJavaObject, new string[] { PermissionsConst.ACCESS_FINE_LOCATION, PermissionsConst.WRITE_EXTERNAL_STORAGE }, new IPermissionRequestResult(permissionGranted, permissionDenied));

        }
    }

    public override void judgeApplication()
    {

    }

    //弹出toast提醒
    public void showToast(string msg)
    {
        mPermissions.Call("showToast", mAndroidJavaObject, msg);
    }
    //跳转设置界面
    public void gotoSetting()
    {
        mPermissions.Call("goToSettingUi", mAndroidJavaObject);
    }

/*
* 回调
***/
    class LocationCallBack : AndroidJavaProxy
    {
        Action<LocationBean> location;
        public LocationCallBack(Action<LocationBean> location) : base("com.nutown.kuanzhai.gaode.GaoDeLocation$GaodeLocation")
        {
            this.location = location;
        }
        void onLocationSuccessful(string str)
        {
            //成功回调
            LocationBean mallscenicsbrandbean = JsonMapper.ToObject<LocationBean>(str);
            location(mallscenicsbrandbean);
        }
        void onLocationFailure(string msg)
        {
            //失败回调
        }
    }

    class ListenerOrientation : AndroidJavaProxy
    {
        Action<OrientationBean> mOrientation;
        public ListenerOrientation(Action<OrientationBean> location) : base("com.nutown.kuanzhai.gaode.GaoDeLocation$ListenerOrientation")
        {
            this.mOrientation = location;
        }
        void onOrientationSuccessful(string str)
        {
            if (mOrientation != null)
            {
                OrientationBean mallscenicsbrandbean = JsonMapper.ToObject<OrientationBean>(str);
                mOrientation(mallscenicsbrandbean);
            }
        }

    }

    class ListenerNavigation : AndroidJavaProxy
    {
        Action Arrive;
        Action Successful;
        Action Failure;
        Action<string, LocationBean, LocationBean> Info;
        public ListenerNavigation(Action Arrive, Action Successful, Action Failure, Action<string, LocationBean, LocationBean> Info) : base("com.nutown.kuanzhai.gaode.GaoDeNavigation$GaodeNavigation")
        {
            this.Arrive = Arrive;
            this.Successful = Successful;
            this.Failure = Failure;
            this.Info = Info;
        }
        void onNavigationArrive()
        {
            if (Arrive != null)
            {
                Arrive();
            }
        }

        void onNavigationSuccessful()
        {
            if (Successful != null)
            {
                Successful();
            }
        }


        void onNavigationFailure()
        {
            if (Failure != null)
            {
                Failure();
            }
        }

        void onNavigationInfo(string nextName, double x1, double y1, double x2, double y2)
        {
            if (Info != null)
            {
                LocationBean double1 = new LocationBean();
                double1.latitude = x1;
                double1.longitude = y1;
                LocationBean double2 = new LocationBean();
                double2.latitude = x2;
                double2.longitude = y2;
                Info(nextName, double1, double2);
            }
        }
    }


    class IPermissionRequestResult : AndroidJavaProxy
    {
        Action permissionGranted;
        Action permissionDenied;
        public IPermissionRequestResult(Action permissionGranted, Action permissionDenied) : base("com.nutown.kuanzhai.permissions.ToUnityPermissions$IPermissionRequestResult")
        {
            this.permissionDenied = permissionDenied;
            this.permissionGranted = permissionGranted;
        }
        void OnPermissionGranted(string var1)
        {

            if (permissionGranted != null)
            {
                permissionGranted();
            }
        }

        void OnPermissionDenied(string var1)
        {
            if (permissionDenied != null)
            {
                permissionDenied();
            }
        }

    }
}
