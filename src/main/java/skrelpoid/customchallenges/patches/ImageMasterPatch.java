package skrelpoid.customchallenges.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import skrelpoid.customchallenges.ChallengeMod;

@SpirePatch(clz = ImageMaster.class, method = "initialize")
public class ImageMasterPatch {
	@SpirePrefixPatch
	public static void Prefix() {
		ChallengeMod.loadTextures();
	}
}
