(function() {
    'use strict';
    angular
        .module('meetmeApp')
        .factory('Meeting', Meeting);

    Meeting.$inject = ['$resource'];

    function Meeting ($resource) {
        var resourceUrl =  'api/meetings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
