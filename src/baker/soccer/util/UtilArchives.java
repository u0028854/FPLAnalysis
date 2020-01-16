package baker.soccer.util;

import ch.ethz.ssh2.StreamGobbler;

public class UtilArchives {
	public static int executeWGETCmd(String wgetCMD) throws Exception{
		String[] cmd = {"cmd.exe", "/C", wgetCMD};
		Runtime rt = Runtime.getRuntime();
		Process proc = rt.exec(cmd);

		StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream());
		StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream());

		int retVal = proc.waitFor();

		errorGobbler.close();
		outputGobbler.close();

		return(retVal);
	}
}
