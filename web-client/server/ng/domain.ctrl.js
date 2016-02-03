angular.module('app')
.controller('DomainController', function($rootScope, $scope, DomainService) {
	if (!$rootScope.domains) {
		DomainService.fetchAll().success (function (domains) {
			if (domains)
				$scope.domains = $rootScope.domains = domains;
		});
	} else {
		$scope.domains = $rootScope.domains;
	}
	$scope.getDomain = function(domainId) {
		DomainService.fetchDomain(domainId).success (function (domain) {
			$rootScope.currentDomain = domain;
			DomainService.singleDomainView();
		});
	}
});