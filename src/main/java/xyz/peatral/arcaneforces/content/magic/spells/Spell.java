package xyz.peatral.arcaneforces.content.magic.spells;


import com.mojang.serialization.Codec;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.*;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import xyz.peatral.arcaneforces.Main;
import xyz.peatral.arcaneforces.ModDataAttachments;
import xyz.peatral.arcaneforces.ModSpells;
import xyz.peatral.arcaneforces.content.magic.SpellSelector;

import javax.annotation.Nullable;

public class Spell {

    public static final Codec<Holder<Spell>> CODEC = RegistryFixedCodec.create(ModSpells.REGISTRY_KEY);
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Spell>> STREAM_CODEC = ByteBufCodecs.holderRegistry(ModSpells.REGISTRY_KEY);


    public static final ResourceLocation CURSE_BACK_GOOD = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "curses/backgrounds/good");
    public static final ResourceLocation CURSE_BACK_BAD = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "curses/backgrounds/bad");
    public ResourceLocation iconResource;
    private boolean isBad;
    private int maxUses;
    private int cooldown;
    @Nullable
    private String descriptionId;

    public Spell(String name, int maxUses, int cooldown) {
        this(name, true, maxUses, cooldown);
    }

    public Spell(String name, boolean isBad, int maxUses, int cooldown) {
        super();
        this.iconResource = ResourceLocation.fromNamespaceAndPath(Main.MOD_ID, "spell/" + name);
        this.isBad = isBad;
        this.maxUses = maxUses;
        this.cooldown = cooldown;
    }

    public static Component getFullname(Holder<Spell> holder, int level) {
        MutableComponent mutablecomponent = holder.value().getDescription().copy();
        ComponentUtils.mergeStyles(mutablecomponent, Style.EMPTY.withColor(ChatFormatting.RED));

        if (level != 1 || holder.value().getMaxLevel() != 1) {
            mutablecomponent.append(CommonComponents.SPACE).append(Component.translatable("curse.level." + level));
        }

        return mutablecomponent;
    }

    public Component getDescription() {
        return Component.translatable(this.getDescriptionId());
    }

    @Override
    public String toString() {
        return ModSpells.REGISTRY.wrapAsHolder(this).getRegisteredName();
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("spell", ModSpells.REGISTRY.getKey(this));
        }

        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public int getMaxLevel() {
        return 1;
    }

    public boolean isBad() {
        return isBad;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public int getCooldown() {
        return cooldown;
    }

    public void castCurseWrapper(Level world, Player target, Player attacker) {
        if (Minecraft.getInstance().player == null)
            return;

        boolean isLocalPlayer = Minecraft.getInstance().player.equals(attacker);
        Player p = isLocalPlayer ? Minecraft.getInstance().player : attacker;
        SpellSelector c = p.getData(ModDataAttachments.SPELL_SELECTOR);

        if (c.cooldown() <= 0) {
            //c.setCooldown(cooldown);
            //c.setCooldownMax(cooldown);

            this.castCurse(world, target, attacker);
        }
    }

    public void castCurse(Level level, Player target, Player attacker) {
    }

    public void renderIcon(int x, int y, Minecraft mc) {
        //TODO: Gets renderd over F3 Screen
        //mc.getTextureManager().bindTexture(isBad ? CURSE_BACK_BAD : CURSE_BACK_GOOD);
        //GuiIngame.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
        //mc.renderEngine.bindTexture(iconResource);
        //GuiIngame.drawModalRectWithCustomSizedTexture(x, y, 0, 0, 16, 16, 16, 16);
    }

/*
    public static ResourceLocation getFromName(String name) {
        for (ResourceLocation rl : Registries.CURSEREGISTRY.getKeys()) {
            if (rl.getResourcePath().equals(name))
                return rl;
        }
        return  null;
    }
         */

}
