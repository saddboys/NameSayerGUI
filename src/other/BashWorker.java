package other;

import javax.swing.SwingWorker;

public class BashWorker extends SwingWorker<Void, Void>{

	private String _cmd;
	
	//swingworker to work with bash
	public BashWorker(String string) {
		_cmd = string;
	}
	
	@Override
	protected Void doInBackground() {
		try {
			ProcessBuilder builder = new ProcessBuilder("bash", "-c", _cmd);
			Process process = builder.start();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
