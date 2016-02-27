angular.module('app')
.controller('CompetitionController', function($rootScope, $scope, CompetitionService) {
	if (!$rootScope.leaderboard) {
        CompetitionService.fetchLeaderboard().success (function (leaderboard) {
            if (leaderboard) {
            	console.log(leaderboard)
                $scope.leaderboard = $rootScope.leaderboard = leaderboard
            }
        })
    } else {
        $scope.leaderboard = $rootScope.leaderboard
    }
});