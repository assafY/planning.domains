angular.module('app')
.service('CompetitionService', function($http) {

    this.fetchLeaderboard = function() {
        return $http.get('/api/domains/leaderboard')
    }
});
