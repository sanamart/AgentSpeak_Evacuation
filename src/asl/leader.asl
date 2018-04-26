// Agent leader in project evacuation

/* Initial beliefs and rules */

/* Initial goals */

/* Plans */

/* negotiation for help injured*/

	//All rescuer agents send their distances to 
	//agent I (injured)
	+canHelp(I,D,Rs) : .count(canHelp(I,_,_),3)
		<-	!allocate_rescuer(I);
			.abolish(canHelp(I,_,_)).
			
	//choosing the agent closest to I (injured)		
	+!allocate_rescuer(I)
		<-	.findall(val(D,Rs),canHelp(I,D,Rs),RL);
			.print("Help list for ",I," : ",RL);
			.min(RL,val(CloserD,RS));
			CloserD < 1000;
			.print(RS, " will help ", I );
			.broadcast(tell,goTo(RS,I)). //inform agent RS (rescuer) to pick up I (injured)
			
	-!allocate_rescuer(inj)
		<-	.print("could not allocate injured").