package net.spellbladenext.interfaces;

import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.spellbladenext.blocks.blockentities.NetherPortal;
import net.spellbladenext.blocks.blockentities.NetherPortalFrame;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static java.lang.Math.sqrt;

public interface IPiglinSummon {
     default void tick(){
     }
     public static Optional<NetherPortalFrame> summonNetherPortal(World level, LivingEntity livingEntity, boolean home){
          double xRand = -1 + level.getRandom().nextDouble()*2;
          double zRand = -1 + level.getRandom().nextDouble()*2;
          double d0 = sqrt(xRand*xRand+zRand*zRand);

          BlockHitResult result = level.raycast(
                  new RaycastContext(
                          livingEntity.getEyePos(),
                          new Vec3d(
                                  livingEntity.getX() + 40 * xRand / d0,
                                  livingEntity.getY() - 40 * .2,
                                  livingEntity.getZ() + 40 * zRand / d0),
                          RaycastContext.ShapeType.COLLIDER,
                          RaycastContext.FluidHandling.NONE,
                          livingEntity)
          );
          if(result.getType() != HitResult.Type.MISS) {
               BlockPos pos = result.getBlockPos();
               if(result.getPos().subtract(livingEntity.getEyePos()).horizontalLength() > 2) {
                    boolean flag = level.getBlockState(pos.up()).shouldSuffocate(level, result.getBlockPos().up())
                            || level.getBlockState(pos.up(2)).shouldSuffocate(level, pos.up(2))
                            || level.getBlockState(pos.up(3)).shouldSuffocate(level, pos.up(3))
                            || level.getBlockState(pos.up(4)).shouldSuffocate(level, pos.up(4));
                    int ii = 0;
                    boolean found = true;
                    while (flag) {
                         pos = pos.up();
                         flag = level.getBlockState(pos.up()).shouldSuffocate(level, result.getBlockPos().up())
                                 || level.getBlockState(pos.up(2)).shouldSuffocate(level, pos.up(2))
                                 || level.getBlockState(pos.up(3)).shouldSuffocate(level, pos.up(3))
                                 || level.getBlockState(pos.up(4)).shouldSuffocate(level, pos.up(4));
                         ii++;
                         if (ii > 10) {
                              found = false;
                              break;
                         }
                    }
                    if (found) {
                         boolean bool = level.getRandom().nextBoolean();
                         NetherPortal portal = new NetherPortal(ExampleModFabric.NETHERPORTAL, level, livingEntity, pos, 0, bool,home);
                         NetherPortalFrame frame = new NetherPortalFrame(ExampleModFabric.NETHERPORTALFRAME, level, livingEntity, pos, 0, bool,home);
                         if(livingEntity instanceof ServerPlayerEntity playerEntity) {
                              playerEntity.getStatHandler().setStat(playerEntity, Stats.CUSTOM.getOrCreateStat(ExampleModFabric.SINCELASTHEX), 0);
                         }

                         return Optional.of(frame);

                    }
               }
          }
          return Optional.empty();
     }

     @Nullable
     static BlockPos getSafePositionAroundPlayer(World world, BlockPos pos, int range)
     {
          if(range == 0)
          {
               return null;
          }
          BlockPos safestPos = null;
          for(int attempts = 0; attempts < 1; attempts++)
          {
               int a = -1;
               int b = -1;
               int c = -1;
               if(world.getRandom().nextBoolean()){
                    a = 1;
               }
               if(world.getRandom().nextBoolean()){
                    b = 1;
               }
               if(world.getRandom().nextBoolean()){
                    c = 1;
               }
               int posX = pos.getX()  + a*world.getRandom().nextInt(10 );
               int posY = pos.getY() + world.getRandom().nextInt(10) - 10 / 2;
               int posZ = pos.getZ()  + c* world.getRandom().nextInt(10);
               BlockPos testPos = findGround(world, new BlockPos(posX, posY, posZ));

               if(testPos != null && world.getFluidState(testPos).isEmpty() && world.getBlockState(testPos.down()).isOpaque())
               {
                    safestPos = testPos;
                    break;
               }
          }
          return safestPos;
     }

     @Nullable
     private static BlockPos findGround(World world, BlockPos pos) {
          if(world.getBlockState(pos).isAir()) {
               BlockPos downPos = pos;
               while(World.isValid(downPos.down()) && world.getBlockState(downPos.down()).isAir() && downPos.down().isWithinDistance(pos, 20))
               {
                    downPos = downPos.down();
               }
               if(!world.getBlockState(downPos.down()).isAir())
               {
                    return downPos;
               }
          } else {
               BlockPos upPos = pos;
               while(World.isValid(upPos.up()) && !world.getBlockState(upPos.up()).isAir() && upPos.up().isWithinDistance(pos, 20))
               {
                    upPos = upPos.up();
               }
               if(!world.getBlockState(upPos.up()).isAir())
               {
                    return upPos;
               }
          }
          return null;
     }
}
