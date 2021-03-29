package audio;

/**
 * Contient tous les fichiers audio et permet de les jouer
 */
public class Audio {


    /*--------------Jingles------------*/

    public static SoundPlayer jingleScore;

    public static SoundPlayer jingleSelection;


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
        jingleScore = new SoundPlayer(path+"control_point.mp3");
        jingleSelection = new SoundPlayer(path+"menu_selection.mp3");
    }

    /**
     * Initialise la musique (loop)
     */
    private static void initMusic(){

    }

}
