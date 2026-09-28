package cn.blockforge.generated.appletreesreborn.craft;

import cn.blockforge.generated.appletreesreborn.AppleTreesMod;
import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

/**
 * 让数据包配方可以按 Forge 配置开关启用／禁用：
 * {@code "conditions": [{"type": "appletreesreborn:config_enabled", "key": "craftNotchApple"}]}
 */
public class ConfigCondition implements ICondition {

    public static final ResourceLocation ID = AppleTreesMod.id("config_enabled");

    private final String key;

    public ConfigCondition(String key) {
        this.key = key;
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public boolean test(IContext context) {
        ForgeConfigSpec.BooleanValue value = AppleConfig.CONDITION_KEYS.get(this.key);
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

    public static class Serializer implements IConditionSerializer<ConfigCondition> {

        @Override
        public void write(JsonObject json, ConfigCondition value) {
            json.addProperty("key", value.key);
        }

        @Override
        public ConfigCondition read(JsonObject json) {
            // 配方里漏写 key 时不要抛空指针，直接当成"未知开关"处理（test 里会返回 false）。
            return new ConfigCondition(json.has("key") ? json.get("key").getAsString() : "");
        }

        @Override
        public ResourceLocation getID() {
            return ID;
        }
    }
}
