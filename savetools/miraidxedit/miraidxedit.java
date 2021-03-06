//Part of hacktools by gnmmarechal

//Special Thanks: ProjectMiraiDXSE and the people involved in making it. I did copypaste code. It was easy considering C#'s likeness to Java.
//As such, this can be considered a port of that program to Java.


/*
 * 
 * 
 * Usage: 
 * 	miraidxedit [-s save (defaults to bk_m2r.bin)] -e <cheatID>/<cheatNAME> -- Applies edit to savedata //Maybe multiple IDs at one time
 * 	miraidxedit [-h/--help] -- Shows information about the program
 * 	miraidxedit -i/--id -- Shows edits IDs
 * 
 * 
 */
 
import java.util.*;
import java.io.*;
import java.nio.file.*;
import org.apache.commons.cli.*;


public class miraidxedit 
{
	//Program Data
	static String programName = "miraidxedit";
	static String programDesc = "Hatsune Miku: Project Mirai DX Save Editor";
	static String programVersion = "1.0";
	
	//Offsets and values, taken from the Project Mirai DX Save Editor.
	
	static String saveName = "bk_m2r.bin";
	
	
	static int MaxMP = 0x0008;
	static int AgentMoose = 0x98;
	static int deathwilldie = 0x20;
	static int SpankrPoodle = 0x60;
	static int SpankrPoodle2 = 0x50;
	static int Wrench_King = 0x00;
	static int Nirvash_TypeZero = 0x0c;
	static int Badger41 = 0x10;
	static int sKiLLz = 0x1c;
	static int reDFlag = 0x2c;
	static int Vernon = 0x40;
	static int eYeDoL = 0x4c;
	static int destructo = 0x5c;
	static int A051019194709_1 = 0x6c;
	static int A051019194709_2 = 0x60;
	
	static int EBH = 0x04;
	static int NBH = 0x14;
	static int HBH = 0x24;
	static int ETH = 0x44;
	static int NTH = 0x54;
	static int HTH = 0x64;
	
	static int EBC = 0x0e;
	static int NBC = 0x1e;
	static int HBC = 0x2e;
	static int ETC = 0x4e;
	static int NTC = 0x5e;
	static int HTC = 0x6e;
	
	static int[] Songs =
	{
		0x2750, 0x2848, 0x2940, 0x2A38, 0x2B30, 0x2C28, 0x2D20, 0x2E18, 0x2F10, 0x3008, 0x3100, 0x31F8, 0x32F0, 0x33E8,
		0x34E0, 0x35D8, 0x36D0, 0x37C8, 0x38C0, 0x39B8, 0x3AB0, 0x3BA8, 0x3CA0, 0x3D98, 0x3E90, 0x3F88, 0x4080, 0x4178,
		0x4270, 0x4368, 0x4460, 0x4558, 0x4650, 0x4748, 0x4840, 0x4938, 0x4A30, 0x4B28, 0x4C20, 0x4D18, 0x4E10, 0x4F08,
		0x5000, 0x50F8, 0x51F0, 0x52E8, 0x53E0, 0x54D8, 0x55D0
	};
	static int[][] SmallItems =
	{
		new int[] { 0x6a28, 0x6a40, 0x6a48, 0x6a58, 0x6a60, 0x6a78, 0x6a88, 0x6a90, 0x6ac8, 0x6ad0, 0x6ad8, 0x6af0, 0x6af8 },
	
		new int[] { 0x69c0, 0x69c8, 0x69d0, 0x69d8, 0x69e0, 0x69e8, 0x69f0, 0x69f8, 0x6a00, 0x6a08, 0x6a10, 0x6a18, 0x6a20,
					0x6a28, 0x6a30, 0x6a38, 0x6a40, 0x6a48, 0x6a50, 0x6a58, 0x6a60, 0x6a68, 0x6a70, 0x6a78, 0x6a80, 0x6a88,
					0x6a90, 0x6a98, 0x6aa0, 0x6aa8, 0x6ab0, 0x6ab8, 0x6ac0, 0x6ac8, 0x6ad0, 0x6ad8, 0x6ae0, 0x6ae8, 0x6af0,
					0x6af8, 0x6b00 }
	};
	
	//Medium Items
	static int[][] MediumItems =
	{
		new int[] { 0x6bd8, 0x6c10, 0x6c80 },
		new int[] { 0x6b50, 0x6B58, 0x6B60, 0x6B68, 0x6B70, 0x6B78, 0x6B80, 0x6B88, 0x6B88,
					0x6B98, 0x6BA0, 0x6BA8, 0x6BB0, 0x6BB8, 0x6BC0, 0x6BC8, 0x6BD0, 0x6BD8,
					0x6BE0, 0x6BE8, 0x6BF0, 0x6BF8, 0x6C00, 0x6C08, 0x6C10, 0x6C18, 0x6C20,
					0x6C28, 0x6C30, 0x6C38, 0x6C40, 0x6C48, 0x6C50, 0x6C58, 0x6C60, 0x6C68,
					0x6C70, 0x6C78, 0x6c80 }
	};
	
	//Large Items
	static int[][] LargeItems =
	{
		new int[] { 0x6d38 },
	
		new int[] { 0x6CE0, 0x6CE8, 0x6CF0, 0x6CF8, 0x6D00, 0x6D08, 0x6D10, 0x6D18, 0x6D20, 0x6D28, 0x6D30, 0x6D38 }
	};
	
	//Wall Items
	static int[] WallItems =
	{
		0x6D80, 0x6D88, 0x6D90, 0x6D98, 0x6DA0, 0x6DA8, 0x6DB0, 0x6DB8, 0x6DC0,
		0x6DC8, 0x6DD0, 0x6DD8, 0x6DE0, 0x6DE8, 0x6DF0, 0x6DF8, 0x6E00, 0x6E08, 0x6E10
	};
	
	//Air Items
	static int[][] AirItems =
	{
		new int[] { 0x6e90 },
		new int[] { 0x6e70, 0x6e78, 0x6e80, 0x6e88, 0x6e90 }
	};
	
	//Pool Items
	static int[] PoolItems =
	{
		0x6EC0, 0x6EC8, 0x6ED0, 0x6ED8
	};
	
	//Outfits
	static int[] Outfits =
	{
		0x56c8, 0x56E8, 0x5708, 0x5728, 0x5748, 0x5768, 0x5788, 0x57A8, 0x57C8, 0x57E8,
		0x5808, 0x5828, 0x5848, 0x5868, 0x5888, 0x58A8, 0x58C8, 0x58E8, 0x5908, 0x5928,
		0x5948, 0x5968, 0x5988, 0x59A8, 0x59C8, 0x59E8, 0x5A08, 0x5A28, 0x5A48, 0x5A68,
		0x5A88, 0x5AA8, 0x5AC8, 0x5AE8, 0x5B08, 0x5B28, 0x5B48, 0x5B68, 0x5B88, 0x5BA8,
		0x5BC8, 0x5C08, 0x5C28, 0x5C48, 0x5C68, 0x5C88, 0x5CA8, 0x5CC8, 0x5CE8, 0x5D08,
		0x5D28, 0x5D48, 0x5D68, 0x5D88, 0x5DA8, 0x5DC8, 0x5DE8, 0x5E08, 0x5E28, 0x5E48,
		0x5E68, 0x5E88, 0x5EA8, 0x5EC8, 0x5EE8, 0x5F08, 0x5F28, 0x5F48, 0x5F68, 0x5F88,
		0x5FC8, 0x5FE8, 0x6008, 0x6028, 0x6048, 0x6068, 0x6088, 0x60A8, 0x60C8, 0x6108,
		0x6128, 0x6148, 0x6168, 0x6188, 0x61A8, 0x61C8, 0x61E8, 0x6228, 0x6248, 0x6268,
		0x6288, 0x62A8, 0x62C8, 0x62E8, 0x6328, 0x6348, 0x6368, 0x6388, 0x63A8, 0x63C8,
		0x63E8, 0x6408, 0x6428, 0x6468, 0x6488, 0x64A8, 0x64C8, 0x64E8, 0x6508, 0x6548,
		0x6568, 0x6588, 0x65A8, 0x65C8, 0x65E8, 0x6608, 0x6628, 0x6648, 0x6668, 0x6688
	};
	
	//Percent Offset
	static int[] percent =
	{
		0x08, 0x18, 0x28, 0x48, 0x58, 0x68
	};
	
	//Percent 100%
	static int[] percvalue =
	{
		0x00, 0x00, 0x80, 0x3F
	};
	
	static Options options = new Options();

	
	public static void main (String[] args) 
	{
		parse(args);
	}
	
	static void parse(String[] args)
	{		
		
		CommandLineParser parser = new BasicParser();
		options.addOption("h", "help", false, "Show help.");
		options.addOption("v", "version", false, "Show program version.");
		
		Option opS = new Option("s", "save", false, "Set saved data file.");
		//Option opE = new Option("e", "edit", false, "Edit the saved data file.");
		opS.setArgs(1);
		Option opE = OptionBuilder.hasArgs(1)
					.withLongOpt("edit")
					.isRequired(false)
					.withDescription("Edit the saved data file")
					.create("e");
		
		options.addOption(opS);
		options.addOption(opE);
		
		CommandLine cmd = null;
		try
		{
			cmd = parser.parse(options, args);
			
			if (!cmd.hasOption("v") && !cmd.hasOption("h"))
			{
				if (args.length < 2) { printHelp(); System.exit(0); }
				
				if (cmd.hasOption("s")) //Set save
				{
					saveName = cmd.getOptionValue("s", "bk_m2r.bin");
					System.out.println("Set save to: " + saveName);
				}
				
				if (cmd.hasOption("e"))
				{
					if (cmd.getOptionValue("e").equals("#"))
						editConsole(saveName);
					else
					{
						backupSave(saveName);
						editSave(saveName, cmd.getOptionValue("e"));
					}
				}
			}
			else
			{
				if (cmd.hasOption("v"))
				{	
					printVersion();
					System.exit(0);
				}
				else if (cmd.hasOption("h"))
				{
					printHelp();
					System.exit(0);
				}
			}
				
		} catch (ParseException e) { System.exit(1); }
	}
	
	//Prints
	static String printChar(String ch, int no)
	{
		String str = "";
		for (int i = 0; i < no; i++)
		{
			str += ch;
		}
		return str;
	}
	static boolean isInteger(String s) 
	{
		return isInteger(s,10);
	}
	
	static boolean isInteger(String s, int radix) 
	{
		if(s.isEmpty()) return false;
		for(int i = 0; i < s.length(); i++) 
		{
			if(i == 0 && s.charAt(i) == '-') 
			{
				if(s.length() == 1) return false;
				else continue;
			}
			if(Character.digit(s.charAt(i),radix) < 0) return false;
		}
		return true;
	}	
	static void printVersion()
	{
		System.out.println(programName + " v." + programVersion);
		System.out.println("\n" + programDesc);
		System.out.println("Author: gnmmarechal\nCredits: Me for the port, those involved in PMDXSE (both the Python and C# versions, this is based on those)\n\nRepository: https://github.com/gnmmarechal/hacktools");
	}
	static void printHelp()
	{
		printVersion();
		System.out.println(printChar("=", 40) + "\n");
		System.out.println("Usage:\n - miraidxedit -s <save> -e <edit ID/name> -- Edit the save");
	}
	
	static void printList()
	{
	}
	
	static boolean saveExists(String path)
	{
		File f = new File(path);
		return (f.isFile() && f.canRead());
	}	
	static void backupSave(String path)
	{
		if (!saveExists(path)) error("File " + path + " doesn't exist or is unreadable.");
		try
		{
			if (saveExists(path + ".bak")) Files.delete(Paths.get(path + ".bak"));
			Files.copy(Paths.get(path), Paths.get(path + ".bak"), StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) { };
		
		
	}
	static void error(String msg)
	{
		System.out.println("ERROR: " + msg);
		System.exit(1);
	}
	
	static void editConsole(String saveName)
	{
		System.out.println("Quick Edit Tool");
		Scanner sca = new Scanner(System.in);
		ArrayList<String> editList = new ArrayList<String>();
		boolean inLoop = true;
		while (inLoop)
		{
			System.out.printf(">");
			String input = sca.nextLine();
			if (input.equals("-1") || input.equals("q") || input.equals("quit")) inLoop = false;
			else if (input.equals("-2") || input.equals("list")) printList();
			else if (input.equals("-3") || input.equals("w"))
			{
				applyMultipleEdits(saveName, editList);
				editList.clear();
			}
			else if (input.equals("-4") || input.equals("wq"))
			{
				applyMultipleEdits(saveName, editList);
				editList.clear();
				inLoop = false;
			}
			else
			{
				editList.add(input);
			}
		}
	}

	static void applyMultipleEdits(String saveName, ArrayList<String> editList)
	{
		System.out.println("Backing up save " + saveName + "...");
		backupSave(saveName);
		System.out.println("Applying edits to save " + saveName + "...");
		System.out.println("Edit list: " + editList);
		for (int i = 0; i < editList.size(); i++)
		{
			System.out.println("Applying edit: " + editList.get(i));
			editSave(saveName, editList.get(i));
		}		
	}
	
	static void editSave(String saveName, String editName)
	{
		if (isInteger(editName)) editSave(saveName, Integer.parseInt(editName));
		switch(editName)
		{
			case "unlockAllSongs":
				editSave(saveName, 1);
				break;
			case "maxMP":
				editSave(saveName, 2);
				break;
			case "unlockHardMode":
				editSave(saveName, 3);
				break;
			case "unlockHardModeButton":
				editSave(saveName, 4);
				break;
			case "unlockHardModeTouch":
				editSave(saveName, 5);
				break;
			case "unlockAllItems":
				editSave(saveName, 6);
				break;
			case "unlockSmallItems":
				editSave(saveName, 7);
				break;
			case "unlockMediumItems":
				editSave(saveName, 8);
				break;
			case "unlockLargeItems":
				editSave(saveName, 9);
				break;
			case "unlockAirItems":
				editSave(saveName, 10);
				break;
			case "unlockOutfits":
				editSave(saveName, 11);
				break;
			
		}
	}
	
	static void editSave(String saveName, int editID)
	{
		switch(editID)
		{
			case 1: //Unlock All Songs
			{
				int[] val = { 0xA0 };
				for (int Song_offset : Songs)
				{
					editFile(saveName, Song_offset + AgentMoose, val);
				}
				break;
			}
			case 2: //Maximum MP
			{
				int[] val = { 0xF4, 0x23, 0x0F, 0x00 };
				editFile(saveName, MaxMP, val);
				break;
			}
			case 3: //Unlock Hard Mode (Button + Touch)
			{
				int[] val = { 0x01 };
				for (int Song_offset : Songs)
				{
					editFile(saveName, deathwilldie, val);
				}
				for (int Song_offset : Songs)
				{
					editFile(saveName, SpankrPoodle, val);
				}
				break;
			}
			case 4: //Unlock Hard Mode (Button)
			{
				int[] val = { 0x01 };
				for (int Song_offset : Songs)
				{
					editFile(saveName, deathwilldie, val);
				}
				break;		
			}
			case 5: //Unlock Hard Mode (Touch)
			{
				int[] val = { 0x01 };
				for (int Song_offset : Songs)
				{
					editFile(saveName, SpankrPoodle, val);
				}
				break;
			}
			case 6: //Unlock All Items In Shop
			{
				int[] val = { 0x03 };
				
				for (int items : SmallItems[0]) //Small Items
				{
					editFile(saveName, items, val);
				}
				for (int items : MediumItems[0]) //Medium Items
				{
					editFile(saveName, items, val);
				}
				for (int items : LargeItems[0]) //Large Items
				{
					editFile(saveName, items, val);
				}
				for (int items : AirItems[0]) //Air Items
				{
					editFile(saveName, items, val);
				}
				break;
			}
			case 7: //Unlock Small Items in Shop
			{
				int[] val = { 0x03 };
				
				for (int items : SmallItems[0])
				{
					editFile(saveName, items, val);
				}				
				break;
			}
			case 8: //Unlock Medium Items in Shop
			{
				int[] val = { 0x03 };
				
				for (int items : MediumItems[0])
				{
					editFile(saveName, items, val);
				}				
				break;
			}
			case 9: //Unlock Large Items in Shop
			{
				int[] val = { 0x03 };
				
				for (int items : LargeItems[0])
				{
					editFile(saveName, items, val);
				}				
				break;
			}
			case 10: //Unlock Air Items in Shop
			{
				int[] val = { 0x03 };
				
				for (int items : AirItems[0])
				{
					editFile(saveName, items, val);
				}				
				break;
			}
			case 11: //Unlock Outfits
			{
				int[] val = { 0x03 };
				
				for (int outfits : Outfits)
				{
					editFile(saveName, outfits, val);
				}
				break;	
			}								
		}
		
	}
	
	static void editFile(String filePath, int offset, int[] value)
	{
		try
		{
			RandomAccessFile fileStore = new RandomAccessFile(filePath, "rw");
			fileStore.seek(offset);
			for (int i = 0; i < value.length; i++)
			{
				fileStore.write(value[i]);
			}
			
			fileStore.close();
		} catch (Exception e) {}
	}
}

