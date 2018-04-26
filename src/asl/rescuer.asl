// Agent rescuer in project evacuation

/* Initial beliefs and rules */
free.

/* Initial goals */

   /* Plans */
	
	//sending the leader my distance to I
	+rescue(VX,VY,I)[source(A)]:free & not carryInjured
		<-	.my_name(Me);
			?pos(Me,AgX,AgY);
			intactions.dist(AgX,AgY,VX,VY,D);
			.print("My distance to ",I," is: ",D);
			!checkDistance(I,D).
	
	-rescue(VX,VY,I)
		<- true.
			
	+!checkDistance(I,D) : D < 25
		<-	.print("Checking distance: ",D);
			//-free;
			.broadcast(tell,iPickUp(I,D));
			.my_name(Me);
			!!goTo(Me,I).
			
	-!checkDistance(I,D)
		<- .print("I am too far to ",I).
			
	
			
			
			//.broadcast(tell,iPickUp(I,D)).
     		//.send(leader,tell,canHelp(I,D,Me)).
     		
    +iPickUp(I,D)[source(A)] : A\==self & not carryInjured &
    		.my_name(Me) &
    		pos(Me,AgX,AgY) &
    		pos(I,VX,VY) &
			intactions.dist(AgX,AgY,VX,VY,MyD) &
			MyD < D
    	<- 	.send(A,tell,closerTo(I));
    		!!goTo(Me,I).
    		
    +closerTo(I)[source(A)] : A\==self
    	<- 	.print("I want to help ",I," but ",A,
    		" is already doing it"
    	);
    		+free;
    		.drop_desire(goTo(_,I)).
  			
			
	/* +iPickUp(I,D)[source(A)]
		<-	.abolish(iPickUp(I,D)).*/
	
	//Information about where to go 
	+!goTo(Ag,I)
		:   .my_name(Ag) & free
		<-	.print("my name is ",Ag, " and I am going to save ",I);
			-free;
			?pos(I,X,Y);
			!save(X,Y,I).
			
	-!goTo(Ag,I)
		<-	+free.
	
	@psave1[atomic]	           		
	+!save(X,Y,I) : .my_name(Ag) & pos(Ag,X,Y) 
		<-	.print("I have reached ",I," at coordinates ",X,", ",Y);
			+carryInjured;
			.kill_agent(I);
			!scape(rescuer,door).
		
	@psave2[atomic]	 	
	+!save(X,Y,I)
  		<- 	?pos(I,X,Y)
  			move_towards(X,Y);
     		!save(X,Y,I).
     
    -!save(X,Y,I)
     	<- true.
	
	+!scape(rescuer,door) : not scape(rescuer,door)
		<-	.my_name(Ag); 
			?pos(Ag,AgX,AgY);
			intactions.door(AgX,AgY,L);
			?pos(L,A,B);
	     	move_towards(A,B);
			!scape(rescuer,door).
	     		
	+!scape(rescuer,door) : scape(rescuer,door) 
		<- 	+free; 
			.print("I am out and ready to help somebody.").
