package chenjunfu2.tpacommand;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.world.World;

import java.nio.file.Files;
import java.nio.file.Path;

public class ModData
{
	public static Path modFolder = null;
	
	public static void createModFolder(MinecraftServer server, String folderName)
	{
		ServerWorld overworld = server.getWorld(World.OVERWORLD);
		if (overworld == null)
		{
			return;
		}
		
		try
		{
			modFolder = overworld.getServer().getRunDirectory().resolve(folderName);
			
			if (!Files.exists(modFolder))
			{
				Files.createDirectories(modFolder);
			}
			else if (!Files.isDirectory(modFolder))
			{
				CrashReport crashReport = CrashReport.create(
					new Exception("Path exists but is not a directory"),
					"Creating mod data folder");
				
				crashReport
					.addElement("Folder Creation Details")
					.add("Path", modFolder.toString())
					.add("Expected", "A directory")
					.add("Actual", "A file");
				
				throw new CrashException(crashReport);
			}
			
			System.out.println("Created mod folders at: " + modFolder);
		}
		catch (Exception e)
		{
			throw new CrashException(CrashReport.create(e,"Creating mod data folder"));
		}
	}









}
