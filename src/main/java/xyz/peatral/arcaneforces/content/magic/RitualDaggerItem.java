package xyz.peatral.arcaneforces.content.magic;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import xyz.peatral.arcaneforces.ModDamageTypes;
import xyz.peatral.arcaneforces.ModDataComponents;
import xyz.peatral.arcaneforces.Utils;
import xyz.peatral.arcaneforces.content.incense.TappingRecipe;

import javax.annotation.Nullable;
import java.util.Optional;

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

    private static boolean playerHasShieldUseIntent(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        return useOnContext.getHand().equals(InteractionHand.MAIN_HAND) && player.getOffhandItem().is(Items.SHIELD) && !player.isSecondaryUseActive();
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
    public boolean canPerformAction(ItemStack stack, ItemAbility toolAction) {
        return ItemAbilities.DEFAULT_SWORD_ACTIONS.contains(toolAction);
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
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();
        Player player = pContext.getPlayer();
        if (playerHasShieldUseIntent(pContext)) {
            return InteractionResult.PASS;
        } else {
            Optional<BlockState> optional = this.evaluateNewBlockState(level, blockpos, player, level.getBlockState(blockpos), pContext);
            if (optional.isEmpty()) {
                return InteractionResult.PASS;
            } else {
                level.setBlock(blockpos, optional.get(), 11);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos, GameEvent.Context.of(player, optional.get()));
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }
    }

    private Optional<BlockState> evaluateNewBlockState(Level pLevel, BlockPos pPos, @Nullable Player pPlayer, BlockState pState, UseOnContext useOnContext) {
        Optional<BlockState> optional = Optional.ofNullable(pState.getToolModifiedState(useOnContext, TappingRecipe.TAPPING, false));
        if (optional.isPresent()) {
            pLevel.playSound(pPlayer, pPos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            return optional;
        }
        return Optional.empty();
    }

    @Override
    public void verifyComponentsAfterLoad(ItemStack stack) {
        Utils.verifyResolvableProfileComponent(stack, ModDataComponents.TARGET.get());
        Utils.verifyResolvableProfileComponent(stack, ModDataComponents.OWNER.get());
    }
}
