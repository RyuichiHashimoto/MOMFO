package lib.experiments;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lib.experiments.Exception.CommandException.HeaderNotFoundException;
import lib.io.FileConstants;
import lib.io.input.FileReader;

public class CommandSettingMaker implements Serializable {

	public static List<CommandSetting> getHashmap(String filePath) throws HeaderNotFoundException {
		List<String> fileRead = null;
		try {
			fileRead = FileReader.FileReadingAsArray(filePath);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
		return decodeSetting(fileRead);
	}

	public static List<CommandSetting> decodeSetting(List<String> filePath) throws HeaderNotFoundException {
		List<String> command = new ArrayList<String>();
		List<String> label = new ArrayList<String>();

		int size = 1;

		if (!filePath.get(0).equalsIgnoreCase(FileConstants.COMMAND_CONSTRAINT)) {
			throw new HeaderNotFoundException();
		}

		for (int i = 1; i < filePath.size(); i++) {
			if (filePath.get(i).contains(FileConstants.SETTING_FILE_DELIMITER)
					&& !filePath.get(i).contains(FileConstants.SETTING_FILE_COMMENTOUT)) {

				String[] dividedStr = filePath.get(i).split(FileConstants.SETTING_FILE_DELIMITER);
				if (dividedStr.length == 2) {
					command.add(filePath.get(i).split(FileConstants.SETTING_FILE_DELIMITER)[1]);
				} else {
					String[] dividedStrEraseOne = new String[dividedStr.length - 1];

					for (int str_index = 1; str_index < dividedStr.length; str_index++) {
						dividedStrEraseOne[str_index - 1] = dividedStr[str_index];
					}
					command.add(String.join(FileConstants.SETTING_FILE_DELIMITER, dividedStrEraseOne));
				}
				label.add(filePath.get(i).split(FileConstants.SETTING_FILE_DELIMITER)[0]);
				size += command.get(command.size() - 1).split(FileConstants.SETTING_FILE_SEQUENCE_DEMILITER).length - 1;
			}
		}

		List<CommandSetting> ret = new ArrayList<>();
		ret.add(new CommandSetting());

		for (int i = 0; i < command.size(); i++) {
			addLabel(ret, label.get(i), command.get(i));
		}

		return ret;
	}

	public static String[] getCommandAtEachLineWithBraces(String componet) {
		List<String> ListRet = new ArrayList<String>();
		String MidStr;
		System.out.println("start");
		do {
			MidStr = getMidStr(componet, "{", "}");
			componet = componet.replace("{" + MidStr + "}", "");
			ListRet.add(MidStr);
		} while (componet.contains("{") && componet.contains("}"));

		do {
			componet = componet.replace(",,", ",");
		} while (componet.contains(",,"));
		if (componet.endsWith(",")) {
			componet = componet.substring(0, componet.length() - 1);
		}
		if (componet.startsWith(",")) {
			componet = componet.substring(1, componet.length());
		}

//		System.out.println(componet);
		String[] ret = null;
		if (componet.equals("")) {
			ret = new String[ListRet.size()];
			for (int i = 0; i < ListRet.size(); i++) {
				ret[i] = ListRet.get(i);
			}
		} else {
			String[] restStr = componet.split(FileConstants.SETTING_FILE_SEQUENCE_DEMILITER);

			ret = new String[ListRet.size() + restStr.length];
			for (int i = 0; i < ListRet.size(); i++) {
				ret[i] = ListRet.get(i);
			}
			for (int i = 0; i < restStr.length; i++) {
				ret[i + ListRet.size()] = restStr[i];
			}
		}
		return ret;
	}

	public static String[] splitCommandAtEachLine(String componet) {
		if (componet.contains("{") && componet.contains("}")) {

			return getCommandAtEachLineWithBraces(componet);
		} else {
			return componet.split(FileConstants.SETTING_FILE_SEQUENCE_DEMILITER);
		}

	}

	public static String getMidStr(String mainStr, String _prefix, String _suffix) {

		final int _preIdx = mainStr.indexOf(_prefix) + _prefix.length();

		final int _sufIdx = mainStr.indexOf(_suffix);

		String ret = mainStr.substring(_preIdx, _sufIdx);
		return ret;
	}

	public static void addLabel(List<CommandSetting> hashmaplist, String label, String componet) {

		String[] splitedcomponet = splitCommandAtEachLine(componet);

		List<CommandSetting> temphashmaplist = new ArrayList<CommandSetting>(hashmaplist);
		hashmaplist.clear();
		for (int i = 0; i < splitedcomponet.length; i++) {
			for (int t = 0; t < temphashmaplist.size(); t++) {
				
				CommandSetting temp = (CommandSetting) temphashmaplist.get(t).clone();
					
				if (splitedcomponet[i].contains("(") && splitedcomponet[i].contains(")")) {
					System.out.println(splitedcomponet[i]);
					String MidStr = getMidStr(splitedcomponet[i], "(", ")");
					String[] keyvalue = MidStr.split(FileConstants.SETTING_FILE_DELIMITER);
					temp.putForce(keyvalue[0], keyvalue[1]);					
				}

				temp.put(label, splitedcomponet[i]);
				hashmaplist.add(temp);
			}
		}
	}

	public static void main(String[] args) {
		List<CommandSetting> setting = new ArrayList<CommandSetting>();

		try {
			setting = getHashmap("CommandSetting.st");
			for (int i = 0; i < setting.size(); i++) {
				setting.get(i).subcriptToFile("output/output" + (i + 1) + ".st");
			}

		} catch (HeaderNotFoundException e) {
			e.printStackTrace();
		}

	}

	private static void subscriptHash(HashMap<String, String> hashMap) {
		System.out.println("------------------start------------------");

		for (String key : hashMap.keySet()) {
			System.out.println(key + "	" + hashMap.get(key));
		}
		System.out.println("------------------end------------------");

	}
}
