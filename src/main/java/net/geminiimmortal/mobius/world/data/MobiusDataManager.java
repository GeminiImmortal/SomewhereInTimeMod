package net.geminiimmortal.mobius.world.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

public class MobiusDataManager extends WorldSavedData {
    private MobiusDataManager(String identifier) {
        super(IDENTIFIER);
    }

    private static final String IDENTIFIER = "mobius_dimension_data";
    private boolean governorTowerDefeated = false;
    private int wardingTowersDefeated = 0;

    @Override
    public String getId() {
        return IDENTIFIER;
    }

    public static MobiusDataManager get(ServerWorld world, RegistryKey<World> dim) {
        if (world instanceof ServerWorld) {
            ServerWorld mobius = world.getServer().getLevel(dim);
            DimensionSavedDataManager storage = mobius.getDataStorage();
            MobiusDataManager data = storage.computeIfAbsent(() -> new MobiusDataManager(IDENTIFIER), IDENTIFIER);
            if (data != null) {
                data.setDirty();
            }
            return data;
        }
        return null;
    }

    @Override
    public void load(CompoundNBT nbt) {
        MobiusDataManager MData = new MobiusDataManager(getId());
        MData.governorTowerDefeated = nbt.getBoolean("GovernorTowerCleared");
        MData.wardingTowersDefeated = nbt.getInt("WardingTowersDefeated");
    }

    @Override
    public CompoundNBT save(CompoundNBT nbt) {
        nbt.putBoolean("GovernorTowerCleared", governorTowerDefeated);
        nbt.putInt("WardingTowersDefeated", wardingTowersDefeated);
        return nbt;
    }

    public boolean isGovernorTowerDefeated() {
        return this.governorTowerDefeated;
    }

    public void setGovernorTowerDefeated(boolean towerCleared) {
        this.governorTowerDefeated = towerCleared;
    }

    public void setWardingTowerDefeated() {
        this.wardingTowersDefeated++;
    }

}
