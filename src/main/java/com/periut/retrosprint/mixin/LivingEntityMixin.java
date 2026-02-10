package com.periut.retrosprint.mixin;

import com.periut.retrosprint.api.EntitySprinting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

    @Mixin(LivingEntity.class)
abstract public class LivingEntityMixin extends Entity {

    public LivingEntityMixin(World world) {
        super(world);
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void increaseMovementSpeed(CallbackInfo info) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (((EntitySprinting) (Object) entity).isSprinting()) {

            if (Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityZ, 2)) < 0.25) {
                float f = yaw * ((float) Math.PI / 180);
                this.velocityX -= (double) (MathHelper.sin(f) * 0.035f);
                this.velocityZ += (double) (MathHelper.cos(f) * 0.035f);
            }
        }
    }

    @Inject(method = "jump", at = @At("TAIL"), cancellable = true)
    void addedSpeedOnSprintJump(CallbackInfo ci) {
        if (((EntitySprinting) (Object) this).isSprinting()) {
            float f = yaw * ((float) Math.PI / 180);
            this.velocityX -= (double) (MathHelper.sin(f) * 0.2f);
            this.velocityZ += (double) (MathHelper.cos(f) * 0.2f);
        }
        velocityModified = true;
    }
}
