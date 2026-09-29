package cn.blockforge.generated.appletreesreborn.world;

import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * 1.20.1 的世界生成入口：由 data/appletreesreborn/worldgen 里的 configured/placed feature
 * 以及 forge biome_modifier 调用，实际种树的逻辑仍在 AppleTreeGenerator 里。
 *  configured feature 的 config.fruit 决定长哪种果树（苹果 / 金苹果 / 翡翠苹果）。
 */
public class AppleTreeFeature extends Feature<AppleTreeConfiguration> {

    public AppleTreeFeature() {
        super(AppleTreeConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<AppleTreeConfiguration> context) {
        if (!AppleConfig.NATURAL_GENERATION.get()) {
            return false;
        }
        FruitType fruit = context.config().fruit();
        // 金树和翡翠树按各自权重再稀释一道，稀有果实不会泛滥。
        double chance = AppleConfig.SPAWN_CHANCE.get() * fruit.getNaturalWeight();
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
        return AppleTreeGenerator.growTree(level, context.random(), base, fruit, true, true);
    }
}
