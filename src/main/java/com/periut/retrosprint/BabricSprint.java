package com.periut.retrosprint;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class BabricSprint implements ModInitializer {
    public static boolean stapi = false;
    public static float movementFovMultiplier;
    public static float lastMovementFovMultiplier;
    public static int runKeyCode = 29;

    @Override
    public void onInitialize() {
        stapi = FabricLoader.getInstance().isModLoaded("stationapi");
    }
}
