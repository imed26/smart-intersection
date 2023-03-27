package StaticFlowSmartIntersection;
public class Main {

    public static void main(String[] args) {
        String [] commande = new String[3];
        String argument ="";
        argument = argument+"C1:SmartIntersection.CareAgent(15,20,5,5)";

        commande [0]="-cp";
        commande [1]="jade.boot";
        commande [2]= argument;
        jade.Boot.main(commande);
    }
}
