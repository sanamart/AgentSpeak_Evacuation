// Agent independant in project evacuation

/* Initial beliefs */

inBuilding.

/* Initial goals */

!start.

/* Plans */

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
			!ata(independant,door).

	+!ata(independant,D) : not ata(independant,D) 
		<- 	.my_name(Ag); 
			?pos(Ag,AgX,AgY);
			intactions.door(AgX,AgY,L);
			?pos(L,A,B);
	     	move_towards(A,B);
			!ata(independant,D).
			
	-!ata(independant,D) : true <- !ata(independant,D).
	           
	+!ata(independant,D) : ata(independant,D) 
		<- 	-inBuilding;
			.print("I am out of the building!").
		