package com.nutown.kuanzhai.gaode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviGuide;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.nutown.kuanzhai.gaode.utils.ErrorInfo;
import com.nutown.kuanzhai.gaode.utils.TTSController;

import java.util.List;

/**
 * Created by LSX on 2018/2/22.
 */

public class GaoDeNavigation {
    public TTSController mTtsManager;
    //获取AMapNavi实例
    AMapNavi mAMapNavi;

    public GaoDeNavigation() {
    }

    public void stopNavi() {
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        mTtsManager.destroy();
    }

    //导航
    public void startNavi(final Context context, final double startLatitude, final double startLongitude, final double endLatitude, final double endLongitude, final GaodeNavigation navigation) {
        //实例化语音引擎
        mTtsManager = TTSController.getInstance(context);
        mTtsManager.init();

        //获取AMapNavi实例
        mAMapNavi = AMapNavi.getInstance(context);
        mAMapNavi.addAMapNaviListener(new AMapNaviListener() {
            @Override
            public void onInitNaviFailure() {

            }

            @Override
            public void onInitNaviSuccess() {

            }

            @Override
            public void onStartNavi(int i) {

            }

            @Override
            public void onTrafficStatusUpdate() {

            }

            @Override
            public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

            }

            @Override
            public void onGetNavigationText(int i, String s) {

            }

            @Override
            public void onGetNavigationText(String s) {

            }

            @Override
            public void onEndEmulatorNavi() {

            }

            @Override
            public void onArriveDestination() {

            }

            @Override
            public void onCalculateRouteFailure(int i) {

            }

            @Override
            public void onReCalculateRouteForYaw() {

            }

            @Override
            public void onReCalculateRouteForTrafficJam() {

            }

            @Override
            public void onArrivedWayPoint(int i) {

            }

            @Override
            public void onGpsOpenStatus(boolean b) {

            }

            @SuppressLint("LongLogTag")
            @Override
            public void onNaviInfoUpdate(NaviInfo naviInfo) {
                if (navigation != null) {
                    //返回当前导航路线的所有坐标点。
                    // List<NaviLatLng> coordList = mAMapNavi.getNaviPath().getCoordList();
                    List<AMapNaviGuide> naviGuideList = mAMapNavi.getNaviGuideList();
//                    for (int i = 0; i < coordList.size(); i++) {
//                        Log.e("wwwwwwwwwwwwwwwwccc<<", coordList.get(i).toString()+","+coordList.size());
//                    }
                    for (int j = 0; j < naviGuideList.size(); j++) {
                        if (naviGuideList.get(j).getName().equals(naviInfo.getNextRoadName())) {
                            // Log.e("onNaviInfoUpdate", "name:" + naviInfo.getNextRoadName() + "1111111:" + naviInfo.m_Latitude + "," + naviInfo.m_Longitude);
                            NaviLatLng coord = naviGuideList.get(j).getCoord();
                            if (j < naviGuideList.size() - 1) {
                                navigation.onNavigationInfo(naviInfo.getNextRoadName(), coord.getLatitude(), coord.getLongitude(), naviGuideList.get(j + 1).getCoord().getLatitude(), naviGuideList.get(j + 1).getCoord().getLongitude());
                            } else {
                                navigation.onNavigationInfo(naviInfo.getNextRoadName(), coord.getLatitude(), coord.getLongitude(), 0.0, 0.0);
                            }
                        }
                    }

                }

            }

            @Override
            public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

            }

            @Override
            public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

            }

            @Override
            public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

            }

            @Override
            public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

            }

            @Override
            public void showCross(AMapNaviCross aMapNaviCross) {

            }

            @Override
            public void hideCross() {

            }

            @Override
            public void showModeCross(AMapModelCross aMapModelCross) {

            }

            @Override
            public void hideModeCross() {

            }

            @Override
            public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

            }

            @Override
            public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

            }

            @Override
            public void hideLaneInfo() {

            }

            @Override
            public void onCalculateRouteSuccess(int[] ints) {

            }

            @Override
            public void notifyParallelRoad(int i) {

            }

            @Override
            public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

            }

            @Override
            public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

            }

            @Override
            public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

            }

            @Override
            public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

            }

            @Override
            public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

            }

            @Override
            public void onPlayRing(int i) {

            }
        });
        mAMapNavi.addAMapNaviListener(mTtsManager);
        mAMapNavi.setUseInnerVoice(true);
//添加监听回调，用于处理算路成功
        mAMapNavi.addAMapNaviListener(new AMapNaviListener() {
            @Override
            public void onInitNaviFailure() {
                if (navigation != null) {
                    navigation.onNavigationFailure();
                }
            }

            //初始化成功,计算步行规划路线
            @Override
            public void onInitNaviSuccess() {
                mAMapNavi.calculateWalkRoute(new NaviLatLng(startLatitude, startLongitude), new NaviLatLng(endLatitude, endLongitude));

            }

            @Override
            public void onStartNavi(int i) {

            }

            @Override
            public void onTrafficStatusUpdate() {

            }

            @Override
            public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

            }

            @Override
            public void onGetNavigationText(int i, String s) {
                Log.e("onGetNavigationText", s);

            }

            @Override
            public void onGetNavigationText(String s) {
                Log.e("onGetNavigationText", s);

            }

            @Override
            public void onEndEmulatorNavi() {

            }

            @Override
            public void onArriveDestination() {
                if (navigation != null) {
                    navigation.onNavigationArrive();
                }
                Log.e("onGetNavigationText", "到达目的地");
            }

            @Override
            public void onCalculateRouteFailure(int i) {
                Log.e("onGetNavigationText", "路径规划失败:" + i + "code:" + ErrorInfo.getError(i));
            }

            @Override
            public void onReCalculateRouteForYaw() {
                Log.e("onGetNavigationText", "路径规划成功");
            }

            @Override
            public void onReCalculateRouteForTrafficJam() {

            }

            @Override
            public void onArrivedWayPoint(int i) {

            }

            @Override
            public void onGpsOpenStatus(boolean b) {

            }

            @Override
            public void onNaviInfoUpdate(NaviInfo naviInfo) {
                if (navigation != null) {
                    navigation.onNavigationArrive();
                }
            }

            @Override
            public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

            }

            @Override
            public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

            }

            @Override
            public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

            }

            @Override
            public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

            }

            @Override
            public void showCross(AMapNaviCross aMapNaviCross) {

            }

            @Override
            public void hideCross() {

            }

            @Override
            public void showModeCross(AMapModelCross aMapModelCross) {

            }

            @Override
            public void hideModeCross() {

            }

            @Override
            public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

            }

            @Override
            public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

            }

            @Override
            public void hideLaneInfo() {

            }

            //路径规划成功,开始导航
            @Override
            public void onCalculateRouteSuccess(int[] ints) {
                if (navigation != null) {
                    navigation.onNavigationSuccessful();
                }
                //显示路径或开启导航
                mAMapNavi.startNavi(NaviType.GPS);


            }

            @Override
            public void notifyParallelRoad(int i) {

            }

            @Override
            public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

            }

            @Override
            public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

            }

            @Override
            public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

            }

            @Override
            public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

            }

            @Override
            public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

            }

            @Override
            public void onPlayRing(int i) {

            }
        });
    }

    public interface GaodeNavigation {
        void onNavigationArrive();//到达目的地

        void onNavigationSuccessful();//路径规划成功,开始导航

        void onNavigationFailure();//路径规划失败

        void onNavigationInfo(String nextName, double nextLat, double nextLong, double nextLat1, double nextLong1);//导航中的距离，和下一路段名,下下路段名
    }

}
