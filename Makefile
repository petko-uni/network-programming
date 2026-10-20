TGT ?=
LAB ?=

build:

	@mvn --batch-mode --update-snapshots package

run:

ifneq ($(strip $(TGT)),)
		@java -cp $(shell pwd)/target/dev.petko.oop-1.0.jar dev.petko.np.${TGT}
else
ifeq ($(strip $(LAB)),)
		@java -cp $(shell pwd)/target/dev.petko.oop-1.0.jar dev.petko.np.Main
else
ifeq ($(strip $(PROBLEM)),)
		@java -cp $(shell pwd)/target/dev.petko.oop-1.0.jar dev.petko.np.lab${LAB}.Main
else
		@java -cp $(shell pwd)/target/dev.petko.oop-1.0.jar dev.petko.np.lab${LAB}.Problem${PROBLEM}
endif
endif
endif
