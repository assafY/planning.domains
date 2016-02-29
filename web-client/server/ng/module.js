angular.module('app', [
	'ngMaterial',
	'ngAnimate',
	'ngRoute'
]).run(function($rootScope, $location, $timeout) {
    $rootScope.$on('$viewContentLoaded', function() {
        $timeout(function() {
            componentHandler.upgradeAllRegistered();
        });
    });
});