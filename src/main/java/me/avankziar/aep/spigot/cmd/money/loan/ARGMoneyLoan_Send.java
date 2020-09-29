package main.java.me.avankziar.aep.spigot.cmd.money.loan;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.aep.spigot.object.EconomySettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGMoneyLoan_Send extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGMoneyLoan_Send(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!EconomySettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoLoan")));
			return;
		}
		if(!PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Loan.NoWaitingLoanProposal")));
			return;
		}
		String otherplayer = args[2];
		String uuid = Utility.convertNameToUUID(otherplayer);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		if(PendingHandler.loanToAccept.containsKey(uuid))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdMoney.Loan.AlreadyHavingProposal")));
			return;
		}
		LoanRepayment dr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
		String hover = plugin.getYamlHandler().getL().getString("CmdMoney.Loan.HoverInfo")
				.replace("%id%", String.valueOf(dr.getId()))
				.replace("%name%", dr.getName())
				.replace("%from%", dr.getFrom())
				.replace("%to%", dr.getTo())
				.replace("%owner%", dr.getLoanOwner())
				.replace("%st%", TimeHandler.getTime(dr.getStartTime()))
				.replace("%et%", TimeHandler.getTime(dr.getEndTime()))
				.replace("%rt%", TimeHandler.getRepeatingTime(dr.getRepeatingTime()))
				.replace("%apsf%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getAmountPaidSoFar())))
				.replace("%ta%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getTotalAmount())))
				.replace("%ar%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getAmountRatio())))
				.replace("%in%", String.valueOf(AdvancedEconomyPlus.getVaultApi().format(dr.getInterest())))
				.replace("%pa%", String.valueOf(dr.isPaused()))
				.replace("%fo%", String.valueOf(dr.isForgiven()))
				.replace("%fi%", String.valueOf(dr.isFinished()));
		player.spigot().sendMessage(
				ChatApi.hoverEvent(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Send.YourProposal")
						.replace("%player%", args[2]),
				HoverEvent.Action.SHOW_TEXT, hover));
		TextComponent tx = ChatApi.hoverEvent(
				plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Send.AProposal")
				.replace("%player%", player.getName()),
				HoverEvent.Action.SHOW_TEXT, hover);
		boolean bungee = EconomySettings.settings.isBungee();
		ArrayList<BaseComponent> list = new ArrayList<>();
		list.add(tx);
		String ar = ChatApi.tl(plugin.getYamlHandler().getL().getString("CmdMoney.Loan.Send.AcceptReject")
				.replace("%cmdA%", plugin.getYamlHandler().getL().getString("CmdMoney.Loan.AcceptCmd"))
				.replace("%cmdR%", plugin.getYamlHandler().getL().getString("CmdMoney.Loan.RejectCmd")));
		if(bungee)
		{
			BungeeBridge.sendBungeeTextComponent(player, uuid,
					BungeeBridge.generateMessage(list), false, "");
			BungeeBridge.sendBungeeMessage(player, uuid, ar, false, "");
		} else
		{
			if(Bukkit.getPlayer(UUID.fromString(uuid)) != null)
			{
				Bukkit.getPlayer(UUID.fromString(uuid)).spigot().sendMessage(tx);
				Bukkit.getPlayer(UUID.fromString(uuid)).sendMessage(ar);
			}
		}
		return;
	}
}
