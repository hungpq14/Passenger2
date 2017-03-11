var functions = require('firebase-functions');

// // Start writing Firebase Functions
// // https://firebase.google.com/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// })
var admin = require('firebase-admin');
var schedule = require('node-schedule');
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

exports.handleCommentBonus = functions.database.ref('/comments/{id}').onWrite(event => {
	var data = event.data;
	if(data.previous.exists()){
		console.log('data exists');
		return;
	}
	const userId = data.val().userUid;
    console.log('Add 2 point to ' + userId);
	var userRef = admin.database().ref('/users/' + userId);
	userRef.once('value').then(function(snapshot){
		var curPoint = snapshot.val().point;
		curPoint += 3;
		return userRef.update({point: curPoint});
	});
});

exports.handleTicketExpired = functions.database.ref('/ticket/{id}').onWrite(event=>{
    var data = event.data;
	// if(data.previous.exists()){
    //     console.log('data exists');
	// 	return;
	// }

	const ticket = data.val();
	const scheduleId = ticket.coach_schedule_id;
	const userId = ticket.user_id;

	admin.database().ref('/coach-schedule').once('value').then(function(snapshot){
		const departureTime = snapshot.val().departureTime;
		var dateArray = departureTime.split('/');
		var date = new Date(dateArray[0],dateArray[1] - 1,dateArray[2],dateArray[3],dateArray[4],0);
		console.log('Job scheduled for ' + userId + ' after ' + date.getTime() - Date.now() + 'ms form now');
        // var userRef = admin.database().ref('/users');
		// schedule.scheduleJob(date, function{
		// 	console.log('Ticket expired: ' + userId);
		// 	userRef.once('value').then(function(snapshot){
		// 		var curPoint = snapshot.val().point;
		// 		curPoint -= 3;
		// 		curPoint = curPoint < 0 ? 0 : curPoint;
		// 		return userRef.update({point: curPoint});
		// 	});
		// });

	});
});
