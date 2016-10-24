## IntentService

intentService 是一个基于Service 的一个类,用来处理异步请求


通过startService(intent)来提交请求,该Service 会在需要的时候创建,完成任务后就会自己关闭,且请求是在工作线程处理


**两个好处:不需要去new Thread,另一方面不需要考虑何时关闭Service**

```java
public MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        // IntentService会创立一个工作线程来完成onHandleIntent中的方法,无需处理多线程的问题
        try {
            Thead.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "onCreate called");
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 提供默认的实现, 将请求Intent放入队列中
        Log.e("TAG", "onStartCommand called");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("TAG", "onStart called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("TAG", "onDestroy called");
    }
}
```