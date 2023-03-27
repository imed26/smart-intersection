package NegociationBasedSmartIntersection;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import static NegociationBasedSmartIntersection.SmartIntersectionMain.grid;

public class IntersectionAgent extends Agent {
     boolean [][] theRoadMap;
     int lineTurn = 1;
     int contextDepth = 2;
     int maxToRelease = 0;
     public AtomicInteger carsNumberInIntersectionLine1 = new AtomicInteger(0);

     public AtomicInteger carsNumberInIntersectionLine2 = new AtomicInteger(0);
     public AtomicInteger carsNumberInIntersectionLine3 = new AtomicInteger(0);
     public AtomicInteger carsNumberInIntersectionLine4 = new AtomicInteger(0);
     public AtomicInteger releasedCarsNumber = new AtomicInteger(0);
     int carsNumber = 0;
     boolean requestMovingArrived = false;
     boolean releaseCellArrived = false;
     boolean tryReceivingMessage = true;
     boolean intersectionAccessAllowed = true;
     boolean frontLineReleaseAllowed = false;
     ACLMessage receivedMessage;
     java.util.HashSet<inRoadAgent> delayedElements = new HashSet<inRoadAgent>();
     java.util.HashSet<inRoadAgent> insideIntersectionElements = new HashSet<inRoadAgent>();

     public class inRoadAgent {
          //AID agentIdentifier;
          String agentName;
          int approach;
          int x;
          int y;
          int requestedX;
          int requestedY;
          long clock;

         // public inRoadAgent(AID agentIdentifier, String agentName, int approach, int x, int y, int requestedX, int requestedY, int clock)

          public inRoadAgent(String agentName, int approach, int x, int y, int requestedX, int requestedY, long clock) {
               //this.agentIdentifier = agentIdentifier;
               this.agentName = agentName;
               this.approach = approach;
               this.x = x;
               this.y = y;
               this.requestedX = requestedX;
               this.requestedY = requestedY;
               this.clock = clock;
          }

          public String getAgentName() {
               return agentName;
          }

          public int getApproach() {
               return approach;
          }

          public int getX() {
               return x;
          }

          public int getY() {
               return y;
          }

          public int getRequestedX() {
               return requestedX;
          }

          public int getRequestedY() {
               return requestedY;
          }

          public long getClock() {
               return clock;
          }

          public void setAgentName(String agentName) {
               this.agentName = agentName;
          }

          public void setApproach(int approach) {
               this.approach = approach;
          }

          public void setX(int x) {
               this.x = x;
          }

          public void setY(int y) {
               this.y = y;
          }

          public void setRequestedX(int requestedX) {
               this.requestedX = requestedX;
          }

          public void setRequestedY(int requestedY) {
               this.requestedY = requestedY;
          }

          public void setClock(long clock) {
               this.clock = clock;
          }

          private void notifyAcceptMoving() {
               theRoadMap[this.getRequestedX()][this.getRequestedY()] = true;
               ACLMessage message = new ACLMessage(ACLMessage.INFORM);
               message.addReceiver(new AID(this.agentName, AID.ISLOCALNAME));
               message.setContent("AcceptMoving#" + this.getRequestedX() + "#" + this.getRequestedY());
               send(message);
          }
     }

     public boolean insideIntersection(int x, int y){
          boolean result = false;
          if (x>49 && x<59 && y>49 && y<59) {
               result = true;
          }
          else {
               result = false;
          }
          return  result;
     }

     public void incrementCarsNumberInIntersection(int line) {
          switch (line){
               case 1: {
                    carsNumberInIntersectionLine1.incrementAndGet();
               }
               case 2: {
                    carsNumberInIntersectionLine2.incrementAndGet();
               }
               case 3: {
                    carsNumberInIntersectionLine3.incrementAndGet();
               }
               case 4: {
                    carsNumberInIntersectionLine4.incrementAndGet();
               }
          }
     }

     public void decrementCarsNumberInIntersection(int line) {
          switch (line){
               case 1: {
                    carsNumberInIntersectionLine1.decrementAndGet();
               }
               case 2: {
                    carsNumberInIntersectionLine2.decrementAndGet();
               }
               case 3: {
                    carsNumberInIntersectionLine3.decrementAndGet();
               }
               case 4: {
                    carsNumberInIntersectionLine4.decrementAndGet();
               }
          }
     }

     public class IntersectionAgentReceivingMessage extends CyclicBehaviour {

          @Override

          public void action() {
               // TODO Auto-generated method stub
               if (tryReceivingMessage) {
                    receivedMessage = receive();
                    if (receivedMessage != null) {
                         tryReceivingMessage = false;
                         String[] msgContent = receivedMessage.getContent().toString().split("#");
                         if (msgContent[0].equals("RequestMoving")) requestMovingArrived = true;
                         if (msgContent[0].equals("ReleaseCell")) releaseCellArrived = true;
                    }
               }// action
          } // IntersectionAgentReceivingMessage Class
     }

     public class WaitingNewEvent extends OneShotBehaviour {

          @Override
          public void action() {
               // TODO Auto-generated method stub

          } // action
          public int onEnd() {
               int valTransition = 0;
               if (frontLineReleaseAllowed) {
                    valTransition = 1;
               }else
                    if (!tryReceivingMessage) {                       //A message is arrived
                         if (requestMovingArrived) valTransition = 2;
                         else if (releaseCellArrived) valTransition = 3;
                    } else valTransition = 0;
               return valTransition;
               }
          } // WaitingNewEvent Class

     public class ReleaseCarsOnLineFront extends OneShotBehaviour {

          @Override
          public void action() {
               // TODO Auto-generated method
               frontLineReleaseAllowed = false;
               Iterator<IntersectionAgent.inRoadAgent> iterator = delayedElements.iterator();
               while (iterator.hasNext()) {
                    IntersectionAgent.inRoadAgent element = iterator.next();
                    if (borderlineOutsideIntersection(element.getApproach(), element.getX(), element.getY())) {
                         if (lineApproachCompatible(lineTurn, element.getApproach())) {
                              if (!theRoadMap[element.getRequestedX()][element.getRequestedY()]) {
                                   incrementCarsNumberInIntersection(lineNumberForApproach(element.getApproach()));
                                   iterator.remove();
                                   element.notifyAcceptMoving();
                                   theRoadMap[element.getRequestedX()][element.getRequestedY()] = true;
                                   releasedCarsNumber.incrementAndGet();
                                   System.out.println("I'm " + getLocalName() + " I send AcceptMoving to the position ( " + element.getRequestedX() + "," + element.getRequestedY() + ") to " + element.getAgentName());
                                   grid.switchOnCell(element.getRequestedX(),element.getRequestedY());
                                   grid.switchOffCell(element.getX(),element.getY());
                              }
                         }
                    }
               }
           } // action
     // CarReceivingMessage Class
}

     public class RequestTreatment extends OneShotBehaviour {

          @Override
          public void action() {
               // TODO Auto-generated method stub
               requestMovingArrived = false;
               String [] msgContent = receivedMessage.getContent().toString().split("#");
               int approach = Integer.parseInt(msgContent[1]);
               int x = Integer.parseInt(msgContent[2]);
               int y = Integer.parseInt(msgContent[3]);
               int requestedX = Integer.parseInt(msgContent[4]);
               int requestedY = Integer.parseInt(msgContent[5]);
               long clock = Long.parseLong(msgContent[6]);

               //inRoadAgent element = new inRoadAgent(receivedMessage.getSender(),receivedMessage.getSender().getLocalName(), approach, x, y, requestedX, requestedY, clock);
               inRoadAgent element = new inRoadAgent(receivedMessage.getSender().getLocalName(), approach, x, y, requestedX, requestedY, clock);
               System.out.println("I'm " + getLocalName() + " I receive RequestMoving from " + receivedMessage.getSender().getLocalName()+" requesting the position ("+requestedX+","+requestedY+")");
               if (theRoadMap[requestedX][requestedY] ){
                    delayedElements.add(element);
               }
               else {
                    if (borderlineOutsideIntersection(approach, x, y)){
                         if (!(lineApproachCompatible(lineTurn, approach) && intersectionAccessAllowed)){
                              delayedElements.add(element);
                         }
                         else {
                              System.out.println("I'm " + getLocalName() + " I send AcceptMoving to the position ( " + element.requestedX + ","
                                      + element.requestedY + ") to " + element.getAgentName());
                              element.notifyAcceptMoving();
                              releasedCarsNumber.incrementAndGet();
                              incrementCarsNumberInIntersection(lineNumberForApproach(approach));
                              grid.switchOnCell(element.getRequestedX(),element.getRequestedY());
                              grid.switchOffCell(element.getX(),element.getY());
                         }
                    }
                    else {
                         System.out.println("I'm " + getLocalName() + " I send AcceptMoving to the position ( " + element.requestedX
                                 + "," + element.requestedY + ") to " + element.getAgentName());
                         element.notifyAcceptMoving();
                         grid.switchOnCell(element.getRequestedX(),element.getRequestedY());
                         grid.switchOffCell(element.getX(),element.getY());
                    }
               }
               tryReceivingMessage = true;
               // action
          }
          // RequestTreatment Class
     }

     public class ReleasedCellTreatment extends OneShotBehaviour {

          @Override
          public void action() {
               // TODO Auto-generated method stub
               releaseCellArrived = false;
               String [] msgContent = receivedMessage.getContent().toString().split("#");
               int approach = Integer.parseInt(msgContent[1]);
               int xCell = Integer.parseInt(msgContent[2]);
               int yCell = Integer.parseInt(msgContent[3]);
               theRoadMap[xCell][yCell] = false;
               grid.switchOffCell(xCell,yCell);
               if (borderlineInsideIntersection(approach,xCell,yCell)) {
                    decrementCarsNumberInIntersection(lineNumberForApproach(approach));
               }
               boolean terminate = false;
               Iterator<inRoadAgent> iterator2 = delayedElements.iterator();
               while (iterator2.hasNext() && !(terminate)) {
                    inRoadAgent element = iterator2.next();
                    if ( (xCell== element.getRequestedX()) && yCell==element.getRequestedY()) {
                         terminate = true;
                         if (borderlineOutsideIntersection(element.getApproach(), element.getX(), element.getY())) {
                              if (lineApproachCompatible(lineTurn, element.getApproach()) && intersectionAccessAllowed) {
                                   System.out.println("I'm " + getLocalName() + " I send AcceptMoving to the position ( " + element.requestedX +
                                           "," + element.requestedY + ") to " + element.getAgentName());
                                   element.notifyAcceptMoving();
                                   releasedCarsNumber.incrementAndGet();
                                   incrementCarsNumberInIntersection(lineNumberForApproach(element.getApproach()));
                                   iterator2.remove();
                                   grid.switchOnCell(element.getRequestedX(),element.getRequestedY());
                                   grid.switchOffCell(element.getX(),element.getY());
                              }
                         } else {
                              System.out.println("I'm " + getLocalName() + " I send AcceptMoving to the position ( " + element.requestedX +
                                      "," + element.requestedY + ") to " + element.getAgentName());
                              element.notifyAcceptMoving();
                              iterator2.remove();
                              grid.switchOnCell(element.getRequestedX(),element.getRequestedY());
                              grid.switchOffCell(element.getX(),element.getY());
                         }
                    }
               }
               tryReceivingMessage = true;
               // action
          }
          // CarReceivingMessage Class
     }

     public class ChangeLineTurn extends OneShotBehaviour {

          @Override

          public void action() {
               // TODO Auto-generated method stub
               intersectionAccessAllowed = true;
               lineTurn = appropriateLineTurn();
               maxToRelease = maxToReleaseFromLine(lineTurn);
               frontLineReleaseAllowed = true;
          }
     } // ChangeLineTurn Class

     public class WaitContextEmpty extends OneShotBehaviour {

          @Override

          public void action() {
               // TODO Auto-generated method stub
          } // action
          public int onEnd() {
               int valTransition;
               if (releasedCarsNumber.get() < maxToRelease) {
                    valTransition = 0;
               } else {
                    valTransition = 1;
                    releasedCarsNumber.set(0);
                    intersectionAccessAllowed = false;
               }
               return valTransition;
          }
     } // WaitIntersectionEmpty Class

     public class WaitIntersectionEmpty extends OneShotBehaviour {

          @Override

          public void action() {
               // TODO Auto-generated method stub
               refreshCarsNumber();
          } // action
          public int onEnd() {
               int valTransition;
               if (carsNumber > 0) {
                    valTransition = 0;
               } else {
                    valTransition = 1;
               }
               return valTransition;
               }
          private void refreshCarsNumber() {
               switch (lineTurn) {
                    case 1 : carsNumber = carsNumberInIntersectionLine1.get();
                    case 2 : carsNumber = carsNumberInIntersectionLine2.get();
                    case 3 : carsNumber = carsNumberInIntersectionLine3.get();
                    case 4 : carsNumber = carsNumberInIntersectionLine4.get();
               }
          }
          } // WaitIntersectionEmpty Class

     public  int lineNumberForApproach(int approach){
          int line = 0;
          switch (approach){
               case 1:
                    case 2:
               case 3: {
                    line = 1;
                    break;
               }
               case 4:
               case 5:
               case 6: {
                    line = 2;
                    break;
               }
               case 7:
               case 8:
               case 9: {
                    line = 3;
                    break;
               }
               case 10:
               case 11:
               case 12: {
                    line = 4;
                    break;
               }
          }
          return line;
     }

     public boolean lineApproachCompatible(int line, int approach){
          boolean result = false;
          switch (approach) {
               case 1:
               case 2: {
                    if (line == 1) {
                         result = true;
                    } else {
                         result = false;
                    }
                    break;
               }
               case 4:
               case 5: {
                    if (line == 2) {
                         result = true;
                    } else {
                         result = false;
                    }
                    break;
               }
               case 7:
               case 8: {
                    if (line == 3) {
                         result = true;
                    } else {
                         result = false;
                    }
                    break;
               }
               case 10:
               case 11: {if (  line == 4) result = true;
                         else result = false;
                    break;
                    }
               case 3:
               case 6:
               case 9:
               case 12:  {
                    result =  true;
                    break;
               }
          }
          return  result;
     }

     public boolean borderlineInsideIntersection (int approach, int x, int y){
          boolean result = false;
           switch (approach){
                case 1: {
                     if ((x == 58) && (y == 52)) result = true;
                     else result = false;
                     break;
                }
                case 2:{
                     if ((x == 58) && (y == 51)) result = true;
                     else result = false;
                     break;
                }
                case 4:{
                     if (x == 56 && y == 58) result = true;
                     else result = false;
                     break;
                }
                case 5:{
                     if ((x == 57) && (y == 58)) result = true;
                     else result = false;
                     break;
                }
                case 7:{
                     if ((x == 50) && (y == 56)) result = true;
                     else result = false;
                     break;
                }
                case 8:{
                     if ((x == 50) && (y == 57)) result = true;
                     else result = false;
                     break;
                }
                case 10: {
                     if ((x == 52) && (y == 50)) result = true;
                     else result = false;
                     break;
                }
                case 11: {
                     if ((x == 51) && (y == 50)) result = true;
                     else result = false;
                     break;
                }
                case 3:
                case 6:
                case 9:
                case 12:  {
                     result = false;
                }
           }
           return result;
     }

     public boolean borderlineOutsideIntersection (int approach, int x, int y){
          boolean result = false;
          switch (approach){
               case 1:{
                    if (x == 49 && y==52) {
                         result = true;
                    } else result = false;
                    break;
               }
               case 2:{
                    if (x == 49 && y==51) {
                         result = true;
                    }
                    else result = false;
                    break;
               }
               case 4:{
                    if (x == 56 && y == 49) {
                         result = true;
                    }
                    else result = false;
                    break;
               }
               case 5:{
                    if (x == 57 && y == 49) {
                         result = true;
                    }
                    else result = false;
                    break;
               }
               case 7:{
                    if (x == 59 && y == 56) {
                         result = true;
                    }
                    else result = false;
                    break;
               }
               case 8:{
                    if (x == 59 && y == 57) {
                         result = true;
                    }
                    else result = false;
                    break;
               }
               case 10:{
                    if (x == 52 && y == 59) {
                         result = true;
                    }
                    else result = false;
                    break;
               }
               case 11:{
                    if (x == 51 && y == 59) {
                         result = true;
                    }
                    else result = false;
                    break;
               }
               case 3:
               case 6:
               case 9:
               case 12: {
                    result = false;
               }
          }
     return result;
     }

     public int appropriateLineTurn() {
          boolean theResult = false;
          int currentApproach = 0;
          Iterator<IntersectionAgent.inRoadAgent> iterator = delayedElements.iterator();
          if (iterator.hasNext()) {
               theResult = true;
               IntersectionAgent.inRoadAgent element1 = iterator.next();
               IntersectionAgent.inRoadAgent element = new IntersectionAgent.inRoadAgent(element1.getAgentName(), element1.getApproach(), element1.getX(), element1.getY(), element1.getRequestedX(), element1.getRequestedY(), element1.getClock());
               currentApproach = element.getApproach();
               while (iterator.hasNext()) {
                    //IntersectionAgent.inRoadAgent element1 = iterator.next();
                    element1 = iterator.next();
                    if (!borderlineOutsideIntersection(element.getApproach(), element.getX(), element.getY())) {
                         if (borderlineOutsideIntersection(element1.getApproach(), element1.getX(), element1.getY())) {
                              element.setAgentName(element1.getAgentName());
                              element.setApproach(element1.getApproach());
                              element.setX(element1.getX());
                              element.setY(element1.getY());
                              element.setClock(element1.getClock());
                              currentApproach = element.getApproach();
                         }
                    } else {
                         if (borderlineOutsideIntersection(element1.getApproach(), element1.getX(), element1.getY())
                                 && (element1.getClock() < element.getClock())) {
                              element.setAgentName(element1.getAgentName());
                              element.setApproach(element1.getApproach());
                              element.setX(element1.getX());
                              element.setY(element1.getY());
                              element.setClock(element1.getClock());
                              currentApproach = element.getApproach();
                         }
                    }
               }
          }
          if (!theResult) return 0;
          else return (lineNumberForApproach(currentApproach));
     }

     public int maxToReleaseFromLine(int lineTurn) {
          boolean theResult = false;
          int maxToRelease = 0;
          Iterator<IntersectionAgent.inRoadAgent> iterator = delayedElements.iterator();
          while (iterator.hasNext() && (maxToRelease < contextDepth)) {
               IntersectionAgent.inRoadAgent element = iterator.next();
               if ( (lineNumberForApproach(element.getApproach()) == lineTurn) &&
                       (depth(element.getApproach(),element.getX(),element.getY()) <= contextDepth)
                         && !(element.getApproach()==3 || element.getApproach()==6 || element.getApproach()==9 || element.getApproach()==12)){
                    maxToRelease++;
               }
          }
          return maxToRelease;
     }

     public int depth(int approach, int x, int y ) {
          int resultat = 0;
          switch (approach){
               case 1: {
               }
               case 2: {
               }
               case 3: {
                    resultat = (50-x);
                    break;
               }
               case 4: {
               }
               case 5: {
               }
               case 6: {
                    resultat = (50-y);
                    break;
               }
               case 7: {
               }
               case 8: {
               }
               case 9: {
                    resultat = (x-58);
                    break;
               }
               case 10: {
               }
               case 11: {
               }
               case 12: {
                    resultat = (y-58);
                    break;
               }
          }
          if (resultat > 0 ) return resultat;
          else return 1000;
     }

     public void setup() {
          System.out.println("I'm the  :" + getLocalName());
          Object[] args = this.getArguments();
          theRoadMap = new boolean[109][109];
               for (int i = 0; i < 109; i++) for (int j = 0; j < 109; j++) {
                    theRoadMap[i][j] = false;
               }
               ParallelBehaviour IntersectionAgentBehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
               FSMBehaviour intersectionBehaviour = new FSMBehaviour(this);
               FSMBehaviour negociationBasedLineChangeBehaviour = new FSMBehaviour(this);;

               // Add states to intersectionBehaviour
               intersectionBehaviour.registerFirstState(new IntersectionAgent.WaitingNewEvent(), "WaitingNewEvent");
               intersectionBehaviour.registerState(new IntersectionAgent.ReleaseCarsOnLineFront(), "ReleaseCarsOnFrontLine");
               intersectionBehaviour.registerState(new IntersectionAgent.RequestTreatment(), "RequestTreatment");
               intersectionBehaviour.registerState(new IntersectionAgent.ReleasedCellTreatment(), "ReleasedCellTreatment");

               // Add transitions to intersectionBehaviour
               intersectionBehaviour.registerTransition("WaitingNewEvent", "WaitingNewEvent",0);
               intersectionBehaviour.registerTransition("WaitingNewEvent", "ReleaseCarsOnFrontLine",1);
               intersectionBehaviour.registerTransition("WaitingNewEvent", "RequestTreatment",2);
               intersectionBehaviour.registerTransition("WaitingNewEvent", "ReleasedCellTreatment",3);
               intersectionBehaviour.registerDefaultTransition("RequestTreatment", "WaitingNewEvent");
               intersectionBehaviour.registerDefaultTransition("ReleasedCellTreatment", "WaitingNewEvent");
               intersectionBehaviour.registerDefaultTransition("ReleaseCarsOnFrontLine", "WaitingNewEvent");

               // Add states to watchDogBehaviour
               negociationBasedLineChangeBehaviour.registerState(new IntersectionAgent.ChangeLineTurn(),"ChangeLineTurn");
               negociationBasedLineChangeBehaviour.registerFirstState(new IntersectionAgent.WaitContextEmpty(),"WaitContextEmpty");
               negociationBasedLineChangeBehaviour.registerState(new IntersectionAgent.WaitIntersectionEmpty(),"WaitIntersectionEmpty");

               // Add transitions to watchDogBehaviour
               negociationBasedLineChangeBehaviour.registerDefaultTransition("ChangeLineTurn", "WaitContextEmpty");
               negociationBasedLineChangeBehaviour.registerTransition("WaitContextEmpty", "WaitContextEmpty",0);
               negociationBasedLineChangeBehaviour.registerTransition("WaitContextEmpty", "WaitIntersectionEmpty",1);
               negociationBasedLineChangeBehaviour.registerTransition("WaitIntersectionEmpty", "WaitIntersectionEmpty",0);
               negociationBasedLineChangeBehaviour.registerTransition("WaitIntersectionEmpty", "ChangeLineTurn",1);

               IntersectionAgentBehaviour.addSubBehaviour(intersectionBehaviour);
               IntersectionAgentBehaviour.addSubBehaviour(negociationBasedLineChangeBehaviour);
               IntersectionAgentBehaviour.addSubBehaviour(new IntersectionAgent.IntersectionAgentReceivingMessage());
               addBehaviour(IntersectionAgentBehaviour);
     }
     }
