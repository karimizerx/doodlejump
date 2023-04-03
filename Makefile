.PHONY: build run clean

FILES := $(patsubst src/%,../src/%,$(shell find src | grep '\.java$$'))
CLASS := $(patsubst out/%,../out/%,$(shell find out | grep '\.class$$'))



build:
	cd out; javac $(FILES) -d .


run: build
	cd out; java gui.App

clean:
	cd out; rm $(CLASS)