package audio;

/**
 * Contient tous les fichiers audio et permet de les jouer
 */
public class Audio {


    /*--------------Jingles------------*/

    public static SoundPlayer scoreJingle;

    public static SoundPlayer selectionJingle;


    /*--------------Musique---------------*/

    private static String path = "src/Son/";

    public Audio(){

    }

    /**
     * Initialise les sons
     */
    public static void initAudio(){
        initJingles();
        initMusic();
    }

    /**
     * Initialise les jingle (no loop)
     */
    private static void initJingles(){
        scoreJingle = new SoundPlayer(path+"control_point.mp3");
        selectionJingle = new SoundPlayer(path+"menu_selection.mp3");
    }

    /**
     * Initialise la musique (loop)
     */
    private static void initMusic(){

    }

}
