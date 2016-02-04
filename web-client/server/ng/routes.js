angular.module('app')
.config(function ($routeProvider) {
	$routeProvider
	.when('/', { controller: 'MainController', templateUrl: 'index.html' })
	.when('/view', { controller: 'DomainController', templateUrl: 'view.html' })
	.when('/view/domain', { controller: 'DomainViewController', templateUrl: 'domain-view.html' })
	.when('/view/domain/pddl', { controller: 'PddlViewController', templateUrl: 'pddl-view.html' })
	.when('/submit', { controller: 'SubmitController', templateUrl: 'submit.html' })
	.when('/competition', { controller: 'CompetitionController', templateUrl: 'competition.html' })
})