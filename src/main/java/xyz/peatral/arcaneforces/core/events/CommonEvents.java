package xyz.peatral.arcaneforces.core.events;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import xyz.peatral.arcaneforces.*;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetwork;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineNetworkNodeBlockEntity;
import xyz.peatral.arcaneforces.content.shrines.network.ShrineSavedData;

import java.util.Optional;

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
        if (event.getItemAbility() == ItemAbilities.AXE_STRIP) {
            BlockState state = ModBlocks.getAxeStrippingState(event.getState());
            if (state != null && !event.isSimulated()) {
                event.setFinalState(state);
            }
        } else if (event.getItemAbility() == ItemAbilities.FIRESTARTER_LIGHT) {
            BlockState state = event.getState();
            if (!CandleBlock.canLight(state) && state.is(ModTags.Blocks.INCENSE_TRAVEL_STARTERS)) {
                return;
            }

            BlockPos pos = event.getPos().below();
            BlockEntity entity = event.getLevel().getBlockEntity(pos);
            if (!(entity instanceof ShrineNetworkNodeBlockEntity networkNode)) {
                return;
            }

            Optional<ShrineNetwork> network = ShrineNetwork.getNetwork(networkNode.getLevel());
            if (network.isEmpty() || network.get().getLocation(pos) != null || network.get().getNeighbors(pos) == null) {
                return;
            }

            int candleCount = state.getValue(BlockStateProperties.CANDLES) - 1;
            if (candleCount <= 0) {
                event.setFinalState(Blocks.AIR.defaultBlockState());
            } else {
                event.setFinalState(state.setValue(BlockStateProperties.LIT, Boolean.FALSE)
                        .setValue(BlockStateProperties.CANDLES, candleCount));
            }

            if (!event.getLevel().isClientSide()) {
                networkNode.teleportToClosestShrine((Level) event.getLevel(), event.getPlayer());
            }
        }
    }
}
