package fr.solmey.clienthings.util;

import fr.solmey.clienthings.config.JsonConfig;

public class Consumables {

    private static long[] timestamps = new long[256]; // For onEntityStatus

    public static void set(long _timestamp) {
        int cursor = 0;
        clear();
        for(int i = 0; i < 256 ; i++)
            if(timestamps[i] == 0)
                cursor = i;

        timestamps[cursor] = _timestamp;
    }

    public static void clear() {
       for(int i = 0; i < 256 ; i++) {
            if(System.currentTimeMillis() - timestamps[i] >= 8000) //JsonConfig.config.consumables_maxTime
                timestamps[i] = 0;
        }
    }

    public static boolean needToCancel() {
        clear();
        int cursor = 0;
        boolean needed = false;
        for(int i = 0; i < 256 ; i++)
            if(timestamps[i] >= timestamps[cursor])
                cursor = i;
        
        for(int i = 0; i < 256 ; i++) {
            if(timestamps[i] <= timestamps[cursor] && timestamps[i] != 0) {
                cursor = i;
                needed = true;
            }
        }
        if(needed == true)
            timestamps[cursor] = 0;

        return needed;
    }
}