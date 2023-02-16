.PHONY: build run clean

FILES := $(patsubst src/%,../src/%,$(shell find src | grep '\.java$$'))
IMAGES := $(patsubst src/%,../src/%,../src/%/%	,$(shell find src | grep '\.png$$'))



build:
	-mkdir out
	cd out; javac $(FILES) -d .

	-mkdir out/gui/images 	
	mv $(IMAGES) out/gui/images/

run: build
	cd out; java App

clean:
	-rm -r out