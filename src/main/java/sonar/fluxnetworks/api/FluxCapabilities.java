package sonar.fluxnetworks.api;

import net.minecraftforge.common.capabilities.*;
import sonar.fluxnetworks.api.energy.IFNEnergyStorage;

public final class FluxCapabilities {

    /**
     * Only make use of this capability if your mod can send/receive energy at a rate greater than Integer.MAX_VALUE
     * Flux Networks will handle all Forge Energy implementations as normal.
     * <p>
     * Functions the same as {@link net.minecraftforge.common.capabilities.ForgeCapabilities} but allows Long.MAX_VALUE
     * you can add this cap to Items or Tile Entities, the Flux Plug & Point also use this capability
     */
    public static final Capability<IFNEnergyStorage> FN_ENERGY_STORAGE = CapabilityManager.get(new CapabilityToken<>() {
    });

    private FluxCapabilities() {
    }
}
