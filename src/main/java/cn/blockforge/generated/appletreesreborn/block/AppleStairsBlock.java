package cn.blockforge.generated.appletreesreborn.block;

import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

/**
 * 苹果木楼梯：可燃（数值同橡木楼梯）。
 */
public class AppleStairsBlock extends StairBlock implements Flammability.Planks {

    public AppleStairsBlock(Supplier<BlockState> baseState, BlockBehaviour.Properties properties) {
        super(baseState, properties);
    }
}
