package npa.SimpleGraphicsTest;

import android.util.Log;
import static npa.SimpleGraphicsTest.Constants.TAG;

public class MainLoop implements Runnable {
    private volatile boolean running=false;
    private MyView contentView=null;

    private int MAX_FPS=60;
    private long beginTime;
    private int framesSkipped=0;
    private int MAX_FRAMES_TO_SKIP=10;
    private long sleepTime;

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
            beginTime=System.currentTimeMillis();
            framesSkipped=0;

            contentView.update();

            contentView.updateView();

            sleepTime=System.currentTimeMillis()-beginTime+(1000/MAX_FPS);

            while (sleepTime<0 && framesSkipped<MAX_FRAMES_TO_SKIP) {
                contentView.update();
                sleepTime+=(1000/MAX_FPS);
            }

            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
