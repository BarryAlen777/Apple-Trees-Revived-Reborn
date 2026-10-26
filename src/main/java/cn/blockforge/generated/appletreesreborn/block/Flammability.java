package cn.blockforge.generated.appletreesreborn.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.extensions.IForgeBlock;

/**
 * 苹果木系列的可燃性参数，数值照抄原版橡木。
 *
 * <p>1.20.1 里方块能不能烧、烧得多快，看的是方块自己回答的
 * {@code getFlammability}（被烧掉的概率）和 {@code getFireSpreadSpeed}（火蔓延速度）。
 * {@code Properties.copy(...)} 只复制方块属性，不复制可燃性，所以必须逐个声明。
 * 这里用三个小接口把数值集中在一处，具体方块类实现对应接口即可。
 */
public final class Flammability {

    /** 树叶：和橡木树叶一致（燃烧 60 / 蔓延 30）。 */
    public interface Leaves extends IForgeBlock {
        @Override
        default int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return 60;
        }

        @Override
        default int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return 30;
        }
    }

    /** 原木、树木、去皮原木、去皮树木：燃烧 5 / 蔓延 5。 */
    public interface Wood extends IForgeBlock {
        @Override
        default int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return 5;
        }

        @Override
        default int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return 5;
        }
    }

    /** 木板、台阶、楼梯、栅栏、栅栏门：燃烧 20 / 蔓延 5。 */
    public interface Planks extends IForgeBlock {
        @Override
        default int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return 20;
        }

        @Override
        default int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
            return 5;
        }
    }

    private Flammability() {
    }
}
