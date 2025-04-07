package ianeli.moredyes.mixin;

import ianeli.moredyes.ColorHandler;
import ianeli.moredyes.CustomSheep;
import ianeli.moredyes.blocks.ModBlocks;
import ianeli.moredyes.items.ModItems;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepEntity.class)
public abstract class SheepEntityMixin extends AnimalEntity implements CustomSheep {
    @Unique
    Random random = Random.create();
    @Unique
    private static final TrackedData<Integer> CUSTOM_COLOR = DataTracker.registerData(SheepEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected SheepEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(CUSTOM_COLOR, 0x000000);
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("HEAD"))
    private void saveCustomColor(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("CustomColor", ((SheepEntity) (Object) this).getDataTracker().get(CUSTOM_COLOR));
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("HEAD"))
    private void loadCustomColor(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("CustomColor")) {
            ((SheepEntity) (Object) this).getDataTracker().set(CUSTOM_COLOR, nbt.getInt("CustomColor").orElse(0x000000));
        }
    }

    @Override
    public int getCustomColor() {
        return ((SheepEntity) (Object) this).getDataTracker().get(CUSTOM_COLOR);
    }

    @Override
    public void setCustomColor(int c) {
        ((SheepEntity) (Object) this).getDataTracker().set(CUSTOM_COLOR, c);
    }

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    public void interact(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(ModItems.DyeVialFilled)) {
            int color = itemStack.get(DataComponentTypes.DYED_COLOR).rgb();
            setCustomColor(color);
            cir.setReturnValue(ActionResult.SUCCESS);
        } else if (itemStack.getItem() instanceof DyeItem dyeItem) {
            int newColor = dyeItem.getColor().getEntityColor();
            setCustomColor(newColor);
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "initialize", at = @At("HEAD"))
    public void init(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CallbackInfoReturnable<EntityData> cir) {
        DyeColor spawnColor = ((SheepEntity) (Object) this).selectSpawnColor(world,
                ((SheepEntity) (Object) this).getBlockPos());
        int c = spawnColor.getEntityColor();
        setCustomColor(c);
    }

    @Inject(method = "createChild", at = @At("HEAD"), cancellable = true)
    public void child(ServerWorld serverWorld, PassiveEntity passiveEntity, CallbackInfoReturnable<SheepEntity> cir) {
        SheepEntity baby = (SheepEntity) EntityType.SHEEP.create(serverWorld, SpawnReason.BREEDING);
        if (baby instanceof CustomSheep customBaby && passiveEntity instanceof CustomSheep other) {
            int c1 = getCustomColor();
            int c2 = other.getCustomColor();
            customBaby.setCustomColor(ColorHandler.mixColors(c1, c2));
        }
        cir.setReturnValue(baby);
    }

    @Override
    public ItemEntity dropStack(ServerWorld world, ItemStack stack, float yOffset) {
        if (stack.isIn(ItemTags.WOOL)) {
            ItemStack s = ModBlocks.CustomWool.asItem().getDefaultStack();
            s.set(DataComponentTypes.DYED_COLOR, new DyedColorComponent(getCustomColor()));
            return ((SheepEntity) (Object) this).dropStack(world, s.copyWithCount(stack.getCount()), 1.0f);
        } else {
            return super.dropStack(world, stack, yOffset);
        }
    }

}