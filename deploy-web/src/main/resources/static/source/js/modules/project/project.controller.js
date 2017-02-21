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
                alert(branchs)
                $scope.branchs = data.branchs
                $scope.modules = data.modules
            },$scope.project.id)

        }

        function pack() {
            alert($scope.project.module)
            alert($scope.project.branch)
        }

    }


})();
