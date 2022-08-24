package cn.chaosdawn.chaosannals;
import android.util.*;
import android.widget.*;
import android.content.*;
import android.graphics.*;
import cn.chaosdawn.chaosannals.art.*;

public class ArtText extends TextView{
    private boolean valid;
    private ShimmerArt shimmerArt;

    public ArtText(Context context){
        this(context,null);
    }
    public ArtText(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        valid = false;
        shimmerArt = null;
    }

    @Override
    protected void onSizeChanged(int w,int h,int oldW,int oldH){
        super.onSizeChanged(w,h,oldW,oldH);
        if(!valid){
            int width = getMeasuredWidth();
            int height = getMeasuredHeight();
            Linear linear = new Linear(width);
            shimmerArt = new ShimmerArt(width,linear);
            shimmerArt.apply(getPaint());
            valid = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(valid){
            shimmerArt.draw(canvas);
            postInvalidateDelayed(64);
        }
    }
}
