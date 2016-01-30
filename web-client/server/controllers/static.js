var express = require('express');
var router = express.Router();

//------------------------------
//	Style, JS, Images Folders
//------------------------------
router.use("/css", express.static(__dirname + '/../assets/css'));
router.use("/js", express.static(__dirname + '/../assets/js'));
router.use("/img", express.static(__dirname + '/../assets/img'));

router.get('/', function(req, res) {
	res.sendfile('layouts/index.html')
});

router.get('/view', function(req, res) {
	res.sendfile('layouts/view.html')
});

router.get('/submit', function(req, res) {
	res.sendfile('layouts/submit.html')
});

router.get('/competition', function(req, res) {
	res.sendfile('layouts/competition.html')
});

router.get('/view/domain', function(req, res) {
	res.sendfile('layouts/domain-view.html')
});

module.exports = router;