package cn.chaosdawn.chaosannals.paper;
import android.app.*;
import android.os.*;
import android.view.*;

import cn.chaosdawn.chaosannals.*;

public class Vita01Fragment extends Fragment{
    @Override public View onCreateView(
            LayoutInflater inflater,
            ViewGroup root,
            Bundle savedInstanceState){
        View view = inflater.inflate(
                R.layout.fragment_paper_vita01,
                root,false);
        ViewKit.setViewAnimation(getActivity(), view,
                R.id.v01_text00, R.anim.v01_text00);
        return view;
    }
}
