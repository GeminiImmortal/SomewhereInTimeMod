package net.geminiimmortal.mobius.entity.custom;

import net.geminiimmortal.mobius.MobiusMod;
import net.geminiimmortal.mobius.entity.ModEntityTypes;
import net.geminiimmortal.mobius.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

public class ManawoodBoatEntity extends BoatEntity {
    private static final DataParameter<String> WOOD_TYPE
            = EntityDataManager.defineId(ManawoodBoatEntity.class, DataSerializers.STRING);

    public ManawoodBoatEntity(EntityType<? extends BoatEntity> type, World world) {
        super(type, world);

    }

    public ManawoodBoatEntity(World worldIn, double x, double y, double z) {
        this(ModEntityTypes.MANAWOOD_BOAT.get(), worldIn);
        this.setPos(x, y, z);
        this.setDeltaMovement(Vector3d.ZERO);
        this.xOld = x;
        this.yOld = y;
        this.zOld = z;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(WOOD_TYPE, "manawood");
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        compound.putString("Type", this.getWoodType());
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("Type", this.getWoodType());
    }

    public String getWoodType() {
        return this.entityData.get(WOOD_TYPE);
    }

    public void setType(String wood) {
        this.entityData.set(WOOD_TYPE, wood);
    }

    @Override
    public Item getDropItem() {
        switch(this.getWoodType()) {
            case "manawood":
                return ModItems.MANAWOOD_BOAT.get();
            default:
                return ModItems.MANAWOOD_BOAT.get();
        }
    }

    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(
                new ResourceLocation(MobiusMod.MOD_ID, this.getWoodType() + "_boat")));
    }

    @Nonnull
    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
