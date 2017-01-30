(function() {
    'use strict';

    angular
        .module('meetmeApp')
        .controller('MeetingDetailController', MeetingDetailController);

    MeetingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Meeting', 'Application', 'User'];

    function MeetingDetailController($scope, $rootScope, $stateParams, previousState, entity, Meeting, Application, User) {
        var vm = this;

        vm.meeting = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('meetmeApp:meetingUpdate', function(event, result) {
            vm.meeting = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
