var app = angular.module('app', []);

app.controller('DomainController', function($scope, DomainService) {
	DomainService.fetch().success (function (domains) {
		$scope.domains = domains
	});
});

app.service('DomainService', function($http) {
	this.fetch = function () {
		return $http.get('/api/domains');
	}
});