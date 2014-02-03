package npa.SimpleGraphicsTest;

import android.content.Context;
import android.graphics.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static npa.SimpleGraphicsTest.Constants.TAG;

public class MyView extends SurfaceView implements SurfaceHolder.Callback {

    private float startAngle=0;

    // TODO - do this with less overhead by using an array
    private List<Baddy> baddies=new CopyOnWriteArrayList<Baddy>();

    private Rect surfaceFrame;
    private float middleX,middleY,width,height;
    private double angleIncrement=0;
    private static double SPEED=0.1;

    public MyView(Context context) {
        super(context);

        getHolder().addCallback(this);
    }

    protected void doDraw(Canvas canvas) {
        Log.v(TAG,"drawing");
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        Path path=new Path();

        RectF a=new RectF(middleX-(width/2),middleY-(height/2),middleX+(width/2),middleY+(height/2));
        path.addArc(a, 0, 360);

        canvas.drawPath(path,paint);

        for (double angle=0;angle<2*Math.PI;angle+=Math.PI/3) {
            canvas.drawLine(middleX,middleY,(float)(Math.cos(angle+startAngle)*(width/2))+middleX,(float)(Math.sin(angle+startAngle)*(height/2))+middleY,paint);
        }

        for (Baddy baddy:baddies) {
            baddy.draw(canvas);
        }
    }



    private void calculateCentre(SurfaceHolder holder) {
        surfaceFrame=holder.getSurfaceFrame();
        middleX=surfaceFrame.width()/2;
        middleY=surfaceFrame.height()/2;
        width=(float)(surfaceFrame.width()*0.9);
        height=(float)(width*0.5);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        calculateCentre(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        calculateCentre(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, String.valueOf(event.getAction()));
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            if (event.getX()<middleX) {
                angleIncrement=SPEED;
            }
            else {
                angleIncrement=-SPEED;
            }

            releaseTheBaddy(event.getX(),event.getY());
        }
        else if (event.getAction()==MotionEvent.ACTION_MOVE) {
        }
        else if (event.getAction()==MotionEvent.ACTION_UP) {
            angleIncrement=0;
        }

        return true;
    }

    public void updateView() {
        startAngle+=angleIncrement;

        for (Baddy baddy:baddies) {
            baddy.update();
            if (baddy.outsideFrame(surfaceFrame)) {
                baddies.remove(baddy);
            }
        }

        Canvas canvas=getHolder().lockCanvas();
        Log.v(TAG, "canvas locked");

        if (canvas!=null) {
            doDraw(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

   void releaseTheBaddy(float startX,float startY) {
       Log.d(TAG,"Release the baddy at "+startX+" "+startY);
       baddies.add(new Baddy(startX, startY, middleX, middleY, 5));
   }
}
