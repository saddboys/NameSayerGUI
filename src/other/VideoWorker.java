package other;

//a worker used to make the creation
public class VideoWorker extends BashWorker{

	private String _creationName;
	
	
	public VideoWorker(String string, String creationName) {
		super(string);
		_creationName = creationName;
	}
	
	@Override
	protected void done() {

		String combinedVidcmd = "ffmpeg -i ./temp/tempVid.mp4 -i ./temp/tempAud.wav -map 0:v -map 1:a \"./creations/"+_creationName+"\".mkv &> /dev/null";
		new BashWorker(combinedVidcmd){}.execute();
		super.done();
	}
}
