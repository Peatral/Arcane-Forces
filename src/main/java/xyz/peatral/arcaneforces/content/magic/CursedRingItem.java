package xyz.peatral.arcaneforces.content.magic;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import xyz.peatral.arcaneforces.ModDataComponents;
import xyz.peatral.arcaneforces.Utils;

public class CursedRingItem extends Item {
    public CursedRingItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack pStack) {
        Utils.verifyResolvableProfileComponent(pStack, ModDataComponents.OWNER.get());
    }
}
