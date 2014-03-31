package streaming_server.server;



import java.io.File;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

public class Servidor  {

	public static String vlcPath = "C:\\Program Files\\VideoLAN\\VLC";
	public static void main(String[] args) throws Exception {

		File videos = new File("./temp");
		File[] files = videos.listFiles();
		String ip = "230.0.0.1";
		int puerto = 5555;
		
		NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(),vlcPath);
		MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(args);
		
		for(int i = 0; i < files.length; i++)
		{
			File temp = files[i];
			String[] x = temp.getAbsolutePath().split("temp");
			String media = x[1];			
			media = ".\\temp"+media;
			String options = formatRtpStream(ip, puerto+i);
			System.out.println("Streaming '" + media + "' to '" + options + "'");
			HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
			mediaPlayer.playMedia(media, ":sout=#rtp{dst=230.0.0.1,port="+(puerto+i)+",mux=ts}",":no-sout-rtp-sap", ":no-sout-standard-sap", ":sout-all", ":sout-keep");
		}

	
		// Don't exit
		Thread.currentThread().join();
	}

	private static String formatRtpStream(String serverAddress, int serverPort) {
		StringBuilder sb = new StringBuilder(60);
		sb.append(":sout=#rtp{dst=");
		sb.append(serverAddress);
		sb.append(",port=");
		sb.append(serverPort);
		sb.append(",mux=ts}");
		return sb.toString();
	}
}
