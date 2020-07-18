package main.java.me.avankziar.advanceeconomy.spigot.commands.eco;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.handler.EcoPlayerHandler;
import main.java.me.avankziar.advanceeconomy.general.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;

public class ARGEcoPlayer extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGEcoPlayer(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_ECO_PLAYER,StringValues.PERM_CMD_ECO_PLAYER,
				AdvanceEconomy.ecoarguments,2,2,StringValues.ARG_ECO_PLAYER_ALIAS,
				StringValues.ECO_SUGGEST_PLAYER);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_ECO;
		String playername = args[1];
		if(!EconomySettings.settings.isPlayerAccount())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoPlayerAccount")));
			return;
		}
		EcoPlayer eco = EcoPlayerHandler.getEcoPlayerFromName(playername);
		if(eco == null)
		{
			//Der Spieler existiert nicht!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Player.Headline")
				.replace("%player%", eco.getName())));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Player.UUID")
				.replace("%uuid%", eco.getUUID())));
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Player.Balance")
				.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
				.replace("%balance%", AdvanceEconomy.getVaultApi().format(eco.getBalance()))));
		if(EconomySettings.settings.isBank())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"Player.BankAccount")
					.replace("%bankaccount%", eco.getBankAccountNumber().toString())));
		}
		eco = null;
		return;
	}
}