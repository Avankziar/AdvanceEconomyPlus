package main.java.me.avankziar.aep.spigot.cmd.tree;

import java.util.ArrayList;

import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;

public class CommandConstructor extends BaseConstructor
{
    public ArrayList<ArgumentConstructor> subcommands;
    public ArrayList<String> tablist;

	public CommandConstructor(CommandExecuteType cet, String path, boolean canConsoleAccess,
    		ArgumentConstructor...argumentConstructors)
    {
		super(cet,
				getPlugin().getYamlHandler().getCom().getString(path+".Name"),
				path,
				getPlugin().getYamlHandler().getCom().getString(path+".Permission"),
				getPlugin().getYamlHandler().getCom().getString(path+".Suggestion"),
				getPlugin().getYamlHandler().getCom().getString(path+".CommandString"),
				getPlugin().getYamlHandler().getCom().getString(path+".HelpInfo"),
				canConsoleAccess);
        this.subcommands = new ArrayList<>();
        this.tablist = new ArrayList<>();
        for(ArgumentConstructor ac : argumentConstructors)
        {
        	if(ac == null)
        	{
        		continue;
        	}
        	this.subcommands.add(ac);
        	this.tablist.add(ac.getName());
        }
        getPlugin().getCommandTree().add(this);
    }
	
	public void addArg(AdvancedEconomyPlus plugin, ArgumentConstructor...argumentConstructors)
	{
		for(ArgumentConstructor ac : argumentConstructors)
        {
			if(ac == null)
			{
				continue;
			}
        	this.subcommands.add(ac);
        	this.tablist.add(ac.getName());
        }
		int index = 0;
		for(CommandConstructor cc : plugin.getCommandTree())
		{
			if(cc.getPath().equals(this.getPath()))
			{
				break;
			}
			index++;
		}
		plugin.getCommandTree().set(index, this);
	}
}