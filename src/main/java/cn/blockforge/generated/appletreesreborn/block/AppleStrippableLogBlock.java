package cn.blockforge.generated.appletreesreborn.block;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.function.Supplier;

/**
 * 可以拿斧头去皮的苹果原木／苹果树木。
 *
 * <p>1.20.1 原版的“原木→去皮原木”对照表是 ImmutableMap，反射往里塞会抛异常，
 * 所以这里改走 Forge 提供的正规接口：AxeItem 右键时先问方块“用这个工具能变成什么”，
 * 我们直接回答去皮方块即可。不需要反射、不需要 AT。
 */
public class AppleStrippableLogBlock extends RotatedPillarBlock implements Flammability.Wood {

    /** 用 Supplier 而不是直接存 Block，避免注册顺序上的先后依赖。 */
    private final Supplier<? extends Block> stripped;

    public AppleStrippableLogBlock(Properties properties, Supplier<? extends Block> stripped) {
        super(properties);
        this.stripped = stripped;
    }

    @Override
    public BlockState getToolModifiedState(BlockState state, UseOnContext context,
                                           ToolAction toolAction, boolean simulate) {
        if (toolAction == ToolActions.AXE_STRIP) {
            Block result = this.stripped.get();
            // 去皮后要保留原来的朝向，否则横放的原木会变成竖放（原版 AxeItem 也是这么做的）。
            return result == null ? null
                    : result.defaultBlockState().setValue(AXIS, state.getValue(AXIS));
        }
        return super.getToolModifiedState(state, context, toolAction, simulate);
    }
}
