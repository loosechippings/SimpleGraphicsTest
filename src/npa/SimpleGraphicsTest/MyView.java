package npa.SimpleGraphicsTest;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import static npa.SimpleGraphicsTest.Constants.TAG;

public class MyView extends SurfaceView implements SurfaceHolder.Callback {

    private float startAngle=0;

    private float x=200,y=200;
    private float radius=100;
    private boolean fingerDown =false;
    private float savedStartAngle;

    public MyView(Context context) {
        super(context);

        getHolder().addCallback(this);
    }

    protected void doDraw(Canvas canvas) {
        Log.v(TAG,"drawing");
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        Paint paint=new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        Path path=new Path();

        RectF a=new RectF(x- radius,y- radius,x+ radius,y+ radius);
        path.addArc(a, startAngle, 270);

        canvas.drawPath(path,paint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, String.valueOf(event.getAction()));
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            x=event.getX();
            y=event.getY();
            savedStartAngle=startAngle;
            fingerDown =true;
        }
        else if (event.getAction()==MotionEvent.ACTION_MOVE) {
            double xDist=x-event.getX();
            double yDist=y-event.getY();
            radius=(float)Math.hypot(xDist,yDist)+100;
            Log.d(TAG,"x "+xDist+", y "+yDist+", rad "+radius);

            startAngle=savedStartAngle+(float)Math.toDegrees(Math.atan2(yDist, xDist));
        }
        else if (event.getAction()==MotionEvent.ACTION_UP) {
            fingerDown =false;
        }

        return true;
    }

    public void updateView() {
        if (fingerDown ==false) startAngle+=1;

        Canvas canvas=getHolder().lockCanvas();
        Log.v(TAG, "canvas locked");

        if (canvas!=null) {
            doDraw(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }
}
