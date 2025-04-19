package com.matthewperiut.babric_sprint.particle;

import net.minecraft.block.Block;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.Tessellator;
import net.minecraft.world.World;

public class BlockDustParticle
        extends Particle {
    private Block block;
    private int colorMultiplier = 0xFFFFFF;

    public BlockDustParticle(World world, double x, double y, double z, double g, double h, double i, Block arg2, int j, int k) {
        super(world, x, y, z, g, h, i);
        this.block = arg2;
        //this.setMiscTexture(arg2.method_396(0, k));
        // set misc texture as 0
        // textureId
        this.textureId = arg2.getTexture(0, k);
        // note: should reference a variable in block for particle's gravity, but in newer versions not used.
        // gravityStrength
        this.gravityStrength = 1.0F;
        this.blue = 0.6f;
        this.green = 0.6f;
        this.red = 0.6f;
        this.scale /= 2.0f;

        if (block != Block.GRASS_BLOCK)
            colorMultiplier = this.block.getColorMultiplier(this.world, (int)x, (int)y, (int)z);

        method_1301();
    }


    public void method_1301() {

        this.red *= (float) (colorMultiplier >> 16 & 0xFF) / 255.0f;
        this.green *= (float) (colorMultiplier >> 8 & 0xFF) / 255.0f;
        this.blue *= (float) (colorMultiplier & 0xFF) / 255.0f;
    }

    // getLayer
    @Override
    public int getGroup() {
        return 1;
    }

    @Override
    public void render(Tessellator arg, float f, float g, float h, float i, float j, float k) {
        // Check if the block is grass and apply a green tint
        if (this.block == Block.LEAVES) {
            this.red = 0.0f;   // No red
            this.green = 1.0f; // Full green
            this.blue = 0.0f;  // No blue
        }

        // Get texture coordinates for the particle
        float f2 = ((float) (this.textureId % 16) + this.prevU / 4.0f) / 16.0f;
        float f3 = f2 + 0.015609375f;
        float f4 = ((float) (this.textureId / 16) + this.prevV / 4.0f) / 16.0f;
        float f5 = f4 + 0.015609375f;

        // Set particle scale (size)
        float f6 = 0.1f * this.scale;

        // Particle position adjustments
        float f7 = (float) (this.prevX + (this.x - this.prevX) * (double) f - xOffset);
        float f8 = (float) (this.prevY + (this.y - this.prevY) * (double) f - yOffset);
        float f9 = (float) (this.prevZ + (this.z - this.prevZ) * (double) f - zOffset);

        // Set the color using the red, green, and blue values
        float f10 = 1.0f;
        arg.color(f10 * this.red, f10 * this.green, f10 * this.blue);

        // Render the particle
        arg.vertex(f7 - g * f6 - j * f6, f8 - h * f6, f9 - i * f6 - k * f6, f2, f5);
        arg.vertex(f7 - g * f6 + j * f6, f8 + h * f6, f9 - i * f6 + k * f6, f2, f4);
        arg.vertex(f7 + g * f6 + j * f6, f8 + h * f6, f9 + i * f6 + k * f6, f3, f4);
        arg.vertex(f7 + g * f6 - j * f6, f8 - h * f6, f9 + i * f6 - k * f6, f3, f5);
    }

}