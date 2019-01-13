package skrelpoid.customchallenges;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import basemod.interfaces.PostCreateStartingDeckSubscriber;
import basemod.interfaces.PostCreateStartingRelicsSubscriber;
import skrelpoid.customchallenges.api.Challenge;

public class ChallengeManager
		implements PostCreateStartingDeckSubscriber, PostCreateStartingRelicsSubscriber {

	private static final Logger logger = LogManager.getLogger(ChallengeManager.class);

	private Challenge currentChallenge;

	public ChallengeManager() {}

	@Override
	public void receivePostCreateStartingDeck(PlayerClass chosenClass, CardGroup addCardsToMe) {
		if (currentChallenge != null && currentChallenge.getStartingDeckChanger() != null) {
			if (currentChallenge.getStartingDeckChanger().shouldRemoveDefault()) {
				addCardsToMe.group.clear();
				logger.info("Challenge with id " + currentChallenge.getId()
						+ " cleared the starting deck");
			}
			if (currentChallenge.getStartingDeckChanger().getCards() != null) {
				for (String str : currentChallenge.getStartingDeckChanger().getCards()) {
					AbstractCard c = CardLibrary.getCard(str);
					if (c == null || !c.cardID.equals(str)) {
						logger.warn("Challenge with id " + currentChallenge.getId()
								+ " tried to add card " + str
								+ " which does not exist or could not be loaded from the CardLibrary!");
					} else {
						logger.info("Challenge with id " + currentChallenge.getId() + " added card "
								+ str + " to the starting deck");
						addCardsToMe.group.add(c.makeCopy());
					}
				}
			}
		}
	}

	@Override
	public void receivePostCreateStartingRelics(PlayerClass chosenClass,
			ArrayList<String> addRelicsToMe) {
		if (currentChallenge != null && currentChallenge.getStartingRelicChanger() != null) {
			if (currentChallenge.getStartingRelicChanger().shouldRemoveDefault()) {
				addRelicsToMe.clear();
				logger.info("Challenge with id " + currentChallenge.getId()
						+ " cleared the starting relics");
			}
			if (currentChallenge.getStartingRelicChanger().getRelics() != null) {
				for (String str : currentChallenge.getStartingRelicChanger().getRelics()) {
					logger.info("Challenge with id " + currentChallenge.getId() + " added relic "
							+ str + " to the starting relics");
					addRelicsToMe.add(str);
				}
			}
		}
	}

	public Challenge getCurrentChallenge() {
		return currentChallenge;
	}

	public void setCurrentChallenge(Challenge currentChallenge) {
		this.currentChallenge = currentChallenge;
	}

}
