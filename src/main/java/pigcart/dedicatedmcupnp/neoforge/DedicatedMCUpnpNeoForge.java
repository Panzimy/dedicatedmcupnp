package pigcart.dedicatedmcupnp.neoforge;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import pigcart.dedicatedmcupnp.DedicatedMCUpnp;

@Mod(DedicatedMCUpnp.MOD_ID)
public final class DedicatedMCUpnpNeoForge {
    public DedicatedMCUpnpNeoForge() {
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(DedicatedMCUpnp.getCommands());
    }

    @SubscribeEvent
    public void onServerStarted(ServerStartedEvent event) {
        DedicatedMCUpnp.onServerStarted(event.getServer());
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event) {
        DedicatedMCUpnp.onServerStopping(event.getServer());
    }
}

