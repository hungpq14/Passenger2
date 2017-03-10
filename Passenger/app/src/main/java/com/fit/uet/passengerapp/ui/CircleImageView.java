package com.fit.uet.passengerapp.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Bien-kun on 10/03/2017.
 */

public class CircleImageView extends ImageView {
    private Path mPath;

    public CircleImageView(Context context) {
        super(context);
        init();
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPath = new Path();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPath.reset();
        mPath.addCircle(w / 2, h / 2, Math.min(w, h) / 2, Path.Direction.CW);

    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            int save = canvas.save();
            canvas.clipPath(mPath);
            super.dispatchDraw(canvas);
            canvas.restoreToCount(save);
        } catch (Exception e) {
            super.dispatchDraw(canvas);
            e.printStackTrace();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            int save = canvas.save();
            canvas.clipPath(mPath);
            super.onDraw(canvas);
            canvas.restoreToCount(save);
        } catch (Exception e) {
            super.onDraw(canvas);
            e.printStackTrace();
        }
    }
}
