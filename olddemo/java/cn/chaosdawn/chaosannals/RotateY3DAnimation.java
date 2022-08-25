package cn.chaosdawn.chaosannals;
import android.util.*;
import android.graphics.*;
import android.view.animation.*;

public class RotateY3DAnimation extends Animation{
    private float formDegree;
    private float toDegree;
    private float centerX;
    private float centerY;
    private Camera camera;

    public RotateY3DAnimation(
            float formDegree, float toDegree,
            float centerX,float centerY){
        this.formDegree = formDegree;
        this.toDegree = toDegree;
        this.centerX = centerX;
        this.centerY = centerY;
        Log.d("center::::>>>>>",centerX + "," + centerY);
    }

    @Override
    public void initialize(int width,int height,int parentW,int parentH){
        super.initialize(width,height,parentW,parentH);
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime,Transformation transformation){
        float degrees = formDegree + (toDegree - formDegree) * interpolatedTime;
        Matrix matrix = transformation.getMatrix();
        camera.save();
        camera.rotateY(degrees);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.preTranslate(-centerX,-centerY);
        matrix.postTranslate(centerX,centerY);
    }
}
