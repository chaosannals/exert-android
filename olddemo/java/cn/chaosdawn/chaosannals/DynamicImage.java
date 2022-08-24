package cn.chaosdawn.chaosannals;
import android.util.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;

public class DynamicImage extends ImageView{
    private int interval;

    public DynamicImage(Context context){
        this(context,null);
    }
    public DynamicImage(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        TypedArray typedArray = context.obtainStyledAttributes(
                attributeSet,R.styleable.ImageInfo);
        interval = typedArray.getInteger(R.styleable.ImageInfo_interval,64);
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        postInvalidateDelayed(interval);
    }
}
