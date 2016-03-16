angular.module("app",["ngMaterial","ngAnimate","ngRoute","ngMessages","ngFileUpload"]).run(["$rootScope","$location","$timeout",function(e,o,l){e.$on("$viewContentLoaded",function(){l(function(){componentHandler.upgradeAllRegistered()})})}]),angular.module("app").controller("CompetitionController",["$rootScope","$scope","CompetitionService",function(e,o,l){e.leaderboard?o.leaderboard=e.leaderboard:l.fetchLeaderboard().success(function(l){l&&(console.log(l),o.leaderboard=e.leaderboard=l)})}]),angular.module("app").service("CompetitionService",["$http",function(e){this.fetchLeaderboard=function(){return e.get("/api/domains/leaderboard")}}]),angular.module("app").controller("DomainController",["$rootScope","$scope","DomainService",function(e,o,l){e.domains?o.domains=e.domains:l.fetchAll().success(function(l){l&&(o.domains=e.domains=l)}),o.getDomain=function(o){l.fetchDomain(o).success(function(o){e.currentDomain=o,l.singleDomainView()})}}]),angular.module("app").service("DomainService",["$http","$location",function(e,o){var l;this.fetchAll=function(){return e.get("/api/domains")},this.fetchDomain=function(o){return l=o,e({url:"/api/domains/"+o,method:"GET",params:{domainId:o}})},this.fetchPddlFile=function(o){return e({url:"/api/domains/"+l+"/"+o,method:"GET",params:{domainId:l,fileName:o}})},this.singleDomainView=function(){o.path("/view/domain")},this.problemView=function(){o.path("/view/domain/pddl")},this.domainView=function(){o.path("/view/domain/pddl")}}]),angular.module("app").controller("DomainViewController",["$rootScope","$scope","DomainService",function(e,o,l){o.domain=e.currentDomain,o.getPddlFile=function(o){l.fetchPddlFile(o).success(function(o){o&&(e.pddl=o),l.problemView()})}}]),angular.module("app").controller("MainController",["$scope",function(e){}]),angular.module("app").controller("PddlViewController",["$rootScope","$scope",function(e,o){o.pddl=e.pddl}]),angular.module("app").config(["$routeProvider",function(e){e.when("/",{controller:"MainController",templateUrl:"index.html"}).when("/view",{controller:"DomainController",templateUrl:"view.html"}).when("/view/domain",{controller:"DomainViewController",templateUrl:"domain-view.html"}).when("/view/domain/pddl",{controller:"PddlViewController",templateUrl:"pddl-view.html"}).when("/submit",{controller:"SubmitController",templateUrl:"submit.html"}).when("/competition",{controller:"CompetitionController",templateUrl:"competition.html"})}]),angular.module("app").controller("SubmitController",["$scope","$timeout","Upload","SubmitService",function(e,o,l,i){e.allDomainFiles=[{id:"domain1"}],e.publishDate=new Date,e.addDomain=function(){if(e.allDomainFiles[e.allDomainFiles.length-1].domainFile&&e.allDomainFiles[e.allDomainFiles.length-1].problemFiles){var o=e.allDomainFiles.length+1;e.allDomainFiles.push({id:"domain"+o})}},e.getUploadButton=function(e){angular.element(document.getElementById(e).click())},e.submitForm=function(){if(e.submitted=!0,e.allDomainFiles[0].domainFile&&e.allDomainFiles[0].problemFiles){e.form.publishDate=e.publishDate,e.form.domainFiles=[];for(var o=0;o<e.allDomainFiles.length;o++){problemFilesNames=[];for(var l=0;l<e.allDomainFiles[o].problemFiles.length;l++)problemFilesNames.push(e.allDomainFiles[o].problemFiles[l].name);e.form.domainFiles[o]={domainFile:e.allDomainFiles[o].domainFile.name,problemFiles:problemFilesNames},i.submitDomainForm(e.form).success(function(o){e.uploadFiles(o)})}}},e.uploadFiles=function(o){for(var i=0;i<e.allDomainFiles.length;++i){var n=e.allDomainFiles[i].domainFile;l.upload({url:"/api/upload",data:{file:n,dirname:o}});for(var a=0;a<e.allDomainFiles[i].problemFiles.length;++a)n=e.allDomainFiles[i].problemFiles[a],l.upload({url:"/api/upload",data:{file:n,dirname:o}})}}}]),angular.module("app").service("SubmitService",["$http",function(e){this.submitDomainForm=function(o){return e({url:"/api/upload/domain-form",method:"POST",data:o})}}]);
//# sourceMappingURL=data:application/json;base64,eyJ2ZXJzaW9uIjozLCJzb3VyY2VzIjpbIm1vZHVsZS5qcyIsImNvbXBldGl0aW9uLmN0cmwuanMiLCJjb21wZXRpdGlvbi5zdmMuanMiLCJkb21haW4uY3RybC5qcyIsImRvbWFpbi5zdmMuanMiLCJkb21haW52aWV3LmN0cmwuanMiLCJtYWluLmN0cmwuanMiLCJwZGRsdmlldy5jdHJsLmpzIiwicm91dGVzLmpzIiwic3VibWl0LmN0cmwuanMiLCJzdWJtaXQuc3ZjLmpzIl0sIm5hbWVzIjpbImFuZ3VsYXIiLCJtb2R1bGUiLCJydW4iLCIkcm9vdFNjb3BlIiwiJGxvY2F0aW9uIiwiJHRpbWVvdXQiLCIkb24iLCJjb21wb25lbnRIYW5kbGVyIiwidXBncmFkZUFsbFJlZ2lzdGVyZWQiLCJjb250cm9sbGVyIiwiJHNjb3BlIiwiQ29tcGV0aXRpb25TZXJ2aWNlIiwibGVhZGVyYm9hcmQiLCJmZXRjaExlYWRlcmJvYXJkIiwic3VjY2VzcyIsImNvbnNvbGUiLCJsb2ciLCJzZXJ2aWNlIiwiJGh0dHAiLCJ0aGlzIiwiZ2V0IiwiRG9tYWluU2VydmljZSIsImRvbWFpbnMiLCJmZXRjaEFsbCIsImdldERvbWFpbiIsImRvbWFpbklkIiwiZmV0Y2hEb21haW4iLCJkb21haW4iLCJjdXJyZW50RG9tYWluIiwic2luZ2xlRG9tYWluVmlldyIsImxvY2FsRG9tYWluSWQiLCJ1cmwiLCJtZXRob2QiLCJwYXJhbXMiLCJmZXRjaFBkZGxGaWxlIiwiZmlsZU5hbWUiLCJwYXRoIiwicHJvYmxlbVZpZXciLCJkb21haW5WaWV3IiwiZ2V0UGRkbEZpbGUiLCJwZGRsIiwiY29uZmlnIiwiJHJvdXRlUHJvdmlkZXIiLCJ3aGVuIiwidGVtcGxhdGVVcmwiLCJVcGxvYWQiLCJTdWJtaXRTZXJ2aWNlIiwiYWxsRG9tYWluRmlsZXMiLCJpZCIsInB1Ymxpc2hEYXRlIiwiRGF0ZSIsImFkZERvbWFpbiIsImxlbmd0aCIsImRvbWFpbkZpbGUiLCJwcm9ibGVtRmlsZXMiLCJuZXdEb21haW5JZCIsInB1c2giLCJnZXRVcGxvYWRCdXR0b24iLCJlbGVtZW50IiwiZG9jdW1lbnQiLCJnZXRFbGVtZW50QnlJZCIsImNsaWNrIiwic3VibWl0Rm9ybSIsInN1Ym1pdHRlZCIsImZvcm0iLCJkb21haW5GaWxlcyIsImkiLCJwcm9ibGVtRmlsZXNOYW1lcyIsImoiLCJuYW1lIiwic3VibWl0RG9tYWluRm9ybSIsImRpcm5hbWUiLCJ1cGxvYWRGaWxlcyIsImZpbGUiLCJ1cGxvYWQiLCJkYXRhIiwiZm9ybURhdGEiXSwibWFwcGluZ3MiOiJBQUFBQSxRQUFBQyxPQUFBLE9BQ0EsYUFDQSxZQUNBLFVBQ0EsYUFDQSxpQkFDQUMsS0FBQSxhQUFBLFlBQUEsV0FBQSxTQUFBQyxFQUFBQyxFQUFBQyxHQUNBRixFQUFBRyxJQUFBLHFCQUFBLFdBQ0FELEVBQUEsV0FDQUUsaUJBQUFDLDhCQ1RBUixRQUFBQyxPQUFBLE9BQ0FRLFdBQUEseUJBQUEsYUFBQSxTQUFBLHFCQUFBLFNBQUFOLEVBQUFPLEVBQUFDLEdBQ0FSLEVBQUFTLFlBUUFGLEVBQUFFLFlBQUFULEVBQUFTLFlBUEFELEVBQUFFLG1CQUFBQyxRQUFBLFNBQUFGLEdBQ0FBLElBQ0FHLFFBQUFDLElBQUFKLEdBQ0FGLEVBQUFFLFlBQUFULEVBQUFTLFlBQUFBLFFDTkFaLFFBQUFDLE9BQUEsT0FDQWdCLFFBQUEsc0JBQUEsUUFBQSxTQUFBQyxHQUVBQyxLQUFBTixpQkFBQSxXQUNBLE1BQUFLLEdBQUFFLElBQUEsZ0NDSkFwQixRQUFBQyxPQUFBLE9BQ0FRLFdBQUEsb0JBQUEsYUFBQSxTQUFBLGdCQUFBLFNBQUFOLEVBQUFPLEVBQUFXLEdBQ0FsQixFQUFBbUIsUUFNQVosRUFBQVksUUFBQW5CLEVBQUFtQixRQUxBRCxFQUFBRSxXQUFBVCxRQUFBLFNBQUFRLEdBQ0FBLElBQ0FaLEVBQUFZLFFBQUFuQixFQUFBbUIsUUFBQUEsS0FLQVosRUFBQWMsVUFBQSxTQUFBQyxHQUNBSixFQUFBSyxZQUFBRCxHQUFBWCxRQUFBLFNBQUFhLEdBQ0F4QixFQUFBeUIsY0FBQUQsRUFDQU4sRUFBQVEs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bUIsR0FBQWIsV0FBQWdCLEtBQ0FmLGFBQUFhLG1CQUdBckIsRUFBQXdCLGlCQUFBNUQsRUFBQXNELE1BQUFsRCxRQUFBLFNBQUF5RCxHQUNBN0QsRUFBQThELFlBQUFELFFBT0E3RCxFQUFBOEQsWUFBQSxTQUFBRCxHQUNBLElBQUEsR0FBQUwsR0FBQSxFQUFBQSxFQUFBeEQsRUFBQXFDLGVBQUFLLFNBQUFjLEVBQUEsQ0FDQSxHQUFBTyxHQUFBL0QsRUFBQXFDLGVBQUFtQixHQUFBYixVQUNBUixHQUFBNkIsUUFDQTNDLElBQUEsY0FDQTRDLE1BQUFGLEtBQUFBLEVBQUFGLFFBQUFBLElBR0EsS0FBQSxHQUFBSCxHQUFBLEVBQUFBLEVBQUExRCxFQUFBcUMsZUFBQW1CLEdBQUFaLGFBQUFGLFNBQUFnQixFQUNBSyxFQUFBL0QsRUFBQXFDLGVBQUFtQixHQUFBWixhQUFBYyxHQUVBdkIsRUFBQTZCLFFBQ0EzQyxJQUFBLGNBQ0E0QyxNQUFBRixLQUFBQSxFQUFBRixRQUFBQSxVQzVEQXZFLFFBQUFDLE9BQUEsT0FDQWdCLFFBQUEsaUJBQUEsUUFBQSxTQUFBQyxHQUNBQyxLQUFBbUQsaUJBQUEsU0FBQU0sR0FDQSxNQUFBMUQsSUFDQWEsSUFBQSwwQkFDQUMsT0FBQSxPQUNBMkMsS0FBQUMiLCJmaWxlIjoiYXBwLmpzIiwic291cmNlc0NvbnRlbnQiOlsiYW5ndWxhci5tb2R1bGUoJ2FwcCcsIFtcblx0J25nTWF0ZXJpYWwnLFxuXHQnbmdBbmltYXRlJyxcblx0J25nUm91dGUnLFxuXHQnbmdNZXNzYWdlcycsXG5cdCduZ0ZpbGVVcGxvYWQnXG5dKS5ydW4oZnVuY3Rpb24oJHJvb3RTY29wZSwgJGxvY2F0aW9uLCAkdGltZW91dCkge1xuICAgICRyb290U2NvcGUuJG9uKCckdmlld0NvbnRlbnRMb2FkZWQnLCBmdW5jdGlvbigpIHtcbiAgICAgICAgJHRpbWVvdXQoZnVuY3Rpb24oKSB7XG4gICAgICAgICAgICBjb21wb25lbnRIYW5kbGVyLnVwZ3JhZGVBbGxSZWdpc3RlcmVkKCk7XG4gICAgICAgIH0pO1xuICAgIH0pO1xufSk7IiwiYW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uY29udHJvbGxlcignQ29tcGV0aXRpb25Db250cm9sbGVyJywgZnVuY3Rpb24oJHJvb3RTY29wZSwgJHNjb3BlLCBDb21wZXRpdGlvblNlcnZpY2UpIHtcblx0aWYgKCEkcm9vdFNjb3BlLmxlYWRlcmJvYXJkKSB7XG4gICAgICAgIENvbXBldGl0aW9uU2VydmljZS5mZXRjaExlYWRlcmJvYXJkKCkuc3VjY2VzcyAoZnVuY3Rpb24gKGxlYWRlcmJvYXJkKSB7XG4gICAgICAgICAgICBpZiAobGVhZGVyYm9hcmQpIHtcbiAgICAgICAgICAgIFx0Y29uc29sZS5sb2cobGVhZGVyYm9hcmQpXG4gICAgICAgICAgICAgICAgJHNjb3BlLmxlYWRlcmJvYXJkID0gJHJvb3RTY29wZS5sZWFkZXJib2FyZCA9IGxlYWRlcmJvYXJkXG4gICAgICAgICAgICB9XG4gICAgICAgIH0pXG4gICAgfSBlbHNlIHtcbiAgICAgICAgJHNjb3BlLmxlYWRlcmJvYXJkID0gJHJvb3RTY29wZS5sZWFkZXJib2FyZFxuICAgIH1cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLnNlcnZpY2UoJ0NvbXBldGl0aW9uU2VydmljZScsIGZ1bmN0aW9uKCRodHRwKSB7XG5cbiAgICB0aGlzLmZldGNoTGVhZGVyYm9hcmQgPSBmdW5jdGlvbigpIHtcbiAgICAgICAgcmV0dXJuICRodHRwLmdldCgnL2FwaS9kb21haW5zL2xlYWRlcmJvYXJkJylcbiAgICB9XG59KTtcbiIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ0RvbWFpbkNvbnRyb2xsZXInLCBmdW5jdGlvbigkcm9vdFNjb3BlLCAkc2NvcGUsIERvbWFpblNlcnZpY2UpIHtcblx0aWYgKCEkcm9vdFNjb3BlLmRvbWFpbnMpIHtcblx0XHREb21haW5TZXJ2aWNlLmZldGNoQWxsKCkuc3VjY2VzcyAoZnVuY3Rpb24gKGRvbWFpbnMpIHtcblx0XHRcdGlmIChkb21haW5zKVxuXHRcdFx0XHQkc2NvcGUuZG9tYWlucyA9ICRyb290U2NvcGUuZG9tYWlucyA9IGRvbWFpbnM7XG5cdFx0fSk7XG5cdH0gZWxzZSB7XG5cdFx0JHNjb3BlLmRvbWFpbnMgPSAkcm9vdFNjb3BlLmRvbWFpbnM7XG5cdH1cblx0JHNjb3BlLmdldERvbWFpbiA9IGZ1bmN0aW9uKGRvbWFpbklkKSB7XG5cdFx0RG9tYWluU2VydmljZS5mZXRjaERvbWFpbihkb21haW5JZCkuc3VjY2VzcyAoZnVuY3Rpb24gKGRvbWFpbikge1xuXHRcdFx0JHJvb3RTY29wZS5jdXJyZW50RG9tYWluID0gZG9tYWluO1xuXHRcdFx0RG9tYWluU2VydmljZS5zaW5nbGVEb21haW5WaWV3KCk7XG5cdFx0fSk7XG5cdH1cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLnNlcnZpY2UoJ0RvbWFpblNlcnZpY2UnLCBmdW5jdGlvbigkaHR0cCwgJGxvY2F0aW9uKSB7XG5cblx0dmFyIGxvY2FsRG9tYWluSWQ7XG5cblx0dGhpcy5mZXRjaEFsbCA9IGZ1bmN0aW9uICgpIHtcblx0XHRyZXR1cm4gJGh0dHAuZ2V0KCcvYXBpL2RvbWFpbnMnKTtcblx0fTtcblxuXHR0aGlzLmZldGNoRG9tYWluID0gZnVuY3Rpb24oZG9tYWluSWQpIHtcblx0XHRsb2NhbERvbWFpbklkID0gZG9tYWluSWQ7XG5cdFx0cmV0dXJuICRodHRwKHtcblx0XHQgICAgdXJsOiAnL2FwaS9kb21haW5zLycgKyBkb21haW5JZCwgXG5cdFx0ICAgIG1ldGhvZDogXCJHRVRcIixcblx0XHQgICAgcGFyYW1zOiB7J2RvbWFpbklkJzogZG9tYWluSWR9XG5cdFx0fSk7XG5cdH07XG5cblx0dGhpcy5mZXRjaFBkZGxGaWxlID0gZnVuY3Rpb24oZmlsZU5hbWUpIHtcblx0XHRyZXR1cm4gJGh0dHAoe1xuXHRcdFx0dXJsOiAnL2FwaS9kb21haW5zLycgKyBsb2NhbERvbWFpbklkICsgJy8nICsgZmlsZU5hbWUsXG5cdFx0XHRtZXRob2Q6IFwiR0VUXCIsXG5cdFx0XHRwYXJhbXM6IHtcblx0XHRcdFx0J2RvbWFpbklkJzogbG9jYWxEb21haW5JZCxcblx0XHRcdFx0J2ZpbGVOYW1lJzogZmlsZU5hbWVcblx0XHRcdH1cblx0XHR9KTtcblx0fTtcblxuXHR0aGlzLnNpbmdsZURvbWFpblZpZXcgPSBmdW5jdGlvbigpIHtcblx0XHQkbG9jYXRpb24ucGF0aCgnL3ZpZXcvZG9tYWluJylcblx0fTtcblxuXHR0aGlzLnByb2JsZW1WaWV3ID0gZnVuY3Rpb24oKSB7XG5cdFx0JGxvY2F0aW9uLnBhdGgoJy92aWV3L2RvbWFpbi9wZGRsJylcblx0fTtcblx0XG5cdHRoaXMuZG9tYWluVmlldyA9IGZ1bmN0aW9uKCkge1xuXHRcdCRsb2NhdGlvbi5wYXRoKCcvdmlldy9kb21haW4vcGRkbCcpXG5cdH07XG5cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ0RvbWFpblZpZXdDb250cm9sbGVyJywgZnVuY3Rpb24oJHJvb3RTY29wZSwgJHNjb3BlLCBEb21haW5TZXJ2aWNlKSB7XG5cdFxuXG5cdCRzY29wZS5kb21haW4gPSAkcm9vdFNjb3BlLmN1cnJlbnREb21haW47XG5cblx0JHNjb3BlLmdldFBkZGxGaWxlID0gZnVuY3Rpb24oZmlsZU5hbWUpIHtcblx0XHREb21haW5TZXJ2aWNlLmZldGNoUGRkbEZpbGUoZmlsZU5hbWUpLnN1Y2Nlc3MgKGZ1bmN0aW9uIChwZGRsKSB7XG5cdFx0XHRpZiAocGRkbClcblx0XHRcdFx0JHJvb3RTY29wZS5wZGRsID0gcGRkbDtcblx0XHRcdERvbWFpblNlcnZpY2UucHJvYmxlbVZpZXcoKVxuXHRcdH0pXG5cdH1cbn0pOyIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbnRyb2xsZXIoJ01haW5Db250cm9sbGVyJywgZnVuY3Rpb24oJHNjb3BlKSB7XG5cdFxufSkiLCJhbmd1bGFyLm1vZHVsZSgnYXBwJylcbi5jb250cm9sbGVyKCdQZGRsVmlld0NvbnRyb2xsZXInLCBmdW5jdGlvbiAoJHJvb3RTY29wZSwgJHNjb3BlKSB7XG5cdCRzY29wZS5wZGRsID0gJHJvb3RTY29wZS5wZGRsXG59KSIsImFuZ3VsYXIubW9kdWxlKCdhcHAnKVxuLmNvbmZpZyhmdW5jdGlvbiAoJHJvdXRlUHJvdmlkZXIpIHtcblx0JHJvdXRlUHJvdmlkZXJcblx0LndoZW4oJy8nLCB7IGNvbnRyb2xsZXI6ICdNYWluQ29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAnaW5kZXguaHRtbCcgfSlcblx0LndoZW4oJy92aWV3JywgeyBjb250cm9sbGVyOiAnRG9tYWluQ29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAndmlldy5odG1sJyB9KVxuXHQud2hlbignL3ZpZXcvZG9tYWluJywgeyBjb250cm9sbGVyOiAnRG9tYWluVmlld0NvbnRyb2xsZXInLCB0ZW1wbGF0ZVVybDogJ2RvbWFpbi12aWV3Lmh0bWwnIH0pXG5cdC53aGVuKCcvdmlldy9kb21haW4vcGRkbCcsIHsgY29udHJvbGxlcjogJ1BkZGxWaWV3Q29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAncGRkbC12aWV3Lmh0bWwnIH0pXG5cdC53aGVuKCcvc3VibWl0JywgeyBjb250cm9sbGVyOiAnU3VibWl0Q29udHJvbGxlcicsIHRlbXBsYXRlVXJsOiAnc3VibWl0Lmh0bWwnIH0pXG5cdC53aGVuKCcvY29tcGV0aXRpb24nLCB7IGNvbnRyb2xsZXI6ICdDb21wZXRpdGlvbkNvbnRyb2xsZXInLCB0ZW1wbGF0ZVVybDogJ2NvbXBldGl0aW9uLmh0bWwnIH0pXG59KSIsIlx0YW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uY29udHJvbGxlcignU3VibWl0Q29udHJvbGxlcicsIGZ1bmN0aW9uICgkc2NvcGUsICR0aW1lb3V0LCBVcGxvYWQsIFN1Ym1pdFNlcnZpY2UpIHtcblx0XG5cdCRzY29wZS5hbGxEb21haW5GaWxlcyA9IFt7aWQ6ICdkb21haW4xJ31dO1xuXHQvLyRzY29wZS5kb21haW5GaWxlcyA9IFtdO1xuXHQvLyRzY29wZS5wcm9ibGVtRmlsZXMgPSBbXTtcblx0JHNjb3BlLnB1Ymxpc2hEYXRlID0gbmV3IERhdGUoKTtcblx0XG5cblx0JHNjb3BlLmFkZERvbWFpbiA9IGZ1bmN0aW9uKCkge1xuXHRcdGlmICgkc2NvcGUuYWxsRG9tYWluRmlsZXNbJHNjb3BlLmFsbERvbWFpbkZpbGVzLmxlbmd0aCAtIDFdLmRvbWFpbkZpbGUgJiZcblx0XHRcdCRzY29wZS5hbGxEb21haW5GaWxlc1skc2NvcGUuYWxsRG9tYWluRmlsZXMubGVuZ3RoIC0gMV0ucHJvYmxlbUZpbGVzKSB7XG5cdFx0XHR2YXIgbmV3RG9tYWluSWQgPSAkc2NvcGUuYWxsRG9tYWluRmlsZXMubGVuZ3RoICsgMTtcblx0XHRcdCRzY29wZS5hbGxEb21haW5GaWxlcy5wdXNoKHsnaWQnOidkb21haW4nICsgbmV3RG9tYWluSWR9KTtcblx0XHR9XG5cdH07XG5cblx0JHNjb3BlLmdldFVwbG9hZEJ1dHRvbiA9IGZ1bmN0aW9uIChpZCkge1xuXHRcdGFuZ3VsYXIuZWxlbWVudChkb2N1bWVudC5nZXRFbGVtZW50QnlJZChpZCkuY2xpY2soKSk7XG5cdH1cblxuXHQkc2NvcGUuc3VibWl0Rm9ybSA9IGZ1bmN0aW9uICgpIHtcblx0XHQvLyBjaGVjayBpZiBkb21haW4gYW5kIHByb2JsZW0gZmlsZXMgd2VyZSBzZWxlY3RlZFxuXHRcdCRzY29wZS5zdWJtaXR0ZWQgPSB0cnVlO1xuXHRcdGlmICgkc2NvcGUuYWxsRG9tYWluRmlsZXNbMF0uZG9tYWluRmlsZSAmJiAkc2NvcGUuYWxsRG9tYWluRmlsZXNbMF0ucHJvYmxlbUZpbGVzKSB7XG5cdFx0XHQkc2NvcGUuZm9ybS5wdWJsaXNoRGF0ZSA9ICRzY29wZS5wdWJsaXNoRGF0ZTtcblx0XHRcdCRzY29wZS5mb3JtLmRvbWFpbkZpbGVzXHQ9IFtdXHRcblxuXHRcdFx0Zm9yICh2YXIgaSA9IDA7IGkgPCAkc2NvcGUuYWxsRG9tYWluRmlsZXMubGVuZ3RoOyBpKyspIHtcblx0XHRcdFx0cHJvYmxlbUZpbGVzTmFtZXMgPSBbXVxuXHRcdFx0XHRcblx0XHRcdFx0Zm9yICh2YXIgaiA9IDA7IGogPCAkc2NvcGUuYWxsRG9tYWluRmlsZXNbaV0ucHJvYmxlbUZpbGVzLmxlbmd0aDsgaisrKSB7XG5cdFx0XHRcdFx0cHJvYmxlbUZpbGVzTmFtZXMucHVzaCgkc2NvcGUuYWxsRG9tYWluRmlsZXNbaV0ucHJvYmxlbUZpbGVzW2pdLm5hbWUpXG5cdFx0XHRcdFx0XG5cdFx0XHRcdH1cblxuXHRcdFx0XHQkc2NvcGUuZm9ybS5kb21haW5GaWxlc1tpXSA9IHtcblx0XHRcdFx0XHRkb21haW5GaWxlOiAkc2NvcGUuYWxsRG9tYWluRmlsZXNbaV0uZG9tYWluRmlsZS5uYW1lLFxuXHRcdFx0XHRcdHByb2JsZW1GaWxlczogcHJvYmxlbUZpbGVzTmFtZXNcblx0XHRcdFx0fVxuXG5cdFx0XHRcdFN1Ym1pdFNlcnZpY2Uuc3VibWl0RG9tYWluRm9ybSgkc2NvcGUuZm9ybSkuIHN1Y2Nlc3MoZnVuY3Rpb24gKGRpcm5hbWUpIHtcblx0XHRcdFx0XHQkc2NvcGUudXBsb2FkRmlsZXMoZGlybmFtZSlcblx0XHRcdFx0XHQvLyBoYW5kbGUgZm9ybSBzdWJtaXNzaW9uIHN1Y2Nlc3Ncblx0XHRcdFx0fSlcblx0XHRcdH1cblx0XHR9XG5cdH1cblxuXHQkc2NvcGUudXBsb2FkRmlsZXMgPSBmdW5jdGlvbiAoZGlybmFtZSkge1xuXHRcdGZvciAodmFyIGkgPSAwOyBpIDwgJHNjb3BlLmFsbERvbWFpbkZpbGVzLmxlbmd0aDsgKytpKSB7XG5cdFx0XHR2YXIgZmlsZSA9ICRzY29wZS5hbGxEb21haW5GaWxlc1tpXS5kb21haW5GaWxlO1xuXHRcdFx0VXBsb2FkLnVwbG9hZCh7XG5cdFx0XHRcdHVybDogJy9hcGkvdXBsb2FkJyxcblx0XHRcdFx0ZGF0YToge2ZpbGU6IGZpbGUsIGRpcm5hbWU6IGRpcm5hbWV9XG5cdFx0XHR9KVxuXG5cdFx0XHRmb3IgKHZhciBqID0gMDsgaiA8ICRzY29wZS5hbGxEb21haW5GaWxlc1tpXS5wcm9ibGVtRmlsZXMubGVuZ3RoOyArK2opIHtcblx0XHRcdFx0ZmlsZSA9ICRzY29wZS5hbGxEb21haW5GaWxlc1tpXS5wcm9ibGVtRmlsZXNbal07XG5cblx0XHRcdFx0VXBsb2FkLnVwbG9hZCh7XG5cdFx0XHRcdFx0dXJsOiAnL2FwaS91cGxvYWQnLFxuXHRcdFx0XHRcdGRhdGE6IHtmaWxlOiBmaWxlLCBkaXJuYW1lOiBkaXJuYW1lfVxuXHRcdFx0XHR9KVxuXHRcdFx0fVxuXHRcdH1cblx0fVxufSkiLCJcblxuYW5ndWxhci5tb2R1bGUoJ2FwcCcpXG4uc2VydmljZSgnU3VibWl0U2VydmljZScsIGZ1bmN0aW9uKCRodHRwKSB7XG5cdFx0dGhpcy5zdWJtaXREb21haW5Gb3JtID0gZnVuY3Rpb24gKGZvcm1EYXRhKSB7XG5cdFx0XHRyZXR1cm4gJGh0dHAoe1xuXHRcdFx0XHR1cmw6ICcvYXBpL3VwbG9hZC9kb21haW4tZm9ybScsXG5cdFx0XHRcdG1ldGhvZDogJ1BPU1QnLFxuXHRcdFx0XHRkYXRhOiBmb3JtRGF0YVxuXHRcdFx0fSlcblx0XHR9XG59KSJdLCJzb3VyY2VSb290IjoiL3NvdXJjZS8ifQ==
