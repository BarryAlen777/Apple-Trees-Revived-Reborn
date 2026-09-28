package cn.blockforge.generated.appletreesreborn.event;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 游戏总线事件：吃完苹果后的增益与清脆音效。
 * 原版苹果的饥饿值／饱和度原封不动，这里只是在原效果之上追加短时增益，可配置关闭。
 */
@Mod.EventBusSubscriber(modid = AppleTreesMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ForgeBusEvents {

    private ForgeBusEvents() {
    }

    @SubscribeEvent
    public static void onFinishEating(LivingEntityUseItemEvent.Finish event) {
        if (!AppleConfig.FOOD_EFFECTS.get()) {
            return;
        }
        if (!(event.getEntity() instanceof Player player) || player.level().isClientSide) {
            return;
        }

        ItemStack stack = event.getItem();
        if (stack.is(Items.APPLE)) {
            applyRegeneration(player, AppleConfig.APPLE_REGEN_SECONDS.get(),
                    AppleConfig.APPLE_EFFECT_AMPLIFIER.get());
            playCrispSound(player);
        } else if (stack.is(Items.GOLDEN_APPLE) || stack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
            applyRegeneration(player, AppleConfig.GOLDEN_APPLE_REGEN_SECONDS.get(),
                    AppleConfig.GOLDEN_APPLE_EFFECT_AMPLIFIER.get());
            if (AppleConfig.GOLDEN_APPLE_ABSORPTION.get()) {
                player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 120 * 20, 0));
            }
            playCrispSound(player);
        }
    }

    private static void applyRegeneration(Player player, int seconds, int amplifier) {
        if (seconds <= 0) {
            return;
        }
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, seconds * 20, amplifier));
    }

    private static void playCrispSound(Player player) {
        player.level().playSound(null, player.blockPosition(), SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES,
                SoundSource.PLAYERS, 0.6F, 1.1F);
    }
}
