package com.periut.retrosprint.mixin.client;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.periut.retrosprint.BabricSprint.lastMovementFovMultiplier;
import static com.periut.retrosprint.BabricSprint.movementFovMultiplier;

@Mixin(value = GameRenderer.class, priority = 900)
public abstract class GameRendererMixin {
    @Shadow
    protected abstract float getFov(float f);

    @Shadow
    private Minecraft client;

    @Shadow
    private float viewDistance;


    @Redirect(method = "renderWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/GameRenderer;getFov(F)F"), require = 0)
    public float redirectToCustomFov(GameRenderer instance, float value) {
        return getFovMultiplier(value, false);
    }

    @Unique
    public float getFovMultiplier(float f, boolean isHand) {
        LivingEntity entity = this.client.camera;
        float fov = 70F;

        if (isHand) {
            fov = 70F;
        }

        if (entity.isInFluid(Material.WATER)) {
            fov *= 60.0F / 70.0F;
        }

        if (entity.health <= 0) {
            float deathTimeFov = (float) entity.deathTime + f;
            fov /= (1.0F - 500F / (deathTimeFov + 500F)) * 2.0F + 1.0F;
        }

        if (!isHand) {
            fov *= lastMovementFovMultiplier + (movementFovMultiplier - lastMovementFovMultiplier) * f;
        }

        return fov;
    }

    @Inject(method = "renderFirstPersonHand", at = @At(value = "HEAD"))
    public void adjustHandFov(float f, int i, CallbackInfo ci) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(getFovMultiplier(f, true), (float) client.displayWidth / (float) client.displayHeight, 0.05F, viewDistance * 2.0F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
