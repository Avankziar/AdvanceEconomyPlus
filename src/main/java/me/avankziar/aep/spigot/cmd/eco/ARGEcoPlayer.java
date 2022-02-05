package main.java.me.avankziar.aep.spigot.cmd.eco;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler._AEPUserHandler_OLD;
import main.java.me.avankziar.aep.spigot.object.OLD_AEPUser;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;

public class ARGEcoPlayer extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGEcoPlayer(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String playername = args[1];
		if(!AEPSettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("NoPlayerAccount")));
			return;
		}
		OLD_AEPUser eco = _AEPUserHandler_OLD.getEcoPlayer(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEco.Player.Headline")
				.replace("%player%", eco.getName())));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEco.Player.UUID")
				.replace("%uuid%", eco.getUUID())));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEco.Player.Balance")
				.replace("%currency%", AdvancedEconomyPlus.getVault().currencyNamePlural())
				.replace("%balance%", AdvancedEconomyPlus.getVault().format(eco.getBalance()))));
		if(AEPSettings.settings.isBank())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdEco.Player.BankAccount")
					.replace("%bankaccount%", eco.getBankAccountNumber().toString())));
		}
		eco = null;
		return;
	}
}