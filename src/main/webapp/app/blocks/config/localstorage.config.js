(function() {
    'use strict';

    angular
        .module('meetmeApp')
        .config(localStorageConfig);

    localStorageConfig.$inject = ['$localStorageProvider', '$sessionStorageProvider'];

    function localStorageConfig($localStorageProvider, $sessionStorageProvider) {
        $localStorageProvider.setKeyPrefix('jhi-');
        $sessionStorageProvider.setKeyPrefix('jhi-');
    }
})();
