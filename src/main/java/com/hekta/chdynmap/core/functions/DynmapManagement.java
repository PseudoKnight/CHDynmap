package com.hekta.chdynmap.core.functions;

import com.hekta.chdynmap.core.CHDynmapStatic;
import com.laytonsmith.PureUtilities.Version;
import com.laytonsmith.abstraction.MCPlayer;
import com.laytonsmith.abstraction.MCWorld;
import com.laytonsmith.annotations.api;
import com.laytonsmith.core.ArgumentValidation;
import com.laytonsmith.core.MSVersion;
import com.laytonsmith.core.ObjectGenerator;
import com.laytonsmith.core.constructs.CBoolean;
import com.laytonsmith.core.constructs.CVoid;
import com.laytonsmith.core.constructs.Target;
import com.laytonsmith.core.environments.CommandHelperEnvironment;
import com.laytonsmith.core.environments.Environment;
import com.laytonsmith.core.exceptions.CRE.CRECastException;
import com.laytonsmith.core.exceptions.CRE.CREFormatException;
import com.laytonsmith.core.exceptions.CRE.CREInvalidPluginException;
import com.laytonsmith.core.exceptions.CRE.CREInvalidWorldException;
import com.laytonsmith.core.exceptions.CRE.CREThrowable;
import com.laytonsmith.core.exceptions.ConfigRuntimeException;
import com.laytonsmith.core.functions.AbstractFunction;
import com.laytonsmith.core.natives.interfaces.Mixed;

/**
 *
 * @author Hekta
 */
public class DynmapManagement {

	public static String docs() {
		return "A class of functions that allows to manage and use general features of Dynmap.";
	}

	@api
	public static class dm_broadcast_to_web extends AbstractFunction {

		@Override
		public String getName() {
			return "dm_broadcast_to_web";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1, 2};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class};
		}

		@Override
		public boolean isRestricted() {
			return true;
		}

		@Override
		public Boolean runAsync() {
			return false;
		}

		@Override
		public String docs() {
			return "void {message, [sender]} Send a generic message to all web users,"
					+ " sender is the label for the sender of the message, could be null,"
					+ " message is the message to be sent.";
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			String senderLabel;
			if (args.length == 2) {
				senderLabel = args[1].val();
			} else {
				senderLabel = "CommandHelper";
			}
			CHDynmapStatic.getDynmapAPI(t).sendBroadcastToWeb(senderLabel, args[0].val());
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_full_radius_renders_paused extends AbstractFunction {

		@Override
		public String getName() {
			return "dm_full_radius_renders_paused";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{0};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class};
		}

		@Override
		public boolean isRestricted() {
			return true;
		}

		@Override
		public Boolean runAsync() {
			return false;
		}

		@Override
		public String docs() {
			return "boolean {} Returns if full and radius renders are paused.";
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			return CBoolean.get(CHDynmapStatic.getDynmapAPI(t).getPauseFullRadiusRenders());
		}
	}

	@api
	public static class dm_set_full_radius_renders_paused extends AbstractFunction {

		@Override
		public String getName() {
			return "dm_set_full_radius_renders_paused";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class};
		}

		@Override
		public boolean isRestricted() {
			return true;
		}

		@Override
		public Boolean runAsync() {
			return false;
		}

		@Override
		public String docs() {
			return "void {boolean} Sets if full and radius renders are paused.";
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			CHDynmapStatic.getDynmapAPI(t).setPauseFullRadiusRenders(ArgumentValidation.getBooleanObject(args[0], t));
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_update_renders_paused extends AbstractFunction {

		@Override
		public String getName() {
			return "dm_update_renders_paused";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{0};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class};
		}

		@Override
		public boolean isRestricted() {
			return true;
		}

		@Override
		public Boolean runAsync() {
			return false;
		}

		@Override
		public String docs() {
			return "boolean {} Returns if update renders are paused.";
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			return CBoolean.get(CHDynmapStatic.getDynmapAPI(t).getPauseUpdateRenders());
		}
	}

	@api
	public static class dm_set_update_renders_paused extends AbstractFunction {

		@Override
		public String getName() {
			return "dm_set_update_renders_paused";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class};
		}

		@Override
		public boolean isRestricted() {
			return true;
		}

		@Override
		public Boolean runAsync() {
			return false;
		}

		@Override
		public String docs() {
			return "void {boolean} Sets if update renders are paused.";
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			CHDynmapStatic.getDynmapAPI(t).setPauseUpdateRenders(ArgumentValidation.getBooleanObject(args[0], t));
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_set_chat_to_web_enabled extends AbstractFunction {

		@Override
		public String getName() {
			return "dm_set_chat_to_web_enabled";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class};
		}

		@Override
		public boolean isRestricted() {
			return true;
		}

		@Override
		public Boolean runAsync() {
			return false;
		}

		@Override
		public String docs() {
			return "void {boolean} Sets if chat to web processing is enabled or not.";
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			CHDynmapStatic.getDynmapAPI(t).setChatToWebProcessingEnabled(ArgumentValidation.getBooleanObject(args[0], t));
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_render_block extends AbstractFunction {

		@Override
		public String getName() {
			return "dm_render_block";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{1};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CRECastException.class, CREFormatException.class, CREInvalidWorldException.class};
		}

		@Override
		public boolean isRestricted() {
			return true;
		}

		@Override
		public Boolean runAsync() {
			return false;
		}

		@Override
		public String docs() {
			return "void {locationArray} Trigger update on tiles at the given block location.";
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCPlayer player = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
			MCWorld world;
			if (player == null) {
				world = null;
			} else {
				world = player.getWorld();
			}
			CHDynmapStatic.getDynmapAPI(t).triggerRenderOfBlock(ObjectGenerator.GetGenerator().location(args[0], world, t));
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_render_volume extends AbstractFunction {

		@Override
		public String getName() {
			return "dm_render_volume";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{2};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class, CRECastException.class, CREFormatException.class, CREInvalidWorldException.class};
		}

		@Override
		public boolean isRestricted() {
			return true;
		}

		@Override
		public Boolean runAsync() {
			return false;
		}

		@Override
		public String docs() {
			return "void {locationArray, locationArray} Trigger update on tiles in the given volume."
					+ " The volume is a cuboid defined by the two locations (opposite corners).";
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			MCPlayer player = environment.getEnv(CommandHelperEnvironment.class).GetPlayer();
			MCWorld world;
			if (player == null) {
				world = null;
			} else {
				world = player.getWorld();
			}
			try {
				CHDynmapStatic.getDynmapAPI(t).triggerRenderOfVolume(ObjectGenerator.GetGenerator().location(args[0], world, t), ObjectGenerator.GetGenerator().location(args[1], world, t));
			} catch (IllegalArgumentException exception) {
				throw new CREFormatException(exception.getMessage(), t);
			}
			return CVoid.VOID;
		}
	}

	@api
	public static class dm_marker_api_initialized extends AbstractFunction {

		@Override
		public String getName() {
			return "dm_marker_api_initialized";
		}

		@Override
		public Integer[] numArgs() {
			return new Integer[]{0};
		}

		@Override
		public Class<? extends CREThrowable>[] thrown() {
			return new Class[]{CREInvalidPluginException.class};
		}

		@Override
		public boolean isRestricted() {
			return true;
		}

		@Override
		public Boolean runAsync() {
			return false;
		}

		@Override
		public String docs() {
			return "boolean {} Returns if the marker API is initialized.";
		}

		@Override
		public Version since() {
			return MSVersion.V3_3_1;
		}

		@Override
		public Mixed exec(Target t, Environment environment, Mixed... args) throws ConfigRuntimeException {
			return CBoolean.get(CHDynmapStatic.getDynmapAPI(t).markerAPIInitialized());
		}
	}
}