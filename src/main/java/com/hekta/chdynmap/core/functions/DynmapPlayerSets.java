package com.hekta.chdynmap.core.functions;

import com.hekta.chdynmap.abstraction.MCDynmapPlayerSet;
import com.hekta.chdynmap.core.CHDynmapStatic;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCOfflinePlayer;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.ArgumentValidation;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CArray;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.CString;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREFormatException;
import com.laytonsmith.core.exceptions.CRE.CREInvalidPluginException;
import com.laytonsmith.core.exceptions.CRE.CRENotFoundException;
import com.laytonsmith.core.exceptions.CRE.CREPlayerOfflineException;
import com.laytonsmith.core.exceptions.CRE.CREPluginInternalException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;

import java.util.Set;

/**
 *
 * @author Hekta
 */
public class DynmapPlayerSets {

	public static String docs() {
		return "A class of functions to manage the Dynmap playersets.";
	}

	public static abstract class DynmapPlayerSetFunction extends AbstractFunction {

		@Override
		public boolean isRestricted() {
			return true;
		}

		@Override
		public Boolean runAsync() {
			return false;
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}
	}

	public static abstract class DynmapPlayerSetGetterFunction extends DynmapPlayerSetFunction {

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPluginInternalException.class, CRENotFoundException.class};
		}
	}

	public static abstract class DynmapPlayerSetSetterFunction extends DynmapPlayerSetFunction {

		@Override
		public Integer[] numArgs() {
			return new Integer[]{2};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPluginInternalException.class, CRENotFoundException.class, CRECastException.class};
		}
	}

	@api
	public static class dm_all_playersets extends DynmapPlayerSetFunction {

		@Override
		public String getName() {
			return "dm_all_playersets";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{0};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPluginInternalException.class};
		}

		@Override
		public String docs() {
			return "array {} Returns an array containing all the playerset IDs.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			CArray setArray = new CArray(t);
			for (MCDynmapPlayerSet set : CHDynmapStatic.getMarkerAPI(t).getPlayerSets()) {
				setArray.push(new CString(set.getId(), t), t);
			}
			return setArray;
		}
	}

	@api
	public static class dm_create_playerset extends DynmapPlayerSetFunction {

		@Override
		public String getName() {
			return "dm_create_playerset";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1, 2};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPluginInternalException.class, CRECastException.class, CREFormatException.class};
		}

		@Override
		public String docs() {
			return "string {newSetID, [optionArray]} Creates a playerset and returns its ID."
					+ " ---- The ID must be unique among playersets and must only contain numbers, letters, periods (.) and underscores (_)."
					+ " The option array is associative and not required, and all its keys are optional."
					+ " <li>KEY - DEFAULT - DESCRIPTION - COMMENT</li>"
					+ " <li>persistent - false - sets if the playerset is persistent - can not be changed later</li>"
					+ " <li>players - empty array - an array of players the playerset will contain</li>"
					+ " <li>symmetric - false - sets if the playerset will be symmetric (players in set can see other the players in set)</li>";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			String setID = args[0].val();
			//is the id valid ?
			CHDynmapStatic.testDynmapIDValidity(setID, t);
			//already exists ?
			if (CHDynmapStatic.getMarkerAPI(t).getPlayerSet(setID) != null) {
				throw new CREPluginInternalException("\"" + setID + "\" is already an existing playerset.", t);
			}
			//create the option array
			CArray optionArray;
			if (args.length == 1) {
				optionArray = new CArray(t);
			} else {
				optionArray = ArgumentValidation.getArray(args[1], t);
			}
			Set<String> keys = optionArray.stringKeySet();
			//set optional values
			//players
			MCOfflinePlayer[] players;
			if (keys.contains("players")) {
				CArray givenPlayers = ArgumentValidation.getArray(optionArray.get("players", t), t);
				players = new MCOfflinePlayer[(int) givenPlayers.size()];
				if (givenPlayers.inAssociativeMode()) {
					throw new CRECastException("The array must not be associative.", t);
				}
				int i = 0;
				for (Mixed player : givenPlayers.asList()) {
					players[i] = Static.getServer().getOfflinePlayer(player.val());
					i++;
				}
			} else {
				players = new MCOfflinePlayer[0];
			}
			//persistent
			boolean persistent;
			if (keys.contains("persistent")) {
				persistent = ArgumentValidation.getBooleanObject(optionArray.get("persistent", t), t);
			} else {
				persistent = false;
			}
			//symmetric
			boolean symmetric;
			if (keys.contains("symmetric")) {
				symmetric = ArgumentValidation.getBooleanObject(optionArray.get("symmetric", t), t);
			} else {
				symmetric = false;
			}
			//create player set
			MCDynmapPlayerSet set = CHDynmapStatic.getMarkerAPI(t).createPlayerSet(setID, symmetric, players, persistent);
			if (set == null) {
				throw new CREPluginInternalException("The markerset creation failed.", t);
			}
			return new CString(set.getId(), t);
		}
	}

	@api
	public static class dm_delete_playerset extends DynmapPlayerSetFunction {

		@Override
		public String getName() {
			return "dm_delete_playerset";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPluginInternalException.class};
		}

		@Override
		public String docs() {
			return "void {setID} Deletes a playerset.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			CHDynmapStatic.getPlayerSet(args[0].val(), t).delete();
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_players_in_playerset extends DynmapPlayerSetGetterFunction {

		@Override
		public String getName() {
			return "dm_players_in_playerset";
		}

		@Override
		public String docs() {
			return "array {setID} Returns an array containing all the players in the playerset.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			CArray playerArray = new CArray(t);
			for (MCOfflinePlayer player : CHDynmapStatic.getPlayerSet(args[0].val(), t).getPlayers()) {
				playerArray.push(new CString(player.getName(), t), t);
			}
			return playerArray;
		}
	}

	@api
	public static class dm_set_players_in_playerset extends DynmapPlayerSetSetterFunction {

		@Override
		public String getName() {
			return "dm_set_players_in_playerset";
		}

		@Override
		public String docs() {
			return "void {setID, array} Sets the players in the playerset."
					+ " This will not throw a PlayerOfflineException, so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			CArray givenPlayers = ArgumentValidation.getArray(args[1], t);
			if (givenPlayers.inAssociativeMode()) {
				throw new CRECastException("The array must not be associative.", t);
			}
			MCOfflinePlayer[] players = new MCOfflinePlayer[(int) givenPlayers.size()];
			int i = 0;
			for (Mixed player : givenPlayers.asList()) {
				players[i] = Static.getServer().getOfflinePlayer(player.val());
				i++;
			}
			CHDynmapStatic.getPlayerSet(args[0].val(), t).setPlayers(players);
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_pis_in_playerset extends DynmapPlayerSetFunction {

		@Override
		public String getName() {
			return "dm_pis_in_playerset";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1, 2};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPluginInternalException.class, CREPlayerOfflineException.class};
		}

		@Override
		public String docs() {
			return "boolean {setID, [playerName]} Returns if a player is in the playerset."
					+ " This will not throw a PlayerOfflineException (except from console), so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCOfflinePlayer player;
			if (args.length == 1) {
				MCPlayer psender = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
				if (psender == null) {
					throw new CREPlayerOfflineException("No player was specified!", t);
				} else {
					player = psender;
				}
			} else {
				player = Static.getServer().getOfflinePlayer(args[1].val());
			}
			return CBoolean.get(CHDynmapStatic.getPlayerSet(args[0].val(), t).isPlayerInSet(player));
		}
	}

	@api
	public static class dm_set_pis_in_playerset extends DynmapPlayerSetFunction {

		@Override
		public String getName() {
			return "dm_set_pis_in_playerset";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{2, 3};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPluginInternalException.class, CREPlayerOfflineException.class};
		}

		@Override
		public String docs() {
			return "void {setID, [playerName], boolean} Sets if a player is in the playerset."
					+ " This will not throw a PlayerOfflineException (except from console), so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCDynmapPlayerSet set = CHDynmapStatic.getPlayerSet(args[0].val(), t);
			MCOfflinePlayer player;
			boolean isInSet;
			if (args.length == 2) {
				MCPlayer psender = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
				if (psender == null) {
					throw new CREPlayerOfflineException("No player was specified!", t);
				} else {
					player = psender;
					isInSet = ArgumentValidation.getBooleanObject(args[1], t);
				}
			} else {
				player = Static.getServer().getOfflinePlayer(args[1].val());
				isInSet = ArgumentValidation.getBooleanObject(args[2], t);
			}
			if (isInSet) {
				if (set.isPlayerInSet(player)) {
					throw new CREPluginInternalException("The player is already in the playerset.", t);
				}
				set.addPlayer(player);	
			} else {
				if (!set.isPlayerInSet(player)) {
					throw new CREPluginInternalException("The player is already not in the playerset.", t);
				}
				set.removePlayer(player);
			}
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_playerset_persistent extends DynmapPlayerSetGetterFunction {

		@Override
		public String getName() {
			return "dm_playerset_persistent";
		}

		@Override
		public String docs() {
			return "boolean {setID} Returns if the playerset is persistent.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			return CBoolean.get(CHDynmapStatic.getPlayerSet(args[0].val(), t).isPersistent());
		}
	}

	@api
	public static class dm_playerset_symmetric extends DynmapPlayerSetGetterFunction {

		@Override
		public String getName() {
			return "dm_playerset_symmetric";
		}

		@Override
		public String docs() {
			return "boolean {setID} Returns if the playerset is symmetric (if true players in set can see other the players in set).";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			return CBoolean.get(CHDynmapStatic.getPlayerSet(args[0].val(), t).isSymmetric());
		}
	}

	@api
	public static class dm_set_playerset_symmetric extends DynmapPlayerSetSetterFunction {

		@Override
		public String getName() {
			return "dm_set_playerset_symmetric";
		}

		@Override
		public String docs() {
			return "void {setID, boolean} Sets if the playerset is symmetric (if true, players in set can see the players in set, if false, privilege is always required).";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			CHDynmapStatic.getPlayerSet(args[0].val(), t).setSymmetric(ArgumentValidation.getBooleanObject(args[1], t));
			return CVoid.VOID;
		}
	}
}