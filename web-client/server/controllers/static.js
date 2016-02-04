var express = require('express');
var router = express.Router();

//------------------------------
//	Style, JS, Images Folders
//------------------------------
router.use("/css", express.static(__dirname + '/../assets/css'));
router.use("/js", express.static(__dirname + '/../assets/js'));
router.use("/img", express.static(__dirname + '/../assets/img'));

router.get('/', function(req, res) {
	res.sendfile('layouts/app.html')
});

router.use(express.static(__dirname + '/../templates'))

module.exports = router;