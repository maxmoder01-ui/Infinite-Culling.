package com.infiniteculling.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

public class InfiniteCullingClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        System.out.println("[Infinite Culling.] Initialized!");

        HudRenderCallback.EVENT.register(this::renderHud);
    }

    private void renderHud(MatrixStack matrices, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.options.debugEnabled) {
            return;
        }

        int fps = client.getCurrentFps();

        Runtime runtime = Runtime.getRuntime();
        long usedMemory = (runtime.totalMemory() - runtime.freeMemory())
                / (1024L * 1024L);

        int x = 6;
        int y = 6;

        client.textRenderer.drawWithShadow(
                matrices,
                new LiteralText("Infinite Culling.")
                        .formatted(Formatting.AQUA, Formatting.BOLD),
                x, y, 0xFFFFFF
        );

        y += 12;

        client.textRenderer.drawWithShadow(
                matrices,
                new LiteralText("FPS: " + fps)
                        .formatted(Formatting.GREEN),
                x, y, 0xFFFFFF
        );

        y += 10;

        client.textRenderer.drawWithShadow(
                matrices,
                new LiteralText("Java RAM: " + usedMemory + " MB")
                        .formatted(Formatting.YELLOW),
                x, y, 0xFFFFFF
        );
    }
    }
