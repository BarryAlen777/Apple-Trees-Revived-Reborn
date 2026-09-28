package cn.blockforge.generated.appletreesreborn.world;

import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * 1.20.1 的世界生成入口：由 data/appletreesreborn/worldgen 里的 configured/placed feature
 * 以及 forge biome_modifier 调用，实际种树的逻辑仍在 AppleTreeGenerator 里。
 */
public class AppleTreeFeature extends Feature<NoneFeatureConfiguration> {

    public AppleTreeFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        if (!AppleConfig.NATURAL_GENERATION.get()) {
            return false;
        }
        double chance = AppleConfig.SPAWN_CHANCE.get();
        if (chance <= 0.0D || context.random().nextDouble() >= chance) {
            return false;
        }
        LevelAccessor level = context.level();
        // 数据里的高度图只给一个大概位置，这里再向下贴一次地，
        // 确保树干脚下是实心泥土而不是悬崖边／悬空的方块，避免树看着悬空。
        BlockPos base = AppleTreeGenerator.findGroundBase(level, context.origin());
        if (base == null) {
            return false;
        }
        return AppleTreeGenerator.growTree(level, context.random(), base, FruitType.APPLE, true, true);
    }
}
