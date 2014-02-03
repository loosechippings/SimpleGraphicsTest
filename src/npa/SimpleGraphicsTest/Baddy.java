package npa.SimpleGraphicsTest;

import android.graphics.*;
import android.util.Log;

import static npa.SimpleGraphicsTest.Constants.TAG;

public class Baddy {

    private float x;
    private float y;
    private double direction;
    private float velocity;
    private float radius=10;

    public Baddy(float x,float y,double directionInDegrees,float velocity) {
        this.x=x;
        this.y=y;
        this.direction=Math.toRadians(directionInDegrees);
        this.velocity=velocity;
    }

    public Baddy(float x,float y,float targetX,float targetY, float velocity) {
        this.x=x;
        this.y=y;
        this.velocity=velocity;

        float xDiff=targetX-x;
        float yDiff=targetY-y;
        this.direction=Math.atan2(yDiff,xDiff);
    }

    public void update() {
        x=x+(float)(Math.cos(direction)*velocity);
        y=y+(float)(Math.sin(direction)*velocity);
    }

    public void draw(Canvas canvas) {
        Log.v(TAG, "drawing baddy");

        Paint paint=new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        Path path=new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        RectF a=new RectF(x- radius,y- radius,x+ radius,y+ radius);
        path.addArc(a, 0, 360);

        canvas.drawPath(path,paint);
    }

    public double getDirectionInDegrees() {
        return Math.toDegrees(direction);
    }

    public float getVelocity() {
        return velocity;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean outsideFrame(Rect frame) {
        if (x<frame.left || x>frame.right || y<frame.top || y>frame.bottom) {
            Log.d(TAG,"left the frame");
            return true;
        }
        else {
            return false;
        }
    }
}
