package xyz.peatral.arcaneforces.api.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.neoforged.bus.api.Event;

public class RankUpEvent extends Event {
    public int rank = 0;
    public ResolvableProfile ownerUUID;
    public Component name;
    public ItemStack icon;

    public RankUpEvent(int rank, ResolvableProfile ownerUUID, Component name, ItemStack icon) {
        this.rank = rank;
        this.ownerUUID = ownerUUID;
        this.name = name;
        this.icon = icon;
    }
}
