## 视频播放

>在Android中有三种方式来实现视频的播放 学习于伯乐在线：刘望舒

1. 使用自带的播放器。指定Action为ACTION_VIEW，Data为Uri，Type为MIME类型
2. 使用ViedoView来播放。布局文件中使用ViedoView结合MediaController来实现对其控制
3. 使用MediaPlayer类和SurfaceView，这种方式比较灵活

```java
// 调用自带播放器
Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/textMovie.mp4");
Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setDataAndType(uri, "Video/mp4");
startActivity(intent)
```

```java
// 使用ViedoView
Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() + "/textMovie.mp4");
VideoView videoview = (VideoView)findViewById(R.id.videoview);
videoview.setMediaController(new MediaCotroller(this));
videoview.setVideoURI(uri);
videoview.start();
videoview.requestFocus();
```

```java
// 使用MediaPlayer
public class Video extends Activity implements OnCompletionListener, OnErrorListener, OnInfoListener, 
        OnPreparedListener, OnSeekCompleteListener, OnVideoSizeChangedListener, SurfaceHolder.Callback {
        
        private Display currDisplay;
        private SurfaceView surfaceView;
        private SurfaceHolder holder;
        private MediaPlayer player;
        private int vWidth, vHeight;

        public void onCreate(Bundle saveInstanceState) {
            super.onCreate(saveInstanceState);
            this.setContentView(R.layout.video_surface);

            surfaceView = (SurfaceView)this.findViewById(R.id.video_surface);
            // 给surfaceView添加callback监听
            holder. = surfaceView.getHolder();
            holder.addCallBack(this);
            // 为了可以播放视频或者使用Camera预览，我们需要指定Buffer类型
            holder.setType(SurfaceHolder.SURFACE_TYPE_BUFFERS);
            // 实例化MediaPlayer
            player = new MediaPlayer();
            player.setOnCompletionListener(this);
            player.setOnErrorListener(this);
            player.setOnInfoListener(this);
            player.setOnPreparedListener(this);
            player.setOnSeekCompleteListener(this);
            player.setOnVideoSizeChangedListener(this);
            // 指定需要的播放文件的路径，初始化MedioPalyer
            String dataPath = Environment.getExternalStorageDirectory().getPath() + "/textMovie.mp4";
            try {
                player.setDataSource(dataPath);
            } catch(IllegalArgumentException e) {
                e.printStackTrace();
            } catch(IllegalStateException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }
            // 取得当前的Display对象
            currDisplay = this.getWindowManager().getDefaultDisplay();
        }

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
            // 当Surface尺寸等参数发生变化是触发

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // 当SurfaceView中的Surfave被创建的时候调用
            // 这里我们指定MediaPlayer在当前的Surface中进行播放
            player.setDisplay(holder);
            // 在指定了MediaPlayer播放的容器后，我们就可以使用prepared或者preparedAsync来准备播放了
            player.prepareAsync();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        @Override
        public void onVideoSizeChanged(MediaPlayer arg0, int arg1, int arg2) {
            // 当video大小改变时触发
            // 这个方法在设置player的source后至少触发一次
        }

        @Override
        public void onSeekComplete(MediaPlayer arg0) {
            // seek操作完成时触发
        }

        @Override
        public void onPrepared(MediaPlayer player) {
            // 当prepared完成时触发，在这里播放视频
            // 先取得vidoe的宽高
            vWidth = player.getVideoWidth();
            vHeight = player.getVideoHeight();

            if(vWidth > currDisplay.getWidth() || vHeight > currDisplay.getHeight()) {
                // 如果vidoe宽高大于当前的屏幕大小，需进行缩放
                float wRatio = (float)vWidth / (float)currDisplay.getWidth();
                float hRatio = (float)vHeight / (float)currDisplay.getHeight();
                // 选择大的一个进行缩放
                float ratio = (int)Math.max(wRatio, hRatio);
                vWidth = (int)Math.ceil((float)vWidth / ratio);
                vHeight = (int)Math.ceil((float)vHeight / ratio);
                // 设置SuerfaceView的布局参数
                surfaceView.setLayoutParams(new LinearLayout.LayoutParams(vWidth, vHeight));
                // 开始播放视频
                player.start();
            }
        }

        @Override
        public boolean onInfo(MediaPlayer player, int whatInfo, int extra) {
            // 当一些特定的信息出现或者警告时触发
            switch(whatInfo) {
                case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                    break;
                case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    break;
                case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    break;
            }
            return false;
        }

        @Override
        public boolean onError(MediaPlayer player, int whatError, int extra) {
            switch(whatError) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    break;
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    break;
                default:
                    break;
            }
            return false;
        }

        @Override
        public void onCompletion(MediaPlayer player) {
            // 当MediaPlayer播放完成后触发
            this.finish();
        }
}
```