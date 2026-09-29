package cn.blockforge.generated.appletreesreborn.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * 果树配置：只带一个果实品种字段，configured_feature JSON 里写
 * {@code "config": {"fruit": "emerald_apple"}} 就能长对应的树。
 * 字段可省略，省略时按普通苹果处理（兼容旧数据包）。
 */
public record AppleTreeConfiguration(FruitType fruit) implements FeatureConfiguration {

    public static final Codec<AppleTreeConfiguration> CODEC = RecordCodecBuilder.create(builder ->
            builder.group(
                    FruitType.CODEC.fieldOf("fruit").orElse(FruitType.APPLE)
                            .forGetter(AppleTreeConfiguration::fruit)
            ).apply(builder, AppleTreeConfiguration::new));

    public static final AppleTreeConfiguration APPLE = new AppleTreeConfiguration(FruitType.APPLE);
}
