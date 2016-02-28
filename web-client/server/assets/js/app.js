angular.module("app",["ngRoute"]).run(["$rootScope","$location","$timeout",function(o,e,n){o.$on("$viewContentLoaded",function(){n(function(){componentHandler.upgradeAllRegistered()})})}]),angular.module("app").controller("CompetitionController",["$rootScope","$scope","CompetitionService",function(o,e,n){o.leaderboard?e.leaderboard=o.leaderboard:n.fetchLeaderboard().success(function(n){n&&(console.log(n),e.leaderboard=o.leaderboard=n)})}]),angular.module("app").service("CompetitionService",["$http",function(o){this.fetchLeaderboard=function(){return o.get("/api/domains/leaderboard")}}]),angular.module("app").controller("DomainController",["$rootScope","$scope","DomainService",function(o,e,n){o.domains?e.domains=o.domains:n.fetchAll().success(function(n){n&&(e.domains=o.domains=n)}),e.getDomain=function(e){n.fetchDomain(e).success(function(e){o.currentDomain=e,n.singleDomainView()})}}]),angular.module("app").service("DomainService",["$http","$location",function(o,e){var n;this.fetchAll=function(){return o.get("/api/domains")},this.fetchDomain=function(e){return n=e,o({url:"/api/domains/"+e,method:"GET",params:{domainId:e}})},this.fetchPddlFile=function(e){return o({url:"/api/domains/"+n+"/"+e,method:"GET",params:{domainId:n,fileName:e}})},this.singleDomainView=function(){e.path("/view/domain")},this.problemView=function(){e.path("/view/domain/pddl")},this.domainView=function(){e.path("/view/domain/pddl")}}]),angular.module("app").controller("DomainViewController",["$rootScope","$scope","DomainService",function(o,e,n){e.domain=o.currentDomain,e.getPddlFile=function(e){n.fetchPddlFile(e).success(function(e){e&&(o.pddl=e),n.problemView()})}}]),angular.module("app").controller("MainController",["$scope",function(o){}]),angular.module("app").controller("PddlViewController",["$rootScope","$scope",function(o,e){e.pddl=o.pddl}]),angular.module("app").config(["$routeProvider",function(o){o.when("/",{controller:"MainController",templateUrl:"index.html"}).when("/view",{controller:"DomainController",templateUrl:"view.html"}).when("/view/domain",{controller:"DomainViewController",templateUrl:"domain-view.html"}).when("/view/domain/pddl",{controller:"PddlViewController",templateUrl:"pddl-view.html"}).when("/submit",{controller:"SubmitController",templateUrl:"submit.html"}).when("/competition",{controller:"CompetitionController",templateUrl:"competition.html"})}]),angular.module("app").controller("SubmitController",["$scope","SubmitService",function(o,e){o.something=1}]),angular.module("app").service("SubmitService",["$http",function(o){}]);
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIm1vZHVsZS5qcyIsImNvbXBldGl0aW9uLmN0cmwuanMiLCJjb21wZXRpdGlvbi5zdmMuanMiLCJkb21haW4uY3RybC5qcyIsImRvbWFpbi5zdmMuanMiLCJkb21haW52aWV3LmN0cmwuanMiLCJtYWluLmN0cmwuanMiLCJwZGRsdmlldy5jdHJsLmpzIiwicm91dGVzLmpzIiwic3VibWl0LmN0cmwuanMiLCJzdWJtaXQuc3ZjLmpzIl0sIm5hbWVzIjpbImFuZ3VsYXIiLCJtb2R1bGUiLCJydW4iLCIkcm9vdFNjb3BlIiwiJGxvY2F0aW9uIiwiJHRpbWVvdXQiLCIkb24iLCJjb21wb25lbnRIYW5kbGVyIiwidXBncmFkZUFsbFJlZ2lzdGVyZWQiLCJjb250cm9sbGVyIiwiJHNjb3BlIiwiQ29tcGV0aXRpb25TZXJ2aWNlIiwibGVhZGVyYm9hcmQiLCJmZXRjaExlYWRlcmJvYXJkIiwic3VjY2VzcyIsImNvbnNvbGUiLCJsb2ciLCJzZXJ2aWNlIiwiJGh0dHAiLCJ0aGlzIiwiZ2V0IiwiRG9tYWluU2VydmljZSIsImRvbWFpbnMiLCJmZXRjaEFsbCIsImdldERvbWFpbiIsImRvbWFpbklkIiwiZmV0Y2hEb21haW4iLCJkb21haW4iLCJjdXJyZW50RG9tYWluIiwic2luZ2xlRG9tYWluVmlldyIsImxvY2FsRG9tYWluSWQiLCJ1cmwiLCJtZXRob2QiLCJwYXJhbXMiLCJmZXRjaFBkZGxGaWxlIiwiZmlsZU5hbWUiLCJwYXRoIiwicHJvYmxlbVZpZXciLCJkb21haW5WaWV3IiwiZ2V0UGRkbEZpbGUiLCJwZGRsIiwiY29uZmlnIiwiJHJvdXRlUHJvdmlkZXIiLCJ3aGVuIiwidGVtcGxhdGVVcmwiLCJTdWJtaXRTZXJ2aWNlIiwic29tZXRoaW5nIl0sIm1hcHBpbmdzIjoiQUFBQUEsUUFBQUMsT0FBQSxPQUNBLFlBQ0FDLEtBQUEsYUFBQSxZQUFBLFdBQUEsU0FBQUMsRUFBQUMsRUFBQUMsR0FDQUYsRUFBQUcsSUFBQSxxQkFBQSxXQUNBRCxFQUFBLFdBQ0FFLGlCQUFBQyw4QkNMQVIsUUFBQUMsT0FBQSxPQUNBUSxXQUFBLHlCQUFBLGFBQUEsU0FBQSxxQkFBQSxTQUFBTixFQUFBTyxFQUFBQyxHQUNBUixFQUFBUyxZQVFBRixFQUFBRSxZQUFBVCxFQUFBUyxZQVBBRCxFQUFBRSxtQkFBQUMsUUFBQSxTQUFBRixHQUNBQSxJQUNBRyxRQUFBQyxJQUFBSixHQUNBRixFQUFBRSxZQUFBVCxFQUFBUyxZQUFBQSxRQ05BWixRQUFBQyxPQUFBLE9BQ0FnQixRQUFBLHNCQUFBLFFBQUEsU0FBQUMsR0FFQUMsS0FBQU4saUJBQUEsV0FDQSxNQUFBSyxHQUFBRSxJQUFBLGdDQ0pBcEIsUUFBQUMsT0FBQSxPQUNBUSxXQUFBLG9CQUFBLGFBQUEsU0FBQSxnQkFBQSxTQUFBTixFQUFBTyxFQUFBVyxHQUNBbEIsRUFBQW1CLFFBTUFaLEVBQUFZLFFBQUFuQixFQUFBbUIsUUFMQUQsRUFBQUUsV0FBQVQsUUFBQSxTQUFBUSxHQUNBQSxJQUNBWixFQUFBWSxRQUFBbkIsRUFBQW1CLFFBQUFBLEtBS0FaLEVBQUFjLFVBQUEsU0FBQUMsR0FDQUosRUFBQUssWUFBQUQsR0FBQVgsUUFBQSxTQUFBYSxHQUNBeEIsRUFBQXlCLGNBQUFELEVBQ0FOLEVBQUFRLHlCQ2JBN0IsUUFBQUMsT0FBQSxPQUNBZ0IsUUFBQSxpQkFBQSxRQUFBLFlBQUEsU0FBQUMsRUFBQWQsR0FFQSxHQUFBMEIsRUFFQVgsTUFBQUksU0FBQSxXQUNBLE1BQUFMLEdBQUFFLElBQUEsaUJBR0FELEtBQUFPLFlBQUEsU0FBQUQsR0FFQSxNQURBSyxHQUFBTCxFQUNBUCxHQUNBYSxJQUFBLGdCQUFBTixFQUNBTyxPQUFBLE1BQ0FDLFFBQUFSLFNBQUFBLE1BSUFOLEtBQUFlLGNBQUEsU0FBQUMsR0FDQSxNQUFBakIsSUFDQWEsSUFBQSxnQkFBQUQsRUFBQSxJQUFBSyxFQUNBSCxPQUFBLE1BQ0FDLFFBQ0FSLFNBQUFLLEVBQ0FLLFNBQUFBLE1BS0FoQixLQUFBVSxpQkFBQSxXQUNBekIsRUFBQWdDLEtBQUEsaUJBR0FqQixLQUFBa0IsWUFBQSxXQUNBakMsRUFBQWdDLEtBQUEsc0JBR0FqQixLQUFBbUIsV0FBQSxXQUNBbEMsRUFBQWdDLEtBQUEseUJDdENBcEMsUUFBQUMsT0FBQSxPQUNBUSxXQUFBLHdCQUFBLGFBQUEsU0FBQSxnQkFBQSxTQUFBTixFQUFBTyxFQUFBVyxHQUdBWCxFQUFBaUIsT0FBQXhCLEVBQUF5QixjQUVBbEIsRUFBQTZCLFlBQUEsU0FBQUosR0FDQWQsRUFBQWEsY0FBQUMsR0FBQXJCLFFBQUEsU0FBQTBCLEdBQ0FBLElBQ0FyQyxFQUFBcUMsS0FBQUEsR0FDQW5CLEVBQUFnQixvQkNWQXJDLFFBQUFDLE9BQUEsT0FDQVEsV0FBQSxrQkFBQSxTQUFBLFNBQUFDLE9DREFWLFFBQUFDLE9BQUEsT0FDQVEsV0FBQSxzQkFBQSxhQUFBLFNBQUEsU0FBQU4sRUFBQU8sR0FDQUEsRUFBQThCLEtBQUFyQyxFQUFBcUMsUUNGQXhDLFFBQUFDLE9BQUEsT0FDQXdDLFFBQUEsaUJBQUEsU0FBQUMsR0FDQUEsRUFDQUMsS0FBQSxLQUFBbEMsV0FBQSxpQkFBQW1DLFlBQUEsZUFDQUQsS0FBQSxTQUFBbEMsV0FBQSxtQkFBQW1DLFlBQUEsY0FDQUQsS0FBQSxnQkFBQWxDLFdBQUEsdUJBQUFtQyxZQUFBLHFCQUNBRCxLQUFBLHFCQUFBbEMsV0FBQSxxQkFBQW1DLFlBQUEsbUJBQ0FELEtBQUEsV0FBQWxDLFdBQUEsbUJBQUFtQyxZQUFBLGdCQUNBRCxLQUFBLGdCQUFBbEMsV0FBQSx3QkFBQW1DLFlBQUEsd0JDUkE1QyxRQUFBQyxPQUFBLE9BQ0FRLFdBQUEsb0JBQUEsU0FBQSxnQkFBQSxTQUFBQyxFQUFBbUMsR0FDQW5DLEVBQUFvQyxVQUFBLEtDRkE5QyxRQUFBQyxPQUFBLE9BQ0FnQixRQUFBLGlCQUFBLFFBQUEsU0FBQUMiLCJmaWxlIjoiYXBwLmpzIiwic291cmNlc0NvbnRlbnQiOlsiYW5ndWxhci5tb2R1bGUoJ2FwcCcsIFtcblx0J25nUm91dGUnXG5dKS5ydW4oZnVuY3Rpb24oJHJvb3RTY29wZSwgJGxvY2F0aW9uLCAkdGltZW91dCkge1xuICAgICRyb290U2NvcGUuJG9uKCckdmlld0NvbnRlbnRMb2FkZWQnLCBmdW5jdGlvbigpIHtcbiAgICAgICAgJHRpbWVvdXQoZnVuY3Rpb24oKSB7XG4gICAgICAgICAgICBjb21wb25lbnRIYW5kbGVyLnVwZ3JhZGVBbGxSZWdpc3RlcmVkKCk7XG4gICAgICAgIH0pO1xuICAgIH0pO1xufSk7IiwiYW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uY29udHJvbGxlcignQ29tcGV0aXRpb25Db250cm9sbGVyJywgZnVuY3Rpb24oJHJvb3RTY29wZSwgJHNjb3BlLCBDb21wZXRpdGlvblNlcnZpY2UpIHtcblx0aWYgKCEkcm9vdFNjb3BlLmxlYWRlcmJvYXJkKSB7XG4gICAgICAgIENvbXBldGl0aW9uU2VydmljZS5mZXRjaExlYWRlcmJvYXJkKCkuc3VjY2VzcyAoZnVuY3Rpb24gKGxlYWRlcmJvYXJkKSB7XG4gICAgICAgICAgICBpZiAobGVhZGVyYm9hcmQpIHtcbiAgICAgICAgICAgIFx0Y29uc29sZS5sb2cobGVhZGVyYm9hcmQpXG4gICAgICAgICAgICAgICAgJHNjb3BlLmxlYWRlcmJvYXJkID0gJHJvb3RTY29wZS5sZWFkZXJib2FyZCA9IGxlYWRlcmJvYXJkXG4gICAgICAgICAgICB9XG4gICAgICAgIH0pXG4gICAgfSBlbHNlIHtcbiAgICAgICAgJHNjb3BlLmxlYWRlcmJvYXJkID0gJHJvb3RTY29wZS5sZWFkZXJib2FyZFxuICAgIH1cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLnNlcnZpY2UoJ0NvbXBldGl0aW9uU2VydmljZScsIGZ1bmN0aW9uKCRodHRwKSB7XG5cbiAgICB0aGlzLmZldGNoTGVhZGVyYm9hcmQgPSBmdW5jdGlvbigpIHtcbiAgICAgICAgcmV0dXJuICRodHRwLmdldCgnL2FwaS9kb21haW5zL2xlYWRlcmJvYXJkJylcbiAgICB9XG59KTtcbiIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ0RvbWFpbkNvbnRyb2xsZXInLCBmdW5jdGlvbigkcm9vdFNjb3BlLCAkc2NvcGUsIERvbWFpblNlcnZpY2UpIHtcblx0aWYgKCEkcm9vdFNjb3BlLmRvbWFpbnMpIHtcblx0XHREb21haW5TZXJ2aWNlLmZldGNoQWxsKCkuc3VjY2VzcyAoZnVuY3Rpb24gKGRvbWFpbnMpIHtcblx0XHRcdGlmIChkb21haW5zKVxuXHRcdFx0XHQkc2NvcGUuZG9tYWlucyA9ICRyb290U2NvcGUuZG9tYWlucyA9IGRvbWFpbnM7XG5cdFx0fSk7XG5cdH0gZWxzZSB7XG5cdFx0JHNjb3BlLmRvbWFpbnMgPSAkcm9vdFNjb3BlLmRvbWFpbnM7XG5cdH1cblx0JHNjb3BlLmdldERvbWFpbiA9IGZ1bmN0aW9uKGRvbWFpbklkKSB7XG5cdFx0RG9tYWluU2VydmljZS5mZXRjaERvbWFpbihkb21haW5JZCkuc3VjY2VzcyAoZnVuY3Rpb24gKGRvbWFpbikge1xuXHRcdFx0JHJvb3RTY29wZS5jdXJyZW50RG9tYWluID0gZG9tYWluO1xuXHRcdFx0RG9tYWluU2VydmljZS5zaW5nbGVEb21haW5WaWV3KCk7XG5cdFx0fSk7XG5cdH1cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLnNlcnZpY2UoJ0RvbWFpblNlcnZpY2UnLCBmdW5jdGlvbigkaHR0cCwgJGxvY2F0aW9uKSB7XG5cblx0dmFyIGxvY2FsRG9tYWluSWQ7XG5cblx0dGhpcy5mZXRjaEFsbCA9IGZ1bmN0aW9uICgpIHtcblx0XHRyZXR1cm4gJGh0dHAuZ2V0KCcvYXBpL2RvbWFpbnMnKTtcblx0fTtcblxuXHR0aGlzLmZldGNoRG9tYWluID0gZnVuY3Rpb24oZG9tYWluSWQpIHtcblx0XHRsb2NhbERvbWFpbklkID0gZG9tYWluSWQ7XG5cdFx0cmV0dXJuICRodHRwKHtcblx0XHQgICAgdXJsOiAnL2FwaS9kb21haW5zLycgKyBkb21haW5JZCwgXG5cdFx0ICAgIG1ldGhvZDogXCJHRVRcIixcblx0XHQgICAgcGFyYW1zOiB7J2RvbWFpbklkJzogZG9tYWluSWR9XG5cdFx0fSk7XG5cdH07XG5cblx0dGhpcy5mZXRjaFBkZGxGaWxlID0gZnVuY3Rpb24oZmlsZU5hbWUpIHtcblx0XHRyZXR1cm4gJGh0dHAoe1xuXHRcdFx0dXJsOiAnL2FwaS9kb21haW5zLycgKyBsb2NhbERvbWFpbklkICsgJy8nICsgZmlsZU5hbWUsXG5cdFx0XHRtZXRob2Q6IFwiR0VUXCIsXG5cdFx0XHRwYXJhbXM6IHtcblx0XHRcdFx0J2RvbWFpbklkJzogbG9jYWxEb21haW5JZCxcblx0XHRcdFx0J2ZpbGVOYW1lJzogZmlsZU5hbWVcblx0XHRcdH1cblx0XHR9KTtcblx0fTtcblxuXHR0aGlzLnNpbmdsZURvbWFpblZpZXcgPSBmdW5jdGlvbigpIHtcblx0XHQkbG9jYXRpb24ucGF0aCgnL3ZpZXcvZG9tYWluJylcblx0fTtcblxuXHR0aGlzLnByb2JsZW1WaWV3ID0gZnVuY3Rpb24oKSB7XG5cdFx0JGxvY2F0aW9uLnBhdGgoJy92aWV3L2RvbWFpbi9wZGRsJylcblx0fTtcblx0XG5cdHRoaXMuZG9tYWluVmlldyA9IGZ1bmN0aW9uKCkge1xuXHRcdCRsb2NhdGlvbi5wYXRoKCcvdmlldy9kb21haW4vcGRkbCcpXG5cdH07XG5cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ0RvbWFpblZpZXdDb250cm9sbGVyJywgZnVuY3Rpb24oJHJvb3RTY29wZSwgJHNjb3BlLCBEb21haW5TZXJ2aWNlKSB7XG5cdFxuXG5cdCRzY29wZS5kb21haW4gPSAkcm9vdFNjb3BlLmN1cnJlbnREb21haW47XG5cblx0JHNjb3BlLmdldFBkZGxGaWxlID0gZnVuY3Rpb24oZmlsZU5hbWUpIHtcblx0XHREb21haW5TZXJ2aWNlLmZldGNoUGRkbEZpbGUoZmlsZU5hbWUpLnN1Y2Nlc3MgKGZ1bmN0aW9uIChwZGRsKSB7XG5cdFx0XHRpZiAocGRkbClcblx0XHRcdFx0JHJvb3RTY29wZS5wZGRsID0gcGRkbDtcblx0XHRcdERvbWFpblNlcnZpY2UucHJvYmxlbVZpZXcoKVxuXHRcdH0pXG5cdH1cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ01haW5Db250cm9sbGVyJywgZnVuY3Rpb24oJHNjb3BlKSB7XG5cdFxufSkiLCJhbmd1bGFyLm1vZHVsZSgnYXBwJylcbi5jb250cm9sbGVyKCdQZGRsVmlld0NvbnRyb2xsZXInLCBmdW5jdGlvbiAoJHJvb3RTY29wZSwgJHNjb3BlKSB7XG5cdCRzY29wZS5wZGRsID0gJHJvb3RTY29wZS5wZGRsXG59KSIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbmZpZyhmdW5jdGlvbiAoJHJvdXRlUHJvdmlkZXIpIHtcblx0JHJvdXRlUHJvdmlkZXJcblx0LndoZW4oJy8nLCB7IGNvbnRyb2xsZXI6ICdNYWluQ29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAnaW5kZXguaHRtbCcgfSlcblx0LndoZW4oJy92aWV3JywgeyBjb250cm9sbGVyOiAnRG9tYWluQ29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAndmlldy5odG1sJyB9KVxuXHQud2hlbignL3ZpZXcvZG9tYWluJywgeyBjb250cm9sbGVyOiAnRG9tYWluVmlld0NvbnRyb2xsZXInLCB0ZW1wbGF0ZVVybDogJ2RvbWFpbi12aWV3Lmh0bWwnIH0pXG5cdC53aGVuKCcvdmlldy9kb21haW4vcGRkbCcsIHsgY29udHJvbGxlcjogJ1BkZGxWaWV3Q29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAncGRkbC12aWV3Lmh0bWwnIH0pXG5cdC53aGVuKCcvc3VibWl0JywgeyBjb250cm9sbGVyOiAnU3VibWl0Q29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAnc3VibWl0Lmh0bWwnIH0pXG5cdC53aGVuKCcvY29tcGV0aXRpb24nLCB7IGNvbnRyb2xsZXI6ICdDb21wZXRpdGlvbkNvbnRyb2xsZXInLCB0ZW1wbGF0ZVVybDogJ2NvbXBldGl0aW9uLmh0bWwnIH0pXG59KSIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ1N1Ym1pdENvbnRyb2xsZXInLCBmdW5jdGlvbigkc2NvcGUsIFN1Ym1pdFNlcnZpY2UpIHtcblx0JHNjb3BlLnNvbWV0aGluZyA9IDE7XG59KSIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLnNlcnZpY2UoJ1N1Ym1pdFNlcnZpY2UnLCBmdW5jdGlvbigkaHR0cCkge1xuXHRcdFxufSkiXSwic291cmNlUm9vdCI6Ii9zb3VyY2UvIn0=
