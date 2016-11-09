node-repl:
	@rlwrap \
		--command-name=cljs-node \
		--prompt-colour=yellow \
		--substitute-prompt="clojusc.env-ini.node-dev=> " \
		--only-cook="cljs.user=>" \
		lein node-repl

rhino-repl:
	@rlwrap \
		--command-name=cljs-node \
		--prompt-colour=yellow \
		--substitute-prompt="clojusc.env-ini.rhino-dev=> " \
		--only-cook="cljs.user=>" \
		lein rhino-repl
