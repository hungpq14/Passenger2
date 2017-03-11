var functions = require('firebase-functions');

// // Start writing Firebase Functions
// // https://firebase.google.com/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// })
var admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);



exports.bookSeat = functions.database.ref('/coach-schedule/{scheduleId}').onWrite(event=>{
	if(!event.data.changed('seatState')){
		return;
	}
	console.log(event.data.val());
	var state = event.data.val().seatState;
	console.log(state);
	var count = 0;
	for(var i= 0; i < state.length; i++){
		if (state.charAt(i) == '0') {
			count++;
		}
	}
	return event.data.ref.update({seatAvailable: count});
});

exports.autogenTicketId = functions.database.ref('/ticket/{ticketId}').onWrite(event=>{
	if (event.data.previous.exists()) {
        return;
      }

	if(!event.data.exists()){
		return;
	}
	const key = event.data.key;
	return event.data.ref.update({uid: key});
});

exports.handleCheckoutBonus = functions.database.ref('/ticket/{ticketId}').onWrite(event=>{

	if(!event.data.changed('checkout')){
		return;
	}
	const ticket = event.data.val();
	const checkout = ticket.checkout;
	const userId = ticket.user_id;
	if (checkout == true) {
		console.log('Add 2 point to ' + userId);
		var userRef = admin.database().ref('/users/' + userId);
		userRef.once('value').then(function(snapshot){
			var curPoint = snapshot.val().point;
			curPoint += 2;
			return userRef.update({point: curPoint});
		});
	}
});

exports.handleCommentBonus = functions.database.ref('/comments/{id}').onWrite(event=>{
	var data = event.data;
	if(data.previous.exists()){
		return;
	}
	const userId = data.val().userUid();
	var userRef = admin.database().ref('/users/' + userId);
	userRef.once('value').then(function(snapshot){
		var curPoint = snapshot.val().point;
		curPoint += 3;
		return userRef.update({point: curPoint});
	});
});
