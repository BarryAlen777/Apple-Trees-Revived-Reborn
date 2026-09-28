package cn.blockforge.generated.appletreesreborn.block;

import cn.blockforge.generated.appletreesreborn.config.AppleConfig;
import cn.blockforge.generated.appletreesreborn.world.FruitType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

/**
 * 挂在树叶下面的苹果。对应 1.12.2 的 BlockApplePlant：
 * 8 个成熟阶段，只长在树叶下方，成熟后可以采摘／自然掉落。
 */
public class ApplePlantBlock extends Block implements BonemealableBlock {

    public static final int MAX_AGE = 7;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

    /** 和 1.12.2 的 APPLE_AABB 一一对应，只是换算成 0~16 的方块坐标。 */
    private static final VoxelShape[] SHAPES = new VoxelShape[]{
            Block.box(4.0D, 14.4D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 12.8D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 11.2D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 8.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 6.4D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 4.8D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 3.2D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(4.0D, 3.2D, 4.0D, 12.0D, 16.0D, 12.0D)
    };

    private final FruitType fruit;

    public ApplePlantBlock(FruitType fruit, Properties properties) {
        super(properties);
        this.fruit = fruit;
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(AGE)];
    }

    // ---- 只能挂在树叶下面 ----

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.above()).is(BlockTags.LEAVES);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (direction == Direction.UP && !state.canSurvive(level, pos)) {
            // 树叶没了就一起消失。成熟苹果的掉落交给 getDrops，
            // 这里不能再手动掉一次，否则会掉双份。
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    // ---- 生长 ----

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.getBlockState(pos.above()).is(BlockTags.LEAVES)) {
            level.destroyBlock(pos, true);
            return;
        }

        int age = state.getValue(AGE);
        if (age >= MAX_AGE) {
            if (AppleConfig.NATURAL_FALL.get()
                    && random.nextInt(AppleConfig.NATURAL_FALL_CHANCE.get()) == 0) {
                this.harvest(level, pos, state, random);
            }
            return;
        }

        if (level.getMaxLocalRawBrightness(pos) >= 8) {
            float chance = growthChance(level, pos);
            double speed = AppleConfig.FRUIT_GROWTH_SPEED.get();
            int bound = Math.max(1, (int) ((20.0F / chance) / speed));
            if (random.nextInt(bound + 1) == 0) {
                level.setBlock(pos, state.setValue(AGE, age + 1), 2);
            }
        }
    }

    /** 1.12.2 getGrowthChance 的 1:1 移植。 */
    private static float growthChance(Level level, BlockPos pos) {
        float f = 2.0F;
        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                float f1 = 0.0F;
                if (level.getBlockState(pos.offset(i, 0, j)).isAir()) {
                    f1 = 2.0F;
                }
                if (i != 0 || j != 0) {
                    f1 /= 2.0F;
                }
                f += f1;
            }
        }
        boolean flag = isNotLeavesOrAir(level, pos.north()) || isNotLeavesOrAir(level, pos.south());
        boolean flag1 = isNotLeavesOrAir(level, pos.east()) || isNotLeavesOrAir(level, pos.west());
        if (flag && flag1) {
            f /= 2.0F;
        } else {
            boolean flag2 = isNotLeavesOrAir(level, pos.west().north())
                    || isNotLeavesOrAir(level, pos.east().north())
                    || isNotLeavesOrAir(level, pos.north().south())
                    || isNotLeavesOrAir(level, pos.west().south());
            if (flag2) {
                f /= 2.0F;
            }
        }
        return f;
    }

    private static boolean isNotLeavesOrAir(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return !state.isAir() && !state.is(BlockTags.LEAVES);
    }

    // ---- 采摘 ----

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {
        if (state.getValue(AGE) >= MAX_AGE && AppleConfig.EASY_HARVEST.get()) {
            if (!level.isClientSide) {
                this.harvest(level, pos, state, level.getRandom());
            }
            // 两边都算"成功"，客户端才知道这次交互被用掉了，不会再去用手里拿的东西。
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    /** 掉苹果 + 轻微音效；有一定概率退回幼果，保证一棵树能持续结果。 */
    private void harvest(Level level, BlockPos pos, BlockState state, RandomSource random) {
        if (level.isClientSide) {
            return;
        }
        Block.popResource(level, pos, new ItemStack(this.fruit.getFruit(), AppleConfig.FRUIT_DROP_COUNT.get()));
        level.playSound(null, pos, SoundEvents.CROP_BREAK, SoundSource.BLOCKS, 0.4F, 1.2F);
        if (random.nextInt(3) == 0) {
            level.setBlock(pos, state.setValue(AGE, 0), 2);
        } else {
            level.removeBlock(pos, false);
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        if (state.getValue(AGE) < MAX_AGE) {
            return List.of();
        }
        return List.of(new ItemStack(this.fruit.getFruit(), AppleConfig.FRUIT_DROP_COUNT.get()));
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(this.fruit.getFruit());
    }

    // ---- 粒子反馈：快成熟时冒绿色光点 ----

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(AGE) >= MAX_AGE - 1 && random.nextInt(12) == 0) {
            level.addParticle(ParticleTypes.HAPPY_VILLAGER,
                    pos.getX() + 0.5D, pos.getY() + 0.4D, pos.getZ() + 0.5D,
                    0.0D, 0.0D, 0.0D);
        }
    }

    // ---- 骨粉 ----

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return state.getValue(AGE) < MAX_AGE;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        int next = Math.min(MAX_AGE, state.getValue(AGE) + random.nextInt(4) + 2);
        level.setBlock(pos, state.setValue(AGE, next), 2);
    }
}
