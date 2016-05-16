/**
 * Created by Temoa
 * on 2016/5/16 14:20
 */
public class AMapLocationServer {

    private AMapLocationClient mLocationClient = null;
    private String cityName = "";

    public AMapLocationServer(Context context) {
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//定位模式为精确模式
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(true);
        mLocationOption.setWifiActiveScan(true);
        mLocationOption.setMockEnable(false);//模拟位置，默认为关闭
        mLocationOption.setInterval(2000);//定位时间间隔，默认2000ms

        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationOption(mLocationOption);
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (aMapLocation.getErrorCode() == 0) {
                    cityName = aMapLocation.getCity();
                } else {
                    Log.e("AmapError",
                            "location Error, ErrCode:"
                                    + aMapLocation.getErrorCode()
                                    + ", errInfo:"
                                    + aMapLocation.getErrorInfo());
                }
            }
        };
        mLocationClient.setLocationListener(mLocationListener);

    }

    public void getLocation() {
        mLocationClient.startLocation();
    }

    public void stopLocation() {
        mLocationClient.stopLocation();
    }

    public void destroyLocation() {
        mLocationClient.onDestroy();
    }
}
