package xyz.peatral.arcaneforces.content.ranking;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ResolvableProfile;

public interface IRanking {
    boolean hasOwner();

    ResolvableProfile getOwner();

    void setOwner(ResolvableProfile uuid);

    Component getName();

    void setName(Component name);

    int getRank();

    void setRank(int rank);

    int getAffinityInCurrentRank();

    void addAffinity(int affinity);

    int getAffinityForNextRank();

    float getAffinityForDisplay();

    int getTotalAffinity();

    void setTotalAffinity(int affinity);

    Item getIcon();

    void setIcon(Item item);
}
