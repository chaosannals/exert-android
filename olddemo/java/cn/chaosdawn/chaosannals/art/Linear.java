package cn.chaosdawn.chaosannals.art;

import android.graphics.*;

public class Linear implements DynamicTransform{
    private int width;
    private int speed;
    private int translate;
    private Matrix matrix;

    public Linear(int width){
        //8只是简单地然速度为宽度的8分之1，没有特别意思
        this(width,width / 8);
    }

    public Linear(int width,int speed){
        this.width = width;
        this.speed = speed;
        this.translate = 0;
        matrix = new Matrix();
    }

    @Override
    public void advance(Shader shader){
        translate += speed;
        if(translate > width * 2)
            translate = -width;
        matrix.setTranslate(translate,0);
        shader.setLocalMatrix(matrix);
    }
}
