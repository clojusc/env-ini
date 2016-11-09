all: clj jar cljs

clj:
	lein compile

jar:
	lein jar

cljs:
	lein cljsbuild once

clean:
	rm -rf target .repl-* pom.xml*
