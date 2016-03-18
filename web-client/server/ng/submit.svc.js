angular.module('app')
.service('SubmitService', function($http, $location) {
		this.submitDomainForm = function (formData) {
			return $http({
				url: '/api/upload/domain-form',
				method: 'POST',
				data: formData
			})
		};

		this.plannerNotifyServer = function (dirName) {
			return $http({
				url: '/api/upload/planner-notify',
				method: 'GET',
				params: {'dirname': dirName}
			})
		}

		this.domainNotifyServer = function (dirName) {
			return $http({
				url: '/api/upload/domain-notify',
				method: 'GET',
				params: {'dirname': dirName}
			})
		}

		this.formSuccess = function () {
			$location.path('/submit/success');
		};

		this.formError = function () {
			$location.path('/submit/error');
		};
})