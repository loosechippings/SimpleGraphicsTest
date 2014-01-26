package npa.SimpleGraphicsTest;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import static npa.SimpleGraphicsTest.Constants.TAG;

public class SimpleGraphicsTest extends Activity {

    private MainLoop mainLoop;
    private Thread mainThread;
    private MyView contentView;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentView = new MyView(this);
        setContentView(contentView);


    }

    @Override
    public void onPause() {
        super.onPause();

        mainLoop.setRunning(false);
        boolean retry=true;
        Log.d(TAG,"pausing");
        while (retry) {
            try {
                mainThread.join();
                Log.d(TAG,"paused");
                retry=false;
            } catch (InterruptedException e) {
                Log.e(TAG,e.toString());
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG,"resuming");
        mainLoop=new MainLoop(contentView);
        mainThread = new Thread(mainLoop);
        mainLoop.setRunning(true);
        mainThread.start();
        Log.d(TAG, "resumed");
    }

}
