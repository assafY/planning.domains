'use strict';

var router = require('express').Router();
var multer = require('multer');
var request = require('request');
var util = require('util');
var exec = require('child_process').exec;
var fs = require('fs');

var SERVER_ADDRESS = 'http://calcium.inf.kcl.ac.uk:8080/';
var SFTP_USER = 'k1333702';
var SFTP_ADDRESS = '@calcium.inf.kcl.ac.uk';
var SFTP_PATH = ':planning_domains/res/';
var SFTP_COMMAND = 'sftp ' + SFTP_USER + SFTP_ADDRESS + SFTP_PATH;

var storage = multer.diskStorage({
	destination: function (request, file, cb) {
		cb(null, './uploads/')
	},
	filename: function (request, file, cb) {
		cb(null, file.originalname)
	}
});

var uploadDomain = multer({
	storage: storage
}).array('files');

var uploadPlanner = multer({
	storage: storage
}).single('file');

var sftpFiles = function (res, dirname) {
	// user must set up public key authentication between 
	// local and receiving servers for this to work

	exec(SFTP_COMMAND + 'domains/uploads/' + dirname +
		" <<< $'put ./uploads/" + dirname + "/*'",
	function (error, stdout, stderr) {
		if (error !== null) {
			res.status(500).send('Error uploading files');
		} else {
			exec('rm -r ./uploads/' + dirname, function (error, stdout, stderr) {
				if (error !== null) {
					res.status(500).send('Error uploading files');
				} else {
					res.status(201).send('Upload successful');
				}
			});
		}
	});
}

// notify Calcium that a planner was uploaded and send directory name
router.get('/planner-notify', function (req, res) {
	request(SERVER_ADDRESS + 'planner-upload/' + req.query.dirname, function (error, response, body) {
		res.status(201).send('Server copy successful');
	})
});

// notify Calcium that a domain was uploaded and send directory name
router.get('/domain-notify', function (req, res) {
	request(SERVER_ADDRESS + 'domain-upload/' + req.query.dirname, function (error, response, body) {
		res.status(201).send('Server copy successful');
	})
});

router.post('/', uploadDomain, function (req, res, next) {
	// make local dir and move uploaded fiiles to it, then
	// call sftp function to send the files to Calcium

	exec('mkdir ./uploads/' + req.body.dirname, function (error, stdout, stderr) {
		if (error !== null) {
			res.status(500).send('Error uploading files');
		} else {
			exec('mv ./uploads/*.pddl ./uploads/' + req.body.dirname + '/',
				function (error, stdout, stderr) {
				if (error !== null) {
					res.status(500).send('Error uploading files');
				} else {
					sftpFiles(res, req.body.dirname)
				}
			});
		}
	});
});

router.post('/planner', uploadPlanner, function (req, res, next) {
	var org = req.body.org;
	if (!org) {
		org = "none";
	}

	// used for creating dirs and text file
	var fileName = req.file.originalname.replace(/ /g, '');
	// used for uploading file
	var originalFileName = req.file.originalname.replace(/ /g, '\\ ');

	// chain of shell commands. First a text file
	// is created with the sender's email and organization,
	// then a folder is created on Calcium, and finally the
	// text file and planner are sftp'd to Calcium. The local files
	// are then deleted and success is return to client
	fs.writeFile("./uploads/" + fileName + ".txt", "Sender: " + req.body.email + 
		", Organization: " + org, function (err) {
		
		if (err) {
			res.status(500).send(err);
		}

		exec(SFTP_COMMAND + "planners/uploads <<< $'mkdir " +
			fileName + "'", function (error, stdout, stderr) {
			if (error !== null) {
				res.status(500).send(error);
			} else {
				exec(SFTP_COMMAND + "planners/uploads/" + fileName + 
					" <<< $'put ./uploads/" + fileName + ".txt'",
				function (error, stdout, stderr) {
					if (error !== null) {
						res.status(500).send(error);
					} else {
						exec(SFTP_COMMAND + "planners/uploads/" + fileName + 
							" <<< $'put ./uploads/" + originalFileName + "'",
						function (error, stdout, stderr) {
							if (error !== null) {
								res.status(500).send(error);
							} else {
								exec("rm ./uploads/" + fileName + ".txt ./uploads/" +
									originalFileName, function (error, stdout, stderr) {
									if (error !== null) {
										res.status(500).send(error);
									} else {
										res.status(201).send(fileName);
									}
								});
							}
						});
					}
				});
			}
		});
	});
});

router.post('/domain-form', function (req, res) {
	request.post({url: SERVER_ADDRESS, form: req.body}, function (error, response, body) {
		if (error) {
			return console.error('upload failed:', error);
		}
		res.send(body);
	});
});

module.exports = router