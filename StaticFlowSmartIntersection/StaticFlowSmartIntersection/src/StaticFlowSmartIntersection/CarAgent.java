package StaticFlowSmartIntersection;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;

import static StaticFlowSmartIntersection.SmartIntersectionMain.leavedCarsNumber;

public class CarAgent extends Agent {
	String agentName;
	int approach;
	int x;
	int y;
	int myRequestedX;
	int myRequestedY;
	int myId;
	String myState;

	// TODO Auto-generated method stub

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getMyRequestedX() {
		return myRequestedX;
	}

	public int getMyRequestedY() {
		return myRequestedY;
	}

	public int getMyId() {
		return myId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public int getApproach() {
		return approach;
	}

	public String getMyState() {
		return myState;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setMyRequestedX(int myRequestedX) {
		this.myRequestedX = myRequestedX;
	}

	public void setMyRequestedY(int myRequestedY) {
		this.myRequestedY = myRequestedY;
	}

	public void setMyId(int myId) {
		this.myId = myId;
	}

	public void setApproach(int approach) {
		this.approach = approach;
	}

	public void setMyState(String state) {
		this.myState = state;
	}

	public class CarReceivingMessage extends CyclicBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage receivedMessage = receive();
			if (receivedMessage != null) {
				String [] msgContent = receivedMessage.getContent().toString().split("#");
				if (msgContent[0].equals("Commit")) {
					setX(Integer.parseInt(msgContent[1]));
					setY(Integer.parseInt(msgContent[2]));
					setApproach(Integer.parseInt(msgContent[3]));
					setMyState("Requesting");
					System.out.println("I'm the car : " + getLocalName() + " my new state is " + getMyState());
				}
				if (msgContent[0].equals("AcceptMoving")) {
					setMyState("Moving");
					System.out.println("I'm the car : " + getLocalName() + " : I receive the Move message, my new state is " + getMyState());
				}
			}
		} // action
	// CarReceivingMessage Class
	}

	public class AdmitState extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
		} // action
		public int onEnd() {
			int valTransition;
			if ("Admitting".equals(getMyState())) {
				valTransition = 0;
			}
			else {
				valTransition = 1;
			}
			return valTransition;
		}

		/* WaitState Class */
	}

	public class RequestMovingState extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			setMyState("Requesting");
			switch (getApproach()){
				case 1:{
					if (getX()==108) setMyState("Terminating");
					else if (getX()<50 || getX() > 57){
						setMyRequestedX(getX()+1);
						setMyRequestedY(getY());
					} else if (getX()>49 && getX()<56) {
						setMyRequestedX(getX()+1);
						setMyRequestedY(getY()+1);
					} else if (getX()==56) {
						setApproach(4);
						setMyRequestedX(getX());
						setMyRequestedY(getY()+1);
					}
					break;
				}
				case 2:{
					if (getX()<108){
						setMyRequestedX(getX()+1);
						setMyRequestedY(getY());
					} else setMyState("Terminating");
					break;
				}
				case 3:{
					if ( ((getX()<50) || (getX()>58)) && (getX()<107)) {
						setMyRequestedX(getX()+1);
						setMyRequestedY(getY());
					} else if (getX() == 50){
							//The car change the approach
							setApproach(12);
							setMyRequestedX(getX());
							setMyRequestedY(getY()-1);
						} else  setMyState("Terminating");
					break;
					}
				case 4:{
					if (getY()==108) setMyState("Terminating");
					else if (getY()<50 || getY() > 57){
						setMyRequestedX(getX());
						setMyRequestedY(getY()+1);
					} else if (getY() > 49 && getY()<56) {
						setMyRequestedX(getX()-1);
						setMyRequestedY(getY()+1);
					} else if (getY()==56) {
						setApproach(7);
						setMyRequestedX(getX()-1);
						setMyRequestedY(getY());
					}
					break;
				}
				case 5:{
					if (getY()<107){
						setMyRequestedX(getX());
						setMyRequestedY(getY()+1);
					} else setMyState("Terminating");
					break;
				}
				case 6:{
					if ( ((getY()<50) || (getY()>58)) && (getY()<107)){
						setMyRequestedY(getY()+1);
						setMyRequestedX(getX());
					} else if (getY() == 50){
						//The car change the approach
						setApproach(3);
						setMyRequestedX(getX()+1);
						setMyRequestedY(getY());
					} else setMyState("Terminating");
					break;
				}
				case 7:{
					if (getX()==0) setMyState("Terminating");
					else if (getX() <51 || getX()>58){
						setMyRequestedX(getX()-1);
						setMyRequestedY(getY());
					} else if (getX()>52 && getX() < 59) {
						setMyRequestedX(getX()-1);
						setMyRequestedY(getY()-1);
					} else if (getX()==52) {
						setApproach(10);
						setMyRequestedX(getX());
						setMyRequestedY(getY()-1);
					}
					break;
				}
				case 8:{
					if (getX()>0){
						setMyRequestedX(getX()-1);
						setMyRequestedY(getY());
					} else setMyState("Terminating");
					break;
				}
				case 9:{
					if ( ((getX() >58) || (getX()<50)) && (getX()>0)){
						setMyRequestedY(getY());
						setMyRequestedX(getX()-1);
					} else if (getX() == 58){
						//The car change the approach
						setApproach(6);
						setMyRequestedX(getX());
						setMyRequestedY(getX()+1);
					} else setMyState("Terminating");
					break;
				}
				case 10:{
					if (getY() == 0) {
						setMyState("Terminating");
					} else if ((getY() > 58) || (getY() < 51)){
						setMyRequestedX(getX());
						setMyRequestedY(getY()-1);
					} else if (getY()>52 && getY()<59) {
						setMyRequestedX(getX()+1);
						setMyRequestedY(getY()-1);
					} else if (getY()==52) {
						setApproach(1);
						setMyRequestedX(getX()+1);
						setMyRequestedY(getY());
					}
					break;
				}
				case 11:{
					if (getY()>0){
						setMyRequestedX(getX());
						setMyRequestedY(getY()-1);
					} else setMyState("Terminating");
					break;
				}
				case 12:{
					if ( ((getY() >58) || (getY()<50)) && (getY()>0)){
						setMyRequestedY(getY()-1);
						setMyRequestedX(getX());
					} else if (getY() == 58){
						//The car change the approach
						setApproach(9);
						myRequestedX = getX()-1;
						myRequestedY = getY();
						System.out.println("I'm  the car: " + getLocalName() + " I'm at position ( " + getX() + "," + getY() + ") requesting position(" + getMyRequestedX() + "," + getMyRequestedY() + ") I change the approach to "+getApproach());
					} else setMyState("Terminating");
					break;
				}
			}
			if (!"Terminating".equals(getMyState())) sendRequestMessage();
		}

		private void sendRequestMessage() {
				System.out.println("I'm  the car: " + getLocalName() + " on approach "+getApproach()+" I'm at position (" + getX() + "," + getY() + ") requesting position(" + getMyRequestedX() + "," + getMyRequestedY() + "))");
				ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
				message.addReceiver(new AID("IntersectionAgent", AID.ISLOCALNAME));
				message.setContent("RequestMoving#" + getApproach() + "#" + getX() + "#" + getY() + "#" + getMyRequestedX() + "#" + getMyRequestedY());
				send(message);
				setMyState("Waiting");
		}
	}

	public class WaitState extends OneShotBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
		} // action

		public int onEnd() {
			int valTransition;
			if ("Moving".equals(getMyState())) valTransition = 1;
			else if  ("Terminating".equals(getMyState())) valTransition = 2;
				else valTransition = 0;
			return valTransition;
		}

	/* WaitState Class */
	}

	public class MoveState extends OneShotBehaviour {

		@Override
		public void action() {
			setMyState("Moving");
			int approach = getApproach();
			int xReleaseCell = getX();
			int yReleaseCell = getY();
			move();
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.addReceiver(new AID("IntersectionAgent", AID.ISLOCALNAME));
			message.setContent("ReleaseCell#" + approach + "#" + xReleaseCell + "#" + yReleaseCell);
			send(message);
			System.out.println("I m the car : " + getLocalName() + " : I moved to position : (" + getMyRequestedX() + "," + getMyRequestedY() + ") and released the cell ("+xReleaseCell+","+yReleaseCell+")");
			} // action
		/* MoveState Class */
		}

	public class TerminateState extends OneShotBehaviour {

		@Override
		public void action() {
			String agentName = getLocalName();
			setMyState("Terminating") ;
			leavedCarsNumber.incrementAndGet();
			System.out.println("========================I m the car : " + getLocalName() + ", I'm at the limit, at position (" + getX() + "," + getY() + ") on the approach " + getApproach() + " I m the "+leavedCarsNumber+ " cars that leaved the system");
			ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
			message.addReceiver(new AID("IntersectionAgent", AID.ISLOCALNAME));
			message.setContent("ReleaseCell#" + approach + "#" + getX() + "#" + getY());
			send(message);

			myAgent.doDelete();
		} // action
		/* MoveState Class */
	}

	public void move() {
		setX(getMyRequestedX());
		setY(getMyRequestedY());
		}

	public void setup() {
		Object[] args = getArguments();
			SmartIntersectionMain.commitmentNumber.incrementAndGet();
			setMyState("Admitting");
			System.out.println("I'm the car :" + getLocalName() + " at state "+ getMyState() );

			// Add the behaviour
			ParallelBehaviour ProcessBehaviour = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
			FSMBehaviour myBehaviour = new FSMBehaviour(this);

			// Add states
			myBehaviour.registerFirstState(new AdmitState(), "Admitting");
			myBehaviour.registerState(new RequestMovingState(), "Requesting");
			myBehaviour.registerState(new WaitState(), "Waiting");
			myBehaviour.registerState(new MoveState(), "Moving");
			myBehaviour.registerLastState(new TerminateState(), "Terminating");

			// Add transitions
			myBehaviour.registerTransition("Admitting", "Admitting", 0);
			myBehaviour.registerTransition("Admitting", "Requesting", 1);
			myBehaviour.registerDefaultTransition("Requesting", "Waiting");
			myBehaviour.registerTransition("Waiting", "Waiting", 0);
			myBehaviour.registerTransition("Waiting", "Moving", 1);
			myBehaviour.registerTransition("Waiting", "Terminating", 2);
			myBehaviour.registerDefaultTransition("Moving", "Requesting");

			ProcessBehaviour.addSubBehaviour(myBehaviour);
			ProcessBehaviour.addSubBehaviour(new CarReceivingMessage());
			addBehaviour(myBehaviour);
			addBehaviour(ProcessBehaviour);
	}
}