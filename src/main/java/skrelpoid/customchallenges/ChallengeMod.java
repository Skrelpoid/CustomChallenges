package skrelpoid.customchallenges;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;

@SpireInitializer
public class ChallengeMod implements PostInitializeSubscriber {

	public static final Logger logger = LogManager.getLogger(ChallengeMod.class.getName());

	public static final String RESOURCES = "challenge/";
	public static final String UI = "ui/";

	public static Texture MENU_PANEL;
	public static Texture DEFAULT_ICON;

	public static final String PANEL_TITLE = "Challenges";
	public static final String PANEL_DESCRIPTION = "Play a custom challenge run";

	public static void initialize() {
		BaseMod.subscribe(new ChallengeMod());
	}

	@Override
	public void receivePostInitialize() {
		logger.info("======================================");
		logger.info("Custom Challenges was successfully loaded");
		logger.info("======================================");
	}

	public static void loadTextures() {
		MENU_PANEL = ImageMaster.loadImage(RESOURCES + UI + "menuPanel.png");
		DEFAULT_ICON = ImageMaster.loadImage(RESOURCES + UI + "defaultIcon.png");
	}
}
