package cn.chaosdawn.chaosannals;
import android.media.*;
import android.util.*;
import android.view.*;
import android.view.animation.*;
import android.widget.*;
import android.content.*;

public class MusicButton extends ImageButton{
    private boolean valid;
    private MediaPlayer player;
    private Animation animation;

    public MusicButton(Context context){
        this(context, null);
    }

    public MusicButton(Context context,AttributeSet attributeSet){
        super(context,attributeSet);
        valid = false;
        player = null;
        animation = makeAnimation(context);
        initListener();
    }
    
    public void setMusic(int resId){
        if(player != null)player.release();
        player = MediaPlayer.create(getContext(),resId);
        player.setLooping(true);
        if(valid)play();
    }
    
    public void releaseMusic(){
        if(player != null){
            player.release();
            player = null;
        }
        valid = false;
    }

    public void play(){
        if(player != null)
            player.start();
        startAnimation(animation);
        valid = true;
    }

    public void stop(){
        if(player != null)
            player.pause();
        clearAnimation();
        valid = false;
    }

    private static Animation makeAnimation(Context context){
        Animation animation = AnimationUtils.loadAnimation(
                context,R.anim.rotate_always);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }

    private void initListener(){
        setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View v){
                if(valid)stop();
                else play();
            }
        });
    }
}
