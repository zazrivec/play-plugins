# Dust Plugin

This plugin provides build time compilation for the LinkedIn fork of [Dust](https://github.com/linkedin/dustjs) templates.

# How to install

* for play 2.1: add

```
resolvers += "zazrivec-releases" at "http://zazrivec.github.io/maven/releases"
addSbtPlugin("com.typesafe" % "play-plugins-dust" % "1.7.1")
```

to your plugin.sbt

# How to Use

* Include dust. Note that this is not provided by the sbt plugin. It can be found here: [dust-core-2.0.3.min.js](https://raw.github.com/typesafehub/play-plugins/master/dust/sample/public/javascripts/dust-core-2.0.3.min.js)
```<script src="@routes.Assets.at("javascripts/dust-core-2.0.3.min.js")"></script>
```

* Put your dust template .tl files under the ```app/assets``` directory

* Reference the generated .js in a  ```<script>``` tag:
```<script src="@routes.Assets.at("example.tl.js")"></script>```

* Render the template when you receive the json 
```
  $(function() {
	$.get('@routes.Application.data', function(data) {
	  console.log('data = ' + JSON.stringify(data));
	  dust.render('example.tl', data, function(err, out) {
	    $('#dust_pan').html(err ? err : out);
	  });
	});
  });
```

# Settings

## dustOutputRelativePath

Specify path relative to public assets folder to place compiled javascript templates.

Example:

    DustPlugin.dustOutputRelativePath := "javascripts/templates/"

## dustNativePath

Elect to use a native Dust.js compiler (via Node) if installed.

Example:

    DustPlugin.dustNativePath := Some("/usr/local/shared/npm/bin/dustc")

# Sample

For an example, see the bundled sample app.

## Licence

This software is licensed under the Apache 2 license, quoted below.

Copyright 2012 Typesafe (http://www.typesafe.com).

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this project except in compliance with the License. You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.