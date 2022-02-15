package main.java.me.avankziar.aep.spigot.assistance;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Text;

public class BungeeBridge
{
	private static AdvancedEconomyPlus plugin;
	
	public BungeeBridge(AdvancedEconomyPlus plugin)
	{
		BungeeBridge.plugin = plugin;
	}
	
	/**
	 * Generates a string from a string array with the character µ as a separator.
	 * @param  messagesParts
	 *         The string array which is assembled to a string.
	 * @return String
	 * @author Christoph Steins/Avankziar
	 */
	
	public static String generateMessage(String... messagesParts)
	{
		return String.join("µ", messagesParts);
	}
	
	/**
	 * 
	 * Generates a string array from a string with the character µ as a separator.
	 * @param  message
	 *         The string which is converted to a string array.
	 * @return String
	 * @author Christoph Steins/Avankziar
	 */
	
	public static String[] generateArray(String message)
	{
		return message.split("µ");
	}
	
	public static String generateMessage(List<BaseComponent> list)
	{
		String idclick = "click";
		String idhover = "hover";
		String sepb = "~";
		String sepw = "@";
		String sepspace = "+";
		String message = "";
		int bci = 0;
		for(BaseComponent bc : list)
		{
			String part = bc.toLegacyText().replace(" ", sepspace);
			HoverEvent he = bc.getHoverEvent();
			ClickEvent ce = bc.getClickEvent();
			message += part;
			if(he != null)
			{
				message += sepb+idhover+sepw+he.getAction().toString()+sepw;
				int hei = 0;
				for(Content hebc : he.getContents())
				{
					Text t = (Text) hebc;
					if(t.getValue() != null)
					{
						if(t.getValue() instanceof BaseComponent[])
						{
							BaseComponent[] bch = (BaseComponent[]) t.getValue();
							if(bch.length-1 == hei)
							{
								for(BaseComponent bchbc : bch)
								{
									message += bchbc.toLegacyText().replace(" ", sepspace);
								}
							} else
							{
								for(BaseComponent bchbc : bch)
								{
									message += bchbc.toLegacyText().replace(" ", sepspace)+"~!~";
								}
							}
							
						} else if(t.getValue() instanceof String)
						{
							String sh = (String) t.getValue();
							message += sh.replace(" ", sepspace);
						}
					}
					hei++;
				}
			}
			if(ce != null)
			{
				message += sepb+idclick+sepw+ce.getAction().toString()+sepw+ce.getValue();
			}
			if(list.size()-1 > bci)
			{
				message += " ";
			}
		}
		return message;
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
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2]);
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
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2]);
					} else if(function[2].contains(idhover))
					{
						String[] at = function[2].split(sepw);
						ChatApi.hoverEvent(newtc,HoverEvent.Action.valueOf(at[1]), at[2]);
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
	
	public static void sendBungeeMessage(Player p, String uuid, String message, boolean returnplayeronline, String returnmessage)
	{
		String µ = "µ";
		String Category = "message";
		String completMessage = Category+µ+uuid+µ+message+µ+returnplayeronline+µ+returnmessage;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(completMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        p.sendPluginMessage(plugin, "advanceeconomy:spigottobungee", stream.toByteArray());
    }
	
	public static void sendBungeeTextComponent(Player p, String uuid, String message, boolean returnplayeronline, String returnmessage)
	{
		String µ = "µ";
		String Category = "messagetextcomponent";
		String completMessage = Category+µ+uuid+µ+message+µ+returnplayeronline+µ+returnmessage;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(completMessage);
		} catch (IOException e) {
			e.printStackTrace();
		}
        p.sendPluginMessage(plugin, "advanceeconomy:spigottobungee", stream.toByteArray());
    }
}
