package cn.chaosdawn.chaosannals;
import android.view.*;

public class YTracker {
    private boolean able;
    private float head;
    private float tail;
    private float slop;
    private TouchState state;

    public YTracker(float slop){
        able = true;
        head = 0.0f;
        tail = 0.0f;
        this.slop = slop;
        state = TouchState.REST;
    }

    public boolean ensure(MotionEvent event){
        if(!able)return false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(state == TouchState.REST)
                    state = TouchState.SCROLLING;
                break;
            case MotionEvent.ACTION_MOVE:
                float offset = event.getY() - tail;
                if(Math.abs(offset) > slop)
                    state = TouchState.SCROLLING;
                break;
        }
        return state != TouchState.REST;
    }

    public boolean change(MotionEvent event){
        if(!able)return false;
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                head = event.getY();
                tail = head;
                break;
            case MotionEvent.ACTION_MOVE:
                tail = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                tail = event.getY();
                state = TouchState.REST;
                break;
        }
        return true;
    }

    public int getVector(){
        return (int)(tail - head);
    }

    public boolean isIdle(){
        return state == TouchState.REST;
    }

    public void reset(){
        head = 0.0f;
        tail = 0.0f;
        state = TouchState.REST;
    }

    public void enable(){
        able = true;
    }

    public void disable(){
        able = false;
    }
}
