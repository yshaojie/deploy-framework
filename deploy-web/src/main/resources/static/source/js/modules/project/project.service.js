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

        function pack(projectId,branch,module,onSuccess) {
            $http
                .put("/project/package",{},{
                    params:{
                        projectId:projectId,
                        branch:branch,
                        module:module
                    }
                })
                .success(function (data) {
                    onSuccess(data);
                });
        }

        function detail(onSuccess,projectId) {
            $http
                .get("/project/detail?projectId="+projectId)
                .success(function (data) {
                    onSuccess(data);
                });
        }


    }
})();
