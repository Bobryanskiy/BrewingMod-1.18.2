package net.bobr.testmod.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class MeadBlockEntity extends BlockEntity {
    private final List<Beer> beers = Lists.newArrayList();

    public MeadBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MEAD_BLOCK, pos, state);
    }

//    public BlockEntityUpdateS2CPacket toUpdatePacket() {
//        return BlockEntityUpdateS2CPacket.create(this);
//    }
//
//    public NbtCompound toInitialChunkDataNbt() {
//        return this.createNbt();
//    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.beers.clear();
        NbtList nbtList = nbt.getList("beers", 10);
        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            this.beers.add(new Beer(nbtCompound.getInt("damage")));
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.put("beers", this.getBeers());
    }

    private NbtList getBeers() {
        NbtList nbtList = new NbtList();
        for (Beer beer : this.beers) {
            NbtCompound nbtCompound = new NbtCompound();
            nbtCompound.putInt("damage", beer.dmg());
        }
        return nbtList;
    }

    public void updateNbt(World world, BlockPos pos, int dmg) {
        this.beers.add(new Beer(dmg));
        world.markDirty(pos);
    }

    public int getDmg(int i) {
        return beers.get(i).dmg();
    }


    record Beer(int dmg) {
        public int dmg() {
            return dmg;
        }
    }
}
