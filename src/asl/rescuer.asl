// Agent rescuer in project evacuation

/* Initial beliefs and rules */

free.

/* Initial goals */

!startPos.

   /* Plans */
   
   	//Save my start position
   	+!startPos : true
   		<-	.my_name(Me);
   			?pos(Me,X,Y);
   			+initPos(X,Y);
   			.print("Saving init position X: ",X,",Y: ",Y).

	//get the request from an injured
	//and calculate the distance to it 
	+rescue(VX,VY,I)[source(A)]: free
		<-	.my_name(Me);
			?pos(Me,AgX,AgY);
			intactions.dist(AgX,AgY,VX,VY,D);
			.print("My distance to ",I," is: ",D);
			!checkDistance(I,D).
	
	-rescue(VX,VY,I)
		<- true.
			
	//if the distance is smaller than the 
	//empirical value 25, then achieve the
	//goal to save the injured		
	+!checkDistance(I,D) : D < 25 & free
		<-	.time(HH,MM,SS);
			+myTime(MM,SS);
			!!save(I,D).
			
	//if the distance is bigger than 25
	//it is too far to help		
	-!checkDistance(I,D) : true
		<- .print("I am too far to ",I).
     
    
    // someone else is closer to injured
    // so drops the intention and remove from
    // my belief base
    @pu1[atomic] 		
    +iPickUp(I,D)[source(A)] : A\==self &
    		.my_name(Me) &
    		pos(Me,AgX,AgY) &
    		pos(I,VX,VY) &
			intactions.dist(AgX,AgY,VX,VY,MyD) &
			D < MyD &
			.desire(save(I,MyD))
    	<- 	.print("I want to help ",I," but ",A,
    		" is closer. Dropping my intention");
    		+free;
    		.drop_desire(save(I,MyD));
	  		?pos(I,VX,VY);
	  		-rescue(VX,VY,I)[source(_)].
    
    
    // someone else want to pick up injured
    // but I am closer
    @pu2[atomic] 			
    +iPickUp(I,D)[source(A)] :	
    		.my_name(Me) &
    		pos(Me,AgX,AgY) &
    		pos(I,VX,VY) &
			intactions.dist(AgX,AgY,VX,VY,MyD) &
			MyD < D & .desire(save(I,MyD))
    	<- 	.print(A," wants to pick up ",I," but I am closer.");
    		!!save(I,MyD).
    	
    // someone else picked up and injured I know about,
    // so drops the intention
    @ppgd[atomic]
	+picked(I)[source(A)]
	  :  pos(I,VX,VY) & .desire(save(I,_))
	  <- .print(A," has taken ",I," that I am pursuing! Dropping my intention.");
	     //.abolish(I);
	     ?pos(I,VX,VY); 
	     .drop_desire(save(I,_));
	     +free;
	     .//!to(stPos).
	
	// someone else picked up an injured I know about,
	// remove from my belief base
	+picked(I)
	  <- 	?pos(I,VX,VY);
	  		-rescue(VX,VY,I,_)[source(_)];
	  		+free.
     
     //	once injured is reached inform the others	
     +!save(I,D) : .my_name(Ag) & pos(Ag,X,Y) & pos(I,X,Y)
		<-	.print("I have reached ",I," at coordinates ",X,", ",Y);
			.broadcast(tell,picked(I));
			.kill_agent(I);
			!escape(rescuer,door).
			
	// will pick up injured "I" so move towards it
	// and inform the others about it.	
	+!save(I,D) : not save(I,D)
  		<- 	//-free;
  			.broadcast(tell,iPickUp(I,D));
  			?pos(I,X,Y)
  			move_towards(X,Y);
     		!save(I,D).
     	  
    -!save(I,D)
     	<- +free.
	
	// try to escape from the building
	@pescape1[atomic]	  
	+!escape(rescuer,door) : not escape(rescuer,door)
		<-	.my_name(Ag); 
			?pos(Ag,AgX,AgY);
			intactions.door(AgX,AgY,L);
			?pos(L,A,B);
	     	move_towards(A,B);
			!escape(rescuer,door).
	
	// I reached the door so I am free again to help
	@pescape2[atomic]     		
	+!escape(rescuer,door) : escape(rescuer,door) 
		<- 	+free; 
			.time(HH,MM2,SS2);
			?myTime(MM,SS); 
			.print("I am out and ready to help somebody.");
			if(SS2<SS) {
				.print("I left the building in ",MM2-MM-1
				," minutes and ",(SS2-SS)+60," seconds.")
			}else{
			.print("I left the building in ",MM2-MM
				," minutes and ",SS2-SS," seconds.")
				}
			//!to(stPos)
			.
	
	// Go to start position if I am free 		
	+!to(stPos) : true
   		<-	?initPos(X,Y);
   			.print("valores inciales ",X," ",Y);
   			!at(X,Y).
   	
   	+!at(X,Y) : .my_name(Me) & pos(Me,X,Y)
   		& X==mX & Y==mY
   		<-	.print("llegue a init pos").
   		
   	+!at(X,Y) : .my_name(Me) & pos(Me,mX,mY)
   		& X\==mX & Y\==mY
   		<-	.print("still going");
   			move_towards(X,Y);
   			!at(X,Y).
   			