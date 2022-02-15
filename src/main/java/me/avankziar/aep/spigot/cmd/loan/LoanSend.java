package main.java.me.avankziar.aep.spigot.cmd.loan;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import main.java.me.avankziar.aep.spigot.assistance.BungeeBridge;
import main.java.me.avankziar.aep.spigot.assistance.Utility;
import main.java.me.avankziar.aep.spigot.cmd.sub.CommandSuggest;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.cmd.tree.BaseConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.CommandExecuteType;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import main.java.me.avankziar.ifh.spigot.economy.account.EconomyEntity.EconomyType;
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
					plugin.getYamlHandler().getLang().getString("CmdLoan.NoWaitingLoanProposal")));
			return;
		}
		String otherplayer = args[1];
		UUID uuid = Utility.convertNameToUUID(otherplayer, EconomyType.PLAYER);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(PendingHandler.loanToAccept.containsKey(uuid))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdLoan.AlreadyHavingProposal")));
			return;
		}
		LoanRepayment dr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
		String hover = plugin.getYamlHandler().getLang().getString("CmdLoan.HoverInfo")
				.replace("%id%", String.valueOf(dr.getId()))
				.replace("%name%", dr.getName())
				.replace("%from%", dr.getFrom())
				.replace("%to%", dr.getTo())
				.replace("%owner%", dr.getLoanOwner())
				.replace("%st%", TimeHandler.getTime(dr.getStartTime()))
				.replace("%et%", TimeHandler.getTime(dr.getEndTime()))
				.replace("%rt%", TimeHandler.getRepeatingTime(dr.getRepeatingTime()))
				.replace("%apsf%", String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getAmountPaidSoFar())))
				.replace("%ta%", String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getTotalAmount())))
				.replace("%ar%", String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getAmountRatio())))
				.replace("%in%", String.valueOf(AdvancedEconomyPlus.getVault().format(dr.getInterest())))
				.replace("%pa%", String.valueOf(dr.isPaused()))
				.replace("%fo%", String.valueOf(dr.isForgiven()))
				.replace("%fi%", String.valueOf(dr.isFinished()));
		player.spigot().sendMessage(
				ChatApi.hoverEvent(plugin.getYamlHandler().getLang().getString("CmdLoan.Send.YourProposal")
						.replace("%player%", args[2]),
				HoverEvent.Action.SHOW_TEXT, hover));
		TextComponent tx = ChatApi.hoverEvent(
				plugin.getYamlHandler().getLang().getString("CmdLoan.Send.AProposal")
				.replace("%player%", player.getName()),
				HoverEvent.Action.SHOW_TEXT, hover);
		ArrayList<BaseComponent> list = new ArrayList<>();
		list.add(tx);
		TextComponent ar = ChatApi.generateTextComponent(plugin.getYamlHandler().getLang().getString("CmdLoan.Send.AcceptReject")
				.replace("%acceptcmd%", CommandSuggest.get(null, CommandExecuteType.LOAN_ACCEPT).replace(" ", "+")+"+"+plugin.getYamlHandler().getLang().getString("CmdLoan.ConfirmTerm"))
				.replace("%rejectcmd%", CommandSuggest.get(null, CommandExecuteType.LOAN_REJECT).replace(" ", "+")));
		ArrayList<BaseComponent> list2 = new ArrayList<>();
		list2.add(ar);
		if(Bukkit.getPlayer(uuid) == null)
		{
			BungeeBridge.sendBungeeTextComponent(player, uuid.toString(),
					BungeeBridge.generateMessage(list), false, "");
			BungeeBridge.sendBungeeTextComponent(player, uuid.toString(),
					BungeeBridge.generateMessage(list2), false, "");
		} else
		{
			if(Bukkit.getPlayer(UUID.fromString(uuid.toString())) != null)
			{
				Bukkit.getPlayer(UUID.fromString(uuid.toString())).spigot().sendMessage(tx);
				Bukkit.getPlayer(UUID.fromString(uuid.toString())).spigot().sendMessage(ar);
			}
		}
		return;
	}
}
