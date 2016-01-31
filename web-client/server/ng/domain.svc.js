angular.module('app')
.service('DomainService', function($http, $location) {
	this.fetchAll = function () {
		return $http.get('/api/domains');
	}
	this.fetchDomain = function(domainId) {
		return $http({
		    url: '/api/domains/' + domainId, 
		    method: "GET",
		    params: {'domainId': domainId}
		});
	};
	this.singleDomainView = function() {
		$location.path('/view/domain')
	}

});