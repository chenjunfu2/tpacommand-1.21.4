package chenjunfu2.tpacommand.home;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static chenjunfu2.tpacommand.ModInit.minecraftServer;

public class HomeCommand
{
	static class PlayerHomeSuggestion implements SuggestionProvider<ServerCommandSource>
	{
		@Override
		public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder)
		{
			ServerCommandSource source = context.getSource();
			ServerPlayerEntity self = source.getPlayer();
			
			// 如果命令源不是玩家，返回空建议
			if (self == null)
			{
				return Suggestions.empty();
			}
			
			//根据玩家查询家名称
			var homeData = PlayerHomeData.getPlayerHomeData(self).getHomePosMap().keySet();
			for(var k:homeData)
			{
				builder.suggest(k);
			}
			
			return builder.buildFuture();
		}
	}
	private final static SuggestionProvider<ServerCommandSource> SUGGEST_PLAYER_HOMES = new PlayerHomeSuggestion();
	
	public static void RegisterCommand(CommandDispatcher<ServerCommandSource> dispatcher)
	{
		LiteralArgumentBuilder<ServerCommandSource> homeCommand =
			CommandManager.literal("home")
			              .requires(ServerCommandSource::isExecutedByPlayer)
			              .executes(HomeCommand::helpHome)//显示帮助
			              .then(CommandManager.literal("tp")
			                                  .then(CommandManager.argument("home_name", StringArgumentType.string())
			                                                      .suggests(SUGGEST_PLAYER_HOMES)
			                                                      .executes(context -> tpHome(context, StringArgumentType.getString(context, "home_name")))))
			              .then(CommandManager.literal("set")
			                                  .then(CommandManager.argument("home_name", StringArgumentType.string())
			                                                      .executes(context -> setHome(context, StringArgumentType.getString(context, "home_name")))))
			              .then(CommandManager.literal("del")
			                                  .then(CommandManager.argument("home_name", StringArgumentType.string())
			                                                      .suggests(SUGGEST_PLAYER_HOMES)
			                                                      .executes(context -> delHome(context, StringArgumentType.getString(context, "home_name")))))
			              .then(CommandManager.literal("list")
			                                  .executes(HomeCommand::listHome));
	
	
		dispatcher.register(homeCommand);
	}
	
	
	private static int helpHome(CommandContext<ServerCommandSource> context)
	{
		ServerPlayerEntity serverPlayer = context.getSource().getPlayer();
		if(serverPlayer==null)
		{
			return 0;
		}
		
		serverPlayer.sendMessage(Text.literal("test"));
		return 1;
	}
	
	private static int tpHome(CommandContext<ServerCommandSource> context, String target)
	{
		ServerPlayerEntity serverPlayer = context.getSource().getPlayer();
		if(serverPlayer==null)
		{
			return 0;
		}
		
		var homeData = PlayerHomeData.getPlayerHomeData(serverPlayer);
		var tpPos = homeData.findPos(target,serverPlayer);
		if(tpPos == null)
		{
			return 0;
		}
		
		serverPlayer.teleport(
			minecraftServer.getWorld(tpPos.dimension),
			tpPos.position.getX(),
			tpPos.position.getY(),
			tpPos.position.getZ(),
			new HashSet<>(),
			tpPos.yaw,
			tpPos.pitch,
			true);
		
		return 1;
	}
	
	private static int setHome(CommandContext<ServerCommandSource> context, String target)
	{
		ServerPlayerEntity serverPlayer = context.getSource().getPlayer();
		if(serverPlayer==null)
		{
			return 0;
		}
		
		var homeData = PlayerHomeData.getPlayerHomeData(serverPlayer);
		if(!homeData.addCurrentPos(target,serverPlayer))
		{
			return 0;
		}
		
		return 1;
	}
	
	private static int delHome(CommandContext<ServerCommandSource> context, String target)
	{
		ServerPlayerEntity serverPlayer = context.getSource().getPlayer();
		if(serverPlayer==null)
		{
			return 0;
		}
		
		var homeData = PlayerHomeData.getPlayerHomeData(serverPlayer);
		if(!homeData.removePos(target,serverPlayer))
		{
			return 0;
		}
		
		return 1;
	}
	
	private static int listHome(CommandContext<ServerCommandSource> context)
	{
		ServerPlayerEntity serverPlayer = context.getSource().getPlayer();
		if(serverPlayer==null)
		{
			return 0;
		}
		
		var homeData = PlayerHomeData.getPlayerHomeData(serverPlayer).getHomePosMap();
		if(homeData.isEmpty())
		{
			serverPlayer.sendMessage(Text.literal("你还没有设置家"));
			return 1;
		}
		
		StringBuilder message = new StringBuilder();
		message.append("§6§l=== 你的家列表 (").append(homeData.size()).append("个) ===§r\n");
		
		int index = 0;
		for (var entry : homeData.entrySet()) {
			String homeName = entry.getKey();
			var pos = entry.getValue();
			
			// 格式化每一行：序号. 家名称 - [维度] (X, Y, Z)
			message.append(String.format("§e%d. §a%s §7- §b[%s] §f(%.1f, %.1f, %.1f)§r\n",
			                             ++index,
			                             homeName,
			                             pos.dimension.getValue().toString(),
			                             pos.position.getX(),
			                             pos.position.getY(),
			                             pos.position.getZ()
			                            ));
		}
		
		serverPlayer.sendMessage(Text.literal(message.toString()));
		
		return 1;
	}
}
