// Agent independent in project evacuation

/* Initial beliefs */

inBuilding.

/* Initial goals */

!start.

/* Plans */

	// independent agents just walk around
	// and escape when there is an emergency

	+!start : true 
	<-	.print("I am working.");
		!work.

	+!work : not alarm
		<- 	next(slot);
			!work.
	
	+!work : alarm
		<-	!leave(building).
		
	+!leave(building)
		: true
		<- 	.print("I will leave the building.");
			!scape(independant,door).

	+!scape(independant,D) : not scape(independant,D) 
		<- 	.my_name(Ag); 
			?pos(Ag,AgX,AgY);
			intactions.door(AgX,AgY,L);
			?pos(L,A,B);
	     	move_towards(A,B);
			!scape(independant,D).
			
	-!scape(independant,D) : true <- !scape(independant,D).
	           
	+!scape(independant,D) : scape(independant,D) 
		<- 	-inBuilding;
			.print("I am out of the building!").
		