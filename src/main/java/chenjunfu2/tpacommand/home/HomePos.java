package chenjunfu2.tpacommand.home;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HomePos
{
	public RegistryKey<World> dimension;
	public Vec3d position;
	public float yaw;
	public float pitch;
	
	HomePos(RegistryKey<World> dimension, Vec3d position,float yaw,float pitch)
	{
		this.dimension=dimension;
		this.position=position;
		this.yaw=yaw;
		this.pitch=pitch;
	}
	
	HomePos()
	{
		this.dimension= World.OVERWORLD;
		this.position= new Vec3d(0.0,0.0,0.0);
		this.yaw=0.0f;
		this.pitch=0.0f;
	}
	
	static HomePos fromPlayer(ServerPlayerEntity serverPlayer)
	{
		return new HomePos(serverPlayer.getServerWorld().getRegistryKey(), serverPlayer.getPos(),serverPlayer.getYaw(),serverPlayer.getPitch());
	}
	
	void writeToNbt(NbtCompound w)
	{
		w.putString("dimension",dimension.getValue().toString());
		
		{
			var pos = new NbtCompound();
			pos.putDouble("x", position.getX());
			pos.putDouble("y", position.getY());
			pos.putDouble("z", position.getZ());
			w.put("position",pos);
		}
		
		w.putFloat("yaw",yaw);
		w.putFloat("pitch",pitch);
	}
	
	
	void readFromNbt(NbtCompound r)
	{
		dimension = RegistryKey.of(RegistryKeys.WORLD, Identifier.of(r.getString("dimension")));
		
		{
			var pos = r.getCompound("position");
			double posX,posY,posZ;
			posX = pos.getDouble("x");
			posY = pos.getDouble("y");
			posZ = pos.getDouble("z");
			position = new Vec3d(posX,posY,posZ);
		}
		
		yaw = r.getFloat("yaw");
		pitch = r.getFloat("pitch");
	}
	
}
