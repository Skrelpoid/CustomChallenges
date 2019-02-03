package skrelpoid.customchallenges.ui;

import java.util.Objects;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import skrelpoid.customchallenges.api.Challenge;

/**
 * @see CharacterOption
 */
public class ChallengeButton {
	public static final int WIDTH = 600;
	public static final int HEIGHT = 200;
	public static final int TEXT_OFFSET = 230;
	public static final int X_OFFSET = 300;
	public static final int LINE_SPACING = 32;


	private Challenge challenge;
	private Hitbox hb;
	private float baseY;

	public ChallengeButton(Challenge challenge) {
		this.challenge = Objects.requireNonNull(challenge, "challenge must not be null");
		hb = new Hitbox(X_OFFSET * Settings.scale, 0, WIDTH * Settings.scale,
				HEIGHT * Settings.scale);
	}

	public void move(float x, float y) {
		this.hb.move(x, y);
	}

	public void setBaseY(float cY) {
		this.baseY = cY;
	}

	public Hitbox getHb() {
		return hb;
	}

	public void render(SpriteBatch sb) {
		sb.draw(ImageMaster.CHAR_OPT_HIGHLIGHT, this.hb.x + (HEIGHT * Settings.scale - HEIGHT) / 2,
				this.hb.cY - HEIGHT / 2.0f, HEIGHT / 2.0f, HEIGHT / 2.0f, HEIGHT,
				HEIGHT, Settings.scale, Settings.scale, 0.0f, 0, 0, HEIGHT, HEIGHT, false, false);
		sb.draw(challenge.getImage(), this.hb.x + (HEIGHT * Settings.scale - HEIGHT) / 2,
				this.hb.cY - HEIGHT / 2.0f, HEIGHT / 2.0f, HEIGHT / 2.0f, HEIGHT,
				HEIGHT, Settings.scale, Settings.scale, 0.0f, 0, 0, HEIGHT, HEIGHT, false, false);
		renderTitle(sb);
		hb.render(sb);
	}

	private void renderTitle(SpriteBatch sb) {
		FontHelper.renderSmartText(sb, FontHelper.charTitleFont, challenge.getName(),
				hb.x + TEXT_OFFSET * Settings.scale,
				hb.cY + LINE_SPACING / 2 * Settings.scale, WIDTH - TEXT_OFFSET,
				LINE_SPACING * Settings.scale,
				Settings.CREAM_COLOR);
	}

	public void update(float scrollY) {
		move(hb.cX, baseY + scrollY);
	}

}
