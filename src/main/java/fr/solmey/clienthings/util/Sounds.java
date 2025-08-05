package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.sound.SoundEvent;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Sounds {


    public static final int CONSUMABLES = 0;
    public static final int FIREWORK = 1;
    public static final int WEAPONS = 2;
    public static final int WINDCHARGE = 3;

    private static long[] timestamps = new long[256];
    private static double[][] coordinates = new double[256][3];
    private static SoundEvent[] sounds = new SoundEvent[256];
    private static int[] category = new int[256];

    public static void set(long _timestamp, double _X, double _Y, double _Z, SoundEvent _sound, int _category) {
        int cursor = 0;
        clear();
        for(int i = 0; i < 256 ; i++)
            if(timestamps[i] == 0)
                cursor = i;

        timestamps[cursor] = _timestamp;
        coordinates[cursor][0] = _X;
        coordinates[cursor][1] = _Y;
        coordinates[cursor][2] = _Z;
        sounds[cursor] = _sound;
        category[cursor] = _category;
    }

    public static void clear() {
       for(int i = 0; i < 256 ; i++) {
            double maxTime;
            switch(category[i]) {
                case CONSUMABLES:
                    maxTime = JsonConfig.config.consumables.maxTime;
                    break;
                case FIREWORK:
                    maxTime = JsonConfig.config.firework.maxTime;
                    break;
                case WEAPONS:
                    maxTime = JsonConfig.config.weapons.maxTime;
                    break;
                case WINDCHARGE:
                    maxTime = JsonConfig.config.windcharge.maxTime;
                    break;
                default:
                    maxTime = 3200;
                    break;
            }
            if(System.currentTimeMillis() - timestamps[i] >= maxTime)
                timestamps[i] = 0;
        }
    }

    public static boolean needToCancel(SoundEvent sound, double posX, double posY, double posZ) {
        clear();
        int cursor = 0;
        boolean needed = false;
        Vec3d distance = new Vec3d(0, 0, 0);
        for(int i = 0; i < 256 ; i++)
            if(timestamps[i] >= timestamps[cursor])
                cursor = i;
        
        for(int i = 0; i < 256 ; i++) {
            if(sounds[i] != null) {

                float f = (float)(coordinates[i][0] - posX);
                float g = (float)(coordinates[i][1] - posY);
                float h = (float)(coordinates[i][2] - posZ);
                //distance = MathHelper.sqrt(f * f + g * g + h * h);
                distance = new Vec3d(f, g, h);
                double maxDistance;
                switch(category[i]) {
                    case CONSUMABLES: 
                        maxDistance = JsonConfig.config.consumables.maxDistance;
                        break;
                    case FIREWORK:
                        maxDistance = JsonConfig.config.firework.maxDistance;
                        break;
                    case WEAPONS:
                        maxDistance = JsonConfig.config.weapons.maxDistance;
                        break;
                    case WINDCHARGE:
                        maxDistance = JsonConfig.config.windcharge.maxDistance;
                        break;
                    default:
                        maxDistance = 2.0;
                        break;
                }
                if(distance.length() <= maxDistance && sound.equals(sounds[i]) && timestamps[i] <= timestamps[cursor] && timestamps[i] != 0) {
                    cursor = i;
                    needed = true;
                }
            }
        }
        if(needed == true)
            timestamps[cursor] = 0;

        return needed;
    }
}