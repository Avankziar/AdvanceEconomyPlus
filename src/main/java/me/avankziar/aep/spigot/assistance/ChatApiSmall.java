package main.java.me.avankziar.aep.spigot.assistance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;

import org.bukkit.configuration.file.YamlConfiguration;

import main.java.me.avankziar.aep.general.ChatApi;
import main.java.me.avankziar.aep.spigot.AdvancedEconomyPlus;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatApiSmall
{

	public static TextComponent generateTextComponent(String message)
	{
		String[] array = message.split(" ");
		YamlConfiguration cfg = AdvancedEconomyPlus.getPlugin().getYamlHandler().getConfig();
		String idclick = cfg.getString("Identifier.Click");
		String idhover = cfg.getString("Identifier.Hover");
		String sepb = cfg.getString("Seperator.BetweenFunction");
		String sepw = cfg.getString("Seperator.WhithinFuction");
		String sepspace = cfg.getString("Seperator.Space");
		ArrayList<BaseComponent> list = new ArrayList<BaseComponent>();
		TextComponent textcomponent = ChatApi.tc("");
		String lastColor = null;
		for(int i = 0; i < array.length; i++)
		{
			String word = array[i];
			lastColor = getLastColor(lastColor, word);
			//Word enthält Funktion
			if(word.contains(idclick) || word.contains(idhover))
			{
				if(word.contains(sepb))
				{
					String[] functionarray = word.split(sepb);
					String originmessage = null;
					if(i+1 == array.length)
					{
						//Letzter Value
						originmessage = functionarray[0].replace(sepspace, " ");
					} else
					{
						originmessage = functionarray[0].replace(sepspace, " ")+" ";
					}
					//Eine Funktion muss mehr als einen wert haben
					if(functionarray.length<2)
					{
						continue;
					}
					TextComponent tc = ChatApi.tctl(lastColor+originmessage);
					for(String f : functionarray)
					{
						if(f.contains(idclick))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String clickaction = function[1];
							String clickstring = function[2].replace(sepspace, " ");
							ChatApi.clickEvent(tc, ClickEvent.Action.valueOf(clickaction), clickstring);
						} else if(f.contains(idhover))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String hoveraction = function[1];
							String hoverstringpath = function[2];
							String hoverstring = ChatApi.tl(
									AdvancedEconomyPlus.getPlugin().getYamlHandler().getLang().getString(hoverstringpath));
							ChatApi.hoverEvent(tc, HoverEvent.Action.valueOf(hoveraction),
									hoverstring);
						}
					}
					list.add(tc);
				}
			} else
			{
				//Beinhalten keine Funktion
				if(i+1 == array.length)
				{
					list.add(ChatApi.tctl(lastColor+word));
				} else
				{
					list.add(ChatApi.tctl(lastColor+word+" "));
				}
			}
		}
		textcomponent.setExtra(list);
		return textcomponent;
	}
	
	public static TextComponent generateTextComponent(String message, HashMap<String,String> hoverReplacer)
	{
		String[] array = message.split(" ");
		YamlConfiguration cfg = AdvancedEconomyPlus.getPlugin().getYamlHandler().getConfig();
		String idclick = cfg.getString("Identifier.Click");
		String idhover = cfg.getString("Identifier.Hover");
		String sepb = cfg.getString("Seperator.BetweenFunction");
		String sepw = cfg.getString("Seperator.WhithinFuction");
		String sepspace = cfg.getString("Seperator.Space");
		ArrayList<BaseComponent> list = new ArrayList<BaseComponent>();
		TextComponent textcomponent = ChatApi.tc("");
		String lastColor = null;
		for(int i = 0; i < array.length; i++)
		{
			String word = array[i];
			lastColor = getLastColor(lastColor, word);
			//Word enthält Funktion
			if(word.contains(idclick) || word.contains(idhover))
			{
				if(word.contains(sepb))
				{
					String[] functionarray = word.split(sepb);
					String originmessage = null;
					if(i+1 == array.length)
					{
						//Letzter Value
						originmessage = functionarray[0].replace(sepspace, " ");
					} else
					{
						originmessage = functionarray[0].replace(sepspace, " ")+" ";
					}
					//Eine Funktion muss mehr als einen wert haben
					if(functionarray.length<2)
					{
						continue;
					}
					TextComponent tc = ChatApi.tctl(lastColor+originmessage);
					for(String f : functionarray)
					{
						if(f.contains(idclick))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String clickaction = function[1];
							String clickstring = function[2].replace(sepspace, " ");
							ChatApi.clickEvent(tc, ClickEvent.Action.valueOf(clickaction), clickstring);
						} else if(f.contains(idhover))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String hoveraction = function[1];
							String hoverstringpath = function[2];
							String hoverstring = replaceHoverReplacer(ChatApi.tl(
									AdvancedEconomyPlus.getPlugin().getYamlHandler().getLang().getString(hoverstringpath)), hoverReplacer);
							ChatApi.hoverEvent(tc, HoverEvent.Action.valueOf(hoveraction),
									hoverstring);
						}
					}
					list.add(tc);
				}
			} else
			{
				//Beinhalten keine Funktion
				if(i+1 == array.length)
				{
					list.add(ChatApi.tctl(lastColor+word));
				} else
				{
					list.add(ChatApi.tctl(lastColor+word+" "));
				}
			}
		}
		textcomponent.setExtra(list);
		return textcomponent;
	}
	
	public static ArrayList<BaseComponent> generateTextComponentII(String message, HashMap<String,String> hoverReplacer)
	{
		String[] array = message.split(" ");
		YamlConfiguration cfg = AdvancedEconomyPlus.getPlugin().getYamlHandler().getConfig();
		String idclick = cfg.getString("Identifier.Click");
		String idhover = cfg.getString("Identifier.Hover");
		String sepb = cfg.getString("Seperator.BetweenFunction");
		String sepw = cfg.getString("Seperator.WhithinFuction");
		String sepspace = cfg.getString("Seperator.Space");
		ArrayList<BaseComponent> list = new ArrayList<BaseComponent>();
		String lastColor = null;
		for(int i = 0; i < array.length; i++)
		{
			String word = array[i];
			lastColor = getLastColor(lastColor, word);
			//Word enthält Funktion
			if(word.contains(idclick) || word.contains(idhover))
			{
				if(word.contains(sepb))
				{
					String[] functionarray = word.split(sepb);
					String originmessage = null;
					if(i+1 == array.length)
					{
						//Letzter Value
						originmessage = functionarray[0].replace(sepspace, " ");
					} else
					{
						originmessage = functionarray[0].replace(sepspace, " ")+" ";
					}
					//Eine Funktion muss mehr als einen wert haben
					if(functionarray.length<2)
					{
						continue;
					}
					TextComponent tc = ChatApi.tctl(lastColor+originmessage);
					for(String f : functionarray)
					{
						if(f.contains(idclick))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String clickaction = function[1];
							String clickstring = function[2].replace(sepspace, " ");
							ChatApi.clickEvent(tc, ClickEvent.Action.valueOf(clickaction), clickstring);
						} else if(f.contains(idhover))
						{
							String[] function = f.split(sepw);
							if(function.length!=3)
							{
								continue;
							}
							String hoveraction = function[1];
							String hoverstringpath = function[2];
							String hoverstring = replaceHoverReplacer(ChatApi.tl(
									AdvancedEconomyPlus.getPlugin().getYamlHandler().getLang().getString(hoverstringpath)), hoverReplacer);
							ChatApi.hoverEvent(tc, HoverEvent.Action.valueOf(hoveraction),
									hoverstring);
						}
					}
					list.add(tc);
				}
			} else
			{
				//Beinhalten keine Funktion
				if(i+1 == array.length)
				{
					list.add(ChatApi.tctl(lastColor+word));
				} else
				{
					list.add(ChatApi.tctl(lastColor+word+" "));
				}
			}
		}
		return list;
	}
	
	private static String getLastColor(String lastColor, String s)
	{
		String color = lastColor;
		for(int i = s.length()-1; i >= 0; i--)
		{
			char c = s.charAt(i);
			   if(c == '&' || c == '§')
			   {
				   if(i+1<s.length())
				   {
					   char d = s.charAt(i+1);
					   if(d == '0' || d == '1' || d == '2' || d == '3' || d == '4' || d == '5' || d == '6'
							   || d == '7' || d == '8' || d == '9' || d == 'a' || d == 'A' || d == 'b' || d == 'B'
							   || d == 'c' || d == 'C' || d == 'd' || d == 'D' || d == 'e' || d == 'E'
							   || d == 'f' || d == 'F' || d == 'k' || d == 'K' || d == 'm' || d == 'M'
							   || d == 'n' || d == 'N' || d == 'l' || d == 'L' || d == 'o' || d == 'O'
							   || d == 'r' || d == 'R')
					   {
						   color = "§"+Character.toString(d);
					   }
				   }
			   }
		}
		return color;
	}
	
	private static String replaceHoverReplacer(String hover, HashMap<String, String> map)
	{
		String message = map.entrySet().stream().map(
				entryToReplace -> (Function<String, String>) 
				s -> s.replace(entryToReplace.getKey(), entryToReplace.getValue()))
		.reduce(Function.identity(), Function::andThen)
        .apply(hover);
		return message;
	}

}
