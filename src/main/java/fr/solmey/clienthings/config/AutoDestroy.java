package fr.solmey.clienthings.config;

import fr.solmey.clienthings.config.Servers;
import fr.solmey.clienthings.config.BypassRequiredAiming;

public class AutoDestroy {
    public Boolean enabled;
    public Servers servers;
    public BypassRequiredAiming bypassRequiredAiming;

    public AutoDestroy () {
        servers = new Servers();
        servers.CUSTOM = false;
        servers.MODDED = true;
        servers.PLUGIN = true;
        servers.VANILLA = true;
    }
}