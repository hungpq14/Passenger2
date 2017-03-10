var functions = require('firebase-functions');

// // Start writing Firebase Functions
// // https://firebase.google.com/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// })
var admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.addCity = functions.https.onRequest((req,res) => {
	const newCity = req.query.text;
	admin.database().ref('/city').push({name:newCity}).then(snapshot =>{
		res.redirect(303,snapshot.ref);
	});
});