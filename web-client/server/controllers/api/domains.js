'use strict'

var router = require('express').Router();
var request = require('request');
var xmlParser = require('xml2js').parseString;

/*var domains = [
	{
		name: 'Depots',
		formulation: 'Temporal',
		ipc: '2002'
	},
	{
		name: 'Driverlog',
		formulation: 'Timewindows',
		ipc: '2004'
	}
]*/

router.get('/', function(req, res) {
	
	request('http://calcium.inf.kcl.ac.uk:8080/', function(error, response, body) {
		if (!error && response.statusCode == 200) {
			xmlParser(body, function(err, result) {
			res.json(result.domains.domain);
			});
		}
	});
	//res.json(domains);
});

router.post('/', function(req, res) {
	console.log('domain reveived :D')
	console.log(req.body.name)
	console.log(req.body.formulation)
	console.log(req.body.ipc)
	res.sendStatus(201)
});

module.exports = router;