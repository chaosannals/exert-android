package cn.chaosdawn.chaosannals;
import android.util.*;
import android.view.*;
import android.content.*;
import android.view.animation.*;

//还没有完成
public class CardGroup extends ViewGroup{
    private int targetIndex;
    private int moverIndex;
    private YTracker tracker;
    private ToTargetListener listener;

    public CardGroup(Context context){
        this(context,null);
    }
    public CardGroup(Context context,AttributeSet attributeSet) {
        super(context, attributeSet);
        targetIndex = 0;
        moverIndex = 0;
        tracker = new YTracker(ViewConfiguration.
                get(context).getScaledTouchSlop());
        listener = null;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        indexChild(targetIndex).layout(l, t, r, b);
        int vector = tracker.getVector();
        if(vector != 0) {
            int top = t;
            int bottom = b;
            int height = getMeasuredHeight();
            if(vector > 0){
                moverIndex = ensureIndex(targetIndex + 1);
                bottom = t + vector;
                top = bottom - height;
            }else{
                moverIndex = ensureIndex(targetIndex - 1);
                top = b - vector;
                bottom = top + height;
            }
            View child = indexChild(moverIndex);
            child.layout(l,top, r,bottom);
            child.bringToFront();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        tracker.change(event);
        if(event.getAction() == MotionEvent.ACTION_UP){
            int mid = getHeight() / 2;
            if(tracker.getVector() < -mid)toTarget(targetIndex - 1);
            else if(tracker.getVector() > mid)toTarget(targetIndex + 1);
            else toTarget(targetIndex);
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        return tracker.ensure(event);
    }

    public int getTargetIndex(){
        return targetIndex;
    }

    public void setPaperListener(ToTargetListener listener){
        this.listener = listener;
    }



    //校正子视图索引，使得循环索引。
    private View indexChild(int index){
        return getChildAt(ensureIndex(index));
    }

    private int ensureIndex(int index){
        int count = getChildCount();
        index %= count;
        if(index < 0)index += count;
        return index;
    }

    public void toTarget(int index){
        tracker.disable();
        index = ensureIndex(index);
        View child = getChildAt(index);
        float vector = tracker.getVector();
        if(index == targetIndex){
            Animation animation = null;
            if(tracker.getVector() > 0){
                animation = new TranslateAnimation(
                        0,0,0,0);
            }
        }
        else{
            child.bringToFront();
            Animation animation = new TranslateAnimation(
                    0,0,vector,0);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    tracker.enable();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animation.setDuration(Math.abs(tracker.getVector()));
            child.startAnimation(animation);
            targetIndex = index;
        }
        tracker.reset();
        requestLayout();
        invalidate();
    }
}
