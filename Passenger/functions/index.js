var functions = require('firebase-functions');

// // Start writing Firebase Functions
// // https://firebase.google.com/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// })
var admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);



exports.bookSeat = functions.database.ref('coach-schedule/{scheduleId}').onWrite(event=>{
	if(!event.data.changed('seatState')){
		return;
	}
	var state = event.data.chid('seatState').val();
	console.log(state);
	var count = 0;
	for(var i= 0; i < state.length; i++){
		if (state.charAt(i) == '0') {
			count++;
		}
	}
	return event.data.ref.update({seatAvailable: count});
});
