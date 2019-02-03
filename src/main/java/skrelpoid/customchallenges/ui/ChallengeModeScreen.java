package skrelpoid.customchallenges.ui;

import static skrelpoid.customchallenges.ChallengeMod.challengesLoaded;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.SeedHelper;
import com.megacrit.cardcrawl.helpers.TrialHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.controller.CInputHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.custom.CustomModeScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MenuCancelButton;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBar;
import com.megacrit.cardcrawl.screens.mainMenu.ScrollBarListener;
import com.megacrit.cardcrawl.ui.buttons.GridSelectConfirmButton;
import skrelpoid.customchallenges.ChallengeMod;
import skrelpoid.customchallenges.patches.PatchesForChallengeModeScreen;

/**
 * @see CharacterSelectScreen
 * @see CustomModeScreen
 */
public class ChallengeModeScreen implements ScrollBarListener {
	public static final Logger logger = LogManager.getLogger(ChallengeModeScreen.class.getName());

	public static final String[] TEXT = {"Challenges"};
	public static final int OFFSET_Y = 600;
	private MenuCancelButton cancelButton = new MenuCancelButton();
	public GridSelectConfirmButton confirmButton =
			new GridSelectConfirmButton(CharacterSelectScreen.TEXT[1]);
	private Hitbox controllerHb;
	public static boolean finalActAvailable = false;
	public int ascensionLevel = 0;
	public boolean screenUp = false;
	private static final float SHOW_X = 300.0f * Settings.scale;;
	private float screenX = SHOW_X;
	private boolean grabbedScreen = false;
	private float grabStartY = 0.0f;
	private float targetY = 0.0f;
	private float scrollY = 0.0f;
	private float scrollLowerBound;
	private float scrollUpperBound;
	private ScrollBar scrollBar;
	private List<ChallengeButton> challenges;

	public ChallengeModeScreen() {
		challenges = challengesLoaded.stream().map(ChallengeButton::new).collect(Collectors.toList());
		int index = 0;
		for (ChallengeButton b : challenges) {
			b.setBaseY(OFFSET_Y * Settings.scale - index * ChallengeButton.HEIGHT * Settings.scale);
			index++;
		}
		this.calculateScrollBounds(challenges.size());
		this.scrollBar = new ScrollBar(this,
				(float) Settings.WIDTH - (ChallengeButton.X_OFFSET + ChallengeButton.WIDTH) * Settings.scale - ScrollBar.TRACK_W / 2.0f,
				(float) Settings.HEIGHT / 2.0f, (float) Settings.HEIGHT - 256.0f * Settings.scale);
		logger.info("Initialized ChallengeModeScreen");
	}

	public void open() {
		logger.info("Opened ChallengeModeScreen");
		this.confirmButton.show();
		this.controllerHb = null;
		this.targetY = 0.0f;
		this.screenUp = true;
		Settings.isEndless = false;
		Settings.seedSet = false;
		Settings.seed = null;
		Settings.specialSeed = null;
		Settings.isTrial = false;
		CardCrawlGame.trial = null;
		CardCrawlGame.mainMenuScreen.screen = PatchesForChallengeModeScreen.CUSTOM_CHALLENGE;
		CardCrawlGame.mainMenuScreen.darken();
		this.cancelButton.show(CharacterSelectScreen.TEXT[5]);
		this.confirmButton.isDisabled = false;
	}

	public void update() {
		this.updateControllerInput();
		if (Settings.isControllerMode && this.controllerHb != null) {
			if ((float) Gdx.input.getY() > (float) Settings.HEIGHT * 0.75f) {
				this.targetY += Settings.SCROLL_SPEED;
			} else if ((float) Gdx.input.getY() < (float) Settings.HEIGHT * 0.25f) {
				this.targetY -= Settings.SCROLL_SPEED;
			}
		}
		boolean isDraggingScrollBar = this.scrollBar.update();
		if (!isDraggingScrollBar) {
			this.updateScrolling();
		}
		updateChallenges();
		this.updateEmbarkButton();
		this.updateCancelButton();
		if (Settings.isControllerMode && this.controllerHb != null) {
			CInputHelper.setCursor(this.controllerHb);
		}
	}

	private void updateCancelButton() {
		this.cancelButton.update();
		if (this.cancelButton.hb.clicked || InputHelper.pressedEscape) {
			logger.info("Clicked Cancel ChallengeModeScreen");
			InputHelper.pressedEscape = false;
			this.cancelButton.hb.clicked = false;
			this.cancelButton.hide();
			CardCrawlGame.mainMenuScreen.panelScreen.refresh();
		}
	}

	private void updateEmbarkButton() {
		this.confirmButton.update();
		if (this.confirmButton.hb.clicked || CInputActionSet.proceed.isJustPressed()) {
			logger.info("Clicked Confirm ChallengeModeScreen");
			CardCrawlGame.chosenCharacter = PlayerClass.IRONCLAD;
			ChallengeMod.startChallenge(ChallengeMod.testChallenge);
			this.confirmButton.hb.clicked = false;
			this.confirmButton.isDisabled = true;
			this.confirmButton.hide();
			if (Settings.seed == null) {
				this.setRandomSeed();
			} else {
				Settings.seedSet = true;
			}
			CardCrawlGame.mainMenuScreen.isFadingOut = true;
			CardCrawlGame.mainMenuScreen.fadeOutMusic();
			Settings.isDailyRun = false;
			boolean isTrialSeed = TrialHelper.isTrialSeed(SeedHelper.getString(Settings.seed));
			if (isTrialSeed) {
				Settings.specialSeed = Settings.seed;
				long sourceTime = System.nanoTime();
				Random rng = new Random(sourceTime);
				Settings.seed = SeedHelper.generateUnoffensiveSeed(rng);
				Settings.isTrial = true;
			}
			ModHelper.setModsFalse();
			AbstractDungeon.generateSeeds();
			AbstractDungeon.isAscensionMode = false;
			AbstractDungeon.ascensionLevel = 0;
		}
	}

	private void setRandomSeed() {
		long sourceTime = System.nanoTime();
		Random rng = new Random(sourceTime);
		Settings.seedSourceTimestamp = sourceTime;
		Settings.seed = SeedHelper.generateUnoffensiveSeed(rng);
	}

	public void render(SpriteBatch sb) {
		this.renderScreen(sb);
		this.scrollBar.render(sb);
		this.cancelButton.render(sb);
		this.confirmButton.render(sb);
		renderChallenges(sb);
	}

	public void renderScreen(SpriteBatch sb) {
		this.renderTitle(sb, TEXT[0], this.scrollY - 50.0f * Settings.scale);
	}

	private void renderTitle(SpriteBatch sb, String text, float y) {
		FontHelper.renderSmartText(sb, FontHelper.charTitleFont, text, this.screenX,
				y + 900.0f * Settings.scale, 9999.0f, 32.0f * Settings.scale, Settings.GOLD_COLOR);
	}
	
	private void updateChallenges() {
		for (ChallengeButton b : challenges) {
			b.update(scrollY);
		}
	}

	private void renderChallenges(SpriteBatch sb) {
		for (ChallengeButton b : challenges) {
			b.render(sb);
		}
	}

	private void updateControllerInput() {
		if (!Settings.isControllerMode) {
			return;
		}
	}

	private void updateScrolling() {
		int y = InputHelper.mY;
		if (this.scrollUpperBound > 0.0f) {
			if (!this.grabbedScreen) {
				if (InputHelper.scrolledDown) {
					this.targetY += Settings.SCROLL_SPEED;
				} else if (InputHelper.scrolledUp) {
					this.targetY -= Settings.SCROLL_SPEED;
				}
				if (InputHelper.justClickedLeft) {
					this.grabbedScreen = true;
					this.grabStartY = (float) y - this.targetY;
				}
			} else if (InputHelper.isMouseDown) {
				this.targetY = (float) y - this.grabStartY;
			} else {
				this.grabbedScreen = false;
			}
		}
		this.scrollY = MathHelper.scrollSnapLerpSpeed(this.scrollY, this.targetY);
		if (this.targetY < this.scrollLowerBound) {
			this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollLowerBound);
		} else if (this.targetY > this.scrollUpperBound) {
			this.targetY = MathHelper.scrollSnapLerpSpeed(this.targetY, this.scrollUpperBound);
		}
		this.updateBarPosition();
	}

	private void calculateScrollBounds(int rows) {
		this.scrollUpperBound =
				(float) rows * ChallengeButton.HEIGHT * Settings.scale - OFFSET_Y * Settings.scale ;
		this.scrollLowerBound = 0;
	}

	@Override
	public void scrolledUsingBar(float newPercent) {
		float newPosition;
		this.scrollY = newPosition = MathHelper.valueFromPercentBetween(this.scrollLowerBound,
				this.scrollUpperBound, newPercent);
		this.targetY = newPosition;
		this.updateBarPosition();
	}

	private void updateBarPosition() {
		float percent = MathHelper.percentFromValueBetween(this.scrollLowerBound,
				this.scrollUpperBound, this.scrollY);
		this.scrollBar.parentScrolledToPercent(percent);
	}

}