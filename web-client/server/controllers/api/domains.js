'use strict';

var router = require('express').Router();
var request = require('request');
var xmlParser = require('xml2js').Parser({explicitArray: false});

var SERVER_ADDRESS = 'http://calcium.inf.kcl.ac.uk:8080/';

router.get('/', function (req, res) {
	request(SERVER_ADDRESS, function(error, response, body) {
		if (!error && response.statusCode == 200) {
			xmlParser.parseString(body, function (err, result) {
				res.json(result.domains.domain)
			})
		}
	})
});

router.get(/[a-z0-9-]+\/(p[0-9]{2}-?)?(domain)?\.pddl/, function (req, res) {
	request(SERVER_ADDRESS + 'pddl-file/' + req.query.domainId + '/' + req.query.fileName,
		function (error, response, body) {
			if (!error && response.statusCode == 200) {
				res.send(body)
			}
		})
});

router.get('/leaderboard', function (req, res) {
	request(SERVER_ADDRESS + 'leaderboard', function (error, response, body) {
		if (!error && response.statusCode == 200) {
			xmlParser.parseString(body, function (err, result) {
				res.json(result.leaderboard)
			})
		}
	})
});

router.get(/[a-z0-9-]+/, function (req, res) {
	request(SERVER_ADDRESS + req.query.domainId, function (error, response, body) {
		if (!error && response.statusCode == 200) {
			xmlParser.parseString(body, function (err, result) {
				if (result) {
					res.json(result['planning:metadata'])
				} else {
					console.log(err)
				}
			})
		}
	})
});

router.post('/', function (req, res) {
	console.log('domain reveived :D')
	console.log(req.body.name)
	console.log(req.body.formulation)
	console.log(req.body.ipc)
	res.sendStatus(201)
});

module.exports = router;