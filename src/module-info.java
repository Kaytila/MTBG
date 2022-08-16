module myTurnedBasedGame
{
	requires junit;

	requires transitive java.desktop;

	requires transitive org.apache.logging.log4j.core;
	requires transitive org.apache.logging.log4j;


	requires org.locationtech.jts;

	requires java.xml;
	requires java.sql;
    requires eventbus.java;
	requires org.apache.commons.lang3;
	requires com.google.common;
	requires imgscalr.lib;
	requires java.datatransfer;
	requires org.apache.commons.io;

    requires org.apache.tika.core;
	requires com.opencsv;

	exports net.ck.game.test;
	exports net.ck.game.backend;
	exports net.ck.game.backend.entities;
	exports net.ck.game.ui;
	exports net.ck.game.graphics;
	exports net.ck.game.map;
	exports net.ck.util.communication.graphics;
	exports net.ck.game.weather;
	exports net.ck.util.communication.keyboard;
	exports net.ck.game.backend.actions;
	exports net.ck.game.sound;
	exports net.ck.game.items;
	exports net.ck.util.communication.sound;
	exports net.ck.game.animation;
    exports net.ck.game.backend.threading;
	exports net.ck.game.old;
	exports net.ck.game.backend.time;
}