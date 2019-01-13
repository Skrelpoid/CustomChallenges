package skrelpoid.customchallenges.api;

import java.util.List;

public class StartingDeckChanger {
	
	private final List<String> cards;
	private final boolean removeDefault;

	public StartingDeckChanger(List<String> cards, boolean removeDefault) {
		this.cards = cards;
		this.removeDefault = removeDefault;
	}

	public List<String> getCards() {
		return cards;
	}

	public boolean shouldRemoveDefault() {
		return removeDefault;
	}

}
