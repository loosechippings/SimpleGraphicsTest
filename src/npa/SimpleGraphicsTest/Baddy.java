package npa.SimpleGraphicsTest;

import android.graphics.*;


public class Baddy {

    private float x;
    private float y;
    private double direction;
    private float velocity;
    private float radius=10;
    private int color=Color.MAGENTA;

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

        Paint paint=new Paint();
        paint.setColor(color);
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
            return true;
        }
        return false;
    }

    public boolean hitTheTarget(float targetX,float targetY) {
        if ((Math.abs(x-targetX)<radius) && (Math.abs(y-targetY)<radius)) {
            return true;
        }
        return false;
     }

    public boolean zappedByTheBeam(float centreX, float centreY, float startAngle, int sweepAngle, float width, float height) {
        Float a=(float)(Math.toDegrees(Math.atan2(centreY - y, centreX - x)));
        if (a>startAngle && a<startAngle+sweepAngle) {
            // TODO - this logic is incorrect when we implement the next bit
            return true;
        }
        // TODO - calculate hypotenuse of point at that angle on the perimeter and compare with our point
        return false;
    }

    public void zapped() {
        setColor(Color.YELLOW);
    }

    public void setColor(int color) {
        this.color = color;
    }

}
