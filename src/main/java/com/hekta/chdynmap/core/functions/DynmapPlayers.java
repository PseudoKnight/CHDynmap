package com.hekta.chdynmap.core.functions;

import com.hekta.chdynmap.core.CHDynmapStatic;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCOfflinePlayer;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.ArgumentValidation;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.Static;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CREInvalidPluginException;
import com.laytonsmith.core.exceptions.CRE.CREPlayerOfflineException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;

/**
 *
 * @author Hekta
 */
public class DynmapPlayers {

	public static String docs() {
		return "A class of functions using the Dynmap features for players.";
	}

	public static abstract class DynmapPlayerFunction extends AbstractFunction {

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

	public static abstract class DynmapPlayerGetterFunction extends DynmapPlayerFunction {

		@Override
		public Integer[] numArgs() {
			return new Integer[]{0, 1};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPlayerOfflineException.class};
		}
	}

	@api
	public static class dm_pvisible extends DynmapPlayerGetterFunction {

		@Override
		public String getName() {
			return "dm_pvisible";
		}

		@Override
		public String docs() {
			return "boolean {[playerName]} Returns if the player is visible on the Dynmap."
					+ " This will not throw a PlayerOfflineException (exept from console), so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCOfflinePlayer player;
			if (args.length == 0) {
				MCPlayer psender = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
				if (psender == null) {
					throw new CREPlayerOfflineException("No player was specified!", t);
				} else {
					player = psender;
				}
			} else {
				player = Static.getServer().getOfflinePlayer(args[0].val());
			}
			return CBoolean.get(CHDynmapStatic.getDynmapAPI(t).getPlayerVisbility(player));
		}
	}

	@api
	public static class dm_set_pvisible extends DynmapPlayerFunction {

		@Override
		public String getName() {
			return "dm_set_pvisible";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1, 2};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPlayerOfflineException.class};
		}

		@Override
		public String docs() {
			return "void {[playerName], boolean} Sets if the player is visible on the Dynmap."
					+ " This will not throw a PlayerOfflineException (exept from console), so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCOfflinePlayer player;
			boolean isVisible;
			if (args.length == 1) {
				MCPlayer psender = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
				if (psender == null) {
					throw new CREPlayerOfflineException("No player was specified!", t);
				} else {
					player = psender;
					isVisible = ArgumentValidation.getBooleanObject(args[0], t);
				}
			} else {
				player = Static.getServer().getOfflinePlayer(args[0].val());
				isVisible = ArgumentValidation.getBooleanObject(args[1], t);
			}
			CHDynmapStatic.getDynmapAPI(t).setPlayerVisiblity(player, isVisible);
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_assert_pvisibility extends DynmapPlayerFunction {

		@Override
		public String getName() {
			return "dm_assert_pvisibility";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1, 2, 3};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPlayerOfflineException.class};
		}

		@Override
		public String docs() {
			return "void {[playerName], boolean | playerName, boolean, [pluginID]} Asserts the player visibility (transient, if player is configured to be visible, it is made hidden if one or more plugins assert its invisibility),"
					+ " pluginID is the id that will be used to assert, default to 'CommandHelper'."
					+ " This will not throw a PlayerOfflineException (exept from console), so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCOfflinePlayer player;
			boolean isVisible;
			String plugin;
			if (args.length == 1) {
				MCPlayer psender = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
				if (psender == null) {
					throw new CREPlayerOfflineException("No player was specified!", t);
				} else {
					player = psender;
					isVisible = ArgumentValidation.getBooleanObject(args[0], t);
					plugin = "CommandHelper";
				}
			} else if (args.length == 2) {
				player = Static.getServer().getOfflinePlayer(args[0].val());
				isVisible = ArgumentValidation.getBooleanObject(args[1], t);
				plugin = "CommandHelper";
			} else {
				player = Static.getServer().getOfflinePlayer(args[0].val());
				isVisible = ArgumentValidation.getBooleanObject(args[1], t);
				plugin = args[2].val();
			}
			CHDynmapStatic.getDynmapAPI(t).assertPlayerVisibility(player, isVisible, plugin);
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_assert_pinvisibility extends DynmapPlayerFunction {

		@Override
		public String getName() {
			return "dm_assert_pinvisibility";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1, 2, 3};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPlayerOfflineException.class};
		}

		@Override
		public String docs() {
			return "void {[playerName], boolean | playerName, boolean, [pluginID]} Asserts the player invisibility (transient, if player is configured to be hidden, it is made visibile if one or more plugins assert its visibility),"
					+ " pluginID is the id that will be used to assert, default to 'CommandHelper'."
					+ " This will not throw a PlayerOfflineException (exept from console), so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCOfflinePlayer player;
			boolean isInvisible;
			String plugin;
			if (args.length == 1) {
				MCPlayer psender = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
				if (psender == null) {
					throw new CREPlayerOfflineException("No player was specified!", t);
				} else {
					player = psender;
					isInvisible = ArgumentValidation.getBooleanObject(args[0], t);
					plugin = "CommandHelper";
				}
			} else if (args.length == 2) {
				player = Static.getServer().getOfflinePlayer(args[0].val());
				isInvisible = ArgumentValidation.getBooleanObject(args[1], t);
				plugin = "CommandHelper";
			} else {
				player = Static.getServer().getOfflinePlayer(args[0].val());
				isInvisible = ArgumentValidation.getBooleanObject(args[1], t);
				plugin = args[2].val();
			}
			CHDynmapStatic.getDynmapAPI(t).assertPlayerInvisibility(player, isInvisible, plugin);
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_pcan_see extends DynmapPlayerGetterFunction {

		@Override
		public String getName() {
			return "dm_pcan_see";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1, 2};
		}

		@Override
		public String docs() {
			return "void {[playerName], otherPlayerName} Returns if the player can see the other player."
					+ " This will not throw a PlayerOfflineException (except from console), so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCOfflinePlayer player;
			MCOfflinePlayer otherPlayer;
			if (args.length == 1) {
				MCPlayer psender = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
				if (psender == null) {
					throw new CREPlayerOfflineException("No player was specified!", t);
				} else {
					player = psender;
					otherPlayer = Static.getServer().getOfflinePlayer(args[0].val());
				}
			} else {
				player = Static.getServer().getOfflinePlayer(args[0].val());
				otherPlayer = Static.getServer().getOfflinePlayer(args[1].val());
			}
			return CBoolean.get(CHDynmapStatic.getDynmapAPI(t).testIfPlayerVisibleToPlayer(player, otherPlayer));
		}
	}

	@api
	public static class dm_post_pchat extends DynmapPlayerFunction {

		@Override
		public String getName() {
			return "dm_post_pchat";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1, 2, 3};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CREPlayerOfflineException.class};
		}

		@Override
		public String docs() {
			return "void {[playerName], message | playerName, message, [displayName]} Post message from player to web,"
					+ " if displayName is an empty string, it is not noticed."
					+ " This will not throw a PlayerOfflineException (except from console), so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCOfflinePlayer player;
			String displayName;
			String message;
			if (args.length == 1) {
				MCPlayer psender = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
				if (psender == null) {
					throw new CREPlayerOfflineException("No player was specified!", t);
				} else {
					player = psender;
					displayName = psender.getDisplayName();
					message = args[0].val();
				}
			} else if (args.length == 2) {
				MCPlayer mcplayer = Static.getServer().getPlayer(args[0].val());
				if (mcplayer == null) {
					player = Static.getServer().getOfflinePlayer(args[0].val());
					displayName = player.getName();
				} else {
					player = mcplayer;
					displayName = mcplayer.getDisplayName();
				}
				message = args[1].val();
			} else {
				player = Static.getServer().getOfflinePlayer(args[0].val());
				displayName = args[2].val();
				message = args[1].val();
			}
			CHDynmapStatic.getDynmapAPI(t).postPlayerMessageToWeb(player, displayName, message);
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_post_pjoin extends DynmapPlayerGetterFunction {

		@Override
		public String getName() {
			return "dm_post_pjoin";
		}

		@Override
		public String docs() {
			return "void {[playerName]} Post a join message for player to web."
					+ " This will not throw a PlayerOfflineException (except from console), so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCOfflinePlayer player;
			String displayName;
			if (args.length == 0) {
				MCPlayer psender = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
				if (psender == null) {
					throw new CREPlayerOfflineException("No player was specified!", t);
				} else {
					player = psender;
					displayName = psender.getDisplayName();
				}
			} else {
				MCPlayer mcplayer = Static.getServer().getPlayer(args[0].val());
				if (mcplayer == null) {
					player = Static.getServer().getOfflinePlayer(args[0].val());
					displayName = player.getName();
				} else {
					player = mcplayer;
					displayName = mcplayer.getDisplayName();
				}
			}
			CHDynmapStatic.getDynmapAPI(t).postPlayerJoinToWeb(player, displayName);
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_post_pquit extends DynmapPlayerGetterFunction {

		@Override
		public String getName() {
			return "dm_post_pquit";
		}

		@Override
		public String docs() {
			return "void {[playerName]} Post a quit message for player to web."
					+ " This will not throw a PlayerOfflineException (except from console), so the name must be exact.";
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCOfflinePlayer player;
			String displayName;
			if (args.length == 0) {
				MCPlayer psender = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
				if (psender == null) {
					throw new CREPlayerOfflineException("No player was specified!", t);
				} else {
					player = psender;
					displayName = psender.getDisplayName();
				}
			} else {
				MCPlayer mcplayer = Static.getServer().getPlayer(args[0].val());
				if (mcplayer == null) {
					player = Static.getServer().getOfflinePlayer(args[0].val());
					displayName = player.getName();
				} else {
					player = mcplayer;
					displayName = mcplayer.getDisplayName();
				}
			}
			CHDynmapStatic.getDynmapAPI(t).postPlayerQuitToWeb(player, displayName);
			return CVoid.VOID;
		}
	}
}