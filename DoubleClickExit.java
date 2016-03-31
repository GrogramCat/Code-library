private long exitTime = 0;  
@Override  
public boolean onKeyDown(int keyCode, KeyEvent event) {  
    if (keyCode == KeyEvent.KEYCODE_BACK  
            && event.getAction() == KeyEvent.ACTION_DOWN) {  
        if ((System.currentTimeMillis() - exitTime) > 2000) {  
            Toast.makeText(getApplicationContext(), "再按一下退出应用",  
                    Toast.LENGTH_SHORT).show();  
            exitTime = System.currentTimeMillis();  
        } else {  
            finish();  
            System.exit(0);  
        }  
        return true;  
    }  
    return super.onKeyDown(keyCode, event);  
} 