package com.periut.retrosprint.mixin.client;

import com.periut.retrosprint.particle.BlockDustParticle;
import com.periut.retrosprint.particle.BlockDustParticleRetroAPI;
import com.periut.retrosprint.particle.BlockDustParticleSTAPI;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.periut.retrosprint.RetroSprint.retroapi;
import static com.periut.retrosprint.RetroSprint.stapi;

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
                // StationAPI is present, so it owns the atlas - let it handle sprite lookup regardless of RetroAPI.
                this.client.particleManager.addParticle(new BlockDustParticleSTAPI(this.world, x, y, z, velocityX, velocityY, velocityZ, Block.BLOCKS[n3], 0, 0));
            } else if (retroapi) {
                this.client.particleManager.addParticle(new BlockDustParticleRetroAPI(this.world, x, y, z, velocityX, velocityY, velocityZ, Block.BLOCKS[n3], 0, 0));
            } else {
                this.client.particleManager.addParticle(new BlockDustParticle(this.world, x, y, z, velocityX, velocityY, velocityZ, Block.BLOCKS[n3], 0, 0));
            }
            ci.cancel();
        }
    }
}
