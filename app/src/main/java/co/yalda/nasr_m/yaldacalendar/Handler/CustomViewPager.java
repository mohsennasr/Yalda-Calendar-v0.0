package co.yalda.nasr_m.yaldacalendar.Handler;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Nasr_M on 2/19/2015.
 */
public class CustomViewPager extends ViewPager {
    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    public int[] getDimention(Context context) {
        int[] dim = new int[2];
//        Point point = new Point();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(point);
        dim[0] = this.getHeight();
        dim[1] = this.getWidth();
        return dim;
    }
}
