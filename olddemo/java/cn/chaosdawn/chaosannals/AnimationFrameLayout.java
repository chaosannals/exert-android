package cn.chaosdawn.chaosannals;
import android.util.*;
import android.content.*;
import android.view.animation.*;
import android.widget.*;
import android.view.*;
import java.util.*;

public class AnimationFrameLayout extends FrameLayout{
    private static class Pair{
        public View view;
        public Animation animation;
        public Pair(View view,Animation animation){
            this.view = view;
            this.animation = animation;
        }
    }
    private ArrayList<Pair> animations;

    public AnimationFrameLayout(Context context){
        this(context,null);
    }
    public AnimationFrameLayout(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        animations = new ArrayList<>();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        setPairAll(animations,this);
    }

    public void animateAgain(){
        for(Pair pair: animations)
            pair.view.startAnimation(pair.animation);
    }

    private static void setPairAll(ArrayList<Pair> arrayList, ViewGroup group){
        int count = group.getChildCount();
        for(int i = 0;i != count;++i) {
            View child = group.getChildAt(i);
            Animation animation = child.getAnimation();
            if(animation != null)
                arrayList.add(new Pair(child,animation));
            if(ViewGroup.class.isInstance(child))
                setPairAll(arrayList,(ViewGroup)child);
        }
    }
}
