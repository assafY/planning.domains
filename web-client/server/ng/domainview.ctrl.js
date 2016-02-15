angular.module('app')
.controller('DomainViewController', function($rootScope, $scope, DomainService) {
	

	$scope.domain = $rootScope.currentDomain;

	$scope.getPddlFile = function(fileName) {
		DomainService.fetchPddlFile(fileName).success (function (pddl) {
			if (pddl)
				$rootScope.pddl = pddl;
			DomainService.problemView()
		})
	}
});