PROJ = env-ini
VERSION = $(lastword $(shell head -1 project.clj))

include dev-resources/make/build.mk
include dev-resources/make/dev.mk
include dev-resources/make/test.mk
