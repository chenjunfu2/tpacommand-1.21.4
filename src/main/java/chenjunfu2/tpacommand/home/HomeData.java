package chenjunfu2.tpacommand.home;

import chenjunfu2.tpacommand.util.TpPos;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class HomeData
{
	private final Map<String, TpPos> homePosMap = new HashMap<>();
	private boolean bDirty = false;
	
	public Map<String, TpPos> getHomePosMap()
	{
		return homePosMap;
	}
	
	void setDirty()
	{
		bDirty = true;
	}
	
	void clearDirty()
	{
		bDirty = false;
	}
	
	boolean isDirty()
	{
		return bDirty;
	}
	
	public boolean addCurrentPos(String posName, ServerPlayerEntity serverPlayer)
	{
		if (homePosMap.containsKey(posName))
		{
			serverPlayer.sendMessage(Text.literal(String.format("添加失败，坐标名[%s]已存在！", posName)));
			return false;
		}
	
		homePosMap.put(posName, TpPos.fromPlayer(serverPlayer));
		setDirty();
		
		serverPlayer.sendMessage(Text.literal(String.format("坐标[%s]添加成功！", posName)));
		return true;
	}
	
	public boolean removePos(String posName, ServerPlayerEntity serverPlayer)
	{
		if (!homePosMap.containsKey(posName))
		{
			serverPlayer.sendMessage(Text.literal(String.format("移除失败，坐标名[%s]不存在！", posName)));
			return false;
		}
		
		homePosMap.remove(posName);
		setDirty();
		
		serverPlayer.sendMessage(Text.literal(String.format("坐标[%s]移除成功！", posName)));
		return true;
	}
	
	public TpPos findPos(String posName)
	{
		return homePosMap.get(posName);
	}
	
	public TpPos findPos(String posName, ServerPlayerEntity serverPlayer)
	{
		var posFind = findPos(posName);
		
		if(posFind == null)
		{
			serverPlayer.sendMessage(Text.literal(String.format("坐标[%s]不存在！", posName)));
			return null;
		}
		
		return posFind;
	}
	
	public void writeToNbt(NbtCompound w)
	{
		NbtCompound homes = new NbtCompound();
		
		for(var k : homePosMap.keySet())
		{
			var v = homePosMap.get(k);
			
			NbtCompound pos = new NbtCompound();
			v.writeToNbt(pos);
			
			homes.put(k,pos);
		}
		
		w.put("homes",homes);
	}
	
	public void readFromNbt(NbtCompound r)
	{
		NbtCompound homes = r.getCompound("homes");
		
		for (var k : homes.getKeys())
		{
			var v = homes.getCompound(k);
			
			var pos = new TpPos();
			pos.readFromNbt(v);
			
			homePosMap.put(k,pos);
		}
	}
	
}
