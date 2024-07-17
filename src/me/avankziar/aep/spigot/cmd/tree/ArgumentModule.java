package me.avankziar.aep.spigot.cmd.tree;

import java.io.IOException;

import org.bukkit.command.CommandSender;

import me.avankziar.aep.general.cmd.tree.ArgumentConstructor;
import me.avankziar.aep.spigot.AEP;

public abstract class ArgumentModule
{
	public ArgumentConstructor argumentConstructor;
	public static AEP plugin = AEP.getPlugin();

    public ArgumentModule(ArgumentConstructor argumentConstructor)
    {
    	if(argumentConstructor == null)
    	{
    		return;
    	}
    	this.argumentConstructor = argumentConstructor;
    	AEP.getPlugin().getArgumentMap().put(argumentConstructor.getPath(), this);
    }
    
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args) throws IOException;

}
