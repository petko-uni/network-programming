TGT ?=
LAB ?=

build:

	@mvn --batch-mode --update-snapshots package

run:

ifneq ($(strip $(TGT)),)
		@java -cp $(shell pwd)/target/dev.petko.np-1.0.jar dev.petko.np.${TGT}
else
ifneq ($(strip $(LAB)),)
		@java -cp $(shell pwd)/target/dev.petko.np-1.0.jar dev.petko.np.lab${LAB}.Main
else
		@echo "LAB or TGT required"
		@exit 1
endif
endif
