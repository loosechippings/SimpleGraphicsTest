package npa.SimpleGraphicsTest;

import android.util.Log;
import static npa.SimpleGraphicsTest.Constants.TAG;

public class MainLoop implements Runnable {
    private volatile boolean running=false;
    private MyView contentView=null;

    public MainLoop(MyView contentView) {
        super();
        this.contentView=contentView;
    }

    public void setRunning(boolean running) {
        this.running=running;
    }

    @Override
    public void run() {
        while(running) {
            Log.v(TAG, "frame");
            contentView.updateView();

            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
