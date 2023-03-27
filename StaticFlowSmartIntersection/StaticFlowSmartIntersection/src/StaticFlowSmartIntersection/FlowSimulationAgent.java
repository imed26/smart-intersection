package StaticFlowSmartIntersection;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

import static StaticFlowSmartIntersection.SmartIntersectionMain.carNumber;
import static StaticFlowSmartIntersection.SmartIntersectionMain.flowCarNumber;
import static StaticFlowSmartIntersection.SmartIntersectionMain.approachNumberPerLine;
import static StaticFlowSmartIntersection.SmartIntersectionMain.intersectionLineNumber;

public class FlowSimulationAgent extends Agent {

    public class WaitAllCarsAdmitted extends Behaviour {

        @Override
        public void action() {
            /*
             TODO Auto-generated method setup
            System.out.println(
                   "I'm  the FlowSimulationAgent: " + getLocalName() + " I'm waiting all cars to be admitted");
            */
            block(100);
            if (SmartIntersectionMain.commitmentNumber.get() == carNumber) System.out.println("All cars are admitted");
        }
        public boolean done () {
            return (SmartIntersectionMain.commitmentNumber.get() == carNumber);
            //done
        }
        /* WaitState Class */
    }
    public class CommitCars extends OneShotBehaviour {

        @Override
        public void action() {
            // int flowCarNumber = (carNumber / 36) ; // Number of cars in each approach
            int approachNumber = (intersectionLineNumber * approachNumberPerLine);
            for (int i = 1; i < (approachNumber+1); i++) {
                for (int j = 0; j < flowCarNumber; j++) {
                    if (i==1 || i==2 || i==3) commitAgent(i, j, 0,53-i);
                    else if (i==4 || i==5 || i==6) commitAgent(i, j, 52+i, 0);
                    else if (i==7 || i==8 || i==9) commitAgent(i, j, 108, 49+i);
                    else if (i==10 || i==11 || i==12) commitAgent(i, j, 62-i, 108);
                    }
                }
            }// action
        } // CommitCars Class

        private void commitAgent(int i, int j, int x, int y) {
            ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
            message.addReceiver(new AID("C" + i + "-" + j, AID.ISLOCALNAME));
            message.setContent("Commit#" + x + "#" +  y + "#" +  i);
            send(message);
            System.out.println("I m the " + getLocalName() + ", I commit the car C" + i + "-" + j);
        }

    public void setup() {
        System.out.println("I'm the FlowSimulationAgent:" + getLocalName());
        Object[] args = getArguments();
            // Add the behaviour
            SequentialBehaviour FlowSimulationAgentBehaviour = new SequentialBehaviour();
            FlowSimulationAgentBehaviour.addSubBehaviour(new WaitAllCarsAdmitted());
            FlowSimulationAgentBehaviour.addSubBehaviour(new CommitCars());
            addBehaviour(FlowSimulationAgentBehaviour);
    }
}
