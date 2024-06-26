package xyz.peatral.arcaneforces.content.magic;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ToolAction;
import net.neoforged.neoforge.common.ToolActions;
import xyz.peatral.arcaneforces.ModDamageTypes;
import xyz.peatral.arcaneforces.ModDataComponents;
import xyz.peatral.arcaneforces.Utils;

public class RitualDaggerItem extends Item {
    public RitualDaggerItem() {
        super(new Properties()
                .stacksTo(1)
                .attributes(ItemAttributeModifiers.builder()
                    .add(
                            Attributes.ATTACK_DAMAGE,
                            new AttributeModifier(
                                    BASE_ATTACK_DAMAGE_ID, 3.0f, AttributeModifier.Operation.ADD_VALUE
                            ),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .add(
                            Attributes.ATTACK_SPEED,
                            new AttributeModifier(BASE_ATTACK_SPEED_ID, -2.4F, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND
                    )
                    .build())
                .component(ModDataComponents.AFFINITY, 0)
        );
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        return true;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof Player targetPlayer) {
            stack.set(ModDataComponents.TARGET, new ResolvableProfile(targetPlayer.getGameProfile()));
        }
        if (!target.isAlive() && attacker instanceof Player attackerPlayer) {
            Utils.modifyAffinity(stack, attackerPlayer, 1000);
        }
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_SWORD_ACTIONS.contains(toolAction);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        player.startUsingItem(hand);

        if (stack.has(ModDataComponents.TARGET)) {
            if (player.isShiftKeyDown()) {
                stack.remove(ModDataComponents.TARGET);
                return InteractionResultHolder.success(stack);
            }
        } else {
            player.hurt(
                    new DamageSource(
                            level
                                    .registryAccess()
                                    .registry(Registries.DAMAGE_TYPE)
                                    .orElseThrow()
                                    .getHolderOrThrow(ModDamageTypes.RITUAL_SACRIFICE),
                            player,
                            player
                    ),
                    3.0f);
            stack.set(ModDataComponents.TARGET, new ResolvableProfile(player.getGameProfile()));

            Utils.modifyAffinity(stack, player, 100);

            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.fail(stack);
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack pStack) {
        Utils.verifyResolvableProfileComponent(pStack, ModDataComponents.TARGET.get());
        Utils.verifyResolvableProfileComponent(pStack, ModDataComponents.OWNER.get());
    }
}
