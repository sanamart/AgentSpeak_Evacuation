// Agent rescuer in project evacuation

/* Initial beliefs and rules */
free.
/* Initial goals */

   /* Plans */
	
	//sending the leader my distance to "I"
	+rescue(VX,VY,I)[source(A)]: free
		<-	.my_name(Me);
			?pos(Me,AgX,AgY);
			intactions.dist(AgX,AgY,VX,VY,D);
			.print("My distance to ",I," is: ",D);
			!checkDistance(VX,VY,I,D).
	
	-rescue(VX,VY,I)
		<- true.
			
	+!checkDistance(VX,VY,I,D) : D < 20 & free
		<-	.my_name(Me);
			!!save(VX,VY,I).
			
	-!checkDistance(VX,VY,I,D) : true
		<- .print("I am too far to ",I).
     
     
    @ppgd1[atomic] 		
    +iPickUp(I,D)[source(A)] : A \== self &
    		.my_name(Me) &
    		pos(Me,AgX,AgY) &
    		pos(I,VX,VY) &
			intactions.dist(AgX,AgY,VX,VY,MyD) &
			D < MyD & .print("-----------comparar distancias-------------") &
			.desire(save(VX,VY,I))
    	<- 	.print("I want to help ",I," but ",A,
    		" is already doing it");
    		.abolish(I);
    		.drop_desire(save(VX,VY,I)).
    
    
    //---------------------PRUEBA---------------------
    @ppgd2[atomic] 			
    +iPickUp(I,D)[source(A)] :	
    		.my_name(Me) &
    		pos(Me,AgX,AgY) &
    		pos(I,VX,VY) &
			intactions.dist(AgX,AgY,VX,VY,MyD) &
			D < MyD & .print("comparar distancias") &
			.desire(save(VX,VY,I))
    	<- .broadcast(tell,picked(I)).
    	
    
    @ppgd[atomic]
	+picked(I)[source(A)]
	  :  pos(I,VX,VY) & .desire(save(VX,VY,I))
	  <- .print(A," has taken ",I," that I am pursuing! Dropping my intention.");
	     .abolish(I);
	     ?pos(I,VX,VY); 
	     .drop_desire(save(VX,VY,I));
	     +free.
	
	// someone else picked up a gold I know about,
	// remove from my belief base
	// ------------------ SIRVE --------------------
	+picked(I)
	  <- 	.print("elimino la idea de recoger a ",I);
	  		?pos(I,VX,VY);
	  		-rescue(VX,VY,I)[source(_)];
	  		+free.
	
	@psave1[atomic]	           		
	+!save(X,Y,I) : .my_name(Ag) & pos(Ag,X,Y)
		<-	.print("I have reached ",I," at coordinates ",X,", ",Y);
			.broadcast(tell,picked(I));
			.kill_agent(I);
			!scape(rescuer,door).
		
	+!save(X,Y,I)
  		<- 	-free;
  			.broadcast(tell,iPickUp(I,D));
  			?pos(I,X,Y)
  			move_towards(X,Y);
     		!save(X,Y,I).
     	  
    -!save(X,Y,I)
     	<- +free.
	
	@pscape1[atomic]	  
	+!scape(rescuer,door) : not scape(rescuer,door)
		<-	.my_name(Ag); 
			?pos(Ag,AgX,AgY);
			intactions.door(AgX,AgY,L);
			?pos(L,A,B);
	     	move_towards(A,B);
			!scape(rescuer,door).
	
	@pscape2[atomic]     		
	+!scape(rescuer,door) : scape(rescuer,door) 
		<- 	+free; 
			.print("I am out and ready to help somebody.").
