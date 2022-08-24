package cn.chaosdawn.chaosannals.paper;
import android.app.*;
import android.os.*;
import android.view.*;

import cn.chaosdawn.chaosannals.*;

public class Vita00Fragment extends Fragment{
    @Override public View onCreateView(
            LayoutInflater inflater,
            ViewGroup root,
            Bundle savedInstanceState){
        View view = inflater.inflate(
                R.layout.fragment_paper_vita00,
                root,false);
        ViewKit.setViewAnimation(getActivity(), view,
                R.id.v00_text00, R.anim.top_to_bottom);
        ViewKit.setViewAnimation(getActivity(), view,
                R.id.v00_image00, R.anim.sunshine);
        ViewKit.setViewAnimation(getActivity(), view,
                R.id.v00_image01, R.anim.sunshine);
        ViewKit.setViewAnimation(getActivity(), view,
                R.id.v00_image02, R.anim.sunshine);
        return view;
    }
}
