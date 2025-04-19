package com.matthewperiut.babric_sprint.mixin.client;

import com.matthewperiut.babric_sprint.particle.BlockDustParticle;
import com.matthewperiut.babric_sprint.particle.BlockDustParticleSTAPI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.matthewperiut.babric_sprint.BabricSprint.stapi;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    private World world;

    @Shadow
    private Minecraft client;

    @Inject(method = "addParticle", at = @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z", ordinal = 0), cancellable = true)
    void addCustomParticle(String particleName, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        if (particleName.startsWith("tilecrack_")) {
            int n3 = Integer.parseInt(particleName.substring(particleName.indexOf("_") + 1));
            if (stapi) {
                this.client.particleManager.addParticle(new BlockDustParticleSTAPI(this.world, x, y, z, velocityX, velocityY, velocityZ, Block.BLOCKS[n3], 0, 0));
            } else {
                this.client.particleManager.addParticle(new BlockDustParticle(this.world, x, y, z, velocityX, velocityY, velocityZ, Block.BLOCKS[n3], 0, 0));
            }
            ci.cancel();
        }
    }
}
