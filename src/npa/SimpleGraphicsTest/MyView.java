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
    private RectF outerRect;
    private RectF innerRect;

    public MyView(Context context) {
        super(context);

        getHolder().addCallback(this);
    }

    protected void doDraw(Canvas canvas) {
        Log.v(TAG,"drawing");
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.DKGRAY);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setPathEffect(new DashPathEffect(new float[] {2,5}, 0));

        Path path=new Path();
        path.addArc(outerRect, 0, 360);
        canvas.drawPath(path, paint);

        for (double angle=0;angle<2*Math.PI;angle+=Math.PI/3) {
            canvas.drawLine(middleX, middleY, (float) (Math.cos(angle + startAngle) * (width / 2)) + middleX, (float) (Math.sin(angle + startAngle) * (height / 2)) + middleY, paint);
        }

        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        path=new Path();
        path.moveTo(middleX,middleY);
        path.lineTo((float)(Math.cos(startAngle)*(width/4))+middleX,(float)(Math.sin(startAngle)*(height/4))+middleY);
        path.addArc(innerRect,(float)Math.toDegrees(startAngle)-10,20);
        path.lineTo(middleX,middleY);
        path.close();

        canvas.drawPath(path,paint);

        for (Baddy baddy:baddies) {
            baddy.draw(canvas);
        }
    }



    private void initialise(SurfaceHolder holder) {
        surfaceFrame=holder.getSurfaceFrame();
        middleX=surfaceFrame.width()/2;
        middleY=surfaceFrame.height()/2;
        width=(float)(surfaceFrame.width()*0.9);
        height=(float)(width*0.5);
        outerRect=new RectF(middleX-(width/2),middleY-(height/2),middleX+(width/2),middleY+(height/2));
        innerRect=new RectF(middleX-(width/4),middleY-(height/4),middleX+(width/4),middleY+(height/4));
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        initialise(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        initialise(holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN) {
            if (event.getX()<middleX) {
                angleIncrement=SPEED;
            }
            else {
                angleIncrement=-SPEED;
            }
        }
        else if (event.getAction()==MotionEvent.ACTION_MOVE) {
        }
        else if (event.getAction()==MotionEvent.ACTION_UP) {
            angleIncrement=0;
        }

        return true;
    }

    public void updateView() {
        Canvas canvas=getHolder().lockCanvas();

        if (canvas!=null) {
            doDraw(canvas);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    public void update() {
        startAngle+=angleIncrement;

        deployNewBaddies();

        for (Baddy baddy:baddies) {
            baddy.update();
            if (baddy.outsideFrame(surfaceFrame) || baddy.hitTheTarget(middleX,middleY)) {
                baddies.remove(baddy);
            }
            else if (baddy.zappedByTheBeam(middleX,middleY,startAngle,20,width/4,height/4)) {
                baddy.zapped();
            }
        }
    }

    private void deployNewBaddies() {
        if (Math.random()*100<5) {

            double angle=Math.random()*2*Math.PI;
            float baddieX=(float)(Math.cos(angle)*(width/2)+middleX);
            float baddieY=(float)(Math.sin(angle)*(height/2)+middleY);
            releaseTheBaddy(baddieX,baddieY);
        }
    }

    void releaseTheBaddy(float startX,float startY) {
       Baddy b=new Baddy(startX, startY, middleX, middleY, 5);
       baddies.add(b);
   }
}
