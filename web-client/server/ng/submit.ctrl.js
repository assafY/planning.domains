	angular.module('app')
.controller('SubmitController', function ($scope, $timeout, Upload, SubmitService) {
	
	$scope.domainFiles = [];
	$scope.problemFiles = [];
	$scope.publishDate = new Date();

	$scope.submitForm = function () {
		// check if domain and problem files were selected
		if ($scope.domainFiles.length > 0 && $scope.problemFiles.length > 0) {
			$scope.form.publishDate = $scope.publishDate;
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
					console.log(result)
					// handle form submission success
				})
			}
		}
	}

	$scope.uploadFiles = function (files) {
		

	}
})