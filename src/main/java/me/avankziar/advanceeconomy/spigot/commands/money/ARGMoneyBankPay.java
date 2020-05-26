package main.java.me.avankziar.advanceeconomy.spigot.commands.money;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.advanceeconomy.spigot.api.MatchApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.spigot.assistance.StringValues;
import main.java.me.avankziar.advanceeconomy.spigot.commands.CommandModule;
import main.java.me.avankziar.advanceeconomy.spigot.events.EconomyLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.events.TrendLoggerEvent;
import main.java.me.avankziar.advanceeconomy.spigot.AdvanceEconomy;
import main.java.me.avankziar.advanceeconomy.spigot.object.BankAccount;
import main.java.me.avankziar.advanceeconomy.spigot.object.EcoPlayer;
import main.java.me.avankziar.advanceeconomy.spigot.object.EconomySettings;
import net.milkbowl.vault.economy.EconomyResponse;

public class ARGMoneyBankPay extends CommandModule
{
	private AdvanceEconomy plugin;
	
	public ARGMoneyBankPay(AdvanceEconomy plugin)
	{
		super(StringValues.ARG_MONEY_BANKPAY,StringValues.PERM_CMD_MONEY_BANKPAY,
				AdvanceEconomy.moneyarguments,3,9999,StringValues.ARG_MONEY_BANKPAY_ALIAS,
				StringValues.MONEY_SUGGEST_BANKPAY);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String path = StringValues.PATH_MONEY;
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
		EcoPlayer eco = EcoPlayer.getEcoPlayer(player);
		if(!AdvanceEconomy.getVaultApi().has(player, amount))
		{
			//&f%amount% &cübersteigt dein Guthaben!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString(path+"Pay.NotEnoughBalance")
					.replace("%currency%", AdvanceEconomy.getVaultApi().currencyNamePlural())
					.replace("%amount%", amountstring)));
			return;
		}
		BankAccount tobank = null;
		if(MatchApi.isBankAccountNumber(tobankname))
		{
			tobank = BankAccount.getBankAccount(tobankname);
		} else if(BankAccount.existMoreBankAccountsWithTheSameName(tobankname))
		{
			//Der Bankkontoname &f%name% &cexistiert mehrfach! Bitte spezifiziere die Angabe, durch die Eingabe der Banknummer!
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("SpecifyBankNumber")
					.replace("%name%", tobankname)));
			return;
		} else
		{
			tobank = BankAccount.getBankAccountFromName(tobankname);
		}
		EconomyResponse withdraw = AdvanceEconomy.getVaultApi().withdrawPlayer(eco.getUUID(), amount);
		if(!withdraw.transactionSuccess())
		{
			player.sendMessage(withdraw.errorMessage);
			return;
		}
		EconomyResponse deposit = AdvanceEconomy.getVaultApi().bankDeposit(tobank.getaccountNumber(), amount);
		if(!deposit.transactionSuccess())
		{
			AdvanceEconomy.getVaultApi().depositPlayer(player, amount);
			player.sendMessage(deposit.errorMessage);
			return;
		}
		Bukkit.getPluginManager().callEvent(new EconomyLoggerEvent(
				LocalDateTime.now(), player.getUniqueId().toString(), tobank.getaccountNumber(),
				player.getName(), tobank.getName(), eco.getUUID(), amount, EconomyLoggerEvent.Type.DEPOSIT_WITHDRAW, comment));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), eco.getUUID(), -amount, eco.getBalance()));
		Bukkit.getPluginManager().callEvent(new TrendLoggerEvent(LocalDate.now(), tobank.getaccountNumber(), amount,
				tobank.getBalance()));
		String frommessage = ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"BankPay.DepositWithDraw")
				.replace("%orderer%", player.getName())
				.replace("%name%", player.getName())
				.replace("%balance%", AdvanceEconomy.getVaultApi().format(eco.getBalance()))
				.replace("%name1%", player.getName())
				.replace("%number2%", tobank.getaccountNumber()).replace("%name2%", tobank.getName()));
		String toomessage = ChatApi.tl(plugin.getYamlHandler().getL().getString(path+"BankPay.DepositWithDraw")
				.replace("%orderer%", player.getName())
				.replace("%name%", tobank.getName())
				.replace("%balance%", AdvanceEconomy.getVaultApi().format(tobank.getBalance()))
				.replace("%name1%", player.getName())
				.replace("%number2%", tobank.getaccountNumber()).replace("%name2%", tobank.getName()));
		boolean bungee = EconomySettings.settings.isBungee();
		player.sendMessage(frommessage);
		EcoPlayer owner = EcoPlayer.getEcoPlayer(tobank.getOwnerUUID());
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
			EcoPlayer ep = EcoPlayer.getEcoPlayer(uuid);
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
			EcoPlayer ep = EcoPlayer.getEcoPlayer(uuid);
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