/**=========================================================
 * Module: project.service.js
 =========================================================*/

(function() {
    'use strict';

    angular
        .module('app.project')
        .service('ProjectService', ProjectService);
    ProjectService.$inject = ['$http','$location'];
    function ProjectService($http) {
        this.findProjectList = findProjectList;
        this.pack = pack;
        this.detail = detail;
        this.findProjectList = findProjectList;

        function findProjectList(onSuccess) {
            $http
                .get("/project/list")
                .success(function (data) {
                    onSuccess(data);
                });
        }

        function pack(onSuccess) {
            $http
                .put("/project/package",{})
                .success(function (data) {
                    onSuccess(data);
                });
        }

        function detail(onSuccess,projectId) {
            $http
                .put("/project/detail",{projectId:projectId})
                .success(function (data) {
                    onSuccess(data);
                });
        }


    }
})();
