package com.periut.retrosprint.mixin;

import com.periut.retrosprint.api.EntitySprinting;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

import static com.periut.retrosprint.SprintingConstants.SPRINT_FLAG;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntitySprinting {
    @Shadow
    protected abstract void setFlag(int index, boolean value);

    @Shadow
    protected abstract boolean getFlag(int index);

    @Shadow
    public World world;

    @Shadow
    @Final
    public Box boundingBox;

    @Shadow
    public double x;

    @Shadow
    public double y;


    @Shadow
    public float standingEyeHeight;

    @Shadow
    public double z;

    @Shadow
    public float width;

    @Shadow
    protected Random random;

    @Shadow
    public double velocityX;

    @Shadow
    public double velocityZ;

	@Shadow
	public boolean onGround;

	@Shadow
	public float horizontalSpeed;

	@Shadow
	public int nextStepSoundDistance;

	private float bufferedStepDistance = 0f;
	private boolean flushedThisTick = false;


    @Override
    public void setSprinting(boolean sprinting) {
        setFlag(SPRINT_FLAG, sprinting);
    }

    @Override
    public boolean isSprinting() {
        return getFlag(SPRINT_FLAG);
    }

    @Inject(method = "baseTick", at = @At("HEAD"))
    public void addParticles(CallbackInfo ci) {
        int n4;
        int n3;
        int n2;
        int n;
        if (this.isSprinting() && !isTouchingWater() && (n4 = this.world.getBlockId(n3 = MathHelper.floor(this.x), n2 = MathHelper.floor(this.y - (double) 0.2f - (double) this.standingEyeHeight), n = MathHelper.floor(this.z))) > 0) {
            this.world.addParticle("tilecrack_" + n4, this.x + ((double) this.random.nextFloat() - 0.5) * (double) this.width, this.boundingBox.minY + 0.1, this.z + ((double) this.random.nextFloat() - 0.5) * (double) this.width, -this.velocityX * 4.0, 1.5, -this.velocityZ * 4.0);
        }

    }

    @Override
    public boolean isTouchingWater() {
        return world.isMaterialInBox(this.boundingBox.expand(-0.10000000149011612, -0.4000000059604645, -0.10000000149011612), Material.WATER);
    }

	@Redirect(
		method = "move",
		at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/entity/Entity;horizontalSpeed:F",
			opcode = org.objectweb.asm.Opcodes.PUTFIELD
		)
	)
	private void onlyAccumulateStepDistanceOnGround(Entity instance, float value) {
		if (this.onGround) {
			this.horizontalSpeed = value + bufferedStepDistance;
			flushedThisTick = bufferedStepDistance > 0f;
			bufferedStepDistance = 0f;
		} else {
			bufferedStepDistance += (value - this.horizontalSpeed);
			flushedThisTick = false;
		}
	}

	@Inject(method = "move", at = @At("TAIL"))
	private void catchUpStepSoundDistance(double dx, double dy, double dz, CallbackInfo ci) {
		if (flushedThisTick) {
			this.nextStepSoundDistance = (int) this.horizontalSpeed + 1;
			flushedThisTick = false;
		}
	}
}
