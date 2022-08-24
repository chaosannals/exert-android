package cn.chaosdawn.chaosannals.art;
import android.graphics.*;

public class ShimmerArt implements Art{
    private LinearGradient gradient;
    private DynamicTransform transform;

    public ShimmerArt(int width,DynamicTransform transform){
        this.gradient = new LinearGradient(-width,0,0,0,
                new int[]{0x33ffffff,0xffffffff,0x33ffffff},
                new float[]{0.0f,0.5f,1.0f},Shader.TileMode.CLAMP);
        this.transform = transform;
    }

    public void apply(Paint paint){
        paint.setShader(gradient);
    }

    @Override
    public void draw(Canvas canvas){
        transform.advance(gradient);
    }
}
