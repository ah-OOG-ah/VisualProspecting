package com.sinthoras.visualprospecting.hooks;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import com.sinthoras.visualprospecting.Utils;
import com.sinthoras.visualprospecting.VP;
import com.sinthoras.visualprospecting.database.ResetClientCacheCommand;
import com.sinthoras.visualprospecting.integration.voxelmap.VoxelMapEventHandler;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public class HooksClient extends HooksShared {

    @Override
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event) {
        super.fmlLifeCycleEvent(event);

        VP.keyAction = new KeyBinding(
                "visualprospecting.key.action.name",
                Keyboard.KEY_DELETE,
                "visualprospecting.key.action.category");
        ClientRegistry.registerKeyBinding(VP.keyAction);
    }

    @Override
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes."
    public void fmlLifeCycleEvent(FMLInitializationEvent event) {
        super.fmlLifeCycleEvent(event);
        FMLCommonHandler.instance().bus().register(new HooksKey());
    }

    @Override
    // postInit "Handle interaction with other mods, complete your setup based on this."
    public void fmlLifeCycleEvent(FMLPostInitializationEvent event) {
        super.fmlLifeCycleEvent(event);
        ClientCommandHandler.instance.registerCommand(new ResetClientCacheCommand());
        if (Utils.isVoxelMapInstalled()) {
            MinecraftForge.EVENT_BUS.register(new VoxelMapEventHandler());
        }
    }

    @Override
    public void fmlLifeCycleEvent(FMLServerAboutToStartEvent event) {
        super.fmlLifeCycleEvent(event);
    }

    @Override
    public void fmlLifeCycleEvent(FMLServerStartingEvent event) {
        super.fmlLifeCycleEvent(event);
    }

    @Override
    public void fmlLifeCycleEvent(FMLServerStartedEvent event) {
        super.fmlLifeCycleEvent(event);
    }

    @Override
    public void fmlLifeCycleEvent(FMLServerStoppingEvent event) {
        super.fmlLifeCycleEvent(event);
    }

    @Override
    public void fmlLifeCycleEvent(FMLServerStoppedEvent event) {
        super.fmlLifeCycleEvent(event);
    }
}
