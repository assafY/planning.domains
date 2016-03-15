angular.module("app",["ngMaterial","ngAnimate","ngRoute","ngMessages","ngFileUpload"]).run(["$rootScope","$location","$timeout",function(e,o,n){e.$on("$viewContentLoaded",function(){n(function(){componentHandler.upgradeAllRegistered()})})}]),angular.module("app").controller("CompetitionController",["$rootScope","$scope","CompetitionService",function(e,o,n){e.leaderboard?o.leaderboard=e.leaderboard:n.fetchLeaderboard().success(function(n){n&&(console.log(n),o.leaderboard=e.leaderboard=n)})}]),angular.module("app").service("CompetitionService",["$http",function(e){this.fetchLeaderboard=function(){return e.get("/api/domains/leaderboard")}}]),angular.module("app").controller("DomainController",["$rootScope","$scope","DomainService",function(e,o,n){e.domains?o.domains=e.domains:n.fetchAll().success(function(n){n&&(o.domains=e.domains=n)}),o.getDomain=function(o){n.fetchDomain(o).success(function(o){e.currentDomain=o,n.singleDomainView()})}}]),angular.module("app").service("DomainService",["$http","$location",function(e,o){var n;this.fetchAll=function(){return e.get("/api/domains")},this.fetchDomain=function(o){return n=o,e({url:"/api/domains/"+o,method:"GET",params:{domainId:o}})},this.fetchPddlFile=function(o){return e({url:"/api/domains/"+n+"/"+o,method:"GET",params:{domainId:n,fileName:o}})},this.singleDomainView=function(){o.path("/view/domain")},this.problemView=function(){o.path("/view/domain/pddl")},this.domainView=function(){o.path("/view/domain/pddl")}}]),angular.module("app").controller("DomainViewController",["$rootScope","$scope","DomainService",function(e,o,n){o.domain=e.currentDomain,o.getPddlFile=function(o){n.fetchPddlFile(o).success(function(o){o&&(e.pddl=o),n.problemView()})}}]),angular.module("app").controller("MainController",["$scope",function(e){}]),angular.module("app").controller("PddlViewController",["$rootScope","$scope",function(e,o){o.pddl=e.pddl}]),angular.module("app").config(["$routeProvider",function(e){e.when("/",{controller:"MainController",templateUrl:"index.html"}).when("/view",{controller:"DomainController",templateUrl:"view.html"}).when("/view/domain",{controller:"DomainViewController",templateUrl:"domain-view.html"}).when("/view/domain/pddl",{controller:"PddlViewController",templateUrl:"pddl-view.html"}).when("/submit",{controller:"SubmitController",templateUrl:"submit.html"}).when("/competition",{controller:"CompetitionController",templateUrl:"competition.html"})}]),angular.module("app").controller("SubmitController",["$scope","$timeout","Upload","SubmitService",function(e,o,n,l){e.allDomainFiles=[{id:"domain1"}],e.publishDate=new Date,e.addDomain=function(){if(e.allDomainFiles[e.allDomainFiles.length-1].domainFile&&e.allDomainFiles[e.allDomainFiles.length-1].problemFiles){var o=e.allDomainFiles.length+1;e.allDomainFiles.push({id:"domain"+o})}},e.getUploadButton=function(e){angular.element(document.getElementById(e).click())},e.submitForm=function(){if(e.submitted=!0,e.allDomainFiles[0].domainFile&&e.allDomainFiles[0].problemFiles){e.form.publishDate=e.publishDate,e.form.domainFiles=[];for(var o=0;o<e.allDomainFiles.length;o++){problemFilesNames=[];for(var n=0;n<e.allDomainFiles[o].problemFiles.length;n++)problemFilesNames.push(e.allDomainFiles[o].problemFiles[n].name);e.form.domainFiles[o]={domainFile:e.allDomainFiles[o].domainFile.name,problemFiles:problemFilesNames},l.submitDomainForm(e.form).success(function(e){console.log(e)})}}},e.uploadFiles=function(e){}}]),angular.module("app").service("SubmitService",["$http",function(e){this.submitDomainForm=function(o){return e({url:"/api/upload/domain-form",method:"POST",data:o})}}]);
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIm1vZHVsZS5qcyIsImNvbXBldGl0aW9uLmN0cmwuanMiLCJjb21wZXRpdGlvbi5zdmMuanMiLCJkb21haW4uY3RybC5qcyIsImRvbWFpbi5zdmMuanMiLCJkb21haW52aWV3LmN0cmwuanMiLCJtYWluLmN0cmwuanMiLCJwZGRsdmlldy5jdHJsLmpzIiwicm91dGVzLmpzIiwic3VibWl0LmN0cmwuanMiLCJzdWJtaXQuc3ZjLmpzIl0sIm5hbWVzIjpbImFuZ3VsYXIiLCJtb2R1bGUiLCJydW4iLCIkcm9vdFNjb3BlIiwiJGxvY2F0aW9uIiwiJHRpbWVvdXQiLCIkb24iLCJjb21wb25lbnRIYW5kbGVyIiwidXBncmFkZUFsbFJlZ2lzdGVyZWQiLCJjb250cm9sbGVyIiwiJHNjb3BlIiwiQ29tcGV0aXRpb25TZXJ2aWNlIiwibGVhZGVyYm9hcmQiLCJmZXRjaExlYWRlcmJvYXJkIiwic3VjY2VzcyIsImNvbnNvbGUiLCJsb2ciLCJzZXJ2aWNlIiwiJGh0dHAiLCJ0aGlzIiwiZ2V0IiwiRG9tYWluU2VydmljZSIsImRvbWFpbnMiLCJmZXRjaEFsbCIsImdldERvbWFpbiIsImRvbWFpbklkIiwiZmV0Y2hEb21haW4iLCJkb21haW4iLCJjdXJyZW50RG9tYWluIiwic2luZ2xlRG9tYWluVmlldyIsImxvY2FsRG9tYWluSWQiLCJ1cmwiLCJtZXRob2QiLCJwYXJhbXMiLCJmZXRjaFBkZGxGaWxlIiwiZmlsZU5hbWUiLCJwYXRoIiwicHJvYmxlbVZpZXciLCJkb21haW5WaWV3IiwiZ2V0UGRkbEZpbGUiLCJwZGRsIiwiY29uZmlnIiwiJHJvdXRlUHJvdmlkZXIiLCJ3aGVuIiwidGVtcGxhdGVVcmwiLCJVcGxvYWQiLCJTdWJtaXRTZXJ2aWNlIiwiYWxsRG9tYWluRmlsZXMiLCJpZCIsInB1Ymxpc2hEYXRlIiwiRGF0ZSIsImFkZERvbWFpbiIsImxlbmd0aCIsImRvbWFpbkZpbGUiLCJwcm9ibGVtRmlsZXMiLCJuZXdEb21haW5JZCIsInB1c2giLCJnZXRVcGxvYWRCdXR0b24iLCJlbGVtZW50IiwiZG9jdW1lbnQiLCJnZXRFbGVtZW50QnlJZCIsImNsaWNrIiwic3VibWl0Rm9ybSIsInN1Ym1pdHRlZCIsImZvcm0iLCJkb21haW5GaWxlcyIsImkiLCJwcm9ibGVtRmlsZXNOYW1lcyIsImoiLCJuYW1lIiwic3VibWl0RG9tYWluRm9ybSIsInJlc3VsdCIsInVwbG9hZEZpbGVzIiwiZmlsZXMiLCJmb3JtRGF0YSIsImRhdGEiXSwibWFwcGluZ3MiOiJBQUFBQSxRQUFBQyxPQUFBLE9BQ0EsYUFDQSxZQUNBLFVBQ0EsYUFDQSxpQkFDQUMsS0FBQSxhQUFBLFlBQUEsV0FBQSxTQUFBQyxFQUFBQyxFQUFBQyxHQUNBRixFQUFBRyxJQUFBLHFCQUFBLFdBQ0FELEVBQUEsV0FDQUUsaUJBQUFDLDhCQ1RBUixRQUFBQyxPQUFBLE9BQ0FRLFdBQUEseUJBQUEsYUFBQSxTQUFBLHFCQUFBLFNBQUFOLEVBQUFPLEVBQUFDLEdBQ0FSLEVBQUFTLFlBUUFGLEVBQUFFLFlBQUFULEVBQUFTLFlBUEFELEVBQUFFLG1CQUFBQyxRQUFBLFNBQUFGLEdBQ0FBLElBQ0FHLFFBQUFDLElBQUFKLEdBQ0FGLEVBQUFFLFlBQUFULEVBQUFTLFlBQUFBLFFDTkFaLFFBQUFDLE9BQUEsT0FDQWdCLFFBQUEsc0JBQUEsUUFBQSxTQUFBQyxHQUVBQyxLQUFBTixpQkFBQSxXQUNBLE1BQUFLLEdBQUFFLElBQUEsZ0NDSkFwQixRQUFBQyxPQUFBLE9BQ0FRLFdBQUEsb0JBQUEsYUFBQSxTQUFBLGdCQUFBLFNBQUFOLEVBQUFPLEVBQUFXLEdBQ0FsQixFQUFBbUIsUUFNQVosRUFBQVksUUFBQW5CLEVBQUFtQixRQUxBRCxFQUFBRSxXQUFBVCxRQUFBLFNBQUFRLEdBQ0FBLElBQ0FaLEVBQUFZLFFBQUFuQixFQUFBbUIsUUFBQUEsS0FLQVosRUFBQWMsVUFBQSxTQUFBQyxHQUNBSixFQUFBSyxZQUFBRCxHQUFBWCxRQUFBLFNBQUFhLEdBQ0F4QixFQUFBeUIsY0FBQUQsRUFDQU4sRUFBQVEseUJDYkE3QixR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V0FBQWdCLEtBQ0FmLGFBQUFhLG1CQUdBckIsRUFBQXdCLGlCQUFBNUQsRUFBQXNELE1BQUFsRCxRQUFBLFNBQUF5RCxHQUNBeEQsUUFBQUMsSUFBQXVELFFBT0E3RCxFQUFBOEQsWUFBQSxTQUFBQyxRQzlDQXpFLFFBQUFDLE9BQUEsT0FDQWdCLFFBQUEsaUJBQUEsUUFBQSxTQUFBQyxHQUNBQyxLQUFBbUQsaUJBQUEsU0FBQUksR0FDQSxNQUFBeEQsSUFDQWEsSUFBQSwwQkFDQUMsT0FBQSxPQUNBMkMsS0FBQUQiLCJmaWxlIjoiYXBwLmpzIiwic291cmNlc0NvbnRlbnQiOlsiYW5ndWxhci5tb2R1bGUoJ2FwcCcsIFtcblx0J25nTWF0ZXJpYWwnLFxuXHQnbmdBbmltYXRlJyxcblx0J25nUm91dGUnLFxuXHQnbmdNZXNzYWdlcycsXG5cdCduZ0ZpbGVVcGxvYWQnXG5dKS5ydW4oZnVuY3Rpb24oJHJvb3RTY29wZSwgJGxvY2F0aW9uLCAkdGltZW91dCkge1xuICAgICRyb290U2NvcGUuJG9uKCckdmlld0NvbnRlbnRMb2FkZWQnLCBmdW5jdGlvbigpIHtcbiAgICAgICAgJHRpbWVvdXQoZnVuY3Rpb24oKSB7XG4gICAgICAgICAgICBjb21wb25lbnRIYW5kbGVyLnVwZ3JhZGVBbGxSZWdpc3RlcmVkKCk7XG4gICAgICAgIH0pO1xuICAgIH0pO1xufSk7IiwiYW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uY29udHJvbGxlcignQ29tcGV0aXRpb25Db250cm9sbGVyJywgZnVuY3Rpb24oJHJvb3RTY29wZSwgJHNjb3BlLCBDb21wZXRpdGlvblNlcnZpY2UpIHtcblx0aWYgKCEkcm9vdFNjb3BlLmxlYWRlcmJvYXJkKSB7XG4gICAgICAgIENvbXBldGl0aW9uU2VydmljZS5mZXRjaExlYWRlcmJvYXJkKCkuc3VjY2VzcyAoZnVuY3Rpb24gKGxlYWRlcmJvYXJkKSB7XG4gICAgICAgICAgICBpZiAobGVhZGVyYm9hcmQpIHtcbiAgICAgICAgICAgIFx0Y29uc29sZS5sb2cobGVhZGVyYm9hcmQpXG4gICAgICAgICAgICAgICAgJHNjb3BlLmxlYWRlcmJvYXJkID0gJHJvb3RTY29wZS5sZWFkZXJib2FyZCA9IGxlYWRlcmJvYXJkXG4gICAgICAgICAgICB9XG4gICAgICAgIH0pXG4gICAgfSBlbHNlIHtcbiAgICAgICAgJHNjb3BlLmxlYWRlcmJvYXJkID0gJHJvb3RTY29wZS5sZWFkZXJib2FyZFxuICAgIH1cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLnNlcnZpY2UoJ0NvbXBldGl0aW9uU2VydmljZScsIGZ1bmN0aW9uKCRodHRwKSB7XG5cbiAgICB0aGlzLmZldGNoTGVhZGVyYm9hcmQgPSBmdW5jdGlvbigpIHtcbiAgICAgICAgcmV0dXJuICRodHRwLmdldCgnL2FwaS9kb21haW5zL2xlYWRlcmJvYXJkJylcbiAgICB9XG59KTtcbiIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ0RvbWFpbkNvbnRyb2xsZXInLCBmdW5jdGlvbigkcm9vdFNjb3BlLCAkc2NvcGUsIERvbWFpblNlcnZpY2UpIHtcblx0aWYgKCEkcm9vdFNjb3BlLmRvbWFpbnMpIHtcblx0XHREb21haW5TZXJ2aWNlLmZldGNoQWxsKCkuc3VjY2VzcyAoZnVuY3Rpb24gKGRvbWFpbnMpIHtcblx0XHRcdGlmIChkb21haW5zKVxuXHRcdFx0XHQkc2NvcGUuZG9tYWlucyA9ICRyb290U2NvcGUuZG9tYWlucyA9IGRvbWFpbnM7XG5cdFx0fSk7XG5cdH0gZWxzZSB7XG5cdFx0JHNjb3BlLmRvbWFpbnMgPSAkcm9vdFNjb3BlLmRvbWFpbnM7XG5cdH1cblx0JHNjb3BlLmdldERvbWFpbiA9IGZ1bmN0aW9uKGRvbWFpbklkKSB7XG5cdFx0RG9tYWluU2VydmljZS5mZXRjaERvbWFpbihkb21haW5JZCkuc3VjY2VzcyAoZnVuY3Rpb24gKGRvbWFpbikge1xuXHRcdFx0JHJvb3RTY29wZS5jdXJyZW50RG9tYWluID0gZG9tYWluO1xuXHRcdFx0RG9tYWluU2VydmljZS5zaW5nbGVEb21haW5WaWV3KCk7XG5cdFx0fSk7XG5cdH1cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLnNlcnZpY2UoJ0RvbWFpblNlcnZpY2UnLCBmdW5jdGlvbigkaHR0cCwgJGxvY2F0aW9uKSB7XG5cblx0dmFyIGxvY2FsRG9tYWluSWQ7XG5cblx0dGhpcy5mZXRjaEFsbCA9IGZ1bmN0aW9uICgpIHtcblx0XHRyZXR1cm4gJGh0dHAuZ2V0KCcvYXBpL2RvbWFpbnMnKTtcblx0fTtcblxuXHR0aGlzLmZldGNoRG9tYWluID0gZnVuY3Rpb24oZG9tYWluSWQpIHtcblx0XHRsb2NhbERvbWFpbklkID0gZG9tYWluSWQ7XG5cdFx0cmV0dXJuICRodHRwKHtcblx0XHQgICAgdXJsOiAnL2FwaS9kb21haW5zLycgKyBkb21haW5JZCwgXG5cdFx0ICAgIG1ldGhvZDogXCJHRVRcIixcblx0XHQgICAgcGFyYW1zOiB7J2RvbWFpbklkJzogZG9tYWluSWR9XG5cdFx0fSk7XG5cdH07XG5cblx0dGhpcy5mZXRjaFBkZGxGaWxlID0gZnVuY3Rpb24oZmlsZU5hbWUpIHtcblx0XHRyZXR1cm4gJGh0dHAoe1xuXHRcdFx0dXJsOiAnL2FwaS9kb21haW5zLycgKyBsb2NhbERvbWFpbklkICsgJy8nICsgZmlsZU5hbWUsXG5cdFx0XHRtZXRob2Q6IFwiR0VUXCIsXG5cdFx0XHRwYXJhbXM6IHtcblx0XHRcdFx0J2RvbWFpbklkJzogbG9jYWxEb21haW5JZCxcblx0XHRcdFx0J2ZpbGVOYW1lJzogZmlsZU5hbWVcblx0XHRcdH1cblx0XHR9KTtcblx0fTtcblxuXHR0aGlzLnNpbmdsZURvbWFpblZpZXcgPSBmdW5jdGlvbigpIHtcblx0XHQkbG9jYXRpb24ucGF0aCgnL3ZpZXcvZG9tYWluJylcblx0fTtcblxuXHR0aGlzLnByb2JsZW1WaWV3ID0gZnVuY3Rpb24oKSB7XG5cdFx0JGxvY2F0aW9uLnBhdGgoJy92aWV3L2RvbWFpbi9wZGRsJylcblx0fTtcblx0XG5cdHRoaXMuZG9tYWluVmlldyA9IGZ1bmN0aW9uKCkge1xuXHRcdCRsb2NhdGlvbi5wYXRoKCcvdmlldy9kb21haW4vcGRkbCcpXG5cdH07XG5cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ0RvbWFpblZpZXdDb250cm9sbGVyJywgZnVuY3Rpb24oJHJvb3RTY29wZSwgJHNjb3BlLCBEb21haW5TZXJ2aWNlKSB7XG5cdFxuXG5cdCRzY29wZS5kb21haW4gPSAkcm9vdFNjb3BlLmN1cnJlbnREb21haW47XG5cblx0JHNjb3BlLmdldFBkZGxGaWxlID0gZnVuY3Rpb24oZmlsZU5hbWUpIHtcblx0XHREb21haW5TZXJ2aWNlLmZldGNoUGRkbEZpbGUoZmlsZU5hbWUpLnN1Y2Nlc3MgKGZ1bmN0aW9uIChwZGRsKSB7XG5cdFx0XHRpZiAocGRkbClcblx0XHRcdFx0JHJvb3RTY29wZS5wZGRsID0gcGRkbDtcblx0XHRcdERvbWFpblNlcnZpY2UucHJvYmxlbVZpZXcoKVxuXHRcdH0pXG5cdH1cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ01haW5Db250cm9sbGVyJywgZnVuY3Rpb24oJHNjb3BlKSB7XG5cdFxufSkiLCJhbmd1bGFyLm1vZHVsZSgnYXBwJylcbi5jb250cm9sbGVyKCdQZGRsVmlld0NvbnRyb2xsZXInLCBmdW5jdGlvbiAoJHJvb3RTY29wZSwgJHNjb3BlKSB7XG5cdCRzY29wZS5wZGRsID0gJHJvb3RTY29wZS5wZGRsXG59KSIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbmZpZyhmdW5jdGlvbiAoJHJvdXRlUHJvdmlkZXIpIHtcblx0JHJvdXRlUHJvdmlkZXJcblx0LndoZW4oJy8nLCB7IGNvbnRyb2xsZXI6ICdNYWluQ29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAnaW5kZXguaHRtbCcgfSlcblx0LndoZW4oJy92aWV3JywgeyBjb250cm9sbGVyOiAnRG9tYWluQ29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAndmlldy5odG1sJyB9KVxuXHQud2hlbignL3ZpZXcvZG9tYWluJywgeyBjb250cm9sbGVyOiAnRG9tYWluVmlld0NvbnRyb2xsZXInLCB0ZW1wbGF0ZVVybDogJ2RvbWFpbi12aWV3Lmh0bWwnIH0pXG5cdC53aGVuKCcvdmlldy9kb21haW4vcGRkbCcsIHsgY29udHJvbGxlcjogJ1BkZGxWaWV3Q29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAncGRkbC12aWV3Lmh0bWwnIH0pXG5cdC53aGVuKCcvc3VibWl0JywgeyBjb250cm9sbGVyOiAnU3VibWl0Q29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAnc3VibWl0Lmh0bWwnIH0pXG5cdC53aGVuKCcvY29tcGV0aXRpb24nLCB7IGNvbnRyb2xsZXI6ICdDb21wZXRpdGlvbkNvbnRyb2xsZXInLCB0ZW1wbGF0ZVVybDogJ2NvbXBldGl0aW9uLmh0bWwnIH0pXG59KSIsIlx0YW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uY29udHJvbGxlcignU3VibWl0Q29udHJvbGxlcicsIGZ1bmN0aW9uICgkc2NvcGUsICR0aW1lb3V0LCBVcGxvYWQsIFN1Ym1pdFNlcnZpY2UpIHtcblx0XG5cdCRzY29wZS5hbGxEb21haW5GaWxlcyA9IFt7aWQ6ICdkb21haW4xJ31dO1xuXHQvLyRzY29wZS5kb21haW5GaWxlcyA9IFtdO1xuXHQvLyRzY29wZS5wcm9ibGVtRmlsZXMgPSBbXTtcblx0JHNjb3BlLnB1Ymxpc2hEYXRlID0gbmV3IERhdGUoKTtcblxuXHQkc2NvcGUuYWRkRG9tYWluID0gZnVuY3Rpb24oKSB7XG5cdFx0aWYgKCRzY29wZS5hbGxEb21haW5GaWxlc1skc2NvcGUuYWxsRG9tYWluRmlsZXMubGVuZ3RoIC0gMV0uZG9tYWluRmlsZSAmJlxuXHRcdFx0JHNjb3BlLmFsbERvbWFpbkZpbGVzWyRzY29wZS5hbGxEb21haW5GaWxlcy5sZW5ndGggLSAxXS5wcm9ibGVtRmlsZXMpIHtcblx0XHRcdHZhciBuZXdEb21haW5JZCA9ICRzY29wZS5hbGxEb21haW5GaWxlcy5sZW5ndGggKyAxO1xuXHRcdFx0JHNjb3BlLmFsbERvbWFpbkZpbGVzLnB1c2goeydpZCc6J2RvbWFpbicgKyBuZXdEb21haW5JZH0pO1xuXHRcdH1cblx0fTtcblxuXHQkc2NvcGUuZ2V0VXBsb2FkQnV0dG9uID0gZnVuY3Rpb24gKGlkKSB7XG5cdFx0YW5ndWxhci5lbGVtZW50KGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKGlkKS5jbGljaygpKTtcblx0fVxuXG5cdCRzY29wZS5zdWJtaXRGb3JtID0gZnVuY3Rpb24gKCkge1xuXHRcdC8vIGNoZWNrIGlmIGRvbWFpbiBhbmQgcHJvYmxlbSBmaWxlcyB3ZXJlIHNlbGVjdGVkXG5cdFx0JHNjb3BlLnN1Ym1pdHRlZCA9IHRydWU7XG5cdFx0aWYgKCRzY29wZS5hbGxEb21haW5GaWxlc1swXS5kb21haW5GaWxlICYmICRzY29wZS5hbGxEb21haW5GaWxlc1swXS5wcm9ibGVtRmlsZXMpIHtcblx0XHRcdCRzY29wZS5mb3JtLnB1Ymxpc2hEYXRlID0gJHNjb3BlLnB1Ymxpc2hEYXRlO1xuXHRcdFx0JHNjb3BlLmZvcm0uZG9tYWluRmlsZXNcdD0gW11cdFxuXG5cdFx0XHRmb3IgKHZhciBpID0gMDsgaSA8ICRzY29wZS5hbGxEb21haW5GaWxlcy5sZW5ndGg7IGkrKykge1xuXHRcdFx0XHRwcm9ibGVtRmlsZXNOYW1lcyA9IFtdXG5cdFx0XHRcdGZvciAodmFyIGogPSAwOyBqIDwgJHNjb3BlLmFsbERvbWFpbkZpbGVzW2ldLnByb2JsZW1GaWxlcy5sZW5ndGg7IGorKykge1xuXHRcdFx0XHRcdHByb2JsZW1GaWxlc05hbWVzLnB1c2goJHNjb3BlLmFsbERvbWFpbkZpbGVzW2ldLnByb2JsZW1GaWxlc1tqXS5uYW1lKVxuXHRcdFx0XHR9XG5cblx0XHRcdFx0JHNjb3BlLmZvcm0uZG9tYWluRmlsZXNbaV0gPSB7XG5cdFx0XHRcdFx0ZG9tYWluRmlsZTogJHNjb3BlLmFsbERvbWFpbkZpbGVzW2ldLmRvbWFpbkZpbGUubmFtZSxcblx0XHRcdFx0XHRwcm9ibGVtRmlsZXM6IHByb2JsZW1GaWxlc05hbWVzXG5cdFx0XHRcdH1cblxuXHRcdFx0XHRTdWJtaXRTZXJ2aWNlLnN1Ym1pdERvbWFpbkZvcm0oJHNjb3BlLmZvcm0pLiBzdWNjZXNzKGZ1bmN0aW9uIChyZXN1bHQpIHtcblx0XHRcdFx0XHRjb25zb2xlLmxvZyhyZXN1bHQpXG5cdFx0XHRcdFx0Ly8gaGFuZGxlIGZvcm0gc3VibWlzc2lvbiBzdWNjZXNzXG5cdFx0XHRcdH0pXG5cdFx0XHR9XG5cdFx0fVxuXHR9XG5cblx0JHNjb3BlLnVwbG9hZEZpbGVzID0gZnVuY3Rpb24gKGZpbGVzKSB7XG5cdFx0XG5cblx0fVxufSkiLCJhbmd1bGFyLm1vZHVsZSgnYXBwJylcbi5zZXJ2aWNlKCdTdWJtaXRTZXJ2aWNlJywgZnVuY3Rpb24oJGh0dHApIHtcblx0XHR0aGlzLnN1Ym1pdERvbWFpbkZvcm0gPSBmdW5jdGlvbiAoZm9ybURhdGEpIHtcblx0XHRcdHJldHVybiAkaHR0cCh7XG5cdFx0XHRcdHVybDogJy9hcGkvdXBsb2FkL2RvbWFpbi1mb3JtJyxcblx0XHRcdFx0bWV0aG9kOiAnUE9TVCcsXG5cdFx0XHRcdGRhdGE6IGZvcm1EYXRhXG5cdFx0XHR9KVxuXHRcdH1cbn0pIl0sInNvdXJjZVJvb3QiOiIvc291cmNlLyJ9