package cn.chaosdawn.chaosannals.paper;
import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.graphics.drawable.*;
import cn.chaosdawn.chaosannals.*;

public class Vita04Fragment extends Fragment{
    @Override public View onCreateView(
            LayoutInflater inflater,
            ViewGroup root,
            Bundle savedInstanceState){
        View view = inflater.inflate(
                R.layout.fragment_paper_vita04,
                root,false);
        ImageView butterfly = (ImageView)view.findViewById(R.id.v04_butterfly);
        ((AnimationDrawable)butterfly.getDrawable()).start();
        ViewKit.setViewAnimation(getActivity(), view,
                R.id.v04_text00, R.anim.v04_text00);
        return view;
    }
}
