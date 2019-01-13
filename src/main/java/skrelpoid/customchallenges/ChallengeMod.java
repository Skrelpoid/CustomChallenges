package skrelpoid.customchallenges;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import basemod.BaseMod;
import basemod.interfaces.PostInitializeSubscriber;
import skrelpoid.customchallenges.api.Challenge;
import skrelpoid.customchallenges.api.StartingDeckChanger;
import skrelpoid.customchallenges.api.StartingRelicChanger;

@SpireInitializer
public class ChallengeMod implements PostInitializeSubscriber {

	public static final Logger logger = LogManager.getLogger(ChallengeMod.class.getName());

	public static final String RESOURCES = "challenge/";
	public static final String UI = "ui/";

	public static Texture MENU_PANEL;
	public static Texture DEFAULT_ICON;

	public static final String PANEL_TITLE = "Challenges";
	public static final String PANEL_DESCRIPTION = "Play a custom challenge run";

	public static List<Challenge> challengesLoaded = new ArrayList<>();
	public static Challenge testChallenge;

	private static ChallengeManager challengeManager;

	public static void initialize() {
		BaseMod.subscribe(new ChallengeMod());
		BaseMod.subscribe(challengeManager = new ChallengeManager());
	}

	public static void startChallenge(Challenge challenge) {
		challengeManager.setCurrentChallenge(challenge);
	}

	public static void registerChallenge(Challenge challenge) {
		challengesLoaded.add(Objects.requireNonNull(challenge, "challenge may not be null"));
	}

	public static boolean unregisterChallenge(String id) {
		Objects.requireNonNull(id, "id may not be null");
		return challengesLoaded.removeIf(c -> c.getId().equals(id));
	}

	@Override
	public void receivePostInitialize() {
		logger.info("======================================");
		logger.info("Custom Challenges was successfully loaded");
		logger.info("======================================");
		
		testChallenge = new Challenge("test", "TEST", "TEST");
		testChallenge.setStartingDeckChanger(new StartingDeckChanger(
				Arrays.asList("Blur", "Blur", "Normality", "Strike_B", "Strike_G", "Strike_R",
						"Defend_B", "Defend_G", "Defend_R", "sagemod:Quackster", "abcdguisduissd", "AscendersBane"),
				true));
		testChallenge.setStartingRelicChanger(
				new StartingRelicChanger(Arrays.asList("Burning Blood",
						"Potion Belt", "sagemod:Red_Beast_Statue", "hsjdudassadkidsa", "Strawberry", "Strawberry"), true));
		logger.info("Created test Challenge");
	}

	public static void loadTextures() {
		MENU_PANEL = ImageMaster.loadImage(RESOURCES + UI + "menuPanel.png");
		DEFAULT_ICON = ImageMaster.loadImage(RESOURCES + UI + "defaultIcon.png");
	}
}
