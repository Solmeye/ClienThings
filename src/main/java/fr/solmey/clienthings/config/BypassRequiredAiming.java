package fr.solmey.clienthings.config;

import fr.solmey.clienthings.config.Servers;

public class BypassRequiredAiming {
    public Boolean enabled;
    public Servers servers;

    public BypassRequiredAiming () {
        servers = new Servers();
        servers.CUSTOM = false;
        servers.MODDED = true;
        servers.PLUGIN = false;
        servers.VANILLA = true;
    }
}