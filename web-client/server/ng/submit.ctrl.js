	angular.module('app')
.controller('SubmitController', function ($scope, $timeout, Upload, SubmitService) {
	
	$scope.allDomainFiles = [{id: 'domain1'}];
	//$scope.domainFiles = [];
	//$scope.problemFiles = [];
	$scope.publishDate = new Date();
	

	$scope.addDomain = function() {
		if ($scope.allDomainFiles[$scope.allDomainFiles.length - 1].domainFile &&
			$scope.allDomainFiles[$scope.allDomainFiles.length - 1].problemFiles) {
			var newDomainId = $scope.allDomainFiles.length + 1;
			$scope.allDomainFiles.push({'id':'domain' + newDomainId});
		}
	};

	$scope.getUploadButton = function (id) {
		angular.element(document.getElementById(id).click());
	}

	$scope.submitForm = function () {
		// check if domain and problem files were selected
		$scope.submitted = true;
		if ($scope.allDomainFiles[0].domainFile && $scope.allDomainFiles[0].problemFiles) {
			$scope.form.publishDate = $scope.publishDate;
			$scope.form.domainFiles	= []	

			for (var i = 0; i < $scope.allDomainFiles.length; i++) {
				problemFilesNames = []
				
				for (var j = 0; j < $scope.allDomainFiles[i].problemFiles.length; j++) {
					problemFilesNames.push($scope.allDomainFiles[i].problemFiles[j].name)
					
				}

				$scope.form.domainFiles[i] = {
					domainFile: $scope.allDomainFiles[i].domainFile.name,
					problemFiles: problemFilesNames
				}

				SubmitService.submitDomainForm($scope.form). success(function (dirname) {
					$scope.uploadFiles(dirname)
					// handle form submission success
				})
			}
		}
	}

	$scope.uploadFiles = function (dirname) {
		for (var i = 0; i < $scope.allDomainFiles.length; ++i) {
			var file = $scope.allDomainFiles[i].domainFile;
			Upload.upload({
				url: '/api/upload',
				data: {file: file, dirname: dirname}
			})

			for (var j = 0; j < $scope.allDomainFiles[i].problemFiles.length; ++j) {
				file = $scope.allDomainFiles[i].problemFiles[j];

				Upload.upload({
					url: '/api/upload',
					data: {file: file, dirname: dirname}
				})
			}
		}
	}
})