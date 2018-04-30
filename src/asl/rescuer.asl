// Agent rescuer in project evacuation

/* Initial beliefs and rules */
free.
/* Initial goals */

   /* Plans */
	
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
		<-	.my_name(Me);
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
    		.print("elimino la idea de recoger a ",I);
	  		?pos(I,VX,VY);
	  		-rescue(VX,VY,I)[source(_)];.
    
    
    // someone else want to pick up injured
    // but I am closer
    @pu2[atomic] 			
    +iPickUp(I,D)[source(A)] :	
    		.my_name(Me) &
    		pos(Me,AgX,AgY) &
    		pos(I,VX,VY) &
			intactions.dist(AgX,AgY,VX,VY,MyD) &
			MyD < D & .print("comparar distancias") &
			.desire(save(I,MyD))
    	<- 	.print("CUANDO MI DISTANCIA ES MAS CORTA QUE ",A);
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
	     +free.
	
	// someone else picked up an injured I know about,
	// remove from my belief base
	+picked(I)
	  <- 	?pos(I,VX,VY);
	  		-rescue(VX,VY,I,_)[source(_)];
	  		+free.
	
	// will pick up injured "I" so move towards it
	// and inform the others about it.	
	+!save(I,D) : not donotHelp(I)
  		<- 	-free;
  			.broadcast(tell,iPickUp(I,D));
  			?pos(I,X,Y)
  			move_towards(X,Y);
     		!save(I,D).
     
     //	once injured is reached inform the others	
     +!save(I,D) : .my_name(Ag) & pos(Ag,X,Y) & pos(I,X,Y)
		<-	.print("I have reached ",I," at coordinates ",X,", ",Y);
			.broadcast(tell,picked(I));
			.kill_agent(I);
			!scape(rescuer,door).
     	  
    -!save(I,D)
     	<- +free.
	
	// try to escape from the building
	@pscape1[atomic]	  
	+!scape(rescuer,door) : not scape(rescuer,door)
		<-	.my_name(Ag); 
			?pos(Ag,AgX,AgY);
			intactions.door(AgX,AgY,L);
			?pos(L,A,B);
	     	move_towards(A,B);
			!scape(rescuer,door).
	
	// I reached the door so I am free again to help
	@pscape2[atomic]     		
	+!scape(rescuer,door) : scape(rescuer,door) 
		<- 	+free; 
			.print("I am out and ready to help somebody.").
