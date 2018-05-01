// Agent injured in project evacuation

	/* Initial beliefs */

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
	//broadcast my coordinates
	+!getHelp	:true
		<- 	.my_name(Me); 
			?pos(Me,X,Y);
			.print("I need help ",Me,
			" I am at ",X,", ",Y);
			.broadcast(tell,rescue(X,Y,Me));
			.wait(3000);
			!getHelp.
			
			