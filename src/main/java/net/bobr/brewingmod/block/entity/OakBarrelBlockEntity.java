package net.bobr.brewingmod.block.entity;

import net.bobr.brewingmod.block.custom.OakBarrelBlock;
import net.bobr.brewingmod.block.enums.LiquidType;
import net.bobr.brewingmod.item.inventory.ImplementedInventory;
import net.bobr.brewingmod.recipe.OakBarrelRecipe;
import net.bobr.brewingmod.screen.OakBarrelScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class OakBarrelBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, ImplementedInventory {
    private final DefaultedList<ItemStack> inventory =
            DefaultedList.ofSize(6, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 6000;
    private int cork;
    private int level;
    private LiquidType type;

    public OakBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.OAK_BARREL, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> OakBarrelBlockEntity.this.progress;
                    case 1 -> OakBarrelBlockEntity.this.maxProgress;
                    case 2 -> OakBarrelBlockEntity.this.cork;
                    case 3 -> OakBarrelBlockEntity.this.level;
                    case 4 -> OakBarrelBlockEntity.this.type.getId();
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> OakBarrelBlockEntity.this.progress = value;
                    case 1 -> OakBarrelBlockEntity.this.maxProgress = value;
                    case 2 -> OakBarrelBlockEntity.this.cork = value;
                    case 3 -> OakBarrelBlockEntity.this.level = value;
                    case 4 -> OakBarrelBlockEntity.this.type = LiquidType.getLiquidType(value);
                }
            }

            @Override
            public int size() {
                return 5;
            }
        };
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return inventory;
    }

//    @Override
//    public boolean canExtract(int slot, ItemStack stack, Direction side) {
//        return !this.getWorld().getBlockState(this.getPos()).get(OakBarrelBlock.CORKED);
//    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("text.oak_barrel");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new OakBarrelScreenHandler(syncId, inv, this, this.propertyDelegate, ScreenHandlerContext.create(this.world, this.getPos()));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        this.progress = nbt.getInt("progress");
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        nbt.putInt("progress", this.progress);
    }

    public static void tick(World world, BlockPos pos, BlockState blockState, OakBarrelBlockEntity entity) {
        entity.cork = blockState.get(OakBarrelBlock.CORKED) ? 1 : 0;
        entity.level = blockState.get(OakBarrelBlock.WATER_LEVEL);
        entity.type = blockState.get(OakBarrelBlock.LIQUID_TYPE_ENUM_PROPERTY);
        if (entity.cork == 1 && OakBarrelBlockEntity.hasRecipe(entity) && blockState.get(OakBarrelBlock.LIQUID_TYPE_ENUM_PROPERTY).equals(LiquidType.WATER)) {
            entity.progress++;
            if (entity.progress > entity.maxProgress) {
                OakBarrelBlockEntity.craftItem(world, pos, entity, blockState);
            }
            OakBarrelBlockEntity.markDirty(world, pos, blockState);
        } else if (entity.progress > 0) entity.progress--;
    }

//    @Override
//    public NbtCompound toInitialChunkDataNbt() {
//        return this.createNbt();
//    }
//
//    public BlockEntityUpdateS2CPacket toUpdatePacket() {
//        return BlockEntityUpdateS2CPacket.create(this);
//    }

    private static boolean hasRecipe(OakBarrelBlockEntity entity) {
        World world = entity.world;
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<OakBarrelRecipe> match = world.getRecipeManager()
                .getFirstMatch(OakBarrelRecipe.Type.INSTANCE, inventory, world);

        return match.isPresent();
    }

    private static void craftItem(World world, BlockPos pos, OakBarrelBlockEntity entity, BlockState blockState) {
        SimpleInventory inventory = new SimpleInventory(entity.inventory.size());
        for (int i = 0; i < entity.inventory.size(); i++) {
            inventory.setStack(i, entity.getStack(i));
        }

        Optional<OakBarrelRecipe> match = world.getRecipeManager()
                .getFirstMatch(OakBarrelRecipe.Type.INSTANCE, inventory, world);

        if(match.isPresent()) {
            for (int i = 0; i < 6; i++) {
                entity.removeStack(i, 1);
            }

            entity.resetProgress();

            world.setBlockState(pos, blockState.with(OakBarrelBlock.LIQUID_TYPE_ENUM_PROPERTY, LiquidType.valueOf(match.get().getGroup().toUpperCase()))
                    .with(OakBarrelBlock.WATER_LEVEL, blockState.get(OakBarrelBlock.WATER_LEVEL))
                    .with(OakBarrelBlock.FACING, blockState.get(OakBarrelBlock.FACING))
                    .with(OakBarrelBlock.CORKED, true));
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }
}
