'use strict'

var router = require('express').Router();
var request = require('request');
var xmlParser = require('xml2js').Parser({explicitArray: false});

router.get('/', function(req, res) {
	request('http://calcium.inf.kcl.ac.uk:8080/', function(error, response, body) {
		if (!error && response.statusCode == 200) {
			xmlParser.parseString(body, function(err, result) {
			res.json(result.domains.domain);
			});
		}
	});
});

// problem here domainId isn't sent in req
router.get(/[a-z0-9-]+/, function(req, res) {
	console.log(req.domainId)
	request('http://calcium.inf.kcl.ac.uk:8080/' + req, function(error, response, body) {
		if (!error && response.statusCode == 200) {
			res.body;
		}
	});
})

router.post('/', function(req, res) {
	console.log('domain reveived :D')
	console.log(req.body.name)
	console.log(req.body.formulation)
	console.log(req.body.ipc)
	res.sendStatus(201)
});

module.exports = router;