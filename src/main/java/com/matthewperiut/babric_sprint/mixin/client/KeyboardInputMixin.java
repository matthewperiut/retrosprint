package com.matthewperiut.babric_sprint.mixin.client;

import com.matthewperiut.babric_sprint.BabricSprint;
import com.matthewperiut.babric_sprint.stapi.KeybindHelperStapi;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.matthewperiut.babric_sprint.BabricSprint.stapi;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {
    @Shadow
    private boolean[] keys;

    @Inject(method = "updateKey", at = @At("TAIL"))
    void extraInput(int i, boolean bl, CallbackInfo ci) {
        if (stapi) {
            if (i == KeybindHelperStapi.getKeyCode()) {
                keys[6] = bl;

            }
        } else {
            if (i == BabricSprint.runKeyCode) {
                keys[6] = bl;
            }
        }
    }

    @Inject(method = "update", at = @At("TAIL"))
    public void addRunKey(PlayerEntity par1, CallbackInfo ci) {
        unused = keys[6];
    }
}
