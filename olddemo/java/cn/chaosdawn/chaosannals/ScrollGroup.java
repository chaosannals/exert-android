package cn.chaosdawn.chaosannals;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.content.*;

public class ScrollGroup extends ViewGroup{
    public static final int VELOCITY_TIME = 1000;
    public static final int MIN_VELOCITY = 0x200;
    private Scroller scroller;
    private VelocityTracker tracker;
    private int targetIndex;
    private TouchState touchState;
    private int touchSlop;
    private float oldY;
    private ToTargetListener listener;

    public ScrollGroup(Context context){
        this(context,null);
    }
    public ScrollGroup(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        scroller = new Scroller(context);
        tracker = null;
        targetIndex = 0;
        oldY = 0.0f;
        touchState = TouchState.REST;
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        listener = null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for(int i = 0,childTop = 0;i != count;++i){
            View child = getChildAt(i);
            if(child.getVisibility() != View.GONE){
                int childHeight = child.getMeasuredHeight();
                child.layout(0,childTop,
                        child.getMeasuredWidth(),
                        childTop + childHeight);
                childTop += childHeight;
            }
        }
    }

    @Override
    protected  void onMeasure(int width,int height){
        super.onMeasure(width,height);
        int count = getChildCount();
        for(int i = 0;i != count;++i)
            getChildAt(i).measure(width,height);
    }

    @Override
    public void computeScroll(){
        if(scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        ensureTracker(event);
        float y = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!scroller.isFinished())
                    scroller.abortAnimation();
                oldY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                scrollBy(0,(int)(oldY - y));
                oldY = y;
                break;
            case MotionEvent.ACTION_UP:
                tracker.computeCurrentVelocity(VELOCITY_TIME);
                float velocityY = tracker.getYVelocity();
                if(velocityY > MIN_VELOCITY
                        && targetIndex > 0)
                    toTarget(targetIndex - 1);
                else if(velocityY < -MIN_VELOCITY
                        && targetIndex < getChildCount() - 1)
                    toTarget(targetIndex + 1);
                else
                    toTarget(targetIndex);
                releaseTracker();
                touchState = TouchState.REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                releaseTracker();
                touchState = TouchState.REST;
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(action == MotionEvent.ACTION_MOVE &&
                touchState != TouchState.REST)
            return true;
        float y = event.getY();
        switch(action){
            case MotionEvent.ACTION_MOVE:
                if((int)Math.abs(oldY - y) > touchSlop)
                    touchState = TouchState.SCROLLING;
                break;
            case MotionEvent.ACTION_DOWN:
                oldY = y;
                touchState = scroller.isFinished() ?
                        TouchState.REST : TouchState.SCROLLING;
                break;
        }
        return touchState != TouchState.REST;
    }

    public int getTargetIndex(){
        return targetIndex;
    }

    public void setPaperListener(ToTargetListener listener){
        this.listener = listener;
    }

    public void toTarget(int index){
        int targetTop = index * getHeight();
        int scrollY = getScrollY();
        if(scrollY != targetTop){
            int offset = targetTop - scrollY;
            scroller.startScroll(0, scrollY, 0, offset, Math.abs(offset) * 2);
            targetIndex = index;
            if(listener != null)
                listener.onPaper(index,getChildAt(index));
            invalidate();
        }
    }

    private void ensureTracker(MotionEvent event){
        if(tracker == null)
            tracker = VelocityTracker.obtain();
        tracker.addMovement(event);
    }

    private void releaseTracker(){
        if(tracker != null){
            tracker.recycle();
            tracker = null;
        }
    }
}
