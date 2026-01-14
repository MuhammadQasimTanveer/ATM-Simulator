package ASimulatorSystem;

//Abstract class
public abstract class BaseScreen {
    protected String title;

    public BaseScreen(String title) {
        this.title = title;
    }

    //Concrete method
    public void showTitle() {
        System.out.println("                                                *** " + title + " ***\n\n");
    }

    //abstract method
    public abstract void run();
}