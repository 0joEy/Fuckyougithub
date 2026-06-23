package net.henrycmoss.bb.datagen;

import net.henrycmoss.bb.structures.StructureZone;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.text.html.Option;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ZoneSavedData extends SavedData {
    private final Map<UUID, StructureZone> zones = new HashMap<>();

    public ZoneSavedData() {}

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag zoneList = new ListTag();

        for(StructureZone zone : zones.values()) {
            CompoundTag zoneTag = new CompoundTag();
            zoneTag.putUUID("id", zone.getId());
            zoneTag.putString("dimension",
                    zone.getDimension().toString());
            zoneTag.putString("structure", zone.getStructure().toString());

            zoneTag.putInt("minX", zone.getBounds().minX());
            zoneTag.putInt("minY", zone.getBounds().minY());
            zoneTag.putInt("minZ", zone.getBounds().minZ());

            zoneTag.putInt("maxX", zone.getBounds().maxX());
            zoneTag.putInt("maxY", zone.getBounds().maxX());
            zoneTag.putInt("maxZ", zone.getBounds().maxZ());

            zoneList.add(zoneTag);
        }

        tag.put("zones", zoneList);
        return tag;
    }


    public static ZoneSavedData load(CompoundTag tag) {
        ZoneSavedData data = new ZoneSavedData();

        ListTag zones = tag.getList("zones", ListTag.TAG_COMPOUND);

        for(Tag t : zones) {
            CompoundTag zoneTag = (CompoundTag) t;

            UUID id = zoneTag.getUUID("id");
            ResourceKey<Level> dimension = keyFromString(zoneTag.getString("dimension"));
            ResourceKey<Structure> structure = keyFromString(zoneTag.getString("structure"));

            BoundingBox bounds = BoundingBox.fromCorners(new Vec3i(zoneTag.getInt("minX"), zoneTag.getInt("minY"),
                    zoneTag.getInt("minZ")), new Vec3i(zoneTag.getInt("maxX"), zoneTag.getInt("maxY"),
                    zoneTag.getInt("maxZ")));

            StructureZone zone = new StructureZone(dimension, structure, bounds);

            data.zones.put(id, zone);
        }
        return data;
    }

    public static ZoneSavedData get(ServerLevel level) {
        DimensionDataStorage storage = level.getDataStorage();

        return storage.computeIfAbsent(ZoneSavedData::load,
                ZoneSavedData::new, "bb_zones");
    }

    public void addZone(StructureZone zone) {
        if(!zones.containsValue(zone)) zones.put(zone.getId(), zone);
        setDirty();
    }

    public Optional<StructureZone> findZone(ResourceKey<Level> dimension,
                             BlockPos pos) {
        for(StructureZone zone : zones.values()) {
            if(!zone.getDimension().equals(dimension)) continue;
            if(zone.contains(pos)) return Optional.of(zone);
        }
        return Optional.empty();
    }

    public static <T> ResourceKey<T> keyFromString(String s) {
        //ResourceLocation registryName = ResourceLocation.of(s.substring(s.indexOf('['), s.indexOf('/') - 1), ':');
        ResourceLocation location = ResourceLocation.of(s.substring(s.indexOf('/'), s.indexOf(']') - 1), ':');

        return ResourceKey.create(ResourceKey.createRegistryKey(location), location);
    }
}
