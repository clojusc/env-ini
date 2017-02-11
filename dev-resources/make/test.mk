check:
	lein test :all

check-jar-config:
	@lein uberjar
	java -cp target/env-ini-$(VERSION)-standalone.jar \
	clojusc.env_ini_test show-example-ini
