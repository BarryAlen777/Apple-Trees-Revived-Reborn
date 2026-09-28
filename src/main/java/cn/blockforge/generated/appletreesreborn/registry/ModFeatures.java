package cn.blockforge.generated.appletreesreborn.registry;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import cn.blockforge.generated.appletreesreborn.world.AppleTreeFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(ForgeRegistries.FEATURES, AppleTreesMod.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> APPLE_TREE =
            FEATURES.register("apple_tree", AppleTreeFeature::new);

    private ModFeatures() {
    }

    public static void register(IEventBus bus) {
        FEATURES.register(bus);
    }
}
