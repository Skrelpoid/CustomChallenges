package skrelpoid.customchallenges.api;

import java.util.List;

public class StartingRelicChanger {
	
	private final List<String> relics;
	private final boolean removeDefault;
	
	public StartingRelicChanger(List<String> relics, boolean removeDefault) {
		this.relics = relics;
		this.removeDefault = removeDefault;
	}

	public List<String> getRelics() {
		return relics;
	}

	public boolean shouldRemoveDefault() {
		return removeDefault;
	}

}
