(function() {
    'use strict';

    angular
        .module('app.project')
        .controller('ProjectController', ProjectController);

    ProjectController.$inject = ['ProjectService','$scope'];
    function ProjectController(ProjectService,$scope) {
        var vm = this;
        $scope.loadProject = loadProject;
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
        }

    }


})();
