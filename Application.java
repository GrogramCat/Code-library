//Applicationȫ��ֻ��һ����������ǵ���
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