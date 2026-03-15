package chenjunfu2.tpacommand;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;
import java.util.UUID;

public class PlayerData
{
	private final String name;
	private final UUID uuid;
	
	public PlayerData(String name, UUID uuid)
	{
		this.name = name;
		this.uuid = uuid;
	}
	
	public PlayerData(ServerPlayerEntity serverPlayerEntity)
	{
		this.name = serverPlayerEntity.getName().getString();
		this.uuid = serverPlayerEntity.getUuid();
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getUuid()
	{
		return name;
	}
	
	public ServerPlayerEntity getServerPlayerEntity(MinecraftServer server)
	{
		return server.getPlayerManager().getPlayer(uuid);
	}
	
	@Override
	public int hashCode()
	{
		return Objects.hash(name, uuid);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		
		var r = (PlayerData)o;
		return name.equals(r.name) && uuid.equals(r.uuid);
	}
}
