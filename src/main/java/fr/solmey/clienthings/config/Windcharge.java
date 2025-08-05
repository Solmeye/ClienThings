package fr.solmey.clienthings.config;

import fr.solmey.clienthings.config.Servers;

public class Windcharge {
    public Boolean enabled;
    public Servers servers;
    //public Boolean experimental;
    public Double maxDistance;
    public Integer maxTime;

    public Windcharge () {
        servers = new Servers();
        servers.CUSTOM = false;
        servers.MODDED = true;
        servers.PLUGIN = true;
        servers.VANILLA = true;
    }
}