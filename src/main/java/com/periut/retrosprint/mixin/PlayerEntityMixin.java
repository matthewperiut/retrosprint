package com.periut.retrosprint.mixin;

import com.periut.retrosprint.api.EntitySprinting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
abstract public class PlayerEntityMixin {

    @Inject(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"))
    public void onAttack(Entity target, CallbackInfo ci) {
        PlayerEntity player = (PlayerEntity) (Object) this;

        // Check if the player is sprinting
        if (((EntitySprinting) (Object) player).isSprinting()) {
            // Apply sprinting effect: Increase knockback
            applyKnockback(player, target);

            // Optionally: Stop sprinting after the attack
            //((EntitySprinting) (Object) player).setSprinting(false);
        }
    }

    @Unique
    private void applyKnockback(PlayerEntity player, Entity target) {
        // Basic knockback calculation based on sprinting
        float knockbackStrength = 1.2F; // You can adjust this value for more/less knockback
        double xKnockback = -MathHelper.sin(player.yaw * ((float) Math.PI / 180F)) * knockbackStrength;
        double zKnockback = MathHelper.cos(player.yaw * ((float) Math.PI / 180F)) * knockbackStrength;

        // Apply knockback to the target entity
        target.addVelocity(xKnockback, 0.1, zKnockback);

        // Reduce player's velocity slightly (to simulate loss of momentum after sprinting attack)
        player.velocityX *= 0.6;
        player.velocityZ *= 0.6;
    }


}
