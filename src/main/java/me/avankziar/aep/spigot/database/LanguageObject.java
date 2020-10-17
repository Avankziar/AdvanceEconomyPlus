package main.java.me.avankziar.aep.spigot.database;

import java.util.LinkedHashMap;

public class LanguageObject
{
	public enum LanguageType
	{
		ARABIC, DUTCH, ENGLISH, FRENCH, GERMAN, HINDI, ITALIAN, JAPANESE, MANDARIN, RUSSIAN, SPANISH;
	}
	
	public LinkedHashMap<LanguageType, Object[]> languageValues = new LinkedHashMap<>();
	//Mit der GERMAN, wird dann "&cDer Spieler ist nicht online" herausgegeben.	
	
	public LanguageObject(LanguageType[] languages, Object[] values)
	{
		if(languages.length == values.length)
		{
			for(int i = 0; i < languages.length; i++)
			{
				if(languages[i] != null && values[i] != null)
				{
					languageValues.put(languages[i], new Object[] {values[i]});
				}				
			}
		} else if(values.length % languages.length == 0) //List
		{
			int multiply = values.length / languages.length;
			for(int i = 0; i < languages.length; i++)
			{
				Object[] valuesArray = new String[multiply];
				for(int j = 0; j < multiply; j++)
				{
					valuesArray[j] = values[i+j];
				}
				languageValues.put(languages[i], valuesArray);
			}
		}
	}

}
