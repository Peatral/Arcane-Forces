package xyz.peatral.arcaneforces.content.ranking;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ResolvableProfile;

public interface IRanking {
    boolean hasOwner();
    void setOwner(ResolvableProfile uuid);
    ResolvableProfile getOwner();

    Component getName();
    void setName(Component name);

    int getRank();
    void setRank(int rank);

    int getAffinityInCurrentRank();
    void setTotalAffinity(int affinity);
    void addAffinity(int affinity);

    int getAffinityForNextRank();
    float getAffinityForDisplay();
    int getTotalAffinity();

    Item getIcon();
    void setIcon(Item item);
}
