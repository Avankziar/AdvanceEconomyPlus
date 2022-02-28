package main.java.me.avankziar.aep.spigot.cmd.tree;

public enum CommandExecuteType
{
	AEP, //Shows all commandshelp
	PLAYER, //Shows all Accounts of the player, where he has access to
	RECOMMENT,
	DELETELOG,
	DELETEALLPLAYERACCOUNTS,
	BALANCE, // /money
	
	//Only Currency bounded
	PAY, 	// /money pay <Betrag> <Spieler> <Spielerkontoname> [Kategorie] [Msg...] | Default Main konto zu Konto A Spieler B
			// /money pay <Kontoname> <Betrag> <Spieler> <Spielerkontoname> [Kategorie] [Msg...]
			// /money pay <Betrag> <Spieler> <Spielerkontoname> [Kategorie] [Msg...]
	PAY_THROUGH_GUI,
	GIVE, // /money give
	GIVE_CONSOLE,
	SET,
	SET_CONSOLE,
	TAKE,
	TAKE_CONSOLE,
	//bound end
	
	WALLETNOTIFICATION, // /money toggle 
	BANKNOTIFICATION, // /money toggle
	
	ACTIONLOG,
	TRENDLOG,
	
	LOGGERSETTINGS,
	LOGGERSETTINGS_GUI,
	LOGGERSETTINGS_OTHER,
	LOGGERSETTINGS_TEXT,
	
	ACCOUNT,
	ACCOUNT_OPEN, // /money open <Spielername> <Accountname> <AccountType> <Category> 
	ACCOUNT_CLOSE, // /money close <Accountname> [Spielername] [confirm (Wenn es ein predefine acc ist mit bypass)]
	ACCOUNT_INFO, // /money info <Accountname> [spielername], sowas in der art
	ACCOUNT_OVERDUE, // zeigt alle Spieler an, welche nicht mehr seit x Tage online waren, mit alle ihren Konten.
	ACCOUNT_SETNAME, // /money setname <Accountname> <New Accountname>
	ACCOUNT_SETOWNER,
	//ACCOUNT_SETCATEGORY, // / money setcategory <Accountname> <Spielername> <Category> INFO keine gute idee, viel zu viel änderungen kommen dabei
	ACCOUNT_MANAGE, // /money manage <Spielername> <Accountname> <Spielername> <ManagementType>, sowas in der art.
	ACCOUNT_SETQUICKPAY, // /money setshortpay <Accountname>
	ACCOUNT_SETDEFAULT, // /money setdefault <Accountname> [Spielername], sowas in der art.
	
	CURRENCY_INFO, // /money currencyinfo, sowas in der Art.
	CURRENCY_EXCHANGE,
	
	GETTOTAL,
	//TOPRANKING, //toprank [Spielername] INFO schwer umsetztbar, besser das einfach im AccountInfo anzeigenlassen mit perm
	TOPLIST, // /toplist <Währung> [seitenzahl]
	
	LOAN,
	LOAN_ACCEPT,
	LOAN_AMOUNT,
	LOAN_CANCEL,
	LOAN_CREATE,
	LOAN_FORGIVE,
	LOAN_INFO,
	LOAN_INHERIT,
	LOAN_LIST,
	LOAN_PAUSE,
	LOAN_PAYBACK,
	LOAN_REJECT,
	LOAN_REPAY,
	LOAN_SEND,
	LOAN_TIME,
	LOAN_TRANSFER,
	
	STORDER,
	STORDER_AMOUNT,
	STORDER_CANCEL,
	STORDER_CREATE,
	STORDER_DELETE,
	STORDER_INFO,
	STORDER_LIST,
	STORDER_PAUSE,
	STORDER_REPEATINGTIME,
	STORDER_STARTTIME,
}
