(function() {
    'use strict';

    angular
        .module('app.instance')
        .controller('InstanceController', InstanceController);

    InstanceController.$inject = ['InstanceService','$scope'];


    InstanceController = function (InstanceService,$scope) {
        var vm = this;
        active();

        function active() {
            InstanceService.queryAllInstanceGroup(function (data) {
                $scope.instanceGroup = data;
            });
        }
    }
})();
