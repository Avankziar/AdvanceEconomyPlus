package main.java.me.avankziar.aep.spigot.cmd.money;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.api.MatchApi;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.events.ActionLoggerEvent;
import main.java.me.avankziar.aep.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.aep.spigot.handler.BankAccountHandler;
import main.java.me.avankziar.aep.spigot.handler.AEPUserHandler;
import main.java.me.avankziar.aep.spigot.object.BankAccount;
import main.java.me.avankziar.aep.spigot.object.AEPUser;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import net.milkbowl.vault.economy.EconomyResponse;

public class ARGMoneyBankPay extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyBankPay(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String tobankname = args[1];
		String amountstring = args[2];
		double amount = 0.0;
		String comment = "";
		if(!EconomySettings.settings.isPlayerAccount() || !EconomySettings.settings.isBank())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoBankOrPlayerAccount")));
			return;
		}
		if(MatchApi.isDouble(amountstring))
		{
			amount = Double.parseDouble(amountstring);
		} else
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoNumber")
					.replace("%args%", amountstring)));
			return;
		}
		if(!MatchApi.isPositivNumber(amount))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NumberIsNegativ")
					.replace("%args%", amountstring)));
			return;
		}
		if(args.length >= 4)
		{
			for(int i = 4; i < args.length; i++)
			{
				if(i == args.length-1)
				{
					comment += args[i];
				} else
				{
					comment += args[i]+" ";
				}
				
			}
		}
		AEPUser eco = AEPUserHandler.getEcoPlayer(player);
		if(!AdvancedEconomyPlus.getVaultApi().has(player, amount))
		{
			//&f%amount% &cübersteigt dein Guthaben!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Pay.NotEnoughBalance")
					.replace("%currency%", AdvancedEconomyPlus.getVaultApi().currencyNamePlural())
					.replace("%amount%", amountstring)));
			return;
		}
		BankAccount tobank = null;
		if(MatchApi.isBankAccountNumber(tobankname))
		{
			tobank = BankAccountHandler.getBankAccount(tobankname);
		} else if(BankAccountHandler.existMoreBankAccountsWithTheSameName(tobankname))
		{
			//Der Bankkontoname &f%name% &cexistiert mehrfach! Bitte spezifiziere die Angabe, durch die Eingabe der Banknummer!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("SpecifyBankNumber")
					.replace("%name%", tobankname)));
			return;
		} else
		{
			tobank = BankAccountHandler.getBankAccountFromName(tobankname);
		}
		EconomyResponse withdraw = AdvancedEconomyPlus.getVaultApi().withdrawPlayer(eco.getUUID(), amount);
		if(!withdraw.transactionSuccess())
		{
			player.sendMessage(withdraw.errorMessage);
			return;
		}
		EconomyResponse deposit = AdvancedEconomyPlus.getVaultApi().bankDeposit(tobank.getaccountNumber(), amount);
		if(!deposit.transactionSuccess())
		{
			AdvancedEconomyPlus.getVaultApi().depositPlayer(player, amount);
			player.sendMessage(deposit.errorMessage);
			return;
		}
		Bukkit.getPluginManager().callEvent(new ActionLoggerEvent(
				LocalDateTime.now(), player.getUniqueId().toString(), tobank.getaccountNumber(),
				player.getName(), tobank.getName(), eco.getUUID(), amount, ActionLoggerEvent.Type.DEPOSIT_WITHDRAW, comment));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), eco.getUUID(), -amount, eco.getBalance()));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), tobank.getaccountNumber(), amount,
				tobank.getBalance()));
		String frommessage = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.BankPay.DepositWithDraw")
				.replace("%orderer%", player.getName())
				.replace("%name%", player.getName())
				.replace("%balance%", AdvancedEconomyPlus.getVaultApi().format(eco.getBalance()))
				.replace("%name1%", player.getName())
				.replace("%number2%", tobank.getaccountNumber()).replace("%name2%", tobank.getName()));
		String toomessage = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.BankPay.DepositWithDraw")
				.replace("%orderer%", player.getName())
				.replace("%name%", tobank.getName())
				.replace("%balance%", AdvancedEconomyPlus.getVaultApi().format(tobank.getBalance()))
				.replace("%name1%", player.getName())
				.replace("%number2%", tobank.getaccountNumber()).replace("%name2%", tobank.getName()));
		boolean bungee = EconomySettings.settings.isBungee();
		player.sendMessage(frommessage);
		AEPUser owner = AEPUserHandler.getEcoPlayer(tobank.getOwnerUUID());
		if(owner != null)
		{
			if(owner.isMoneyBankFlow())
			{
				if(bungee)
				{
					BungeeBridge.sendBungeeMessage(player, tobank.getOwnerUUID(), toomessage, false, "");
				} else
				{
					if(Bukkit.getPlayer(UUID.fromString(owner.getUUID())) != null)
					{
						Bukkit.getPlayer(UUID.fromString(owner.getUUID())).sendMessage(toomessage);
					}
				}
			}
		}
		for(String uuid : tobank.getViceUUID())
		{
			AEPUser ep = AEPUserHandler.getEcoPlayer(uuid);
			if(ep != null)
			{
				if(ep.isMoneyBankFlow())
				{
					if(bungee)
					{
						BungeeBridge.sendBungeeMessage(player, uuid, toomessage, false, "");
					} else
					{
						if(Bukkit.getPlayer(UUID.fromString(uuid)) != null)
						{
							Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(toomessage);
						}
					}
				}
			}
		}
		for(String uuid : tobank.getMemberUUID())
		{
			AEPUser ep = AEPUserHandler.getEcoPlayer(uuid);
			if(ep != null)
			{
				if(ep.isMoneyBankFlow())
				{
					if(bungee)
					{
						BungeeBridge.sendBungeeMessage(player, uuid, toomessage, false, "");
					} else
					{
						if(Bukkit.getPlayer(UUID.fromString(uuid)) != null)
						{
							Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(toomessage);
						}
					}
				}
			}
		}
		return;
	}
}