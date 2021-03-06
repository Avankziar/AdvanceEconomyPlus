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
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentConstructor;
import main.java.me.avankziar.aep.spigot.cmd.tree.ArgumentModule;
import main.java.me.avankziar.aep.spigot.handler.KeyHandler;
import main.java.me.avankziar.aep.spigot.handler.PendingHandler;
import main.java.me.avankziar.aep.spigot.handler.TimeHandler;
import main.java.me.avankziar.aep.spigot.object.AEPSettings;
import main.java.me.avankziar.aep.spigot.object.LoanRepayment;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ARGLoan_Send extends ArgumentModule
{
	private AdvancedEconomyPlus plugin;
	
	public ARGLoan_Send(AdvancedEconomyPlus plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}
	
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		if(!AEPSettings.settings.isLoanRepayment())
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("NoLoan")));
			return;
		}
		if(!PendingHandler.loanRepayment.containsKey(player.getUniqueId().toString()))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdLoan.NoWaitingLoanProposal")));
			return;
		}
		String otherplayer = args[1];
		String uuid = Utility.convertNameToUUID(otherplayer);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		if(PendingHandler.loanToAccept.containsKey(uuid))
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getL().getString("CmdLoan.AlreadyHavingProposal")));
			return;
		}
		LoanRepayment dr = PendingHandler.loanRepayment.get(player.getUniqueId().toString());
		String hover = plugin.getYamlHandler().getL().getString("CmdLoan.HoverInfo")
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
				ChatApi.hoverEvent(plugin.getYamlHandler().getL().getString("CmdLoan.Send.YourProposal")
						.replace("%player%", args[2]),
				HoverEvent.Action.SHOW_TEXT, hover));
		TextComponent tx = ChatApi.hoverEvent(
				plugin.getYamlHandler().getL().getString("CmdLoan.Send.AProposal")
				.replace("%player%", player.getName()),
				HoverEvent.Action.SHOW_TEXT, hover);
		boolean bungee = AEPSettings.settings.isBungee();
		ArrayList<BaseComponent> list = new ArrayList<>();
		list.add(tx);
		TextComponent ar = ChatApi.generateTextComponent(plugin.getYamlHandler().getL().getString("CmdLoan.Send.AcceptReject")
				.replace("%acceptcmd%", AEPSettings.settings.getCommands(KeyHandler.L_ACCEPT).replace(" ", "+")+"+"+plugin.getYamlHandler().getL().getString("CmdLoan.ConfirmTerm"))
				.replace("%rejectcmd%", AEPSettings.settings.getCommands(KeyHandler.L_REJECT).replace(" ", "+")));
		ArrayList<BaseComponent> list2 = new ArrayList<>();
		list2.add(ar);
		if(bungee)
		{
			BungeeBridge.sendBungeeTextComponent(player, uuid,
					BungeeBridge.generateMessage(list), false, "");
			BungeeBridge.sendBungeeTextComponent(player, uuid,
					BungeeBridge.generateMessage(list2), false, "");
		} else
		{
			if(Bukkit.getPlayer(UUID.fromString(uuid)) != null)
			{
				Bukkit.getPlayer(UUID.fromString(uuid)).spigot().sendMessage(tx);
				Bukkit.getPlayer(UUID.fromString(uuid)).spigot().sendMessage(ar);
			}
		}
		return;
	}
}
