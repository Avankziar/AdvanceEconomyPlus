package main.java.me.avankziar.aep.spigot.cmd.loan;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.general.objects.LoanRepayment;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.ifh.general.economy.account.EconomyEntity.EconomyType;
import main.java.me.avankziar.ifh.spigot.economy.account.Account;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class LoanSend extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public LoanSend(ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = BaseConstructor.getPlugin();
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.NoWaitingLoanProposal")));
			return;
		}
		String otherplayer = args[1];
		UUID uuid = Utility.convertNameToUUID(otherplayer, EconomyType.PLAYER);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(PendingHandler.loanToAccept.containsKey(uuid.toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("Cmd.Loan.AlreadyHavingProposal")));
			return;
		}
		LoanRepayment lr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
		Account from = plugin.getIFHApi().getAccount(lr.getAccountFromID());
		Account to = plugin.getIFHApi().getAccount(lr.getAccountToID());
		String low = Utility.convertUUIDToName(lr.getOwner().toString(), EconomyType.PLAYER);
		String ldb = Utility.convertUUIDToName(lr.getDebtor().toString(), EconomyType.PLAYER);
		if(low == null)
		{
			low = "/";
		}
		if(ldb == null)
		{
			ldb = "/";
		}
		String hover = plugin.getYamlHandler().getLang().getString("Cmd.Loan.HoverInfo")
				.replace("%id%", String.valueOf(lr.getId()))
				.replace("%name%", lr.getName())
				.replace("%fromaccount%", from != null ? from.getOwner().getName() : "N.A.")
				.replace("%fromowner%", from != null ? from.getAccountName() : "N.A.")
				.replace("%toaccount%", to != null ? to.getAccountName() : "N.A.")
				.replace("%toowner%", to != null ? to.getOwner().getName() : "N.A.")
				.replace("%owner%", low)
				.replace("%owner%", ldb)
				.replace("%st%", TimeHandler.getTime(lr.getStartTime()))
				.replace("%et%", TimeHandler.getTime(lr.getEndTime()))
				.replace("%rt%", TimeHandler.getRepeatingTime(lr.getRepeatingTime()))
				.replace("%apsf%", plugin.getIFHApi().format(lr.getAmountPaidSoFar(), from.getCurrency()))
				.replace("%ta%", plugin.getIFHApi().format(lr.getTotalAmount(), from.getCurrency()))
				.replace("%ar%", plugin.getIFHApi().format(lr.getAmountRatio(), from.getCurrency()))
				.replace("%in%", String.valueOf(lr.getInterest()))
				.replace("%pa%", String.valueOf(lr.isPaused()))
				.replace("%fo%", String.valueOf(lr.isForgiven()))
				.replace("%fi%", String.valueOf(lr.isFinished()));
		player.spigot().sendMessage(
				ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Send.YourProposal")
						.replace("%player%", args[2]),
				HoverEvent.Action.SHOW_TEXT, hover));
		TextComponent tx = ChatApi.hoverEvent(
				plugin.getYamlHandler().getLang().getString("Cmd.Loan.Send.AProposal")
				.replace("%player%", player.getName()),
				HoverEvent.Action.SHOW_TEXT, hover);
		ArrayList<BaseComponent> list = new ArrayList<>();
		list.add(tx);
		TextComponent ar = ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("Cmd.Loan.Send.AcceptReject")
				.replace("%acceptcmd%", CommandSuggest.get(null, CommandExecuteType.LOAN_ACCEPT).replace(" ", "+")+"+"+plugin.getYamlHandler().getLang().getString("Cmd.Loan.ConfirmTerm"))
				.replace("%rejectcmd%", CommandSuggest.get(null, CommandExecuteType.LOAN_REJECT).replace(" ", "+")));
		ArrayList<BaseComponent> list2 = new ArrayList<>();
		list2.add(ar);
		if(Bukkit.getPlayer(uuid) == null)
		{
			//BungeeBridge.sendBungeeTextComponent(player, uuid.toString(), BungeeBridge.generateMessage(list), false, "");
			//BungeeBridge.sendBungeeTextComponent(player, uuid.toString(), BungeeBridge.generateMessage(list2), false, "");
		} else
		{
			if(Bukkit.getPlayer(uuid) != null)
			{
				Bukkit.getPlayer(uuid).spigot().sendMessage(tx);
				Bukkit.getPlayer(uuid).spigot().sendMessage(ar);
			}
		}
		return;
	}
}
