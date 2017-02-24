(function() {
    'use strict';

    angular
        .module('app.project')
        .controller('ProjectController', ProjectController);

    ProjectController.$inject = ['ProjectService','$scope'];
    function ProjectController(ProjectService,$scope) {
        var vm = this;
        $scope.loadProject = loadProject;
        $scope.pack = pack;
        active();
        function active() {

            ProjectService.findProjectList(function (data) {
                $scope.projectList=data;
                $scope.project = $scope.projectList[0]
            })


        }

        $scope.project = {
            url:"http://xxx"
        }
        function loadProject(index) {
            $scope.project = $scope.projectList[index];
            ProjectService.detail(function (data) {
                $scope.branchs = data.branchs
                $scope.modules = data.modules
            },$scope.project.id)

        }

        function pack() {
            $scope.package_message=undefined;
            $("#project_main").addClass(" whirl traditional ")
            ProjectService.pack($scope.project.id,$scope.project.branch,$scope.project.module,function (data) {
                $("#project_main").removeClass(" whirl traditional ")
                $scope.package_message=data.message;
            })
        }

    }


})();
