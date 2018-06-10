package com.nutown.kuanzhai.gaode;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.CoordinateConverter;
import com.amap.api.location.DPoint;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.nutown.kuanzhai.gaode.utils.ErrorInfo;
import com.nutown.kuanzhai.gaode.utils.TTSController;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by LSX on 2018/2/13.
 */

public class GaoDeLocation {
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    public static CoordinateConverter mCoordinateConverter;
    private SensorManager sensorManager = null;//传感器
    private Sensor gyroSensor;
    private SensorEventListener mSensorEventListener;
    private        Handler mainHandler;
    public GaoDeLocation() {
    }

    //获取当前SHA1值
    public String getSHA1(Context context){
        return sHA1(context) + "," + context.getPackageName();
    }

    //检查两个经纬度之间的距离
    public static double checkDisparity(Context context, double startLatitude, double startLongitude, double endLatitude, double endLongitude) {
        if (mCoordinateConverter == null) {
            mCoordinateConverter = new CoordinateConverter(context);
        }
        return mCoordinateConverter.calculateLineDistance(new DPoint(startLatitude, startLongitude), new DPoint(endLatitude, endLongitude));
    }

    //获取当前朝向
    public void getOrientation(Context context, final ListenerOrientation orientation) {
        sensorManager = (SensorManager) context.getSystemService(SENSOR_SERVICE);
        gyroSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                    if(orientation!=null){
                        try{
                            JSONObject obj = new JSONObject(); // 首先创建一个对象
                            obj.put("orientation",event.values[0]);
                            orientation.onOrientationSuccessful(obj.toString());
                            Log.e("getOrientation",obj.toString());
                        }catch (Exception e){
                            Log.e("getOrientation",e.getMessage());
                        }

                    }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        sensorManager.registerListener(mSensorEventListener, gyroSensor,
                SensorManager.SENSOR_DELAY_NORMAL); //为传感器注册监听器
    }

    //停止监听当前朝向
    public void stopOrientation() {
        if (sensorManager != null&&mSensorEventListener!=null) {
            sensorManager.unregisterListener(mSensorEventListener); // 解除监听器注册
        }
    }

    private String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //定位
    public void startGetLocation(final Context context, final GaodeLocation gaodelocation) {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                try {
                    if (aMapLocation != null && gaodelocation != null) {
                        final JSONObject obj = new JSONObject(); // 首先创建一个对象
                        obj.put("longitude", aMapLocation.getLongitude());
                        obj.put("latitude", aMapLocation.getLatitude());
                        //obj.put("bearing", aMapLocation.getBearing());
                        //obj.put("address", aMapLocation.getAddress());
                        gaodelocation.onLocationSuccessful(obj.toString());
                    }
                } catch (final Exception e) {
                    //已在主线程中，可以更新UI
                    gaodelocation.onLocationFailure(e.getMessage());
                }
            }
        };
//初始化定位
        mLocationClient = new AMapLocationClient(context);
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();


        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(2000);
//设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);

        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
//启动定位
        mLocationClient.startLocation();
    }

    public void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        }
    }


    public interface GaodeLocation {
        void onLocationSuccessful(String location);

        void onLocationFailure(String msg);
    }

    public interface ListenerOrientation {
        void onOrientationSuccessful(String orientation);
    }

}
