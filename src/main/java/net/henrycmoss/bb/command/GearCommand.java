package net.henrycmoss.bb.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.logging.LogUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GearCommand {

    public GearCommand(CommandDispatcher<CommandSourceStack> dispatch) {
        dispatch.register(Commands.literal("gear").then(
                Commands.argument("armor", IntegerArgumentType.integer())
                        .executes((source) -> gearUp(
                                IntegerArgumentType.getInteger(source, "armor"), source.getSource().getPlayer())))).createBuilder();
    }

    Map<Enchantment, Integer> eSword = new HashMap<>();
    Map<Enchantment, Integer> ePickaxe = new HashMap<>();
    Map<Enchantment, Integer> eShield = new HashMap<>();

    Map<Enchantment, Integer> elytra = new HashMap<>();
    Map<Enchantment, Integer> bow = new HashMap<>();
    Map<Enchantment, Integer> armor = new HashMap<>();
    Map<Enchantment, Integer> helmet = new HashMap<>();
    Map<Enchantment, Integer> boots = new HashMap<>();

    Map<Item, Map<Enchantment, Integer>> eItems = new HashMap<>();
    Map<Item, Map<Enchantment, Integer>> eAItems = new HashMap<>();

    private int gearUp(int wArmor, Player player) {
        if(player != null) {
            List<ItemStack> items = List.of(
                    new ItemStack(Items.NETHERITE_SWORD),
                    new ItemStack(Items.BOW),
                    new ItemStack(Items.NETHERITE_PICKAXE),
                    new ItemStack(Items.NETHERITE_AXE),
                    new ItemStack(Items.NETHERITE_SHOVEL),
                    new ItemStack(Items.SHIELD),
                    new ItemStack(Items.ELYTRA));

            List<ItemStack> aItems = List.of(
                    new ItemStack(Items.NETHERITE_SWORD),
                    new ItemStack(Items.BOW),
                    new ItemStack(Items.NETHERITE_PICKAXE),
                    new ItemStack(Items.NETHERITE_AXE),
                    new ItemStack(Items.NETHERITE_SHOVEL),
                    new ItemStack(Items.SHIELD),
                    new ItemStack(Items.ELYTRA),
                    new ItemStack(Items.NETHERITE_HELMET),
                    new ItemStack(Items.NETHERITE_CHESTPLATE),
                    new ItemStack(Items.NETHERITE_LEGGINGS),
                    new ItemStack(Items.NETHERITE_BOOTS));

            List<ItemStack> enchanted = new ArrayList<>();

            defineMaps();

            if (wArmor == 1) {
                for (ItemStack item : aItems) {
                    if (eAItems.containsKey(item.getItem())) {
                        LogUtils.getLogger().info(item.getItem().getDescriptionId());
                        eAItems.get(item.getItem()).forEach(item::enchant);
                    }
                    enchanted.add(item);
                }
            } else {
                for (ItemStack item : items) {
                    if (eItems.containsKey(item.getItem())) {
                        LogUtils.getLogger().info(item.getItem().getDescriptionId());
                        eItems.get(item.getItem()).forEach(item::enchant);
                    }
                }
            }
            for (ItemStack i : enchanted) {
                LogUtils.getLogger().info("new: " + i.getItem().getDescriptionId());
                player.addItem(i);
            }
            player.addItem(new ItemStack(Items.FIREWORK_ROCKET, 1000));

            return 1;
        }
        return 0;
    }

    private void defineMaps() {
        final int standVal = 15;
        final int protVal = 35;


        eSword.put(Enchantments.SHARPNESS, standVal);
        eSword.put(Enchantments.UNBREAKING, standVal);
        eSword.put(Enchantments.MOB_LOOTING, standVal);
        eSword.put(Enchantments.MENDING, 50);
        eSword.put(Enchantments.FIRE_ASPECT, standVal);
        eSword.put(Enchantments.KNOCKBACK, standVal);
        eSword.put(Enchantments.SMITE, standVal);
        eSword.put(Enchantments.BANE_OF_ARTHROPODS, standVal);
        eSword.put(Enchantments.SWEEPING_EDGE, 3);


        bow.put(Enchantments.POWER_ARROWS, standVal);
        bow.put(Enchantments.PUNCH_ARROWS, standVal);
        bow.put(Enchantments.FLAMING_ARROWS, standVal);
        bow.put(Enchantments.UNBREAKING, standVal * 3);
        bow.put(Enchantments.MENDING, 50);

        ePickaxe.put(Enchantments.BLOCK_EFFICIENCY, standVal);
        ePickaxe.put(Enchantments.UNBREAKING, standVal);
        ePickaxe.put(Enchantments.BLOCK_FORTUNE, standVal);
        ePickaxe.put(Enchantments.MENDING, 50);

        eShield.put(Enchantments.MENDING, 100);
        eShield.put(Enchantments.UNBREAKING, 100);

        elytra.put(Enchantments.UNBREAKING, 100);
        elytra.put(Enchantments.MENDING, 100);

        armor.put(Enchantments.ALL_DAMAGE_PROTECTION, protVal);
        armor.put(Enchantments.BLAST_PROTECTION, protVal);
        armor.put(Enchantments.PROJECTILE_PROTECTION, protVal);
        armor.put(Enchantments.FALL_PROTECTION, protVal);
        armor.put(Enchantments.FIRE_PROTECTION, protVal);
        armor.put(Enchantments.MENDING, 100);
        armor.put(Enchantments.THORNS, standVal);
        armor.put(Enchantments.UNBREAKING, protVal);

        helmet.put(Enchantments.AQUA_AFFINITY, standVal);
        helmet.put(Enchantments.RESPIRATION, standVal);

        boots.put(Enchantments.DEPTH_STRIDER, standVal);
        boots.put(Enchantments.SOUL_SPEED, standVal / 2);

        defineItemMaps();
    }

    private void defineItemMaps() {
        eItems.put(Items.NETHERITE_SWORD, eSword);
        eItems.put(Items.BOW, bow);
        eItems.put(Items.NETHERITE_PICKAXE, ePickaxe);
        eItems.put(Items.NETHERITE_AXE, ePickaxe);
        eItems.put(Items.NETHERITE_SHOVEL, ePickaxe);
        eItems.put(Items.SHIELD, eShield);


        eAItems.putAll(eItems);

        Map<Enchantment, Integer> helm = new HashMap<>();
        helm.putAll(armor);
        helm.putAll(helmet);

        eAItems.put(Items.NETHERITE_HELMET, helm);

        eAItems.put(Items.NETHERITE_CHESTPLATE, armor);
        eAItems.put(Items.NETHERITE_LEGGINGS, armor);

        Map<Enchantment, Integer> boot = new HashMap<>();
        boot.putAll(armor);
        boot.putAll(boots);

        eAItems.put(Items.NETHERITE_BOOTS, boot);
        eAItems.put(Items.ELYTRA, elytra);
    }
}
