package NegociationBasedSmartIntersection;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import java.util.Random;

import static NegociationBasedSmartIntersection.SmartIntersectionMain.carNumber;
import static NegociationBasedSmartIntersection.SmartIntersectionMain.flowCarNumber;
import static NegociationBasedSmartIntersection.SmartIntersectionMain.approachNumberPerLine;
import static NegociationBasedSmartIntersection.SmartIntersectionMain.intersectionLineNumber;


public class FlowSimulationAgent extends Agent {

    Random r = new Random();

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

    public class CommitCarsForApproach1  extends OneShotBehaviour {
        int approach = 1;
        int duration = 10;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 0,53-approach, duration);
            }
        }// action
    } // CommitCarsForApproach1 Class

    public class CommitCarsForApproach2  extends OneShotBehaviour {
        int approach = 2;
        int duration = 20000;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 0,53-approach, duration);
            }
        }// action
    } // CommitCarsForApproach2 Class

    public class CommitCarsForApproach3  extends OneShotBehaviour {
        int approach = 3;
        int duration = 100000;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 0,53-approach, duration);
            }
        }// action
    } // CommitCarsForApproach3 Class

    public class CommitCarsForApproach4  extends OneShotBehaviour {
        int approach = 4;
        int duration = 10;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 52+approach, 0, duration);
            }
        }// action
    } // CommitCarsForApproach4 Class

    public class CommitCarsForApproach5  extends OneShotBehaviour {
        int approach = 5;
        int duration = 20000;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 52+approach, 0, duration);
            }
        }// action
    } // CommitCarsForApproach5 Class

    public class CommitCarsForApproach6  extends OneShotBehaviour {
        int approach = 6;
        int duration = 100000;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 52+approach, 0, duration);
            }
        }// action
    } // CommitCarsForApproach6 Class

    public class CommitCarsForApproach7  extends OneShotBehaviour {
        int approach = 7;
        int duration = 10;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 108, 49+approach, duration);
            }
        }// action
    } // CommitCarsForApproach7 Class

    public class CommitCarsForApproach8  extends OneShotBehaviour {
        int approach = 8;
        int duration = 20000;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 108, 49+approach, duration);
            }
        }// action
    } // CommitCarsForApproach8 Class

    public class CommitCarsForApproach9  extends OneShotBehaviour {
        int approach = 9;
        int duration = 100000;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 108, 49+approach, duration);
            }
        }// action
    } // CommitCarsForApproach9 Class

    public class CommitCarsForApproach10  extends OneShotBehaviour {
        int approach = 10;
        int duration = 10;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 62-approach, 108, duration);
            }
        }// action
    } // CommitCarsForApproach10 Class

    public class CommitCarsForApproach11  extends OneShotBehaviour {
        int approach = 11;
        int duration = 20000;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 62-approach, 108, duration);
            }
        }// action
    } // CommitCarsForApproach11 Class

    public class CommitCarsForApproach12  extends OneShotBehaviour {
        int approach = 12;
        int duration = 100000;
        @Override
        public void action() {
            for (int j = 0; j < flowCarNumber; j++) {
                commitAgent(approach, j, 62-approach, 108, duration);
            }
        }// action
    } // CommitCarsForApproach12 Class

    public void commitAgent(int i, int j, int x, int y, int duration) {
        System.out.println("I m the " + getLocalName() + ", I commit the car C" + i + "-" + j);
        ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
        message.addReceiver(new AID("C" + i + "-" + j, AID.ISLOCALNAME));
        message.setContent("Commit#" + x + "#" +  y + "#" +  i + "#" + j + "#" + nextTime(duration, flowCarNumber));
        send(message);
    }

    public int nextTime(int globalDuration, int flowCarNumber) {
        Double lambda = (1.0 / (globalDuration/flowCarNumber));
        return ((int) Math.round(-Math.log(1.0 - r.nextDouble()) / lambda));
    }

    public void setup() {
        int approachNumber = (intersectionLineNumber * approachNumberPerLine);

        System.out.println("I'm the FlowSimulationAgent:" + getLocalName());
        Object[] args = getArguments();

            // Add the behaviour
            ParallelBehaviour CommitCars = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
            CommitCarsForApproach1  CarsForApproach1 = new CommitCarsForApproach1();
            CommitCarsForApproach2  CarsForApproach2 = new CommitCarsForApproach2();
            CommitCarsForApproach3  CarsForApproach3 = new CommitCarsForApproach3();
            CommitCarsForApproach4  CarsForApproach4 = new CommitCarsForApproach4();
            CommitCarsForApproach5  CarsForApproach5 = new CommitCarsForApproach5();
            CommitCarsForApproach6  CarsForApproach6 = new CommitCarsForApproach6();
            CommitCarsForApproach7  CarsForApproach7 = new CommitCarsForApproach7();
            CommitCarsForApproach8  CarsForApproach8 = new CommitCarsForApproach8();
            CommitCarsForApproach9  CarsForApproach9 = new CommitCarsForApproach9();
            CommitCarsForApproach10  CarsForApproach10 = new CommitCarsForApproach10();
            CommitCarsForApproach11  CarsForApproach11 = new CommitCarsForApproach11();
            CommitCarsForApproach12  CarsForApproach12 = new CommitCarsForApproach12();

            CommitCars.addSubBehaviour(CarsForApproach1);
            CommitCars.addSubBehaviour(CarsForApproach2);
            CommitCars.addSubBehaviour(CarsForApproach3);
            CommitCars.addSubBehaviour(CarsForApproach4);
            CommitCars.addSubBehaviour(CarsForApproach5);
            CommitCars.addSubBehaviour(CarsForApproach6);
            CommitCars.addSubBehaviour(CarsForApproach7);
            CommitCars.addSubBehaviour(CarsForApproach8);
            CommitCars.addSubBehaviour(CarsForApproach8);
            CommitCars.addSubBehaviour(CarsForApproach9);
            CommitCars.addSubBehaviour(CarsForApproach10);
            CommitCars.addSubBehaviour(CarsForApproach11);
            CommitCars.addSubBehaviour(CarsForApproach12);

            SequentialBehaviour FlowSimulationAgentBehaviour = new SequentialBehaviour();
            FlowSimulationAgentBehaviour.addSubBehaviour(new WaitAllCarsAdmitted());
            FlowSimulationAgentBehaviour.addSubBehaviour(CommitCars);
            addBehaviour(FlowSimulationAgentBehaviour);
    }
}
