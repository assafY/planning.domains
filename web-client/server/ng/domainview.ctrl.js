angular.module('app')
.controller('DomainViewController', function($rootScope, $scope, DomainService) {
	$scope.domain = $rootScope.currentDomain;

	$scope.getProblemFile = function(problemFile) {
		DomainService.fetchProblemFile(problemFile).success (function (problem) {
			if (problem)
				$scope.problem = problem;
			DomainService.problemView();
		});
	};

	$scope.getDomainFile = function(domainFile) {
		DomainService.fetchDomainFile(domainFile).success (function(domainF) {
			if (domainF)
				$scope.domainF = domainF;
			DomainService.domainView();
		});
	};
})