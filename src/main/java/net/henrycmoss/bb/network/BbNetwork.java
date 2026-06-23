package net.henrycmoss.bb.network;

import net.henrycmoss.bb.Bb;
import net.henrycmoss.bb.network.packet.ForcePushPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class BbNetwork {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Bb.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        CHANNEL.registerMessage(packetId++,
                ForcePushPacket.class,
                ForcePushPacket::encode,
                ForcePushPacket::decode,
                ForcePushPacket::handle
        );
    }
}
