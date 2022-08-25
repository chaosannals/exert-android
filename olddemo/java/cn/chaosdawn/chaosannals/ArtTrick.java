package cn.chaosdawn.chaosannals;

public enum ArtTrick {
    NONE(0b0),
    SHIMMER(0b1);
    private int sign;
    private ArtTrick(int sign){
        this.sign = sign;
    }
    public int getSign(){
        return sign;
    }

    public static boolean has(int sign,ArtTrick trick){
        return (sign & trick.sign) != 0;
    }
    public static int add(int sign,ArtTrick trick){
        return sign | trick.sign;
    }
}
