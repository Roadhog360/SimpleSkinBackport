package roadhog360.simpleskinbackport.skins;

public class LimbManager {

    public static final byte ALL_LIMBS = (byte) 0xFF;

    public byte createLimbInfo(Limb... limbs)
    {
        byte result = 0;

        for(Limb limb : limbs) {
            result |= (byte) limb.ordinal();
        }

        return result;
    }

    public static boolean showLimb(byte data, Limb limbToShow) {
        return (data & limbToShow.ordinal()) != 0;
    }

    public enum Limb {
        HEAD,
        BODY,
        LEFT_ARM,
        RIGHT_ARM,
        LEFT_LEG,
        RIGHT_LEG
        ;
    }
}
