package com.periut.retrosprint.mixin.server;

import com.periut.retrosprint.SprintingConstants;
import com.periut.retrosprint.api.EntitySprinting;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow
    private ServerPlayerEntity player;

    @Inject(method = "handleClientCommand", at = @At(value = "HEAD"), cancellable = true)
    void handleExtendedClientCommands(ClientCommandC2SPacket packet, CallbackInfo ci) {
        if (packet.mode == SprintingConstants.START_RUNNING_COMMAND) {
            ((EntitySprinting) (Object) player).setSprinting(true);
            ci.cancel();
        } else if (packet.mode == SprintingConstants.STOP_RUNNING_COMMAND) {
            ((EntitySprinting) (Object) player).setSprinting(false);
            ci.cancel();
        }
    }
}
