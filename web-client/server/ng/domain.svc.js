angular.module('app')
.service('DomainService', function($http, $location) {

	var localDomainId;

	this.fetchAll = function () {
		return $http.get('/api/domains');
	};

	this.fetchDomain = function(domainId) {
		localDomainId = domainId;
		return $http({
		    url: '/api/domains/' + domainId, 
		    method: "GET",
		    params: {'domainId': domainId}
		});
	};

	this.fetchProblemFile = function(problemFile) {
		return $http({
			url: '/api/domains/' + localDomainId + '/' + problemFile,
			method: "GET",
			params: {
				'domainId': localDomainId,
				'problemFile': problemFile
			}
		});
	};

	this.fetchDomainFile = function(domainFile) {
		return $http({
			url: '/api/domains/' + localDomainId + '/' + domainFile,
			method: "GET",
			params: {
				'domainId': localDomainId,
				'domainFile': domainFile
			}
		});
	};

	this.singleDomainView = function() {
		$location.path('/view/domain')
	};

	this.problemView = function() {
		$location.path('/view/domain/problem')
	};
	
	this.domainView = function() {
		$location.path('/view/domain/domain')
	};

});