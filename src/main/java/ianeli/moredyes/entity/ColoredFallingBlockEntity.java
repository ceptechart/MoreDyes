package ianeli.moredyes.entity;

import com.mojang.logging.LogUtils;
import ianeli.moredyes.blockEntity.ColoredBlockEntity;
import ianeli.moredyes.blocks.ColoredFalling;
import ianeli.moredyes.blocks.CustomPowder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.AutomaticItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryOps;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.EntityTrackerEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.function.Predicate;

public class ColoredFallingBlockEntity extends Entity {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final BlockState DEFAULT_BLOCK_STATE;
    private static final int DEFAULT_TIME = 0;
    private static final float DEFAULT_FALL_HURT_AMOUNT = 0.0F;
    private static final int DEFAULT_FALL_HURT_MAX = 40;
    private static final boolean DEFAULT_DROP_ITEM = true;
    private static final boolean DEFAULT_DESTROYED_ON_LANDING = false;
    private BlockState blockState;
    public int timeFalling;
    public boolean dropItem;
    private boolean destroyedOnLanding;
    private boolean hurtEntities;
    private int fallHurtMax;
    private float fallHurtAmount;
    @Nullable
    public NbtCompound blockEntityData;
    public boolean shouldDupe;
    protected static final TrackedData<BlockPos> BLOCKPOS;
    public static final TrackedData<Integer> COLOR;

    public ColoredFallingBlockEntity(EntityType<? extends ColoredFallingBlockEntity> entityType, World world) {
        super(entityType, world);
        this.blockState = DEFAULT_BLOCK_STATE;
        this.timeFalling = 0;
        this.dropItem = true;
        this.destroyedOnLanding = false;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 0.0F;
    }

    private ColoredFallingBlockEntity(World world, double x, double y, double z, BlockState blockState) {
        this(ModEntities.ColoredFallingBlock, world);
        this.blockState = blockState;
        this.intersectionChecked = true;
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;
        this.setFallingBlockPos(this.getBlockPos());
    }

    public static ColoredFallingBlockEntity spawnFromBlock(World world, BlockPos pos, BlockState state) {
        ColoredFallingBlockEntity fallingBlockEntity = new ColoredFallingBlockEntity(world, (double)pos.getX() + (double)0.5F, (double)pos.getY(), (double)pos.getZ() + (double)0.5F, state.contains(Properties.WATERLOGGED) ? (BlockState)state.with(Properties.WATERLOGGED, false) : state);
        BlockEntity be = world.getBlockEntity(pos);
        Integer col = 0x000000;
        if (be instanceof ColoredBlockEntity coloredBlockEntity) {
            col = coloredBlockEntity.getColor();
        }
        world.setBlockState(pos, state.getFluidState().getBlockState(), 3);
        world.spawnEntity(fallingBlockEntity);
        fallingBlockEntity.dataTracker.set(COLOR, col);
        return fallingBlockEntity;
    }

    public boolean isAttackable() {
        return false;
    }

    public final boolean damage(ServerWorld world, DamageSource source, float amount) {
        if (!this.isAlwaysInvulnerableTo(source)) {
            this.scheduleVelocityUpdate();
        }

        return false;
    }

    public void setFallingBlockPos(BlockPos pos) {
        this.dataTracker.set(BLOCKPOS, pos);
    }

    public BlockPos getFallingBlockPos() {
        return (BlockPos)this.dataTracker.get(BLOCKPOS);
    }

    protected Entity.MoveEffect getMoveEffect() {
        return MoveEffect.NONE;
    }


    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(BLOCKPOS, BlockPos.ORIGIN);
        builder.add(COLOR, 0x000000);
    }

    public boolean canHit() {
        return !this.isRemoved();
    }

    protected double getGravity() {
        return 0.04;
    }

    public void tick() {
        if (this.blockState.isAir()) {
            this.discard();
        } else {
            Block block = this.blockState.getBlock();
            ++this.timeFalling;
            this.applyGravity();
            this.move(MovementType.SELF, this.getVelocity());
            this.tickBlockCollision();
            this.tickPortalTeleportation();
            World world = this.getWorld();

            if (world instanceof ServerWorld serverWorld) {
                if (this.isAlive() || this.shouldDupe) {
                    BlockPos blockPos = this.getBlockPos();
                    boolean isConcrete = this.blockState.getBlock() instanceof CustomPowder;
                    boolean concreteSolidify = isConcrete && this.getWorld().getFluidState(blockPos).isIn(FluidTags.WATER);
                    double d = this.getVelocity().lengthSquared();
                    if (isConcrete && d > (double)1.0F) {
                        BlockHitResult blockHitResult = this.getWorld().raycast(new RaycastContext(new Vec3d(this.lastX, this.lastY, this.lastZ), this.getPos(), RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.SOURCE_ONLY, this));
                        if (blockHitResult.getType() != HitResult.Type.MISS && this.getWorld().getFluidState(blockHitResult.getBlockPos()).isIn(FluidTags.WATER)) {
                            blockPos = blockHitResult.getBlockPos();
                            concreteSolidify = true;
                        }
                    }

                    if (!this.isOnGround() && !concreteSolidify) {
                        if (this.timeFalling > 100 && (blockPos.getY() <= this.getWorld().getBottomY() || blockPos.getY() > this.getWorld().getTopYInclusive()) || this.timeFalling > 600) {
                            if (this.dropItem && serverWorld.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                this.dropItem(serverWorld, block);
                            }

                            this.discard();
                        }
                    } else {
                        BlockState blockState = this.getWorld().getBlockState(blockPos);
                        this.setVelocity(this.getVelocity().multiply(0.7, (double)-0.5F, 0.7));
                        if (!blockState.isOf(Blocks.MOVING_PISTON)) {
                            if (!this.destroyedOnLanding) {
                                boolean canPlace = blockState.canReplace(new AutomaticItemPlacementContext(this.getWorld(), blockPos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                                boolean canFall = FallingBlock.canFallThrough(this.getWorld().getBlockState(blockPos.down())) && (!isConcrete || !concreteSolidify);
                                boolean shouldPlace = this.blockState.canPlaceAt(this.getWorld(), blockPos) && !canFall;
                                if (canPlace && shouldPlace) {
                                    if (this.blockState.contains(Properties.WATERLOGGED) && this.getWorld().getFluidState(blockPos).getFluid() == Fluids.WATER) {
                                        this.blockState = (BlockState)this.blockState.with(Properties.WATERLOGGED, true);
                                    }

                                    if (this.getWorld().setBlockState(blockPos, this.blockState, 3)) {
                                        ((ServerWorld)this.getWorld()).getChunkManager().chunkLoadingManager.sendToOtherNearbyPlayers(this, new BlockUpdateS2CPacket(blockPos, this.getWorld().getBlockState(blockPos)));
                                        if (block instanceof ColoredFalling) {
                                            ((ColoredFalling)block).onLanding(this.getWorld(), blockPos, this.blockState, blockState, this);
                                        }

                                        BlockEntity blockEntity = this.getWorld().getBlockEntity(blockPos);
                                        if (blockEntity != null) {
                                            if (blockEntity instanceof ColoredBlockEntity coloredBlockEntity) {

                                                coloredBlockEntity.setColor(this.dataTracker.get(COLOR));
                                                this.getWorld().updateListeners(this.getBlockPos(), this.blockState, this.blockState, 0);

                                            }
                                        }
                                        this.discard();
                                    } else if (this.dropItem && serverWorld.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                        this.discard();
                                        this.onDestroyedOnLanding(block, blockPos);
                                        this.dropItem(serverWorld, block);
                                    }
                                } else {
                                    this.discard();
                                    if (this.dropItem && serverWorld.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                                        this.onDestroyedOnLanding(block, blockPos);
                                        this.dropItem(serverWorld, block);
                                    }
                                }
                            } else {
                                this.discard();
                                this.onDestroyedOnLanding(block, blockPos);
                            }
                        }
                    }
                }
            }

            this.setVelocity(this.getVelocity().multiply(0.98));
        }
    }

    public void onDestroyedOnLanding(Block block, BlockPos pos) {
        if (block instanceof ColoredFalling) {
            ((ColoredFalling)block).onDestroyedOnLanding(this.getWorld(), pos, this);
        }

    }

    public boolean handleFallDamage(double fallDistance, float damagePerDistance, DamageSource damageSource) {
        if (!this.hurtEntities) {
            return false;
        } else {
            int i = MathHelper.ceil(fallDistance - (double)1.0F);
            if (i < 0) {
                return false;
            } else {
                Predicate<Entity> predicate = EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.and(EntityPredicates.VALID_LIVING_ENTITY);
                Block bl = this.blockState.getBlock();
                DamageSource var10000;
                if (bl instanceof ColoredFalling falling) {
                    var10000 = falling.getDamageSource(this);
                } else {
                    var10000 = this.getDamageSources().fallingBlock(this);
                }

                DamageSource damageSource2 = var10000;
                float f = (float)Math.min(MathHelper.floor((float)i * this.fallHurtAmount), this.fallHurtMax);
                this.getWorld().getOtherEntities(this, this.getBoundingBox(), predicate).forEach((entity) -> entity.serverDamage(damageSource2, f));
                boolean isAnvil = this.blockState.isIn(BlockTags.ANVIL);
                if (isAnvil && f > 0.0F && this.random.nextFloat() < 0.05F + (float)i * 0.05F) {
                    BlockState blockState = AnvilBlock.getLandingState(this.blockState);
                    if (blockState == null) {
                        this.destroyedOnLanding = true;
                    } else {
                        this.blockState = blockState;
                    }
                }

                return false;
            }
        }
    }

    protected void writeCustomDataToNbt(NbtCompound nbt) {
        RegistryOps<NbtElement> registryOps = this.getRegistryManager().getOps(NbtOps.INSTANCE);
        nbt.put("BlockState", BlockState.CODEC, registryOps, this.blockState);
        nbt.putInt("Time", this.timeFalling);
        nbt.putBoolean("DropItem", this.dropItem);
        nbt.putBoolean("HurtEntities", this.hurtEntities);
        nbt.putFloat("FallHurtAmount", this.fallHurtAmount);
        nbt.putInt("FallHurtMax", this.fallHurtMax);
        if (this.blockEntityData != null) {
            nbt.put("TileEntityData", this.blockEntityData);
        }

        nbt.putBoolean("CancelDrop", this.destroyedOnLanding);
    }

    protected void readCustomDataFromNbt(NbtCompound nbt) {
        RegistryOps<NbtElement> registryOps = this.getRegistryManager().getOps(NbtOps.INSTANCE);
        this.blockState = (BlockState)nbt.get("BlockState", BlockState.CODEC, registryOps).orElse(DEFAULT_BLOCK_STATE);
        this.timeFalling = nbt.getInt("Time", 0);
        boolean bl = this.blockState.isIn(BlockTags.ANVIL);
        this.hurtEntities = nbt.getBoolean("HurtEntities", bl);
        this.fallHurtAmount = nbt.getFloat("FallHurtAmount", 0.0F);
        this.fallHurtMax = nbt.getInt("FallHurtMax", 40);
        this.dropItem = nbt.getBoolean("DropItem", true);
        this.blockEntityData = (NbtCompound)nbt.getCompound("TileEntityData").map(NbtCompound::copy).orElse(null);
        this.destroyedOnLanding = nbt.getBoolean("CancelDrop", false);
    }

    public void setHurtEntities(float fallHurtAmount, int fallHurtMax) {
        this.hurtEntities = true;
        this.fallHurtAmount = fallHurtAmount;
        this.fallHurtMax = fallHurtMax;
    }

    public void setDestroyedOnLanding() {
        this.destroyedOnLanding = true;
    }

    public boolean doesRenderOnFire() {
        return false;
    }

    public void populateCrashReport(CrashReportSection section) {
        super.populateCrashReport(section);
        section.add("Immitating BlockState", this.blockState.toString());
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    protected Text getDefaultName() {
        return Text.translatable("entity.minecraft.falling_block_type", new Object[]{this.blockState.getBlock().getName()});
    }

    public Packet<ClientPlayPacketListener> createSpawnPacket(EntityTrackerEntry entityTrackerEntry) {
        return new EntitySpawnS2CPacket(this, entityTrackerEntry, Block.getRawIdFromState(this.getBlockState()));
    }

    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.blockState = Block.getStateFromRawId(packet.getEntityData());
        this.intersectionChecked = true;
        double d = packet.getX();
        double e = packet.getY();
        double f = packet.getZ();
        this.setPosition(d, e, f);
        this.setFallingBlockPos(this.getBlockPos());
    }

    public int getColor() {
        return this.dataTracker.get(COLOR);
    }

    @Nullable
    public Entity teleportTo(TeleportTarget teleportTarget) {
        RegistryKey<World> registryKey = teleportTarget.world().getRegistryKey();
        RegistryKey<World> registryKey2 = this.getWorld().getRegistryKey();
        boolean bl = (registryKey2 == World.END || registryKey == World.END) && registryKey2 != registryKey;
        Entity entity = super.teleportTo(teleportTarget);
        this.shouldDupe = entity != null && bl;
        return entity;
    }

    static {
        DEFAULT_BLOCK_STATE = Blocks.SAND.getDefaultState();
        BLOCKPOS = DataTracker.registerData(ColoredFallingBlockEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
        COLOR = DataTracker.registerData(ColoredFallingBlockEntity.class, TrackedDataHandlerRegistry.INTEGER);
    }
}