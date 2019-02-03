package skrelpoid.customchallenges.patches;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher.MethodCallMatcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen.CurScreen;
import com.megacrit.cardcrawl.screens.mainMenu.SaveSlotScreen;
import javassist.CtBehavior;
import skrelpoid.customchallenges.ui.ChallengeModeScreen;

public class PatchesForChallengeModeScreen {

	@SpireEnum
	public static CurScreen CUSTOM_CHALLENGE;

	@SpirePatch(clz = MainMenuScreen.class, method = SpirePatch.CLASS)
	public static class Field {
		public static SpireField<ChallengeModeScreen> screen = new SpireField<>(() -> null);
	}

	@SpirePatch(clz = MainMenuScreen.class,
			method = SpirePatch.CONSTRUCTOR,
			paramtypez = {boolean.class})
	public static class InitializePatch {
		@SpirePostfixPatch
		public static void Postfix(MainMenuScreen m, boolean b) {
			Field.screen.set(m, new ChallengeModeScreen());
		}
	}

	@SpirePatch(clz = MainMenuScreen.class, method = "update")
	public static class UpdatePatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(MainMenuScreen m) {
			if (m.screen == CUSTOM_CHALLENGE) {
				Field.screen.get(m).update();
			}
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				return LineFinder.findInOrder(ctMethodToPatch,
						new MethodCallMatcher(SaveSlotScreen.class, "update"));
			}
		}
	}

	@SpirePatch(clz = MainMenuScreen.class, method = "render")
	public static class Render {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(MainMenuScreen m, SpriteBatch sb) {
			if (m.screen == CUSTOM_CHALLENGE) {
				Field.screen.get(m).render(sb);
			}
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				return LineFinder.findInOrder(ctMethodToPatch,
						new MethodCallMatcher(SaveSlotScreen.class, "render"));
			}
		}
	}

	@SpirePatch(clz = MainMenuScreen.class, method = "updateMenuPanelController")
	public static class UpdateController {
		@SpirePostfixPatch
		public static void Postfix(MainMenuScreen screen) {
			if (!Settings.isControllerMode) {
				return;
			}
			Hitbox hb = screen.panelScreen.panels.get(0).hb;
			if (CInputActionSet.up.isJustPressed()
					|| CInputActionSet.altUp.isJustPressed()) {
				Gdx.input.setCursorPosition((int) screen.panelScreen.panels.get(1).hb.cX,
						Settings.HEIGHT - (int) screen.panelScreen.panels.get(1).hb.cY);
			} else if (CInputActionSet.down.isJustPressed()
					|| CInputActionSet.altDown.isJustPressed()) {
				Gdx.input.setCursorPosition((int) hb.cX,
						Settings.HEIGHT - (int) hb.cY);
			}
		}
	}


}
