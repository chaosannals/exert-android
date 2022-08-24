package cn.chaosdawn.chaosannals;
import android.content.res.*;
import android.util.*;
import android.view.animation.*;
import android.widget.*;
import android.content.*;

public class JumpyImage extends ImageView{
    private Animation animation;
    public JumpyImage(Context context){
        this(context,null);
    }

    public JumpyImage(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        animation = AnimationUtils.loadAnimation(
                context,R.anim.jump_always);
        TypedArray typedArray = context.obtainStyledAttributes(
                attributeSet,R.styleable.ImageInfo);
        if(typedArray.getBoolean(R.styleable.ImageInfo_jumpy,false))
            jump();
    }

    public void jump(){
        startAnimation(animation);
    }

    public void stop(){
        clearAnimation();
    }
}
