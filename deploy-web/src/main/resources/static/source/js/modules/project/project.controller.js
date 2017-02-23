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
            ProjectService.pack($scope.project.id,$scope.project.branch,$scope.project.module,function () {
                alert("打包成功")
            })
        }

    }


})();
