package com.periut.retrosprint.mixin.client;

import com.periut.retrosprint.api.EntitySprinting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.periut.retrosprint.RetroSprint.lastMovementFovMultiplier;
import static com.periut.retrosprint.RetroSprint.movementFovMultiplier;

@Mixin(GameRenderer.class)
public class GameRendererMixinCaptureVar {
    @Shadow private Minecraft client;
    private float field_1831; // SOMETHING MOVEMENT IDK

    @Inject(method = "updateCamera", at = @At("HEAD"))
    void tick(CallbackInfo ci) {
        updateMovementFovMultiplier();
    }

    @Unique
    public float method_1305(PlayerEntity p) {
        if (((EntitySprinting) (Object) p).isSprinting()) {
            return 1.15f;
        }
        return 1.f;
    }

    @Unique
    private void updateMovementFovMultiplier() {
        ClientPlayerEntity class_5182 = (ClientPlayerEntity) client.camera;
        this.field_1831 = method_1305(class_5182);
        lastMovementFovMultiplier = movementFovMultiplier;
        movementFovMultiplier += (field_1831 - movementFovMultiplier) * 0.5f;
    }
}
