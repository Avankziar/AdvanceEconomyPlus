package main.java.me.avankziar.aep.spigot.object;

public enum CommandExecuteType
{
	EDITLOG,
	DELETELOG,
	BALANCE, // /money
	PAY, 	// /money pay <Betrag> <Spieler> <Spielerkontoname> [Kategorie] [Msg...] | Default Main konto zu Konto A Spieler B
			// /money pay <Kontoname> <Betrag> <Spieler> <Spielerkontoname> [Kategorie] [Msg...]
			// /money pay <Betrag> <Spieler> <Spielerkontoname> [Kategorie] [Msg...]
	GIVE, // /money give
	GIVE_CONSOLE,
	SET,
	SET_CONSOLE,
	TAKE,
	TAKE_CONSOLE,
	WALLETNOTIFICATION, // /money toggle 
	BANKNOTIFICATION, // /money toggle
	
	ACTIONLOG,
	TRENDLOG,
	
	LOGGERSETTINGS,
	LOGGERSETTINGS_GUI,
	LOGGERSETTINGS_OTHER,
	LOGGERSETTINGS_TEXT,
	
	ACCOUNT_OPEN, // /money open <Spielername> <Accountname> <AccountType> <Category> 
	ACCOUNT_CLOSE, // /money close <Accountname> [Spielername]
	ACCOUNT_INFO, // /money info <Accountname> [spielername], sowas in der art
	ACCOUNT_SETNAME, // /money setname <Accountname> <New Accountname>
	ACCOUNT_SETOWNER,
	ACCOUNT_SETCATEGORY, // / money setcategory <Accountname> <Spielername> <Category>
	ACCOUNT_MANAGE, // /money manage <Spielername> <Accountname> <Spielername> <ManagementType>, sowas in der art.
	ACCOUNT_SETSHORTPAY, // /money setshortpay <Accountname>
	ACCOUNT_SETDEFAULT, // /money setdefault <Spielername> <Accountname>, sowas in der art.
	
	CURRENCY_INFO, // /money currencyinfo, sowas in der Art.
	CURRENCY_EXCHANGE,
	
	TOPRANKING, //toprank [Spielername]
	TOPLIST, // /toplist <Währung> [seitenzahl]
}
