package net.bobr.testmod.screen;

import net.bobr.testmod.block.ModBlocks;
import net.bobr.testmod.block.custom.OakBarrelBlock;
import net.bobr.testmod.block.entity.OakBarrelBlockEntity;
import net.bobr.testmod.block.enums.LiquidType;
import net.bobr.testmod.property.ModProperties;
import net.bobr.testmod.tag.ModItemTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.tag.ItemTags;

public class OakBarrelScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    private final ScreenHandlerContext context;

    public OakBarrelScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(6){

            @Override
            public boolean isValid(int slot, ItemStack stack) {
                return stack.isIn(ModItemTags.OAK_BARREL_ITEMS);
            }

            @Override
            public int getMaxCountPerStack() {
                return 1;
            }
        }, new ArrayPropertyDelegate(5), ScreenHandlerContext.EMPTY);
    }

    public OakBarrelScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate delegate, ScreenHandlerContext context) {
        super(ModScreenHandlers.OAK_BARREL_SCREEN_HANDLER, syncId);
        checkSize(inventory, 6);
        this.inventory = inventory;
        inventory.onOpen(playerInventory.player);
        this.propertyDelegate = delegate;
        this.context = context;

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlot(new Slot(this.inventory, j + i * 2, 62 + j * 18, 17 + i * 18){

                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.isIn(ModItemTags.OAK_BARREL_ITEMS) && !OakBarrelScreenHandler.this.getCork();
                    }

                    @Override
                    public boolean canTakeItems(PlayerEntity playerEntity) {
                        return !OakBarrelScreenHandler.this.getCork();
                    }

                    @Override
                    public int getMaxItemCount() {
                        return 1;
                    }
                });
            }
        }
//        this.addSlot(new ModResultSlot(inventory, 3, 18, 50));

        this.addPlayerInventory(playerInventory);
        this.addPlayerHotbar(playerInventory);

        this.addProperties(delegate);
    }

    public int getLevel() {
        return this.propertyDelegate.get(3);
    }

    public LiquidType getLiquidType() {
        return LiquidType.getLiquidType(this.propertyDelegate.get(4));
    }

    public boolean getCork() {
        return this.propertyDelegate.get(2) == 1;
    }

    public boolean isCrafting() {
        return propertyDelegate.get(0) > 0;
    }

    public int getScaledProgress() {
        int progress = this.propertyDelegate.get(0);
        int maxProgress = this.propertyDelegate.get(1);
        return maxProgress != 0 && progress != 0 ? progress * 42 / maxProgress : 0;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return OakBarrelScreenHandler.canUse(this.context, player, ModBlocks.OAK_BARREL);
    }

    @Override
    public void setProperty(int id, int value) {
        super.setProperty(id, value);
        this.sendContentUpdates();
    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {
        switch (id) {
            case 1: {
                this.setProperty(2, 1);
                this.context.run((world, pos) -> {
                    BlockState blockState = world.getBlockState(pos);
                    world.setBlockState(pos, ModBlocks.OAK_BARREL.getDefaultState()
                            .with(OakBarrelBlock.CORKED, true)
                            .with(OakBarrelBlock.LIQUID_TYPE_ENUM_PROPERTY, blockState.get(OakBarrelBlock.LIQUID_TYPE_ENUM_PROPERTY))
                            .with(OakBarrelBlock.WATER_LEVEL, blockState.get(OakBarrelBlock.WATER_LEVEL))
                            .with(OakBarrelBlock.FACING, blockState.get(OakBarrelBlock.FACING)));
                    this.sendContentUpdates();
                });
                return true;
            }
            case 2:
                this.setProperty(2, 0);
                this.context.run((world, pos) -> {
                    BlockState blockState = world.getBlockState(pos);
                    world.setBlockState(pos, ModBlocks.OAK_BARREL.getDefaultState()
                            .with(OakBarrelBlock.CORKED, false)
                            .with(OakBarrelBlock.LIQUID_TYPE_ENUM_PROPERTY, blockState.get(OakBarrelBlock.LIQUID_TYPE_ENUM_PROPERTY))
                            .with(OakBarrelBlock.WATER_LEVEL, blockState.get(OakBarrelBlock.WATER_LEVEL))
                            .with(OakBarrelBlock.FACING, blockState.get(OakBarrelBlock.FACING)));
                    this.sendContentUpdates();
                });
                return true;
        }
        return false;
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack originalStack = slot.getStack();
            newStack = originalStack.copy();
            if (invSlot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                fullSlots(originalStack);
                return ItemStack.EMPTY;
            }
            if (originalStack.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (originalStack.getCount() == newStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, originalStack);
        }
        return newStack;
    }

    private void fullSlots(ItemStack originalStack) {
        for (int i = 0; i < 6; ++i) {
            if (!((Slot) this.slots.get(i)).hasStack() && ((Slot) this.slots.get(i)).canInsert(originalStack)) {
                ItemStack itemStack3 = originalStack.copy();
                itemStack3.setCount(1);
                originalStack.decrement(1);
                ((Slot) this.slots.get(i)).setStack(itemStack3);
                return;
            }
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }
}
