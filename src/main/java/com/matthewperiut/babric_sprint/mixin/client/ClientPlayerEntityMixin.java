package com.matthewperiut.babric_sprint.mixin.client;

import com.matthewperiut.babric_sprint.api.EntitySprinting;
import net.minecraft.block.material.Material;
import net.minecraft.client.input.Input;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.matthewperiut.babric_sprint.SprintingConstants.SPRINT_FLAG;

@Mixin(ClientPlayerEntity.class)
abstract public class ClientPlayerEntityMixin extends PlayerEntity implements EntitySprinting {
    @Unique
    private int ticksSinceSprintingChanged = 0;
    @Unique
    private int sprintStartTicks = 0;
    @Unique
    private float prevMovementForward = 0;

    @Shadow
    public Input input;

    @Override
    public void setSprinting(boolean sprinting) {
        setFlag(SPRINT_FLAG, sprinting);
        this.ticksSinceSprintingChanged = sprinting ? 600 : 0;
    }

    @Override
    public boolean isSprinting() {
        return getFlag(SPRINT_FLAG);
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    public void tickSprintingMovement(CallbackInfo ci) {
        if (this.ticksSinceSprintingChanged > 0) {
            --this.ticksSinceSprintingChanged;
            if (this.ticksSinceSprintingChanged == 0) {
                this.setSprinting(false);
            }
        }

        if (this.sprintStartTicks > 0) {
            --this.sprintStartTicks;
        }

        // canSprint is relevant if implementing hunger
        boolean canSprint = true;
        float threshold = 0.8f;
        // input.movementForward named
        float movementForward = input.movementForward;

        // we come back to this part
        //boolean bl3 = movementForward >= threshold;
        boolean bl3 = prevMovementForward < threshold && movementForward > threshold;

        if (this.onGround && movementForward >= threshold && !this.isSprinting() && canSprint /*&& !this.isUsingItem()*/) {
            if (input.unused) {
                this.setSprinting(true);
            } else {
                if (this.sprintStartTicks == 0) {
                    this.sprintStartTicks = 7;
                } else if (bl3 && sprintStartTicks > 0) {
                    this.setSprinting(true);
                    this.sprintStartTicks = 0;
                }
            }
        }

        if (this.isSprinting() && (movementForward < threshold || this.horizontalCollision || !canSprint || isInFluid(Material.WATER))) {
            this.setSprinting(false);
        }

        prevMovementForward = movementForward;
    }

    // shove this down here because it is useless and just required by jvm
    public ClientPlayerEntityMixin(World world) {
        super(world);
    }
}
