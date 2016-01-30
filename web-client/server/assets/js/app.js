var app = angular.module('app', []);

app.controller('DomainController', function($scope, $location, DomainService) {
	DomainService.fetchAll().success (function (domains) {
		if (domains)
			$scope.domains = domains
	});
	$scope.getDomain = function(domainId) {
		DomainService.fetchDomain(domainId).success (function (domain) {
			$scope.domain = domain;
		})
	}
});

app.service('DomainService', function($http) {
	this.fetchAll = function () {
		return $http.get('/api/domains');
	}
	this.fetchDomain = function(domainId) {
		return $http({
		    url: '/api/domains/' + domainId, 
		    method: "GET",
		    params: {'domainId': domainId}
		});
	};
});