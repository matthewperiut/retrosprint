package com.periut.retrosprint.particle;

import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.world.World;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.mixin.arsenic.client.ParticleAccessor;

public class BlockDustParticleSTAPI extends BlockDustParticle {
    private Sprite texture;

    public BlockDustParticleSTAPI(World arg, double d, double e, double f, double g, double h, double i, Block arg2, int j, int k) {
        super(arg, d, e, f, g, h, i, arg2, j, k);

        texture = StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).getSprite(arg2.getAtlas().getTexture(textureId).getId());
    }

    @Override
    public void render(Tessellator tessellator, float delta, float yawX, float pitchX, float yawY, float pitchY1, float pitchY2) {
        float
                startU = texture.getMinU() + (prevU / 4) * (texture.getMaxU() - texture.getMinU()),
                endU = startU + 0.24975F * (texture.getMaxU() - texture.getMinU()),
                startV = texture.getMinV() + (prevV / 4) * (texture.getMaxV() - texture.getMinV()),
                endV = startV + 0.24975F * (texture.getMaxV() - texture.getMinV()),
                randomMultiplier = 0.1F * ((ParticleAccessor) this).getScale(),
                renderX = (float) (prevX + (x - prevX) * (double) delta - xOffset),
                renderY = (float) (prevY + (y - prevY) * (double) delta - yOffset),
                renderZ = (float) (prevZ + (z - prevZ) * (double) delta - zOffset),
                brightness = getBrightnessAtEyes(delta);
        tessellator.color(brightness * ((ParticleAccessor) this).getRed(), brightness * ((ParticleAccessor) this).getGreen(), brightness * ((ParticleAccessor) this).getBlue());
        tessellator.vertex(renderX - yawX * randomMultiplier - pitchY1 * randomMultiplier, renderY - pitchX * randomMultiplier, renderZ - yawY * randomMultiplier - pitchY2 * randomMultiplier, startU, endV);
        tessellator.vertex(renderX - yawX * randomMultiplier + pitchY1 * randomMultiplier, renderY + pitchX * randomMultiplier, renderZ - yawY * randomMultiplier + pitchY2 * randomMultiplier, startU, startV);
        tessellator.vertex(renderX + yawX * randomMultiplier + pitchY1 * randomMultiplier, renderY + pitchX * randomMultiplier, renderZ + yawY * randomMultiplier + pitchY2 * randomMultiplier, endU, startV);
        tessellator.vertex(renderX + yawX * randomMultiplier - pitchY1 * randomMultiplier, renderY - pitchX * randomMultiplier, renderZ + yawY * randomMultiplier - pitchY2 * randomMultiplier, endU, endV);
    }
}
