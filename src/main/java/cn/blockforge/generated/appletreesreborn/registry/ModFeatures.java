package cn.blockforge.generated.appletreesreborn.registry;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import cn.blockforge.generated.appletreesreborn.world.AppleTreeConfiguration;
import cn.blockforge.generated.appletreesreborn.world.AppleTreeFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 世界生成用的树特征注册。NeoForge 用原版注册表键 Registries.FEATURE，
 * 不再有 Forge 的 ForgeRegistries.FEATURES。
 */
public final class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, AppleTreesMod.MOD_ID);

    public static final DeferredHolder<Feature<?>, Feature<AppleTreeConfiguration>> APPLE_TREE =
            FEATURES.register("apple_tree", AppleTreeFeature::new);

    private ModFeatures() {
    }

    public static void register(IEventBus bus) {
        FEATURES.register(bus);
    }
}
