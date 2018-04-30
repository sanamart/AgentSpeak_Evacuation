// Agent rescuer in project evacuation

/* Initial beliefs and rules */
free.

/* Initial goals */
//!startPos.
   /* Plans */
   
   	/*// Save my start position
   	+!startPos : true
   		<-	.my_name(Me);
   			?pos(Me,X,Y);
   			+initPos(X,Y);
   			.print("Saving init position X: ",X,",Y: ",Y).*/
	
	// sending the leader my distance to I
	+rescue(VX,VY,I):free
		<-	.my_name(Me);
			?pos(Me,AgX,AgY);
			intactions.dist(AgX,AgY,VX,VY,D);
			.print("My distance to ",I," is: ",D)
     		.send(leader,tell,canHelp(I,D,Me)).
     
    // If not free, value distance 1000 		
    +rescue(VX,VY,I)
    	<-	.my_name(Ag);
    		.send(leader,tell,canHelp(I,1000,Ag)).
	
	// Information about where to go 
	+!goTo(I)[source(leader)]
		:   .my_name(Ag) & free
		<-	.print("my name is ",Ag, " and I am going to save ",I);
			-free;
			.time(_,MM,SS);
			+myTime(MM,SS);
			?pos(I,X,Y);
			!save(X,Y,I). 
	
	// Some injured was allocated to me but I am not free
	// announce it to the others		
	+!goTo(I)[source(leader)]	: .my_name(Ag) & not free
		<-	?pos(I,VX,VY);
			.print("I am already helping someone!");
			.broadcast(tell,rescue(VX,VY,I)).
	
	// Save plans to pick up the injured	           		
	+!save(X,Y,I) : .my_name(Ag) & pos(Ag,X,Y) 
		<-	.print("I have reached ",I," at coordinates ",X,", ",Y);
			.kill_agent(I);
			!escape(rescuer,door).
			
	+!save(X,Y,I)
  		<- 	?pos(I,X,Y)
  			move_towards(X,Y);
     		!save(X,Y,I).
	
	// Escape plans to leave the building after
	// an injured has been picked up
	+!escape(rescuer,door) : not escape(rescuer,door)
		<-	.my_name(Ag); 
			?pos(Ag,AgX,AgY);
			intactions.door(AgX,AgY,L);
			?pos(L,A,B);
	     	move_towards(A,B);
			!escape(rescuer,door).
	     		
	+!escape(rescuer,door) : escape(rescuer,door) 
		<- 	+free;
			.time(_,MM2,SS2);
			?myTime(MM,SS); 
			.print("I am out and ready to help somebody.");
			if(SS2<SS) {
				.print("-----I left the building in ",MM2-MM-1
				," minutes and ",(SS2-SS)+60," seconds.-----")
			}else{
			.print("-------I left the building in ",MM2-MM
				," minutes and ",SS2-SS," seconds.------")
				}
			.
