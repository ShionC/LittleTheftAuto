package Tools;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class ScrollingStates extends Thread {

    /**Stop the thread**/
    private boolean run = true;
    /**For the pause**/
    private boolean pause = false;

    /**Flag mode**/
    private boolean modeTo0 = false;
    /**Flag mode**/
    private boolean modeLoop = true;

    /**List of the images and the corresponding state**/
    private HashMap<Integer, BufferedImage> listImages = null;
    /**List of the states**/
    private ArrayList<Integer> listState = new ArrayList<>();

    /**Interval of time between 2 states. In milliseconds**/
    private int gap = 40;

    /**The current state**/
    private int currentState =0;

    /**
     * Create an object which scroll states in a way depending on the mode. There is 2 modes. Images can be associated to a state
     * <br/>Mode loop : (default) scrolling will go to one state to another from the minimal state to the maximal, before looping
     * <br/> Mode to 0 : scrolling will constantly get the current state back to 0 by decreasing or increasing the current state until it reach 0.
     * To use with <i>setCurrentState(int state)</i>
     * <br/><br/>If the mode is modeTo0, at least one of the state must be 0. For better use, make full use of positive and negative numbers.
     * <br/>The interval at which the state change is at 40 milliseconds by default but can be changed with setGap()
     * <br/>The thread must be started by start() and can be stopped by stopRun(). It can also be paused and resumed
     * @param listState_Images The list of the states and their associated Images. The states must be consecutive and are considered to be the key in the HashMap
     */
    public ScrollingStates(HashMap<Integer, BufferedImage> listState_Images){
        setImages(listState_Images);
    }

    /**
     * Create an object which scroll states in a way depending on the mode. There is 2 modes. Images can be associated to a state
     * <br/>Mode loop : (default) scrolling will go to one state to another from the minimal state to the maximal, before looping
     * <br/> Mode to 0 : scrolling will constantly get the current state back to 0 by decreasing or increasing the current state until it reach 0.
     * To use with <i>setCurrentState(int state)</i>
     * <br/><br/>If the mode is modeTo0, at least one of the state must be 0. For better use, make full use of positive and negative numbers.
     * <br/>The list of images is to null by default. Using <i>getCurrentImage()</i> will raise an exception
     * <br/>The interval at which the state change is at 40 milliseconds by default but can be changed with setGap()
     * <br/>The thread must be started by start() and can be stopped by stopRun(). It can also be paused and resumed
     * @param listState The list of the states. The states must be consecutive
     */
    public ScrollingStates(ArrayList<Integer> listState){
        this.listState = listState;
    }

    /**
     * Create an object which scroll states in a way depending on the mode. There is 2 modes. Images can be associated to a state
     * <br/><b>Either a list of image or a list of states must be set before starting the thread. These can be set with <i>setImages</i> or <i>setStates</i>.
     * If not done, exceptions will be raised</b>
     * <br/>Mode loop : (default) scrolling will go to one state to another from the minimal state to the maximal, before looping
     * <br/> Mode to 0 : scrolling will constantly get the current state back to 0 by decreasing or increasing the current state until it reach 0.
     * To use with <i>setCurrentState(int state)</i>
     * <br/><br/>If the mode is modeTo0, at least one of the state must be 0. For better use, make full use of positive and negative numbers.
     * <br/>The list of images is to null by default. Using <i>getCurrentImage()</i> will raise an exception
     * <br/>The interval at which the state change is at 40 milliseconds by default but can be changed with setGap()
     * <br/>The thread must be started by start() and can be stopped by stopRun(). It can also be paused and resumed
     */
    public ScrollingStates(){

    }

    /**
     * Stop the run. This operation is definitive
     */
    public void stopRun(){
        this.run = false;
    }

    /**
     * Pause the scrolling of the state
     */
    public void pause(){
        if(! pause){
            pause = true;
        }
    }

    /**
     * Resume the scrolling of the state
     */
    public void resumeScrolling(){
        if(pause){
            pause = false;
        }
    }

    /**
     * Add a list of images and states to the instance. The instance must not be started to do this operation. If not respected, an exception will be raised.
     * @param listState_Images The list of the states and their associated Images. The states must be consecutive and are considered to be the key in the HashMap
     */
    public void setImages(HashMap<Integer, BufferedImage> listState_Images){
        if(! this.isAlive()){
            this.listImages = listState_Images;
            Object[] listState = listState_Images.keySet().toArray();
            for(Object obj : listState){
                this.listState.add((int)obj);
            }
        } else {
            throw new RuntimeException("Cannot add images and states to an instance already started");
        }
    }

    /**
     * Add a list of states to the instance. The instance must not be started to do this operation. If not respected, an exception will be raised.
     * <br/>The list of images is to null by default. Using <i>getCurrentImage()</i> will raise an exception
     * @param listState The list of the states. The states must be consecutive
     */
    public void setStates(ArrayList<Integer> listState){
        if(! this.isAlive()){
            this.listState = listState;
        } else {
            throw new RuntimeException("Cannot add states to an instance already started");
        }
    }

    /**
     * The mode Loop will go to one state to another from the minimal state to the maximal, before looping
     * @return
     */
    public boolean isModeLoop() {
        return modeLoop;
    }

    /**
     * The mode to0 will constantly get the current state back to 0 by decreasing or increasing the current state until it reach 0.
     * </br>It need the current state to be modified externally in order to have a change when currentState reached 0.
     * @return
     */
    public boolean isModeTo0() {
        return modeTo0;
    }

    /**
     * Set the interval at which the state change. By default it is at 40
     * @param gap
     */
    public void setGap(int gap) {
        this.gap = gap;
    }

    /**
     * The state with the smaller number
     * @return
     */
    private int getMinState(){
        if(listState.size() != 0){
            int min = listState.get(0);
            for (Object i: listState) {
                if((int)i<min){
                    min = (int)i;
                }
            }
            return min;
        } else {
            throw new RuntimeException("An empty list cant be scrolled !");
        }
    }

    /**
     * The state with the highest number
     * @return
     */
    private int getMaxState(){
        if(listState.size() != 0){
            int max = listState.get(0);
            for (Object i: listState) {
                if((int)i>max){
                    max = (int)i;
                }
            }
            return max;
        } else {
            throw new RuntimeException("An empty list cant be scrolled !");
        }
    }

    /**
     * Return the current state. Is 0 by default
     * @return
     */
    public int getCurrentState(){
        return this.currentState;
    }

    /**
     * Set the current state to the desired value.
     * <br/>Mainly used during modeTo0
     * @param state
     */
    public void setCurrentState(int state){
        if(listState.size() != 0){
            if(this.listState.contains(state)){
                this.currentState = state;
            } else {
                throw new IllegalArgumentException("This number is not one of the states choices provided !");
            }
        } else {
            throw new RuntimeException("An empty list cant be scrolled !");
        }
    }

    /**
     * Get the BufferedImage corresponding to the current state
     * @return raise an exception if the list of images is null
     */
    public BufferedImage getCurrentImage(){
        if(this.listImages != null){
            return this.listImages.get(this.currentState);
        } else {
            throw new RuntimeException("No image instantiated");
        }
    }

    /**
     * Le list of the states given
     * @return
     */
    public ArrayList<Integer> getListState() {
        return listState;
    }



    /**
     * The list of the Buffered image and their corresponding state
     * @return
     */
    public HashMap<Integer, BufferedImage> getListImages(){
        return this.listImages;
    }

    /**
     * The mode Loop will go to one state to another from the minimal state to the maximal, before looping.
     * <br/>Warning : States must be consecutive !
     */
    public void setModeLoop() {
        this.modeLoop = true;
        this.modeTo0 = false;
    }

    /**
     * The mode to0 will constantly get the current state back to 0 by decreasing or increasing the current state until it reach 0.
     * </br>It need the current state to be modified externally in order to have a change when currentState reached 0.
     * <br/>Warning : States must be consecutive !
     */
    public void setModeTo0(){
        this.modeLoop = false;
        this.modeTo0 = true;
    }

    /**
     * For looping mode
     */
    private void goToNextState(){
        int min = getMinState();
        int max = getMaxState();
        if(this.currentState == max){
            this.currentState = min;
        } else {
            this.currentState++;
        }
    }

    /**
     * For goTo0 mode
     */
    private void goToward0(){
        if(this.listState.size()!=0){
            if(this.currentState<0){
                this.currentState++;
            } else if(this.currentState>0){
                this.currentState--;
            }
        } else {
            throw new RuntimeException("An empty list cant be scrolled !");
        }
    }


    @Override
    public void run() {
        while(run){

            if(! this.pause){

                if(this.modeLoop){
                    goToNextState();
                } else if(this.modeTo0){
                    goToward0();
                }
            }

            try {
                //noinspection BusyWait
                Thread.sleep(this.gap);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
