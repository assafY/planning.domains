angular.module('app')
.controller('DomainController', function($rootScope, $scope, DomainService) {
	DomainService.fetchAll().success (function (domains) {
		if (domains)
			$scope.domains = domains
	});
	$scope.getDomain = function(domainId) {
		DomainService.fetchDomain(domainId).success (function (domain) {
			$rootScope.currentDomain = domain;
			DomainService.singleDomainView();
		})
	}
});