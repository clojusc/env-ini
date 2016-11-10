repl:
	@rlwrap \
		--command-name=clojure \
		--prompt-colour=yellow \
		lein repl

node-repl: node
	@rlwrap \
		--command-name=cljs-node \
		--prompt-colour=yellow \
		--substitute-prompt="clojusc.env-ini.node-dev=> " \
		--only-cook=".*=>" \
		lein node-repl

rhino-repl:
	@rlwrap \
		--command-name=cljs-rhino \
		--prompt-colour=yellow \
		--substitute-prompt="clojusc.env-ini.rhino-dev=> " \
		--only-cook=".*=>" \
		lein rhino-repl
