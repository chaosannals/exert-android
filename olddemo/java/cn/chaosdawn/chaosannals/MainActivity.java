package cn.chaosdawn.chaosannals;
import android.app.*;
import android.os.*;
import android.view.*;

import cn.chaosdawn.chaosannals.paper.*;

public class MainActivity extends Activity{
    @Override public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MusicButton button = (MusicButton)findViewById(R.id.music_button);
        button.setMusic(R.raw.black_keys);
        initScrollGroup();
    }

    private void initCardGroup(){
        CardGroup group = (CardGroup)findViewById(R.id.paper_group);
        FragmentManager fm = getFragmentManager();
        Vita00Fragment v00 = (Vita00Fragment)fm.findFragmentById(R.id.vita00);
        group.setPaperListener(new ToTargetListener() {
            @Override
            public void onPaper(int index, View view) {
                if(ViewGroup.class.isInstance(view))
                    animateViewAll((ViewGroup)view);
            }
        });
    }

    private void initScrollGroup(){
        ScrollGroup group = (ScrollGroup)findViewById(R.id.paper_group);
        FragmentManager fm = getFragmentManager();
        Vita00Fragment v00 = (Vita00Fragment)fm.findFragmentById(R.id.vita00);
        group.setPaperListener(new ToTargetListener() {
            @Override
            public void onPaper(int index, View view) {
                if(ViewGroup.class.isInstance(view))
                    animateViewAll((ViewGroup)view);
            }
        });
    }

    public static void animateViewAll(ViewGroup group){
        if(AnimationFrameLayout.class.isInstance(group))
            ((AnimationFrameLayout) group).animateAgain();
        int count = group.getChildCount();
        for(int i = 0;i != count;++i){
            View child = group.getChildAt(i);
            if(ViewGroup.class.isInstance(child))
                animateViewAll((ViewGroup)child);
        }
    }
}
