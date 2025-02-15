package vivatech.api.block.entity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import io.github.cottonmc.energy.api.EnergyAttribute;
import io.github.cottonmc.energy.api.EnergyAttributeProvider;
import io.github.cottonmc.energy.impl.SimpleEnergyAttribute;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Tickable;
import vivatech.common.Vivatech;
import vivatech.api.block.AbstractMachineBlock;

public abstract class AbstractMachineBlockEntity extends BlockEntity implements Tickable, SidedInventory, PropertyDelegateHolder,
        BlockEntityClientSerializable, EnergyAttributeProvider {
    protected DefaultedList<ItemStack> inventory = DefaultedList.ofSize(getInvSize(), ItemStack.EMPTY);
    protected SimpleEnergyAttribute energy = new SimpleEnergyAttribute(getMaxEnergy(), Vivatech.INFINITE_VOLTAGE) {
        @Override
        public boolean canInsertEnergy() { return AbstractMachineBlockEntity.this.canInsertEnergy(); }

        @Override
        public boolean canExtractEnergy() { return AbstractMachineBlockEntity.this.canExtractEnergy(); }
    };

    public AbstractMachineBlockEntity(BlockEntityType<?> type) {
        super(type);
    }

    protected abstract int getMaxEnergy();

    protected boolean canInsertEnergy() {
        return true;
    }

    protected boolean canExtractEnergy() {
        return true;
    }

    public EnergyAttribute getEnergy() {
        return energy;
    }

    protected void serverTick() {}

    protected void clientTick() {}

    protected void setBlockActive(boolean active) {
        world.setBlockState(pos, world.getBlockState(pos).with(AbstractMachineBlock.ACTIVE, active), 3);
    }

    protected void updateEntity() {
        markDirty();
        world.updateListeners(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
    }

    // BlockEntity
    @Override
    public void fromTag(CompoundTag tag) {
        super.fromTag(tag);
        energy.fromTag(tag.getTag("Energy"));
        inventory = DefaultedList.ofSize(getInvSize(), ItemStack.EMPTY);
        Inventories.fromTag(tag, inventory);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Energy", energy.toTag());
        Inventories.toTag(tag, inventory);
        return tag;
    }

    // Tickable
    @Override
    public void tick() {
        if (world.isClient()) {
            clientTick();
        } else {
            serverTick();
        }
    }

    // SidedInventory
    @Override
    public boolean isInvEmpty() {
        for (ItemStack itemStack : inventory) if (!itemStack.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getInvStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack takeInvStack(int slot, int parts) {
        return Inventories.splitStack(inventory, slot, parts);
    }

    @Override
    public ItemStack removeInvStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setInvStack(int slot, ItemStack itemStack) {
        inventory.set(slot, itemStack);
        if (itemStack.getCount() > getInvMaxStackAmount()) {
            itemStack.setCount(getInvMaxStackAmount());
        }
        markDirty();
    }

    @Override
    public boolean canPlayerUseInv(PlayerEntity player) {
        if (world.getBlockEntity(pos) != this) {
            return false;
        } else {
            return player.squaredDistanceTo(
                    (double) pos.getX() + 0.5D,
                    (double) pos.getY() + 0.5D,
                    (double) pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    // BlockEntityClientSerializable
    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }

    // EnergyAttributeProvider
    @Override
    public EnergyAttribute getEnergyAttribute() {
        return energy;
    }
}
