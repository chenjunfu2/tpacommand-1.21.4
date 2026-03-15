package chenjunfu2.tpacommand.home;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerHomeData
{
	static final Map<UUID,HomeData> homeDataMap= new HashMap<>();

	public static HomeData getPlayerHomeData(ServerPlayerEntity serverPlayer)
	{
		var uuid = serverPlayer.getUuid();
		return homeDataMap.computeIfAbsent(uuid, k -> new HomeData());
	}
}
