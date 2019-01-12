package skrelpoid.customchallenges.patches;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.Matcher.FieldAccessMatcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton.PanelClickResult;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton.PanelColor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuPanelButton;
import com.megacrit.cardcrawl.screens.mainMenu.MenuPanelScreen;
import basemod.ReflectionHacks;
import javassist.CtBehavior;
import skrelpoid.customchallenges.ChallengeMod;

public class PatchesForPanel {
	
	public static final Logger logger = LogManager.getLogger(PatchesForPanel.class.getName());
	
	@SpireEnum
	public static PanelClickResult PLAY_CHALLENGE;
	
	@SpireEnum
	public static PanelColor CHALLENGE_COLOR;

	@SpirePatch(clz = MenuPanelScreen.class, method = "initializePanels")
	public static class PlaceAndPositionPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(MenuPanelScreen o) {
			logger.info("Challenge Button Panel added");
			o.panels.add(new MainMenuPanelButton(
					PLAY_CHALLENGE,
					CHALLENGE_COLOR,
					Settings.WIDTH / 2.0f,
					-Settings.HEIGHT * 0.233f));
		}

		private static class Locator extends SpireInsertLocator {

			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				return LineFinder.findInOrder(ctMethodToPatch,
						new FieldAccessMatcher(PanelClickResult.class, "PLAY_NORMAL"));
			}

		}
	}
	
	@SpirePatch(clz = MainMenuPanelButton.class, method = "setLabel")
	public static class SetLabelPatch {
		@SpirePostfixPatch
		public static void Postfix(MainMenuPanelButton b) {
			PanelClickResult result = (PanelClickResult) ReflectionHacks.getPrivate(b, MainMenuPanelButton.class, "result");
			if (result == PLAY_CHALLENGE) {
				logger.info("Challenge Button Panel initialized");
				ReflectionHacks.setPrivate(b, MainMenuPanelButton.class, "portraitImg", ImageMaster.P_STANDARD);
				ReflectionHacks.setPrivate(b, MainMenuPanelButton.class, "panelImg", ChallengeMod.MENU_PANEL);
				ReflectionHacks.setPrivate(b, MainMenuPanelButton.class, "header", ChallengeMod.PANEL_TITLE);
				ReflectionHacks.setPrivate(b, MainMenuPanelButton.class, "description", ChallengeMod.PANEL_DESCRIPTION);
			}
		}
	}
	
	@SpirePatch(clz = MainMenuPanelButton.class, method = "buttonEffect")
	public static class ClickPatch {
		@SpirePostfixPatch
		public static void Postfix(MainMenuPanelButton b) {
			PanelClickResult result = (PanelClickResult) ReflectionHacks.getPrivate(b, MainMenuPanelButton.class, "result");
			if (result == PLAY_CHALLENGE) {
				logger.info("Challenge Button Panel clicked");
			}
		}
	}

}
