package net.henrycmoss.bb.network.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkInstance;

import java.util.List;
import java.util.function.Supplier;

public class ForcePushPacket {

    public ForcePushPacket() {}

    public static void encode(ForcePushPacket msg, FriendlyByteBuf buf) {}

    public static ForcePushPacket decode(FriendlyByteBuf buf) { return new ForcePushPacket(); }

    public static void handle(ForcePushPacket msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();

        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;
            Level level = player.level();

            double distance = 40;
            double dR = 5d;
            Vec3 direction = player.getLookAngle().scale(distance);
            Vec3 playerPosition = player.position().add(0d, 1d, 0d);
            AABB box = new AABB(playerPosition, playerPosition.add(direction.x, direction.y,
                    direction.z));
            List<Entity> victims = level.getEntities(player, box, (entity) -> {
                Vec3 d = entity.position().subtract(playerPosition);
                double dH = d.dot(direction.normalize());
                Vec3 a = direction.normalize().scale(dH).add(playerPosition);
                double d2 = a.distanceTo(entity.position());
                if (d2 <= dH * dR) return true;
                return false;
            });
            if (!level.isClientSide()) {
                player.sendSystemMessage(Component.literal("" + victims.size()));
                victims.forEach((entity) -> {
                    Vec3 dir = entity.position().subtract(playerPosition).normalize();
                    entity.setDeltaMovement(dir.xRot((float)(Math.atan2(dir.y, dir.z) + 0.5F)).scale(5d));
                });
            }
        });
    }
}
