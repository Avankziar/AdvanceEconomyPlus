package me.avankziar.aep.general.assistance;

import java.util.regex.Pattern;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;

public class ChatApi
{
	private static final Pattern po = Pattern.compile("(?<!\\\\)(&#[a-fA-F0-9]{6})");
	private static final Pattern pt = Pattern.compile("(?<!\\\\)(&[a-fA-F0-9k-oK-OrR]{1})");
	public static final TagResolver ALL = TagResolver.builder()
		      .resolvers(
		    		  StandardTags.hoverEvent(),
		    		  StandardTags.clickEvent(),
		    		  StandardTags.color(),
		    		  StandardTags.keybind(),
		    		  StandardTags.translatable(),
		    		  StandardTags.translatableFallback(),
		    		  StandardTags.insertion(),
		    		  StandardTags.font(),
		    		  StandardTags.decorations(),
		    		  StandardTags.gradient(),
		    		  StandardTags.reset(),
		    		  StandardTags.newline(),
		    		  StandardTags.transition(),
		    		  StandardTags.selector(),
		    		  StandardTags.score(),
		    		  StandardTags.nbt()
		      ).build();
	public static final TagResolver ALLOW_ONLY_COLOR = TagResolver.builder()
		      .resolvers(
		    		  StandardTags.hoverEvent(),
		    		  StandardTags.clickEvent(),
		    		  StandardTags.keybind(),
		    		  StandardTags.translatable(),
		    		  StandardTags.translatableFallback(),
		    		  StandardTags.insertion(),
		    		  StandardTags.font(),
		    		  StandardTags.decorations(),
		    		  StandardTags.gradient(),
		    		  StandardTags.reset(),
		    		  StandardTags.newline(),
		    		  StandardTags.transition(),
		    		  StandardTags.selector(),
		    		  StandardTags.score(),
		    		  StandardTags.nbt()
		      ).build();
	
	public static Component text(String s)
	{
		return Component.text(s);
	}
	
	public static Component tl(String s)
	{
		if(s == null)
		{
			return text("");
		} else if(po.matcher(s).find() || pt.matcher(s).find())
		{
			//Old Bukkit pattern
			return MiniMessage.miniMessage().deserialize(oldBukkitFormat(s), ALL);
		} else
		{
			//new kyori adventure pattern
			return MiniMessage.miniMessage().deserialize(s, ALL);
		}
	}
	
	/**
	 * Only for Spigot, NOT Paper
	 * @param s
	 * @return
	 */
	public static net.md_5.bungee.api.chat.BaseComponent[] tctl(String s)
	{
		return BungeeComponentSerializer.get().serialize(tl(s));
	}
	
	public static String oldBukkitFormatShort(String s)
	{
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if(c == '&' && i+1 < s.length())
			{
				char cc = s.charAt(i+1);
				if(cc == '#' && i+7 < s.length())
				{
					String rc = s.substring(i, i+7);
					b.append(getBukkitHexColorConvertKyoriAdventure(rc));
					i += 7;
				} else
				{
					b.append(getBukkitColorConvertKyoriAdventure(cc));
					i++;
				}
			} else
			{
				b.append(c);
			}
		}
		return b.toString();
	}
	
	public static String oldBukkitFormat(String s)
	{
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < s.length(); i++)
		{
			char c = s.charAt(i);
			if(c == '&' && i+1 < s.length())
			{
				char cc = s.charAt(i+1);
				if(cc == '#' && i+7 < s.length())
				{
					String rc = s.substring(i, i+7);
					b.append(getBukkitHexColorConvertKyoriAdventure(rc));
					i += 7;
				} else
				{
					b.append(getBukkitColorConvertKyoriAdventure(cc));
					i++;
				}
			} else if(c == '~' && i+2 < s.length())
			{
				char ca = s.charAt(i+1);
				char cb = s.charAt(i+2);
				if(ca == '!' && cb == '~')
				{
					b.append("<newline>");
				} else
				{
					b.append(c);
				}
				i += 2;
			} else
			{
				b.append(c);
			}
		}
		return b.toString();
	}
	
	public static String oldBukkitFormatOnly(String s)
	{
		return oldBukkitFormat(s.replace("~!~", ""));
	}
	
	private static String getBukkitColorConvertKyoriAdventure(char c)
	{
		String r = "";
		switch(c)
		{
		default:
			break;
		case '0':
			r = "<black>";
			break;
		case '1':
			r = "<dark_blue>";
			break;
		case '2':
			r = "<dark_green>";
			break;
		case '3':
			r = "<dark_aqua>";
			break;
		case '4':
			r = "<dark_red>";
			break;
		case '5':
			r = "<dark_purple>";
			break;
		case '6':
			r = "<gold>";
			break;
		case '7':
			r = "<gray>";
			break;
		case '8':
			r = "<dark_gray>";
			break;
		case '9':
			r = "<blue>";
			break;
		case 'a':
			r = "<green>";
			break;
		case 'b':
			r = "<aqua>";
			break;
		case 'c':
			r = "<red>";
			break;
		case 'd':
			r = "<light_purple>";
			break;
		case 'e':
			r = "<yellow>";
			break;
		case 'f':
			r = "<white>";
			break;
		case 'k':
			r = "<obf>";
			break;
		case 'l':
			r = "<b>";
			break;
		case 'm':
			r = "<st>";
			break;
		case 'n':
			r = "<u>";
			break;
		case 'o':
			r = "<i>";
			break;
		case 'r':
			r = "<reset>";
			break;
		}
		return r;
	}
	
	private static String getBukkitHexColorConvertKyoriAdventure(String hexnumber)
	{
		if(hexnumber.contains("&#"))
		{
			return "<"+hexnumber.replace("&", "")+">";
		} else if(hexnumber.contains("#"))
		{
			return "<"+hexnumber+">";
		}
		return "<#"+hexnumber+">";
	}
	
	public static String hover(String s, String hoverType, String hover)
	{
		switch(hoverType)
		{
		default:
		case "SHOW_TEXT":
			return "<hover:show_text:'"+oldBukkitFormat(hover)+"'>"+oldBukkitFormat(s)+"</hover>";
		case "SHOW_ITEM":
			return "<hover:show_item:'"+oldBukkitFormat(hover)+"'>"+oldBukkitFormat(s)+"</hover>";
		}
	}
	
	public static String hover(String s, String itemjson)
	{
		return "<hover:show_item:"+oldBukkitFormat(itemjson)+">"+oldBukkitFormat(s)+"</hover>";
	}
	
	public static String click(String s, String clickType, String click)
	{
		switch(clickType)
		{
		default:
		case "SUGGEST_COMMAND":
			return "<click:suggest_command:'"+click+"'>"+oldBukkitFormat(s)+"</click>";
		case "RUN_COMMAND":
			return "<click:run_command:'"+click+"'>"+oldBukkitFormat(s)+"</click>";
		case "OPEN_URL":
			return "<click:open_url:'"+click+"'>"+oldBukkitFormat(s)+"</click>";
		case "COPY_TO_CLIPBOARD":
			return "<click:copy_to_clipboard:'"+click+"'>"+oldBukkitFormat(s)+"</click>";
		}
	}
	
	public static String clickHover(String s, String clickType, String click, String hoverType, String hover)
	{
		StringBuilder sb = new StringBuilder();
		switch(hoverType)
		{
		default:
		case "SHOW_TEXT":
			sb.append("<hover:show_text:'"+oldBukkitFormat(hover)+"'>"); break;
		case "SHOW_ITEM":
			sb.append("<hover:show_item:"+oldBukkitFormat(hover)+">"); break;
		}
		switch(clickType)
		{
		default:
		case "SUGGEST_COMMAND":
			sb.append("<click:suggest_command:'"+click+"'>"+oldBukkitFormat(s)); break;
		case "RUN_COMMAND":
			sb.append("<click:run_command:'"+click+"'>"+oldBukkitFormat(s)); break;
		case "OPEN_URL":
			sb.append("<click:open_url:'"+click+"'>"+oldBukkitFormat(s)); break;
		case "COPY_TO_CLIPBOARD":
			sb.append("<click:copy_to_clipboard:'"+click+"'>"+oldBukkitFormat(s)); break;
		}
		sb.append("</click></hover>");
		return sb.toString();
	}
}