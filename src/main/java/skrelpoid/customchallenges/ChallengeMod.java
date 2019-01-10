package skrelpoid.customchallenges;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import basemod.interfaces.PostInitializeSubscriber;

@SpireInitializer
public class ChallengeMod implements PostInitializeSubscriber {
	
	public static final Logger logger = LogManager.getLogger(ChallengeMod.class.getName());
	
	public static void initialize() {
		
	}

	@Override
	public void receivePostInitialize() {
		logger.info("======================================");
		logger.info("Custom Challenges was successfully loaded");
		logger.info("======================================");
	}
}
