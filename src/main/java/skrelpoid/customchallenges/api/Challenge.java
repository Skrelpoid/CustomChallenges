package skrelpoid.customchallenges.api;

import java.util.Objects;
import com.badlogic.gdx.graphics.Texture;
import skrelpoid.customchallenges.ChallengeMod;

/**
 * A Challenge is what is actually being loaded and executed when the player starts a challenge run.
 * This class holds all the relevant information needed to control what the challenge is.
 */
public class Challenge {

	private final String id;
	private final String name;
	private final String description;
	private final Texture image;
	private StartingDeckChanger startingDeckChanger;
	private StartingRelicChanger startingRelicChanger;

	/**
	 * Creates a new Challenge with the specified id, name and description. This does not make the
	 * challenge playable yet! To configure it, use the different setter methods. When you're
	 * finished, add the Challenge to the game.
	 * 
	 * @param id the id which should uniquely identify the challenge. (Never displayed to the
	 *        player)
	 * @param name the name that gets displayed to the player
	 * @param description the description that gets displayed to the player
	 */
	public Challenge(String id, String name, String description) {
		this(id, name, description, null);
	}

	/**
	 * Creates a new Challenge with the specified id, name and description. This does not make the
	 * challenge playable yet! To configure it, use the different setter methods. When you're
	 * finished, add the Challenge to the game.
	 * 
	 * @param id the id which should uniquely identify the challenge. (Never displayed to the
	 *        player) [NOT NULL]
	 * @param name the name that gets displayed to the player [NOT NULL]
	 * @param description the description that gets displayed to the player when he clicks on the
	 *        challenge [NOT NULL]
	 * @param image the image that gets displayed to the player as the icon of this challenge. it
	 *        should be 200x200 px. NULL means to just take the default image
	 */
	public Challenge(String id, String name, String description, Texture image) {
		this.id = Objects.requireNonNull(id, "id must not be null");
		this.name = Objects.requireNonNull(name, "name must not be null");
		this.description = Objects.requireNonNull(description, "description must not be null");
		this.image = image == null ? ChallengeMod.DEFAULT_ICON : image;
	}

	/**
	 * Get the StartingDeckChanger, which is responsible for changing the starting deck for
	 * challenge runs.
	 * 
	 * @return the StartingDeckChanger of this Challenge
	 */
	public StartingDeckChanger getStartingDeckChanger() {
		return startingDeckChanger;
	}

	/**
	 * Set the StartingDeckChanger, which is responsible for changing the starting deck for
	 * challenge runs. You can remove the starting deck of the player and/or give the player a
	 * specific starting deck with a StartingDeckChanger
	 * 
	 * @param startingDeckChanger the StartingDeckChanger this Challenge should use. NULL means that
	 *        this challenge doesn't change the starting deck in any way
	 */
	public void setStartingDeckChanger(StartingDeckChanger startingDeckChanger) {
		this.startingDeckChanger = startingDeckChanger;
	}

	/**
	 * Get the StartingRelicChanger, which is responsible for changing the starting relic for
	 * challenge runs.
	 * 
	 * @return the StartingRelicChanger of this Challenge
	 */
	public StartingRelicChanger getStartingRelicChanger() {
		return startingRelicChanger;
	}

	/**
	 * Set the StartingRelicChanger, which is responsible for changing the starting relic for
	 * challenge runs. You can remove the starting relic of the player and/or give the player a
	 * specific starting relic with a StartingRelicChanger
	 * 
	 * @param startingRelicChanger the StartingRelicChanger this Challenge should use. NULL means
	 *        that this challenge doesn't change the starting relic in any way
	 */
	public void setStartingRelicChanger(StartingRelicChanger startingRelicChanger) {
		this.startingRelicChanger = startingRelicChanger;
	}

	/**
	 * Get the id of this challenge. The id uniquely identifies the challenge and will not be
	 * displayed to the player
	 * 
	 * @return the id of this challenge
	 */
	public String getId() {
		return id;
	}

	/**
	 * Get the name of this challenge. The name is what gets displayed directly next to the image of
	 * this challenge. This gets displayed to the player in the screen with all challenges
	 * 
	 * @return the name of this challenge
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the description of this challenge. The description is what gets displayed when the player
	 * selects your challenge. This gets displayed to the player in the screen with all challenges
	 * 
	 * @return the description of this challenge
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Get the image of this challenge. The image is what gets displayed directly next to the name of
	 * this challenge. This gets displayed to the player in the screen with all challenges
	 * 
	 * @return the image of this challenge
	 */
	public Texture getImage() {
		return image;
	}

}
