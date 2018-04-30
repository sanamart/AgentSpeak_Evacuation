// Agent injured in project evacuation

	/* Initial beliefs */
	
	inBuilding.
	healthy.
	
	/* Initial goals */
	
	!work.
	
	/* Plans */

	+!work : not alarm
		<- 	nextSlot;
			!work.
	
	+!work : alarm
		<-	-healthy;
			.wait(500);
			!getHelp.
	
	//Ask for help
	//broadcast with my coordinates
	+!getHelp:true
		<- 	.my_name(Ag); 
			?pos(Ag,VX,VY);
			.print("I need help ",Ag," I am at ",VX,", ",VY);
			move_towards(VX,VY); 
			.broadcast(tell,rescue(VX,VY,Ag));
			.wait(3000);
			!getHelp.
			
		
			