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

	this.fetchPddlFile = function(fileName) {
		return $http({
			url: '/api/domains/' + localDomainId + '/' + fileName,
			method: "GET",
			params: {
				'domainId': localDomainId,
				'fileName': fileName
			}
		});
	};

	/*this.fetchDomainFile = function(domainFile) {
		return $http({
			url: '/api/domains/' + localDomainId + '/' + domainFile,
			method: "GET",
			params: {
				'domainId': localDomainId,
				'domainFile': domainFile
			}
		});
	};*/

	this.singleDomainView = function() {
		$location.path('/view/domain')
	};

	this.problemView = function() {
		$location.path('/view/domain/pddl')
	};
	
	this.domainView = function() {
		$location.path('/view/domain/pddl')
	};

});