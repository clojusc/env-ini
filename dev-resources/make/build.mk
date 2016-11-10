all: clj jar cljs

node:
	lein cljsbuild once node

clj:
	lein compile

jar:
	lein jar

cljs:
	lein cljsbuild once

clean:
	rm -rf target .repl-* pom.xml*
