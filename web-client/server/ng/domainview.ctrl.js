angular.module('app')
.controller('DomainViewController', function($rootScope, $scope, DomainService) {
	//DomainService.getCurrentDomain().success (function (domain) {
	$scope.domain = $rootScope.currentDomain;
	//});
})