# env-ini

[![Build Status][travis-badge]][travis]
[![Dependencies Status][deps-badge]][deps]
[![Clojars Project][clojars-badge]][clojars]
[![Tag][tag-badge]][tag]
[![JDK version][jdk-v]](.travis.yml)
[![Clojure version][clojure-v]](project.clj)

[![][logo]][logo-large]

*Access config data from the ENV or INI files*


#### Contents

* [Introduction](#introduction-)
* [Usage](#usage-)
  * [Clojure](#clojure-)
  * [Clojurescript](#clojurescript-)
* [License](#license-)


## Introduction [&#x219F;](#contents)

Config/INI files are an old stand-by for configuration. Even now, projects such
as AWS and OpenStack clients use the Config/INI format. Still, environment
variables reign supreme in different deployment configurations, so it would be
nice to use them both, with ENV settings overriding Config/INI ones. Thus this
project was created.

This project uses the [clojure-ini][clojure ini] project to read Config/INI
files. For Clojurescript support, Node.js, NPM, and lein-npm are used. JS INI file
support is provided by the Node.js better-require library.


## Usage [&#x219F;](#contents)


### Clojure [&#x219F;](#contents)

Data from the environment and a given configuration are loaded into the same
data structure, one keyed off of `:env` and the other off of `:ini`. This is
obvious after loading the data:

```clj
(def data (env-ini/load-data "~/.aws/credentials"))
(pprint data)
```
```clj
{:ini
 {:alice
  {:aws-access-key-id "AAAAAAAABBBBBBBBCCCD",
   :aws-secret-access-key "ZZZZZZZZZZZZZXXXXXXXXXXXXXXXXXXxYYYYY123"}},
 :env
 {:aws-access-key-id "AAAAAAAABBBBBBBBCCCD",
  ...}}
  ```

By default, it is assumed that a key `mykey` in section `mysection` of a
Config/INI file would be overridden with the environment variable
`MYSECTION_MYKEY`:

```clj
clojusc.env-ini.dev=> (env-ini/get data :mysection :mykey)
```

If the envionment and configuration key don't act in that way, you can provide
two different keys, the env one first, and then the config key:

```clj
clojusc.env-ini.dev=> (env-ini/get data :my-env-key :mysection :my-ini-key)
```

This would result in the environment variable `MY_ENV_KEY` being looked up. If
and only if a nil result was obtained, the loaded Config/INI data would be
checked for the value associated with the `:my-ini-key` key in the section
`:mysection`.

Note that environment variable names are loaded as lower-cased keywords (with
underscores converte to dashes).


### Clojurescript [&#x219F;](#contents)

The usage is exactly the same from Clojurescript. In a Node.js Clojurescript
REPL, let's bring in the required namespace:


```
$ make node-repl
```
```clj
clojusc.env-ini.node-dev=> (require '[clojusc.env-ini.core :as env-ini])
```

Then run the same code as above:

```clj
clojusc.env-ini.node-dev=> (def data (env-ini/load-data "~/.aws/credentials"))
clojusc.env-ini.node-dev=> (pprint data)
...
clojusc.env-ini.node-dev=> (env-ini/get data :mysection :mykey)
clojusc.env-ini.node-dev=> (env-ini/get data :my-env-key :mysection :my-ini-key)
```


## License [&#x219F;](#contents)

Copyright © 2016, Clojure-Aided Enrichment Center

Copyright © 2016, Element 84, Inc.

Apache License, Version 2.0.


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/clojusc/env-ini
[travis-badge]: https://travis-ci.org/clojusc/env-ini.png?branch=master
[deps]: http://jarkeeper.com/clojusc/env-ini
[deps-badge]: http://jarkeeper.com/clojusc/env-ini/status.svg
[logo]: resources/images/logo.png
[logo-large]: resources/images/logo-large.png
[tag-badge]: https://img.shields.io/github/tag/clojusc/env-ini.svg
[tag]: https://github.com/clojusc/env-ini/tags
[clojure-v]: https://img.shields.io/badge/clojure-1.8.0-blue.svg
[jdk-v]: https://img.shields.io/badge/jdk-1.7+-blue.svg
[clojars]: https://clojars.org/clojusc/env-ini
[clojars-badge]: https://img.shields.io/clojars/v/clojusc/env-ini.svg
[clojure ini]: https://github.com/jonase/clojure-ini
