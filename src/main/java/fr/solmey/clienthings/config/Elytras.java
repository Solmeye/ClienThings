package fr.solmey.clienthings.config;

import fr.solmey.clienthings.config.Servers;

public class Elytras {
    public Boolean enabled;
    public Servers servers;
    //public Boolean experimental;

    public Elytras () {
        servers = new Servers();
        servers.CUSTOM = false;
        servers.MODDED = true;
        servers.PLUGIN = true;
        servers.VANILLA = true;
    }
}