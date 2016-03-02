angular.module('app')
.controller('SubmitController', function ($scope, $timeout, Upload, SubmitService) {
	
	$scope.domainFiles = [];
	$scope.problemFiles = [];

	$scope.submitForm = function () {
		console.log('this is happening')
		$scope.form.domainFiles	= []	

		for (var i = 0; i < $scope.domainFiles.length; i++) {
			problemFilesNames = []
			for (var j = 0; j < $scope.problemFiles[i].length; j++) {
				problemFilesNames.push($scope.problemFiles[i][j].name)
			}

			$scope.form.domainFiles[i] = {
				domainFile: $scope.domainFiles[i].name,
				problemFiles: problemFilesNames
			}

			SubmitService.submitDomainForm($scope.form). success(function (result) {
				console.log("submission success!")
				// handle form submission success
			})
		}
	}

	$scope.testSubmit = function() {
		console.log("form submitted");
	}

	$scope.uploadFiles = function (files) {
		

	}
})