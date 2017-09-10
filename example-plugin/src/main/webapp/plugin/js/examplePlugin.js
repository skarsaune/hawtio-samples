/**
 * @module Example
 */
var Example = (function(Example) {

	/**
	 * @property pluginName
	 * @type {string}
	 * 
	 * The name of this plugin
	 */
	Example.pluginName = 'example_plugin';

	/**
	 * @property log
	 * @type {Logging.Logger}
	 * 
	 * This plugin's logger instance
	 */
	Example.log = Logger.get('Example');

	/**
	 * @property contextPath
	 * @type {string}
	 * 
	 * The top level path of this plugin on the server
	 * 
	 */
	Example.contextPath = "/sample-plugin/";

	/**
	 * @property templatePath
	 * @type {string}
	 * 
	 * The path to this plugin's partials
	 */
	Example.templatePath = Example.contextPath + "plugin/html/";
	
	Example.domain = "no.kantega.example.application";
	
	Example.mbean = Example.domain + ":type=ExampleApplication";

	/**
	 * @property module
	 * @type {object}
	 * 
	 * This plugin's angularjs module instance. This plugin only needs
	 * hawtioCore to run, which provides services like workspace, viewRegistry
	 * and layoutFull used by the run function
	 */
	Example.module = angular.module(Example.pluginName, [ 'hawtioCore' ]).config(
		function($routeProvider) {

			/**
			 * Here we define the route for our plugin. One note is to avoid
			 * using 'otherwise', as hawtio has a handler in place when a
			 * route doesn't match any routes that routeProvider has been
			 * configured with.
			 */
			$routeProvider.when('/example_plugin', {
				templateUrl : Example.templatePath + 'example.html'
			});
		});

	/**
	 * Here we define any initialization to be done when this angular module is
	 * bootstrapped. In here we do a number of things:
	 * 
	 * 1. We log that we've been loaded (kinda optional) 2. We load our .css
	 * file for our views 3. We configure the viewRegistry service from hawtio
	 * for our route; in this case we use a pre-defined layout that uses the
	 * full viewing area 4. We configure our top-level tab and provide a link to
	 * our plugin. This is just a matter of adding to the workspace's
	 * topLevelTabs array.
	 */
	Example.module.run(function(workspace, viewRegistry, layoutFull) {

		Example.log.info(Example.pluginName, " loaded");

		Core.addCSS(Example.contextPath + "plugin/css/example.css");

		// tell the app to use the full layout, also could use layoutTree
		// to get the JMX tree or provide a URL to a custom layout
		viewRegistry[Example.pluginName] = layoutFull;

		/*
		 * Set up top-level link to our plugin. Requires an object with the
		 * following attributes:
		 * 
		 * id - the ID of this plugin, used by the perspective plugin and by the
		 * preferences page content - The text or HTML that should be shown in
		 * the tab title - This will be the tab's tooltip isValid - A function
		 * that returns whether or not this plugin has functionality that can be
		 * used for the current JVM. The workspace object is passed in by
		 * hawtio's navbar controller which lets you inspect the JMX tree,
		 * however you can do any checking necessary and return a boolean href -
		 * a function that returns a link, normally you'd return a hash link
		 * like #/foo/bar but you can also return a full URL to some other site
		 * isActive - Called by hawtio's navbar to see if the current
		 * $location.url() matches up with this plugin. Here we use a helper
		 * from workspace that checks if $location.url() starts with our route.
		 */
		workspace.topLevelTabs.push({
			id : Example.pluginName,
			content : "Example Plugin",
			title : "Example Plugin",
			isValid : function(workspace) {
				return workspace.treeContainsDomainAndProperties(Example.domain);
			},
			href : function() {
				return "#/example_plugin";
			},
			isActive : function(workspace) {
				return workspace.isLinkActive(Example.pluginName);
			}
		});

	});

	/**
	 * @function ExampleController
	 * @param $scope
	 * @param jolokia
	 * 
	 * The controller for example.html, only requires the jolokia service from
	 * hawtioCore
	 * 
	 */
	Example.ExampleController = function($scope, jolokia, workspace, $filter) {

		// register a watch with jolokia on this mbean to
		// regularly refresh display
		Core.register(jolokia, $scope, {
			type : 'read',
			mbean : Example.mbean,
			arguments : []
		}, onSuccess(render));
		
		// update display of attributes
		function render(response) {
			$scope.attributes = response.value;
			Core.$apply($scope);
		}
		
		$scope.flushCaches = function() {
			Example.log.info(" flushing caches");
			jolokia.request([ {
				type : 'exec',
				operation : 'flushCaches()',
				mbean : Example.mbean,
				arguments : []
			} ], onSuccess(function(response) {
				Core.notification("success", "Refreshed caches ");
			}));
		};
	};

	return Example;

})(Example || {});

// tell the hawtio plugin loader about our plugin so it can be
// bootstrapped with the rest of angular
hawtioPluginLoader.addModule(Example.pluginName);