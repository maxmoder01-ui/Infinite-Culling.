package com.infiniteculling.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class InfiniteCullingClient implements ClientModInitializer {

    private static Config config;

    private long lastFpsUpdate = System.nanoTime();
    private int frameCounter = 0;
    private int currentFps = 0;

    @Override
    public void onInitializeClient() {
        config = Config.load();

        System.out.println("[Infinite Culling.] Initialized!");

        HudRenderCallback.EVENT.register(this::renderHud);
    }

    private void updateFps() {
        frameCounter++;

        long now = System.nanoTime();
        long elapsed = now - lastFpsUpdate;

        if (elapsed >= 1_000_000_000L) {
            currentFps = (int) (
                frameCounter * 1_000_000_000L / elapsed
            );

            frameCounter = 0;
            lastFpsUpdate = now;
        }
    }

    private void renderHud(MatrixStack matrices, float tickDelta) {
        if (!config.showHud) return;

        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.options.debugEnabled) {
            return;
        }

        updateFps();

        int x = 6;
        int y = 6;

        client.textRenderer.drawWithShadow(
            matrices,
            new LiteralText("Infinite Culling.")
                .formatted(Formatting.AQUA, Formatting.BOLD),
            x, y, 0xFFFFFF
        );

        y += 12;

        if (config.showFps) {
            client.textRenderer.drawWithShadow(
                matrices,
                new LiteralText("FPS: " + currentFps)
                    .formatted(Formatting.GREEN),
                x, y, 0xFFFFFF
            );

            y += 10;
        }

        if (config.showMemory) {
            Runtime runtime = Runtime.getRuntime();

            long usedMemory =
                (runtime.totalMemory() - runtime.freeMemory())
                / (1024L * 1024L);

            client.textRenderer.drawWithShadow(
                matrices,
                new LiteralText("Java RAM: " + usedMemory + " MB")
                    .formatted(Formatting.YELLOW),
                x, y, 0xFFFFFF
            );
        }
    }
}
