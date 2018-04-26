// Agent rescuer in project evacuation

/* Initial beliefs and rules */
free.
//at(P) :- pos(P,X,Y) & pos(rescuer,X,Y).

/* Initial goals */

   /* Plans */
	
	//sending the leader my distance to I
	+rescue(VX,VY,I)[source(A)]:free
		<-	.my_name(Me);
			?pos(Me,AgX,AgY);
			intactions.dist(AgX,AgY,VX,VY,D);
			.print("My distance to ",I," is: ",D)
     		.send(leader,tell,canHelp(I,D,Me)).
     
    //If not free, value distance 1000 		
    +rescue(VX,VY,I)[source(A)]
    	<-	.my_name(Ag);
    		.send(leader,tell,canHelp(I,1000,Ag)).
	
	//Information about where to go 
	+goTo(Ag,I)[source(leader)]
		:   .my_name(Ag) & free
		<-	.print("my name is ",Ag, " and I am going to save ",I);
			-free;
			?pos(I,X,Y);
			!save(X,Y,I). 
			
	+goTo(Ag,I)[source(leader)]	: .my_name(Ag) & not free
		<-	?pos(I,VX,VY);
			.print("I am already helping someone!");
			.broadcast(tell,rescue(VX,VY,I)).
		           		
	+!save(X,Y,I) : .my_name(Ag) & pos(Ag,X,Y) 
		<-	.print("I have reached ",I," at coordinates ",X,", ",Y);
			.kill_agent(I);
			!scape(rescuer,door).
			
	+!save(X,Y,I)
  		<- 	?pos(I,X,Y)
  			move_towards(X,Y);
     		!save(X,Y,I).
	
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
