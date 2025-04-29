package net.henrycmoss.bb.screen;

import net.henrycmoss.bb.block.BbBlocks;
import net.henrycmoss.bb.block.entity.CrucibleBlockEntity;
import net.henrycmoss.bb.block.entity.ElectrolyticCellBlockEntity;
import net.henrycmoss.bb.recipe.ElectrolysisResultType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.Nullable;

public class ElectrolyticCellMenu extends AbstractContainerMenu {

    private final ElectrolyticCellBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    private final int length = 17;
    private final int size = 35;

    private ElectrolysisResultType resultType = ElectrolysisResultType.LIQUID;

    public ElectrolyticCellMenu(int containerId, Inventory inv, FriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(7));
    }

    public ElectrolyticCellMenu(int containerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(BbMenuTypes.ELECTROLYTIC_CELL.get(), containerId);

        this.blockEntity = (ElectrolyticCellBlockEntity) entity;
        this.level = blockEntity.getLevel();
        this.data = data;

        addPlayerInv(inv);
        addPlayerHotbar(inv);

        blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent((iItemHandler) -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 58, 62));
            this.addSlot(new SlotItemHandler(iItemHandler, 1, 104, 62));
            this.addSlot(new SlotItemHandler(iItemHandler, 2, 81, 13));
            this.addSlot(new SlotItemHandler(iItemHandler, 3, 30, 52));
            this.addSlot(new SlotItemHandler(iItemHandler, 4, 132, 52));
            this.addSlot(new SlotItemHandler(iItemHandler, 5, 30, 25));
            this.addSlot(new SlotItemHandler(iItemHandler, 6, 132, 25));
        });

        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress() {
        int prog = data.get(0);
        int max = data.get(1);

        return max != 0  && prog != 0 ? prog * getLength() / max : 0;
    }

    public int getScaledProgress1() {
        int prog = this.data.get(0);
        int max = this.data.get(1);
        int length = 35;

        return prog != 0 && max != 0 ? prog * length / max : 0;
    }

    public int getScaled(int n, int length) {
        int max = this.data.get(1);

        return n != 0 && max != 0 ? n * length / max : 0;
    }

    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;


    private static final int TE_INVENTORY_SLOT_COUNT = 7;
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    public Level getLevel() {
        return level;
    }

    public ContainerData getData() {
        return data;
    }

    public ElectrolyticCellBlockEntity getBlockEntity() {
        return blockEntity;
    }

    public int getLength() {
        return length;
    }

    public int indicatorSize() {
        return size;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, BbBlocks.ELECTROLYTIC_CELL.get());
    }

    private void addPlayerInv(Inventory inv) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(inv, l + i * 18 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(inv, i, 8 + i * 18, 142));
        }
    }
}
