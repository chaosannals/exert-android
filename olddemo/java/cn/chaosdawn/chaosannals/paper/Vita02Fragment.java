package cn.chaosdawn.chaosannals.paper;
import android.app.*;
import android.os.*;
import android.view.*;
import cn.chaosdawn.chaosannals.*;

public class Vita02Fragment extends Fragment{
    @Override public View onCreateView(
            LayoutInflater inflater,
            ViewGroup root,
            Bundle savedInstanceState){
        View view = inflater.inflate(
                R.layout.fragment_paper_vita02,
                root,false);
        return view;
    }
}
