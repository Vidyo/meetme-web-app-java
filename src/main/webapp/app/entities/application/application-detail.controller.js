(function() {
    'use strict';

    angular
        .module('meetmeApp')
        .controller('ApplicationDetailController', ApplicationDetailController);

    ApplicationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Application', 'User'];

    function ApplicationDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Application, User) {
        var vm = this;

        vm.application = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('meetmeApp:applicationUpdate', function(event, result) {
            vm.application = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
