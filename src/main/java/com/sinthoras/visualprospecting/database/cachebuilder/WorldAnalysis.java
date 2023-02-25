package com.sinthoras.visualprospecting.database.cachebuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;

import net.minecraft.util.ChunkCoordinates;

import com.sinthoras.visualprospecting.Utils;
import com.sinthoras.visualprospecting.VP;
import com.sinthoras.visualprospecting.database.ServerCache;
import io.xol.enklume.MinecraftWorld;

public class WorldAnalysis {

    private final MinecraftWorld world;

    public WorldAnalysis(File worldDirectory) throws IOException {
        world = new MinecraftWorld(worldDirectory);
    }

    public void cacheVeins() throws IOException, DataFormatException {

        VP.info("Starting to parse world save to cache GT vein locations. This might take some time...");

        ServerCache.instance.reset();

        final List<Integer> dimensionIds = world.getDimensionIds();
        AnalysisProgressTracker.setNumberOfDimensions(dimensionIds.size());

        for (int dimensionId : dimensionIds) {

            final DimensionAnalysis dimension = new DimensionAnalysis(dimensionId);
            dimension.processMinecraftWorld(world);
            AnalysisProgressTracker.dimensionProcessed();
        }

        AnalysisProgressTracker.processingFinished();
        VP.info("Saving ore vein cache...");
        ServerCache.instance.saveVeinCache();
    }

    /**
     * Die, !visualprospectingnearspawn, die!
     * 
     * @param spawn World spawn as a ChunkCoordinates. Despite the name, this is actually in block coordinates.
     */
    public void cacheSpawnVeins(ChunkCoordinates spawn) throws IOException, DataFormatException {

        VP.info("Re-caching spawn chunks. This might take some time...");

        // Reset the spawn chunks
        // I'm pretty sure the spawn chunks are a 16x16 area centered on the world spawn

        // Convert to chunk coords, and make a 17x17 rect centered on the spawn chunk
        // I'm *fairly certain* that this will convert block pos to chunk pos... probably
        int spawnChunkX = Utils.coordBlockToChunk(spawn.posX);
        int spawnChunkZ = Utils.coordBlockToChunk(spawn.posZ);

        // The first corner is 8 chunks less in XZ, and the last is 8 more
        int startX = spawnChunkX - 8;
        int startZ = spawnChunkZ - 8;
        int endX = spawnChunkX + 8;
        int endZ = spawnChunkZ + 8;

        // delete
        ServerCache.instance.resetSome(0, startX, startZ, endX, endZ);

        // Only doing one dim
        AnalysisProgressTracker.setNumberOfDimensions(1);

        // Analyze
        (new DimensionAnalysis(0)).processMinecraftWorldSection(world, startX, startZ, endX, endZ);
        AnalysisProgressTracker.dimensionProcessed();
        AnalysisProgressTracker.processingFinished();

        // Log and save
        VP.info("Saving ore vein cache...");
        ServerCache.instance.saveVeinCache();
    }
}
