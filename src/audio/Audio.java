package audio;

/**
 * Contient tous les fichiers audio et permet de les jouer
 */
public class Audio {


    /*--------------Jingles------------*/

    /**Le jungle correspondant au gaint de points pour le score**/
    public static SoundPlayer jingleScore;

    /**Le jungle correspondant au son du menu lors du deplacement vers une selection**/
    public static SoundPlayer jingleScrolling;

    /**Le jungle correspondant au son du menu lorsquun choix est selectionne**/
    public static SoundPlayer jingleSelection;

    /**Le jingle correspondant au bruit lors d une collision**/
    public static SoundPlayer jingleCollision;

    /**Le jingle correspondant au bruit lorsque un concurrent est depasse**/
    public static SoundPlayer jingleOvertakeCar;


    /*--------------Musique---------------*/

    /**La musique jouee lors d une partie**/
    public static SoundPlayer musicInGame;

    /**La musique jouee lorsque la partie est mise en pause**/
    public static SoundPlayer musicPause;

    /**La musique jouee dans le menu**/
    public static SoundPlayer musicMenu;


    /*-------------Path------------------*/

    private static String path = "src/Son/";


    /*------------------Init-----------------*/

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
        jingleScrolling = new SoundPlayer(path+"menu_selection.mp3");
        jingleSelection = new SoundPlayer(path+"menu_select.mp3");
        jingleCollision = new SoundPlayer(path+"collision.mp3");
        jingleOvertakeCar = new SoundPlayer(path+"pass_car.mp3");
    }

    /**
     * Initialise la musique (loop)
     */
    private static void initMusic(){
        musicInGame = new SoundPlayer(path+"race_music.mp3", true);
        musicMenu = new SoundPlayer(path+"menu_music.mp3", true);
        musicPause = new SoundPlayer(path+"pause_music.mp3", true);
    }

}
