package cn.chaosdawn.chaosannals.paper;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import cn.chaosdawn.chaosannals.*;

public class Vita03Fragment extends Fragment{
    @Override public View onCreateView(
            LayoutInflater inflater,
            ViewGroup root,
            Bundle savedInstanceState){
        View view = inflater.inflate(
                R.layout.fragment_paper_vita03,
                root,false);
        TextView textView = (TextView)view.findViewById(R.id.v03_text00);
        RotateY3DAnimation animation = new RotateY3DAnimation(0,360,
                textView.getMeasuredWidth() / 2,textView.getMeasuredHeight() / 2);
        animation.setDuration(2048);
        textView.startAnimation(animation);
        return view;
    }
}
