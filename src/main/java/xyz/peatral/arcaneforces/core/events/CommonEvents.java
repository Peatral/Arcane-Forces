package xyz.peatral.arcaneforces.core.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.peatral.arcaneforces.*;
import xyz.peatral.arcaneforces.content.shrines.ShrineSavedData;

@EventBusSubscriber(modid = Main.MOD_ID)
public class CommonEvents {
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player target && event.getSource().getEntity() instanceof Player cause) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack stack = cause.getItemInHand(hand);
                if (stack.has(ModDataComponents.AFFINITY)) {
                    Utils.modifyAffinity(stack, cause, 100);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
            PacketDistributor.sendToPlayer(player, player.getData(ModDataAttachments.SPELL_SELECTOR));
            ShrineSavedData.syncToPlayer(player);
        }
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getEntity().level().isClientSide && event.getEntity() instanceof ServerPlayer player) {
            ShrineSavedData.syncToPlayer(player);
        }
    }

    @SubscribeEvent
    public static void onBlockToolModification(BlockEvent.BlockToolModificationEvent event) {
        if (event.getItemAbility().equals(ItemAbilities.AXE_STRIP)) {
            BlockState state = ModBlocks.getAxeStrippingState(event.getState());
            if (state != null && !event.isSimulated()) {
                event.setFinalState(state);
            }
        }
    }
}
