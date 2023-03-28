package net.spellbladenext.blocks;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Function;

public class Hexblade extends HorizontalFacingBlock {
    private final VoxelShape[] occlusionByIndex;
    private final Object2IntMap<BlockState> stateToIndex = new Object2IntOpenHashMap<>();
    public static final BooleanProperty NORTH;
    public static final BooleanProperty EAST;
    public static final BooleanProperty SOUTH;
    public static final BooleanProperty WEST;
    public static final DirectionProperty FACING;

    public Hexblade(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(NORTH, false).with(EAST, false).with(SOUTH, false).with(WEST, false));
        this.occlusionByIndex = this.makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
    }

    private static int indexFor(Direction direction) {
        return 1 << direction.getHorizontal();
    }
    protected VoxelShape[] makeShapes(float f, float g, float h, float i, float j) {
        float k = 8.0F - f;
        float l = 8.0F + f;
        float m = 8.0F - g;
        float n = 8.0F + g;
        VoxelShape voxelShape = Block.createCuboidShape(k, 0.0D, k, l, h, l);
        VoxelShape voxelShape2 = Block.createCuboidShape(m, i, 0.0D, n, j, n);
        VoxelShape voxelShape3 = Block.createCuboidShape(m, i, m, n, j, 16.0D);
        VoxelShape voxelShape4 = Block.createCuboidShape(0.0D, i, m, n, j, n);
        VoxelShape voxelShape5 = Block.createCuboidShape(m, i, m, 16.0D, j, n);
        VoxelShape voxelShape6 = VoxelShapes.union(voxelShape2, voxelShape5);
        VoxelShape voxelShape7 = VoxelShapes.union(voxelShape3, voxelShape4);
        VoxelShape[] voxelShapes = new VoxelShape[]{
                VoxelShapes.empty(),
                voxelShape3,
                voxelShape4,
                voxelShape7,
                voxelShape2,
                VoxelShapes.union(
                        voxelShape3,
                        voxelShape2
                ),
                VoxelShapes.union(
                        voxelShape4,
                        voxelShape2
                ),
                VoxelShapes.union(
                        voxelShape7,
                        voxelShape2
                ),
                voxelShape5,
                VoxelShapes.union(
                        voxelShape3,
                        voxelShape5
                ),
                VoxelShapes.union(
                        voxelShape4,
                        voxelShape5
                ),
                VoxelShapes.union(
                        voxelShape7,
                        voxelShape5
                ),
                voxelShape6,
                VoxelShapes.union(
                        voxelShape3,
                        voxelShape6
                ),
                VoxelShapes.union(
                        voxelShape4,
                        voxelShape6
                ),
                VoxelShapes.union(
                        voxelShape7,
                        voxelShape6
                )
        };

        for(int o = 0; o < 16; ++o) {
            voxelShapes[o] = VoxelShapes.union(voxelShape, voxelShapes[o]);
        }

        return voxelShapes;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }

    protected int getAABBIndex(BlockState blockState) {
        return this.stateToIndex.computeIfAbsent(blockState, (blockStat1) -> {
            int i = 0;
            if (blockStat1.equals(NORTH)) {
                i |= indexFor(Direction.NORTH);
            }

            if (blockStat1.equals(EAST)) {
                i |= indexFor(Direction.EAST);
            }

            if (blockStat1.equals(SOUTH)) {
                i |= indexFor(Direction.SOUTH);
            }

            if (blockStat1.equals(WEST)) {
                i |= indexFor(Direction.WEST);
            }

            return i;
        });
    }
    static {
        FACING = HorizontalFacingBlock.FACING;
        NORTH = ConnectingBlock.NORTH;
        EAST = ConnectingBlock.EAST;
        SOUTH = ConnectingBlock.SOUTH;
        WEST = ConnectingBlock.WEST;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        tooltip.add(Text.translatable("You have triumphed over Magus. Place to ward off Hexblade invasions "));
        tooltip.add(Text.translatable("in a 64 block radius, or carry to prevent yourself from being Hexed."));
        super.appendTooltip(stack, world, tooltip, options);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return this.occlusionByIndex[this.getAABBIndex(state)];
    }

    @Override
    protected ImmutableMap<BlockState, VoxelShape> getShapesForStates(Function<BlockState, VoxelShape> stateToShape) {
        return super.getShapesForStates(stateToShape);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return super.getCollisionShape(state, world, pos, context);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING,NORTH, EAST, WEST, SOUTH);
    }
}
