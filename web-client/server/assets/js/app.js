angular.module("app",["ngRoute"]),angular.module("app").controller("CompetitionController",["$scope",function(o){}]),angular.module("app").controller("DomainController",["$rootScope","$scope","DomainService",function(o,n,e){e.fetchAll().success(function(o){o&&(n.domains=o)}),n.getDomain=function(n){e.fetchDomain(n).success(function(n){o.currentDomain=n,e.singleDomainView()})}}]),angular.module("app").service("DomainService",["$http","$location",function(o,n){var e;this.fetchAll=function(){return o.get("/api/domains")},this.fetchDomain=function(n){return e=n,o({url:"/api/domains/"+n,method:"GET",params:{domainId:n}})},this.fetchPddlFile=function(n){return o({url:"/api/domains/"+e+"/"+n,method:"GET",params:{domainId:e,fileName:n}})},this.singleDomainView=function(){n.path("/view/domain")},this.problemView=function(){n.path("/view/domain/pddl")},this.domainView=function(){n.path("/view/domain/pddl")}}]),angular.module("app").controller("DomainViewController",["$rootScope","$scope","DomainService",function(o,n,e){n.domain=o.currentDomain,n.getPddlFile=function(n){e.fetchPddlFile(n).success(function(n){n&&(o.pddl=n),e.problemView()})}}]),angular.module("app").controller("MainController",["$scope",function(o){}]),angular.module("app").controller("PddlViewController",["$rootScope","$scope",function(o,n){n.pddl=o.pddl}]),angular.module("app").config(["$routeProvider",function(o){o.when("/view",{controller:"DomainController",templateUrl:"view.html"}).when("/view/domain",{controller:"DomainViewController",templateUrl:"domain-view.html"}).when("/view/domain/pddl",{controller:"PddlViewController",templateUrl:"pddl-view.html"}).when("/submit",{controller:"SubmitController",templateUrl:"submit.html"}).when("/competition",{controller:"CompetitionController",templateUrl:"competition.html"})}]),angular.module("app").controller("SubmitController",["$scope",function(o){}]);
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIm1vZHVsZS5qcyIsImNvbXBldGl0aW9uLmN0cmwuanMiLCJkb21haW4uY3RybC5qcyIsImRvbWFpbi5zdmMuanMiLCJkb21haW52aWV3LmN0cmwuanMiLCJtYWluLmN0cmwuanMiLCJwZGRsdmlldy5jdHJsLmpzIiwicm91dGVzLmpzIiwic3VibWl0LmN0cmwuanMiXSwibmFtZXMiOlsiYW5ndWxhciIsIm1vZHVsZSIsImNvbnRyb2xsZXIiLCIkc2NvcGUiLCIkcm9vdFNjb3BlIiwiRG9tYWluU2VydmljZSIsImZldGNoQWxsIiwic3VjY2VzcyIsImRvbWFpbnMiLCJnZXREb21haW4iLCJkb21haW5JZCIsImZldGNoRG9tYWluIiwiZG9tYWluIiwiY3VycmVudERvbWFpbiIsInNpbmdsZURvbWFpblZpZXciLCJzZXJ2aWNlIiwiJGh0dHAiLCIkbG9jYXRpb24iLCJsb2NhbERvbWFpbklkIiwidGhpcyIsImdldCIsInVybCIsIm1ldGhvZCIsInBhcmFtcyIsImZldGNoUGRkbEZpbGUiLCJmaWxlTmFtZSIsInBhdGgiLCJwcm9ibGVtVmlldyIsImRvbWFpblZpZXciLCJnZXRQZGRsRmlsZSIsInBkZGwiLCJjb25maWciLCIkcm91dGVQcm92aWRlciIsIndoZW4iLCJ0ZW1wbGF0ZVVybCJdLCJtYXBwaW5ncyI6IkFBQUFBLFFBQUFDLE9BQUEsT0FDQSxZQ0RBRCxRQUFBQyxPQUFBLE9BQ0FDLFdBQUEseUJBQUEsU0FBQSxTQUFBQyxPQ0RBSCxRQUFBQyxPQUFBLE9BQ0FDLFdBQUEsb0JBQUEsYUFBQSxTQUFBLGdCQUFBLFNBQUFFLEVBQUFELEVBQUFFLEdBQ0FBLEVBQUFDLFdBQUFDLFFBQUEsU0FBQUMsR0FDQUEsSUFDQUwsRUFBQUssUUFBQUEsS0FFQUwsRUFBQU0sVUFBQSxTQUFBQyxHQUNBTCxFQUFBTSxZQUFBRCxHQUFBSCxRQUFBLFNBQUFLLEdBQ0FSLEVBQUFTLGNBQUFELEVBQ0FQLEVBQUFTLHlCQ1RBZCxRQUFBQyxPQUFBLE9BQ0FjLFFBQUEsaUJBQUEsUUFBQSxZQUFBLFNBQUFDLEVBQUFDLEdBRUEsR0FBQUMsRUFFQUMsTUFBQWIsU0FBQSxXQUNBLE1BQUFVLEdBQUFJLElBQUEsaUJBR0FELEtBQUFSLFlBQUEsU0FBQUQsR0FFQSxNQURBUSxHQUFBUixFQUNBTSxHQUNBSyxJQUFBLGdCQUFBWCxFQUNBWSxPQUFBLE1BQ0FDLFFBQUFiLFNBQUFBLE1BSUFTLEtBQUFLLGNBQUEsU0FBQUMsR0FDQSxNQUFBVCxJQUNBSyxJQUFBLGdCQUFBSCxFQUFBLElBQUFPLEVBQ0FILE9BQUEsTUFDQUMsUUFDQWIsU0FBQVEsRUFDQU8sU0FBQUEsTUFLQU4sS0FBQUwsaUJBQUEsV0FDQUcsRUFBQVMsS0FBQSxpQkFHQVAsS0FBQVEsWUFBQSxXQUNBVixFQUFBUyxLQUFBLHNCQUdBUCxLQUFBUyxXQUFBLFdBQ0FYLEVBQUFTLEtBQUEseUJDdENBMUIsUUFBQUMsT0FBQSxPQUNBQyxXQUFBLHdCQUFBLGFBQUEsU0FBQSxnQkFBQSxTQUFBRSxFQUFBRCxFQUFBRSxHQUNBRixFQUFBUyxPQUFBUixFQUFBUyxjQUVBVixFQUFBMEIsWUFBQSxTQUFBSixHQUNBcEIsRUFBQW1CLGNBQUFDLEdBQUFsQixRQUFBLFNBQUF1QixHQUNBQSxJQUNBMUIsRUFBQTBCLEtBQUFBLEdBQ0F6QixFQUFBc0Isb0JDUkEzQixRQUFBQyxPQUFBLE9BQ0FDLFdBQUEsa0JBQUEsU0FBQSxTQUFBQyxPQ0RBSCxRQUFBQyxPQUFBLE9BQ0FDLFdBQUEsc0JBQUEsYUFBQSxTQUFBLFNBQUFFLEVBQUFELEdBQ0FBLEVBQUEyQixLQUFBMUIsRUFBQTBCLFFDRkE5QixRQUFBQyxPQUFBLE9BQ0E4QixRQUFBLGlCQUFBLFNBQUFDLEdBQ0FBLEVBRUFDLEtBQUEsU0FBQS9CLFdBQUEsbUJBQUFnQyxZQUFBLGNBQ0FELEtBQUEsZ0JBQUEvQixXQUFBLHVCQUFBZ0MsWUFBQSxxQkFDQUQsS0FBQSxxQkFBQS9CLFdBQUEscUJBQUFnQyxZQUFBLG1CQUNBRCxLQUFBLFdBQUEvQixXQUFBLG1CQUFBZ0MsWUFBQSxnQkFDQUQsS0FBQSxnQkFBQS9CLFdBQUEsd0JBQUFnQyxZQUFBLHdCQ1JBbEMsUUFBQUMsT0FBQSxPQUNBQyxXQUFBLG9CQUFBLFNBQUEsU0FBQUMiLCJmaWxlIjoiYXBwLmpzIiwic291cmNlc0NvbnRlbnQiOlsiYW5ndWxhci5tb2R1bGUoJ2FwcCcsIFtcblx0J25nUm91dGUnXG5dKSIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ0NvbXBldGl0aW9uQ29udHJvbGxlcicsIGZ1bmN0aW9uKCRzY29wZSkge1xuXHRcbn0pIiwiYW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uY29udHJvbGxlcignRG9tYWluQ29udHJvbGxlcicsIGZ1bmN0aW9uKCRyb290U2NvcGUsICRzY29wZSwgRG9tYWluU2VydmljZSkge1xuXHREb21haW5TZXJ2aWNlLmZldGNoQWxsKCkuc3VjY2VzcyAoZnVuY3Rpb24gKGRvbWFpbnMpIHtcblx0XHRpZiAoZG9tYWlucylcblx0XHRcdCRzY29wZS5kb21haW5zID0gZG9tYWluc1xuXHR9KTtcblx0JHNjb3BlLmdldERvbWFpbiA9IGZ1bmN0aW9uKGRvbWFpbklkKSB7XG5cdFx0RG9tYWluU2VydmljZS5mZXRjaERvbWFpbihkb21haW5JZCkuc3VjY2VzcyAoZnVuY3Rpb24gKGRvbWFpbikge1xuXHRcdFx0JHJvb3RTY29wZS5jdXJyZW50RG9tYWluID0gZG9tYWluO1xuXHRcdFx0RG9tYWluU2VydmljZS5zaW5nbGVEb21haW5WaWV3KCk7XG5cdFx0fSlcblx0fVxufSk7IiwiYW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uc2VydmljZSgnRG9tYWluU2VydmljZScsIGZ1bmN0aW9uKCRodHRwLCAkbG9jYXRpb24pIHtcblxuXHR2YXIgbG9jYWxEb21haW5JZDtcblxuXHR0aGlzLmZldGNoQWxsID0gZnVuY3Rpb24gKCkge1xuXHRcdHJldHVybiAkaHR0cC5nZXQoJy9hcGkvZG9tYWlucycpO1xuXHR9O1xuXG5cdHRoaXMuZmV0Y2hEb21haW4gPSBmdW5jdGlvbihkb21haW5JZCkge1xuXHRcdGxvY2FsRG9tYWluSWQgPSBkb21haW5JZDtcblx0XHRyZXR1cm4gJGh0dHAoe1xuXHRcdCAgICB1cmw6ICcvYXBpL2RvbWFpbnMvJyArIGRvbWFpbklkLCBcblx0XHQgICAgbWV0aG9kOiBcIkdFVFwiLFxuXHRcdCAgICBwYXJhbXM6IHsnZG9tYWluSWQnOiBkb21haW5JZH1cblx0XHR9KTtcblx0fTtcblxuXHR0aGlzLmZldGNoUGRkbEZpbGUgPSBmdW5jdGlvbihmaWxlTmFtZSkge1xuXHRcdHJldHVybiAkaHR0cCh7XG5cdFx0XHR1cmw6ICcvYXBpL2RvbWFpbnMvJyArIGxvY2FsRG9tYWluSWQgKyAnLycgKyBmaWxlTmFtZSxcblx0XHRcdG1ldGhvZDogXCJHRVRcIixcblx0XHRcdHBhcmFtczoge1xuXHRcdFx0XHQnZG9tYWluSWQnOiBsb2NhbERvbWFpbklkLFxuXHRcdFx0XHQnZmlsZU5hbWUnOiBmaWxlTmFtZVxuXHRcdFx0fVxuXHRcdH0pO1xuXHR9O1xuXG5cdHRoaXMuc2luZ2xlRG9tYWluVmlldyA9IGZ1bmN0aW9uKCkge1xuXHRcdCRsb2NhdGlvbi5wYXRoKCcvdmlldy9kb21haW4nKVxuXHR9O1xuXG5cdHRoaXMucHJvYmxlbVZpZXcgPSBmdW5jdGlvbigpIHtcblx0XHQkbG9jYXRpb24ucGF0aCgnL3ZpZXcvZG9tYWluL3BkZGwnKVxuXHR9O1xuXHRcblx0dGhpcy5kb21haW5WaWV3ID0gZnVuY3Rpb24oKSB7XG5cdFx0JGxvY2F0aW9uLnBhdGgoJy92aWV3L2RvbWFpbi9wZGRsJylcblx0fTtcblxufSk7IiwiYW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uY29udHJvbGxlcignRG9tYWluVmlld0NvbnRyb2xsZXInLCBmdW5jdGlvbigkcm9vdFNjb3BlLCAkc2NvcGUsIERvbWFpblNlcnZpY2UpIHtcblx0JHNjb3BlLmRvbWFpbiA9ICRyb290U2NvcGUuY3VycmVudERvbWFpblxuXG5cdCRzY29wZS5nZXRQZGRsRmlsZSA9IGZ1bmN0aW9uKGZpbGVOYW1lKSB7XG5cdFx0RG9tYWluU2VydmljZS5mZXRjaFBkZGxGaWxlKGZpbGVOYW1lKS5zdWNjZXNzIChmdW5jdGlvbiAocGRkbCkge1xuXHRcdFx0aWYgKHBkZGwpXG5cdFx0XHRcdCRyb290U2NvcGUucGRkbCA9IHBkZGxcblx0XHRcdERvbWFpblNlcnZpY2UucHJvYmxlbVZpZXcoKVxuXHRcdH0pXG5cdH1cbn0pIiwiYW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uY29udHJvbGxlcignTWFpbkNvbnRyb2xsZXInLCBmdW5jdGlvbigkc2NvcGUpIHtcblx0XG59KSIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ1BkZGxWaWV3Q29udHJvbGxlcicsIGZ1bmN0aW9uICgkcm9vdFNjb3BlLCAkc2NvcGUpIHtcblx0JHNjb3BlLnBkZGwgPSAkcm9vdFNjb3BlLnBkZGxcbn0pIiwiYW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uY29uZmlnKGZ1bmN0aW9uICgkcm91dGVQcm92aWRlcikge1xuXHQkcm91dGVQcm92aWRlclxuXHQvLy53aGVuKCcvJywgeyBjb250cm9sbGVyOiAnTWFpbkNvbnRyb2xsZXInLCB0ZW1wbGF0ZVVybDogJ2luZGV4Lmh0bWwnIH0pXG5cdC53aGVuKCcvdmlldycsIHsgY29udHJvbGxlcjogJ0RvbWFpbkNvbnRyb2xsZXInLCB0ZW1wbGF0ZVVybDogJ3ZpZXcuaHRtbCcgfSlcblx0LndoZW4oJy92aWV3L2RvbWFpbicsIHsgY29udHJvbGxlcjogJ0RvbWFpblZpZXdDb250cm9sbGVyJywgdGVtcGxhdGVVcmw6ICdkb21haW4tdmlldy5odG1sJyB9KVxuXHQud2hlbignL3ZpZXcvZG9tYWluL3BkZGwnLCB7IGNvbnRyb2xsZXI6ICdQZGRsVmlld0NvbnRyb2xsZXInLCB0ZW1wbGF0ZVVybDogJ3BkZGwtdmlldy5odG1sJyB9KVxuXHQud2hlbignL3N1Ym1pdCcsIHsgY29udHJvbGxlcjogJ1N1Ym1pdENvbnRyb2xsZXInLCB0ZW1wbGF0ZVVybDogJ3N1Ym1pdC5odG1sJyB9KVxuXHQud2hlbignL2NvbXBldGl0aW9uJywgeyBjb250cm9sbGVyOiAnQ29tcGV0aXRpb25Db250cm9sbGVyJywgdGVtcGxhdGVVcmw6ICdjb21wZXRpdGlvbi5odG1sJyB9KVxufSkiLCJhbmd1bGFyLm1vZHVsZSgnYXBwJylcbi5jb250cm9sbGVyKCdTdWJtaXRDb250cm9sbGVyJywgZnVuY3Rpb24oJHNjb3BlKSB7XG5cdFxufSkiXSwic291cmNlUm9vdCI6Ii9zb3VyY2UvIn0=
