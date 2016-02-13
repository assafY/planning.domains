angular.module('app')
.controller('CompetitionController', function($rootScope, $scope) {
	if (!$rootScope.leaderboard) {
        CompetitionService.fetchLeaderboard().success (function (leaderboard) {
            if (leaderboard) {
                $scope.leaderboard = $rootScope.leaderboard = leaderboard
            }
        })
    } else {
        $scope.leaderboard = $rootScope.leaderboard
    }
});