package xyz.peatral.arcaneforces.content.ranking;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.neoforge.common.MutableDataComponentHolder;
import xyz.peatral.arcaneforces.ModDataComponents;

import java.util.Objects;

public class ComponentRanking implements IRanking {
    protected final MutableDataComponentHolder parent;
    protected final DataComponentType<Integer> affinityComponent;
    protected final DataComponentType<ResolvableProfile> ownerComponent;

    public ComponentRanking(MutableDataComponentHolder parent, DataComponentType<Integer> affinityComponent, DataComponentType<ResolvableProfile> ownerComponent) {
        this.parent = parent;
        this.affinityComponent = affinityComponent;
        this.ownerComponent = ownerComponent;
    }

    public ComponentRanking(MutableDataComponentHolder parent) {
        this(parent, ModDataComponents.AFFINITY.get(), ModDataComponents.OWNER.get());
    }

    @Override
    public int getTotalAffinity() {
        return this.parent.getOrDefault(affinityComponent, 0);
    }

    public void setTotalAffinity(int value) {
        this.parent.set(affinityComponent, value);
    }

    public boolean hasOwner() {
        return this.getOwner() != null;
    }

    public int getRank() {
        return (int) Math.floor(Math.sqrt(this.getTotalAffinity() / 1000.0f));
    }

    @Override
    public void setRank(int rank) {
        this.setTotalAffinity(rank * rank * 1000);
    }

    @Override
    public ResolvableProfile getOwner() {
        return this.parent.get(ownerComponent);
    }

    public void setOwner(ResolvableProfile owner) {
        this.parent.set(ownerComponent, owner);
    }

    @Override
    public Component getName() {
        return this.parent.getOrDefault(
                DataComponents.CUSTOM_NAME,
                this.parent.getOrDefault(
                        DataComponents.ITEM_NAME,
                        Component.literal("Undefined Name")
                )
        );
    }

    @Override
    public void setName(Component name) {
        this.parent.set(DataComponents.CUSTOM_NAME, name);
    }

    @Override
    public void addAffinity(int affinity) {
        this.setTotalAffinity(this.getTotalAffinity() + affinity);
    }

    @Override
    public int getAffinityForNextRank() {
        int rank = getRank();
        return 1000 * ((rank + 1) * (rank + 1) - rank * rank);
    }

    @Override
    public int getAffinityInCurrentRank() {
        int rank = getRank();
        return this.getTotalAffinity() - rank * rank * 1000;
    }

    @Override
    public float getAffinityForDisplay() {
        return (float) this.getAffinityInCurrentRank() / this.getAffinityForNextRank();
    }

    @Override
    public Item getIcon() {
        if (this.parent instanceof ItemStack stack)
            return stack.getItem();
        return null;
    }

    @Override
    public void setIcon(Item item) {
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getAffinityInCurrentRank(), this.getOwner());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return obj instanceof ComponentRanking rc
                    && this.getAffinityInCurrentRank() == rc.getAffinityInCurrentRank()
                    && this.getOwner().equals(rc.getOwner());
        }
    }
}
