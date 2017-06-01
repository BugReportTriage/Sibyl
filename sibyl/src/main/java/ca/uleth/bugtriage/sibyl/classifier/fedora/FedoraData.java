package ca.uleth.bugtriage.sibyl.classifier.fedora;

import ca.uleth.bugtriage.sibyl.utils.Environment;

public class FedoraData {
	public final static String FEDORA_DIR = Environment.getBugDataDir() + "fedora/";
	public static final String DUPLICATES = FEDORA_DIR + "fedoraDups.bugs";
}
