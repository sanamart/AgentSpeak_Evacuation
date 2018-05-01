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
		<- 	nextSlot;
			!work.
	
	+!work : alarm
		<-	.time(_,MM,SS);
			+myTime(MM,SS);
			!leaveBuilding.
		
	+!leaveBuilding
		: 	true
		<- 	.print("I will leave the building.");
			!escape(independent,door).

	+!escape(independent,D) : not escape(independent,D) 
		<- 	.my_name(Ag); 
			?pos(Ag,AgX,AgY);
			intactions.door(AgX,AgY,L);
			?pos(L,A,B);
	     	move_towards(A,B);
			!escape(independent,D).
			
	-!escape(independent,D) : true <- !escape(independent,D).
	           
	+!escape(independent,D) : escape(independent,D) 
		<- 	-inBuilding;
			.time(_,MM2,SS2);
			?myTime(MM,SS);
			.print("I am out of the building!");
			if(SS2<SS) {
				.print("I left the building in ",MM2-MM-1
				," minutes and ",(SS2-SS)+60," seconds.")
			}else{
				.print("I left the building in ",MM2-MM
				," minutes and ",SS2-SS," seconds.")
				}
			.