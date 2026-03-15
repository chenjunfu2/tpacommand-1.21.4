package chenjunfu2.tpacommand;

import chenjunfu2.tpacommand.home.HomeCommand;
import chenjunfu2.tpacommand.tpa.TpaCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public class CommandRegister
{
	public static void RegisterCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		TpaCommand.RegisterCommand(dispatcher);
		HomeCommand.RegisterCommand(dispatcher);
	}
}