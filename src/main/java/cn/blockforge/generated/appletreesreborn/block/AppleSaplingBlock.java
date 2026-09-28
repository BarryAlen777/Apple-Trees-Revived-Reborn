package cn.blockforge.generated.appletreesreborn.block;

import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import cn.blockforge.generated.appletreesreborn.world.AppleTreeGenerator;
import cn.blockforge.generated.appletreesreborn.world.FruitType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * 苹果树苗。对应 1.12.2 的 BlockAppleSapling：两个生长阶段，光照足够时随机生长，
 * 长成后变成一棵苹果树。
 */
public class AppleSaplingBlock extends BushBlock implements BonemealableBlock {

    public static final IntegerProperty STAGE = BlockStateProperties.STAGE;
    private static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 12.0D, 14.0D);

    private final FruitType fruit;

    public AppleSaplingBlock(FruitType fruit, Properties properties) {
        super(properties);
        this.fruit = fruit;
        this.registerDefaultState(this.stateDefinition.any().setValue(STAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        return List.of(new ItemStack(this.asItem()));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getMaxLocalRawBrightness(pos.above()) >= 9
                && random.nextInt(AppleConfig.SAPLING_GROW_CHANCE.get()) == 0) {
            this.advance(level, pos, state, random);
        }
    }

    private void advance(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        if (state.getValue(STAGE) == 0) {
            level.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            this.growTree(level, pos, state, random);
        }
    }

    private void growTree(ServerLevel level, BlockPos pos, BlockState state, RandomSource random) {
        level.removeBlock(pos, false);
        // natural = false：树苗长出来的树从最幼的果子开始（和 1.12.2 一致）。
        if (!AppleTreeGenerator.growTree(level, random, pos, this.fruit, false, false)) {
            level.setBlock(pos, state, 4);
        }
    }

    // ---- 骨粉 ----

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return random.nextFloat() < 0.45F;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        this.advance(level, pos, state, random);
    }
}
