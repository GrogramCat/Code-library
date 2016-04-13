//Application全局只有一个，本身就是单例
public class MyApplication extends Application {
	private static MyApplication app;
	public static MyApplication getInstance() {
		return app;
	}
	public void onCreate() {
		super.onCreate();
		app = this;
	}
}