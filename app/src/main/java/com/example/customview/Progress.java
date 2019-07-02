package com.example.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class Progress extends View {

    /*Callack*/
    private ProgressCallback progressCallback;



    // Paint object for coloring and styling
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float progress = 5f; //In %

    // View size in pixels
    private float width_view = 0;
    private float height_view = 0;

    private float curve_height = 0;

    private float radius = 14f;


    /*For backgroud curves*/
    private float offset_1 = 20;
    private float offset_2 = 100;

    private boolean pressing = false;


    public Progress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgress(float progress) {
        this.progress = progress;
        this.invalidate();
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        width_view = getWidth();
        height_view = getHeight();
        curve_height = height_view/2 - height_view/6;

        onDrawCurves(canvas);
        onDrawMainCurve(canvas);
        onDrawCursor(canvas);
    }

    protected void onDrawCursor(Canvas canvas) {

        double X = progress*width_view/100;
        double Y = (float) (curve_height*Math.sin((13*Math.PI)/(9*width_view)* X));


        paint.setColor(Color.WHITE);
        paint.setShader(null);
        canvas.drawCircle((float) X, (float) Y + height_view/2-1, radius, paint);
        paint.setShader(new LinearGradient(0, 0, width_view,height_view, Color.rgb(86, 46, 135), Color.rgb(199, 72, 123), Shader.TileMode.MIRROR));
        canvas.drawCircle((float) X, (float) Y + height_view/2-1, radius-6f, paint);


    }

    protected void onDrawCurves(Canvas canvas) {

        int stroke = 4;



        /*This are the element which change the curve*/
        float w_1 = (float) ((13*Math.PI)/(9*width_view));
        float curve_height_1 = height_view/2 - height_view/10;

        float w_2 = (float) ((8*Math.PI)/(9*width_view));
        float curve_height_2 = height_view/2 - height_view/5;

        float x0_1 = 0, y0_1 = (float) (curve_height_1*Math.sin(offset_1 ));
        float x0_2 = 0, y0_3 = (float) (curve_height_2*Math.sin(offset_2 ));

        Path curve_1 = new Path();
        Path curve_2 = new Path();

        curve_1.moveTo( x0_1, y0_1 +  height_view/2);
        curve_2.moveTo( x0_2, y0_3 +  height_view/2);



        for(int i = 0; i < width_view ; i+=10)
        {

            double X0_1 = i;
            double Y0_1 =  curve_height_1*Math.sin(offset_1 + w_1* i);

            double X0_2 = i;
            double Y0_2 =  curve_height_2*Math.sin(offset_2 + w_2* i);

            i+=10;

            double X1_1 = i;
            double Y1_1 =   curve_height_1*Math.sin(offset_1+ w_1* i);

            double X1_2 = i;
            double Y1_2 =   curve_height_2*Math.sin(offset_2+ w_2* i);


            curve_1.quadTo((float) X0_1, (float)Y0_1+ height_view/2, (float)X1_1,(float)Y1_1+ height_view/2);
            curve_2.quadTo((float) X0_2, (float)Y0_2+ height_view/2, (float)X1_2,(float)Y1_2+ height_view/2);

        }



        for(int i = (int) width_view; i > 0 ; i-=10)
        {

            double X0_1 = i;
            double Y0_1 =  curve_height_1*Math.sin(offset_1 + w_1* i) - stroke;

            double X0_2 = i;
            double Y0_2 =  curve_height_2*Math.sin(offset_2 + w_2* i) - stroke ;

            i-=10;

            double X1_1 = i;
            double Y1_1 =   curve_height_1*Math.sin(offset_1+ w_1* i) - stroke ;

            double X1_2 = i;
            double Y1_2 =   curve_height_2*Math.sin(offset_2+ w_2* i)- stroke ;

            curve_1.quadTo((float) X0_1, (float)Y0_1+ height_view/2, (float)X1_1,(float)Y1_1+ height_view/2);
            curve_2.quadTo((float) X0_2, (float)Y0_2+ height_view/2, (float)X1_2,(float)Y1_2+ height_view/2);
        }

        // 4
        paint.setShader(null);
        paint.setColor(Color.rgb(32,38,52));

        paint.setStyle(Paint.Style.FILL);
        // 5
        canvas.drawPath(curve_1, paint);
        canvas.drawPath(curve_2, paint);

    }

    protected void onDrawMainCurve(Canvas canvas) {

        int stroke = 4;

        float x0 = 0, y0 = height_view/2 , xend = width_view, yend = (float) (curve_height * Math.sin(13*Math.PI/9));

        Path mouthPath = new Path();

        mouthPath.moveTo(x0, y0);


        for(int i = 0; i < width_view ; i+=10)
        {

            double X0 = i;
            double Y0 =  curve_height*Math.sin((13*Math.PI)/(9*width_view)* i);

            i+=10;

            double X1 = i;
            double Y1 = curve_height*Math.sin( (13*Math.PI)/(9*width_view)* i);

            System.out.println("Coo : (" + X1 + " : " + Y1 + ")");

            mouthPath.quadTo((float) X0, (float)Y0+ height_view/2, (float)X1,(float)Y1+ height_view/2);
           // canvas.drawLine( (float) X0, (float)Y0+ height_view/2, (float)X1,(float)Y1+ height_view/2 , paint);
        }

        for(int i = (int) width_view; i > 0 ; i-=10)
        {

            double X0 = i;
            double Y0 =  curve_height*Math.sin((13*Math.PI)/(9*width_view)* i) - stroke;

            i-=10;

            double X1 = i;
            double Y1 = curve_height*Math.sin( (13*Math.PI)/(9*width_view)* i) - stroke;

            System.out.println("Coo : (" + X1 + " : " + Y1 + ")");

            mouthPath.quadTo((float) X0, (float)Y0+ height_view/2, (float)X1,(float)Y1+ height_view/2);
            // canvas.drawLine( (float) X0, (float)Y0+ height_view/2, (float)X1,(float)Y1+ height_view/2 , paint);
        }

        // 4
        paint.setShader(new LinearGradient(x0, y0, xend,yend, Color.rgb(86, 46, 135), Color.rgb(199, 72, 123), Shader.TileMode.MIRROR));

        paint.setStyle(Paint.Style.FILL);
        // 5
        canvas.drawPath(mouthPath, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        if(pressing)
        {
            double delta = progress*width_view/100;

            if(delta>event.getX())
            {
                offset_1 += (delta-event.getX())/15;
            }
            else
            {
                offset_2 -= (delta-event.getX())/15;
            }
        }

        progress = event.getX()*100/width_view;

        if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
            progressCallback.onStartSeeking();
            pressing = true;
            radius = 18f;
        } else if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
            progressCallback.onStopSeeking(progress);
            pressing = false;
            radius = 14f;
        }


        this.invalidate();

        return true;
    }

    public interface ProgressCallback {
        void onStartSeeking();
        void onStopSeeking(float newProgress);
    }

}
