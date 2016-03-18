angular.module('app')
.controller('SubmitController', function ($scope, $rootScope, $timeout, Upload, SubmitService) {
	
	$scope.allDomainFiles = [{id: 'domain1'}];
	$scope.publishDate = new Date();
	
	$scope.formSubmitted = function() {
		if ($rootScope.formSubmitted === true) {
			return true;
		}
		return false;
	}

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
		// check if the first domain and problem files pair was selected
		$scope.submitted = true; // necessary?
		if ($scope.allDomainFiles[0].domainFile && $scope.allDomainFiles[0].problemFiles) {
			$scope.form.publishDate = $scope.publishDate;
			$scope.form.domainFiles	= []	

			for (var i = 0; i < $scope.allDomainFiles.length; i++) {
				// if both a domain and problem files have been selected for this entry
				if ($scope.allDomainFiles[i].domainFile &&
					$scope.allDomainFiles[i].problemFiles) {

					problemFilesNames = []
					
					for (var j = 0; j < $scope.allDomainFiles[i].problemFiles.length; j++) {
						problemFilesNames.push($scope.allDomainFiles[i].problemFiles[j].name)
					}

					$scope.form.domainFiles[i] = {
						domainFile: $scope.allDomainFiles[i].domainFile.name,
						problemFiles: problemFilesNames
					}
				}
			}

			SubmitService.submitDomainForm($scope.form). success(function (dirname) {
					$rootScope.formSubmitted = true;

					$scope.uploadFiles(dirname).success (function () {
						SubmitService.domainNotifyServer(dirname).success(function () {
							SubmitService.formSuccess();
						})
					}).error (function () {
						SubmitService.formError();
					})
				});
		}
	}

	$scope.submitPlanner = function () {
		if ($scope.plannerForm.plannerFile) {
			$rootScope.formSubmitted = true;

			$scope.uploadPlanner().success (function (dirName) {
				SubmitService.plannerNotifyServer(dirName).success (function () {
					SubmitService.formSuccess();
				})
			}).error (function () {
				SubmitService.formError();
			})
		}
	}

	$scope.uploadFiles = function (dirname) {
		var files = []

		// fill files array with all files selected
		for (var i = 0; i < $scope.allDomainFiles.length; ++i) {
			// if both a domain and problem files have been selected for this entry
			if ($scope.allDomainFiles[i].domainFile &&
				$scope.allDomainFiles[i].problemFiles) {
				files.push($scope.allDomainFiles[i].domainFile)

				for (var j = 0; j < $scope.allDomainFiles[i].problemFiles.length; ++j) {
					files.push($scope.allDomainFiles[i].problemFiles[j])
				}
			}
		}

		// send files array to node js
		return Upload.upload({
				url: '/api/upload',
				arrayKey: '',
				data: {files: files, dirname: dirname}
			})
	}

	$scope.uploadPlanner = function () {
		return Upload.upload({
			url: '/api/upload/planner',
			data: {
				file: $scope.plannerForm.plannerFile,
				email: $scope.plannerForm.email,
				org: $scope.plannerForm.org
			}
		})
	}
})