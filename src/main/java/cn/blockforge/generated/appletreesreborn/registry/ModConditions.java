package cn.blockforge.generated.appletreesreborn.registry;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import cn.blockforge.generated.appletreesreborn.craft.ConfigCondition;
import com.mojang.serialization.MapCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/**
 * 配方条件（recipe condition）的注册：1.21 NeoForge 走注册表，
 * 数据包里的 {@code neoforge:conditions} 按类型 id 找到这里的 MapCodec。
 */
public final class ModConditions {

    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS =
            DeferredRegister.create(NeoForgeRegistries.Keys.CONDITION_CODECS, AppleTreesMod.MOD_ID);

    public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<ConfigCondition>> CONFIG_ENABLED =
            CONDITIONS.register("config_enabled", () -> ConfigCondition.CODEC);

    private ModConditions() {
    }

    public static void register(IEventBus bus) {
        CONDITIONS.register(bus);
    }
}
