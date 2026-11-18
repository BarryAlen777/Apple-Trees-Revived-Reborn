package cn.blockforge.generated.appletreesreborn.craft;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.conditions.ICondition;

/**
 * 让数据包配方可以按模组配置开关启用／禁用：
 * {@code "neoforge:conditions": [{"type": "appletreesreborn:config_enabled", "key": "craftNotchApple"}]}
 *
 * <p>1.21 的 NeoForge 把配方条件改成了 codec 注册制（CONDITION_CODECS 注册表），
 * 不再用 Forge 时代的 Serializer + CraftingHelper.register。
 */
public record ConfigCondition(String key) implements ICondition {

    public static final ResourceLocation ID = AppleTreesMod.id("config_enabled");

    public static final MapCodec<ConfigCondition> CODEC = RecordCodecBuilder.mapCodec(builder ->
            builder.group(Codec.STRING.fieldOf("key").orElse("")
                            .forGetter(ConfigCondition::key))
                    .apply(builder, ConfigCondition::new));

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(IContext context) {
        ModConfigSpec.BooleanValue value = AppleConfig.CONDITION_KEYS.get(this.key);
        if (value == null) {
            return false;
        }
        try {
            return Boolean.TRUE.equals(value.get());
        } catch (Exception e) {
            // 配置还没加载时退回到默认值，而不是让数据包加载崩掉。
            return Boolean.TRUE.equals(value.getDefault());
        }
    }
}
