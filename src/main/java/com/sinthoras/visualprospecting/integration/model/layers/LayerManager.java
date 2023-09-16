package com.sinthoras.visualprospecting.integration.model.layers;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.sinthoras.visualprospecting.integration.model.SupportedMods;
import com.sinthoras.visualprospecting.integration.model.buttons.ButtonManager;
import com.sinthoras.visualprospecting.integration.model.buttons.LayerButton;
import com.sinthoras.visualprospecting.integration.model.locations.ILocationProvider;

public abstract class LayerManager {

    private final ButtonManager buttonManager;

    protected boolean forceRefresh = false;
    private List<? extends ILocationProvider> visibleElements = new ArrayList<>();
    protected Map<SupportedMods, LayerRenderer> layerRenderer = new EnumMap<>(SupportedMods.class);
    private int miniMapWidth = 0;
    private int miniMapHeight = 0;
    private int fullscreenMapWidth = 0;
    private int fullscreenMapHeight = 0;

    public LayerManager(ButtonManager buttonManager) {
        this.buttonManager = buttonManager;
    }

    public void onButtonClicked(LayerButton button) {
        if (buttonManager.containsButton(button)) {
            toggleLayer();
        }
    }

    public boolean isLayerActive() {
        return buttonManager.isActive();
    }

    public void activateLayer() {
        buttonManager.activate();
    }

    public void deactivateLayer() {
        buttonManager.deactivate();
    }

    public void toggleLayer() {
        buttonManager.toggle();
    }

    public void forceRefresh() {
        forceRefresh = true;
    }

    public void onOpenMap() {}

    protected abstract List<? extends ILocationProvider> generateVisibleElements(int minBlockX, int minBlockZ,
            int maxBlockX, int maxBlockZ);

    protected boolean needsRegenerateVisibleElements(int minBlockX, int minBlockZ, int maxBlockX, int maxBlockZ) {
        return true;
    }

    public void recacheMiniMap(int centerBlockX, int centerBlockZ, int blockRadius) {
        recacheMiniMap(centerBlockX, centerBlockZ, blockRadius, blockRadius);
    }

    public void recacheMiniMap(int centerBlockX, int centerBlockZ, int blockWidth, int blockHeight) {
        miniMapWidth = blockWidth;
        miniMapHeight = blockHeight;
        recacheVisibleElements(centerBlockX, centerBlockZ);
    }

    public void recacheFullscreenMap(int centerBlockX, int centerBlockZ, int blockWidth, int blockHeight) {
        fullscreenMapWidth = blockWidth;
        fullscreenMapHeight = blockHeight;
        recacheVisibleElements(centerBlockX, centerBlockZ);
    }

    private void recacheVisibleElements(int centerBlockX, int centerBlockZ) {
        final int radiusBlockX = (Math.max(miniMapWidth, fullscreenMapWidth) + 1) >> 1;
        final int radiusBlockZ = (Math.max(miniMapHeight, fullscreenMapHeight) + 1) >> 1;

        final int minBlockX = centerBlockX - radiusBlockX;
        final int minBlockZ = centerBlockZ - radiusBlockZ;
        final int maxBlockX = centerBlockX + radiusBlockX;
        final int maxBlockZ = centerBlockZ + radiusBlockZ;

        checkAndUpdateElements(minBlockX, minBlockZ, maxBlockX, maxBlockZ);
    }

    protected void checkAndUpdateElements(int minBlockX, int minBlockZ, int maxBlockX, int maxBlockZ) {
        if (forceRefresh || needsRegenerateVisibleElements(minBlockX, minBlockZ, maxBlockX, maxBlockZ)) {
            visibleElements = generateVisibleElements(minBlockX, minBlockZ, maxBlockX, maxBlockZ);
            layerRenderer.values().forEach(layer -> layer.updateVisibleElements(visibleElements));
            forceRefresh = false;
        }
    }

    public void registerLayerRenderer(SupportedMods map, LayerRenderer renderer) {
        layerRenderer.put(map, renderer);
    }

    public LayerRenderer getLayerRenderer(SupportedMods map) {
        return layerRenderer.get(map);
    }
}
