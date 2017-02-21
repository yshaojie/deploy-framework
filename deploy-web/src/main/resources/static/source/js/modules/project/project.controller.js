(function() {
    'use strict';

    angular
        .module('app.project')
        .controller('ProjectController', ProjectController);

    ProjectController.$inject = ['ProjectService','$scope'];
    function ProjectController(ProjectService,$scope) {
        var vm = this;
        active();
        function active() {

            ProjectService.findProjectList(function (data) {
                $scope.projectList=data;
                $scope.projectList=data;
            })


        }

    }


})();
