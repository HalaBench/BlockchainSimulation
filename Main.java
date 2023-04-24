
public class Main {

    public static void main(String[] args) {

        String[] commande = new String[3];
        String argument = "";


        argument = argument + "Agent1:Node(Agent2,Agent3,Agent4,Agent5,Agent6)";
        argument = argument + ";";
        argument = argument + "Agent2:Node(Agent1,Agent3,Agent4,Agent5,Agent6)";
        argument = argument + ";";
        argument = argument + "Agent3:Node(Agent1,Agent2,Agent4,Agent5,Agent6)";
        argument = argument + ";";
        argument = argument + "Agent4:Node(Agent1,Agent2,Agent3,Agent5,Agent6)";
        argument = argument + ";";
        argument = argument + "Agent5:Node(Agent1,Agent2,Agent3,Agent4,Agent6)";
        argument = argument + ";";
        argument = argument + "Agent6:Node(Agent1,Agent2,Agent3,Agent4,Agent5)";

        commande[0] = "-cp";
        commande[1] = "jade.boot";
        commande[2] = argument;


        jade.Boot.main(commande);


    }

}
