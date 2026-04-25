package pigcart.dedicatedmcupnp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class MOD implements ModInitializer {

    @Override
    public void onInitialize() {
        // registrace příkazů
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(DedicatedMCUpnp.getCommands());
        });

        // server lifecycle
        ServerLifecycleEvents.SERVER_STARTED.register(DedicatedMCUpnp::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(DedicatedMCUpnp::onServerStopping);
    }
}
