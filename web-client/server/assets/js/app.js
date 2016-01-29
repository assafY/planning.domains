var app = angular.module('app', []);

app.controller('DomainController', function($scope, DomainService) {
	DomainService.fetchAll().success (function (domains) {
		$scope.domains = domains
	});
	$scope.getDomain = function(domainId) {
		DomainService.fetchDomain(domainId).success (function (domain) {
			// What do I do here?
			console.log(domain);
		})
	}
});

app.service('DomainService', function($http) {
	this.fetchAll = function () {
		return $http.get('/api/domains');
	}
	this.fetchDomain = function(domainId) {
		return $http.get('/api/domains/' + domainId, {
			params: {
				'domainId': domainId
			}
		});
	}
});