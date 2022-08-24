package cn.chaosdawn.chaosannals;
import android.app.*;
import android.view.*;
import android.view.animation.*;

public class ViewKit {
    public static View setViewAnimation(Activity activity,View root,int id,int animId){
        View result = root.findViewById(id);
        Animation animation = AnimationUtils.loadAnimation(activity, animId);
        result.setAnimation(animation);
        return result;
    }
}
