
//------------------------------
//	Imports
//------------------------------
var express = require('express');
var bodyParser = require('body-parser');
var path = require('path');

//------------------------------
//	App Initialisation
//------------------------------
var app = express();
app.use(bodyParser.json());
app.get('/*', function(req, res, next) {
	res.header('Access-Control-Allow-Origin', '*');
	next()
});

//------------------------------
//	Controllers
//------------------------------
app.use('/api/domains', require('./controllers/api/domains'));
app.use('/', require('./controllers/static'));

//------------------------------
//	Start Server
//------------------------------
app.listen(3000, function() {
	console.log('Server listening on port', 3000)
});