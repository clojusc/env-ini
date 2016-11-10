all: clj jar cljs

node:
	lein cljsbuild once node

clj:
	lein compile

jar:
	lein jar

cljs:
	lein cljsbuild once env-ini

clean:
	rm -rf target .repl-* pom.xml*

travis: clean clj jar node cljs check
