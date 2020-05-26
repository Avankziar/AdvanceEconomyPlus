package main.java.me.avankziar.advanceeconomy.bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import main.java.me.avankziar.advanceeconomy.bungee.assistance.ChatApi;
import main.java.me.avankziar.advanceeconomy.bungee.AdvanceEconomy;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class SpigotListener implements Listener
{
	private AdvanceEconomy plugin;
	
	public SpigotListener(AdvanceEconomy plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	 public void onPluginMessage(PluginMessageEvent ev) 
	 {
		 if (ev.getTag().equals("advanceeconomy:spigottobungee")) 
		 {
			 ByteArrayInputStream streamin = new ByteArrayInputStream(ev.getData());
		     DataInputStream in = new DataInputStream(streamin);
		     String µ = "µ";
		     try 
		     {
		        String[] s = in.readUTF().split(µ);
		        String category = s[0];
		        String playerUUID = s[1];
				if(plugin.getProxy().getPlayer(UUID.fromString(playerUUID)) == null)
				{
					return;
				}
				if(category.equals("message"))
				{
					TextComponent message = ChatApi.tctl(s[2]);
					boolean returnplayeronline = Boolean.valueOf(s[3]);
					ProxiedPlayer player = plugin.getProxy().getPlayer(UUID.fromString(playerUUID));
					if(player != null)
					{
						player.sendMessage(message);
						return;
					} else if(returnplayeronline)
					{
						((ProxiedPlayer) ev.getSender()).sendMessage(ChatApi.tctl(s[4]));
					}
					µ = null;
					category = null;
					playerUUID = null;
					player = null;
					message = null;
					return;
				} else if(category.equals("messagetextcomponent"))
				{
					TextComponent message = generateTextComponent(s[2]);
					boolean returnplayeronline = Boolean.valueOf(s[3]);
					ProxiedPlayer player = plugin.getProxy().getPlayer(UUID.fromString(playerUUID));
					if(player != null)
					{
						player.sendMessage(message);
						return;
					} else if(returnplayeronline)
					{
						((ProxiedPlayer) ev.getSender()).sendMessage(ChatApi.tctl(s[4]));
					}
					µ = null;
					category = null;
					playerUUID = null;
					player = null;
					message = null;
					return;
				}
		     } catch (IOException e) 
			    {
					e.printStackTrace();
				}
			    return;
		 }
	 }
	
	public static TextComponent generateTextComponent(String message)
	{
		String idclick = "click";
		String idhover = "hover";
		String sepb = "~";
		String sepw = "@";
		String sepspace = "+";
		TextComponent tc = ChatApi.tc("");
		List<BaseComponent> list = new ArrayList<>();
		String[] space = message.split(" ");
		for(String word : space)
		{
			TextComponent newtc = null;
			if(word.contains(sepb))
			{
				String[] function = word.split(sepb);
				newtc = ChatApi.tctl(function[0].replace(sepspace, " ")+" ");
				if(function.length >= 2)
				{
					if(function[1].contains(idhover))
					{
						String[] at = function[1].split(sepw);
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					}
					if(function[1].contains(idclick))
					{
						String[] at = function[1].split(sepw);
						ChatApi.clickEvent(newtc,ClickEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					}
				}
				if(function.length == 3)
				{
					if(function[1].contains(idhover))
					{
						String[] at = function[1].split(sepw);
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					} else if(function[2].contains(idhover))
					{
						String[] at = function[2].split(sepw);
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					}
					if(function[1].contains(idclick))
					{
						String[] at = function[1].split(sepw);
						ChatApi.clickEvent(newtc,ClickEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					} else if(function[2].contains(idclick))
					{
						String[] at = function[2].split(sepw);
						ChatApi.clickEvent(newtc,ClickEvent.Action.valueOf(at[1]), at[2].replace(sepspace, " "));
					}
				}
			} else
			{
				newtc = ChatApi.tctl(word+" ");
			}
			list.add(newtc);
		}
		tc.setExtra(list);
		return tc;
	}
}