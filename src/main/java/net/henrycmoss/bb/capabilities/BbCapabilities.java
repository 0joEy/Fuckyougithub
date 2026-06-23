package net.henrycmoss.bb.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class BbCapabilities {

    public static final Capability<IExplosion> EXPLOSION =
            CapabilityManager.get(new CapabilityToken<>() {});
}
