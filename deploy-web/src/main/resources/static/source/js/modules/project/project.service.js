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

        function findProjectList(onSuccess) {
            $http
                .get("/project/list")
                .success(function (data) {
                    onSuccess(data);
                });
        }
    }
})();
