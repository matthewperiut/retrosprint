package com.periut.retrosprint.particle;

import com.periut.retroapi.client.texture.AtlasExpander;
import net.minecraft.block.Block;
import net.minecraft.client.render.Tessellator;
import net.minecraft.world.World;

/**
 * Only used when RetroAPI is present and StationAPI is not. RetroAPI expands the terrain atlas
 * past vanilla's 256x16 grid, so the column count and sub-sprite size have to scale with it -
 * mirrors the math RetroAPI's own BlockParticleMixin applies to vanilla's BlockParticle. When
 * StationAPI is also present, StationAPI owns the atlas and BlockDustParticleSTAPI is used instead.
 */
public class BlockDustParticleRetroAPI extends BlockDustParticle {

    public BlockDustParticleRetroAPI(World world, double x, double y, double z, double g, double h, double i, Block block, int j, int k) {
        super(world, x, y, z, g, h, i, block, j, k);
    }

    @Override
    public void render(Tessellator arg, float f, float g, float h, float i, float j, float k) {
        if (this.block == Block.LEAVES) {
            this.red = 0.0f;
            this.green = 1.0f;
            this.blue = 0.0f;
        }

        float columns = AtlasExpander.terrainAtlasSize / 16.0f;
        float subSpriteSize = 0.015609375f * 256.0f / AtlasExpander.terrainAtlasSize;

        float f2 = ((float) (this.textureId % (int) columns) + this.prevU / 4.0f) / columns;
        float f3 = f2 + subSpriteSize;
        float f4 = ((float) (this.textureId / (int) columns) + this.prevV / 4.0f) / columns;
        float f5 = f4 + subSpriteSize;

        float f6 = 0.1f * this.scale;

        float f7 = (float) (this.prevX + (this.x - this.prevX) * (double) f - xOffset);
        float f8 = (float) (this.prevY + (this.y - this.prevY) * (double) f - yOffset);
        float f9 = (float) (this.prevZ + (this.z - this.prevZ) * (double) f - zOffset);

        float f10 = 1.0f;
        arg.color(f10 * this.red, f10 * this.green, f10 * this.blue);

        arg.vertex(f7 - g * f6 - j * f6, f8 - h * f6, f9 - i * f6 - k * f6, f2, f5);
        arg.vertex(f7 - g * f6 + j * f6, f8 + h * f6, f9 - i * f6 + k * f6, f2, f4);
        arg.vertex(f7 + g * f6 + j * f6, f8 + h * f6, f9 + i * f6 + k * f6, f3, f4);
        arg.vertex(f7 + g * f6 - j * f6, f8 - h * f6, f9 + i * f6 - k * f6, f3, f5);
    }
}
