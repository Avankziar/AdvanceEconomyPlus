package main.java.me.avankziar.aep.spigot.cmd.tree;

import java.io.IOException;

import org.bukkit.command.CommandSender;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;

public abstract class ArgumentModule
{
	public ArgumentConstructor argumentConstructor;
	public static AdvancedEconomyPlus plugin = BaseConstructor.getPlugin();

    public ArgumentModule(ArgumentConstructor argumentConstructor)
    {
    	if(argumentConstructor == null)
    	{
    		return;
    	}
    	this.argumentConstructor = argumentConstructor;
    	BaseConstructor.getPlugin().getArgumentMap().put(argumentConstructor.getPath(), this);
    }
    
    //This method will process the command.
    public abstract void run(CommandSender sender, String[] args) throws IOException;

}
