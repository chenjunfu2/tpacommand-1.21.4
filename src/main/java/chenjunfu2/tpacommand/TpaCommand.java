package chenjunfu2.tpacommand;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TpaCommand implements ModInitializer {
	public static final String MOD_ID = "tpacommand";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static MinecraftServer minecraftServer = null;
	
	
	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
		                                           {
			                                           CommandRegister.RegisterCommand(dispatcher);
		                                           });
		ServerLifecycleEvents.SERVER_STARTING.register((server) ->
		                                              {
			                                              TpaCommand.minecraftServer = server;
		                                              });
		ServerLifecycleEvents.SERVER_STOPPING.register((server) ->
		                                               {
			                                               TpaCommand.minecraftServer = null;
		                                               });
		ServerLifecycleEvents.SERVER_STARTED.register((server)->
		                                              {
														  TpaData.createModFolder(server, MOD_ID);
		                                              });
		ServerTickEvents.END_SERVER_TICK.register((server)->
		                                              {
													      TpaRequest.requestTimeoutCheck(server);
												      });
	}
	
	
}