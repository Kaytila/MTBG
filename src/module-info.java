module myTurnedBasedGame
{
    requires junit;

    requires transitive java.desktop;

    requires transitive org.apache.logging.log4j.core;
    requires transitive org.apache.logging.log4j;

    //requires javafx.graphics;
    requires javafx.media;

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
    requires javafx.swing;

	exports net.ck.game.test;
	exports net.ck.game.backend.entities;
	exports net.ck.game.graphics;
	exports net.ck.game.map;
	exports net.ck.util.communication.graphics;
	exports net.ck.game.weather;
	exports net.ck.util.communication.keyboard;
	exports net.ck.game.backend.actions;
	exports net.ck.game.music;
	exports net.ck.game.items;
	exports net.ck.util.communication.sound;
	exports net.ck.game.backend.threading;
	exports net.ck.game.backend.time;
	exports net.ck.game.soundeffects;
    exports net.ck.game.backend.game;
	exports net.ck.game.backend.state;
	exports net.ck.game.backend.configuration;
	exports net.ck.game.backend.queuing;
	exports net.ck.game.ui.dialogs;
	exports net.ck.game.ui.components;
	exports net.ck.game.ui.buttons;
	exports net.ck.game.ui.listeners;
	exports net.ck.game.ui.dnd;
	exports net.ck.game.ui.renderers;
	exports net.ck.game.ui.highlighting;
	exports net.ck.game.ui.mainframes;
	exports net.ck.game.animation.missile;
	exports net.ck.game.animation.background;
	exports net.ck.game.animation.foreground;
	exports net.ck.game.animation.lifeform;
    exports net.ck.game.ui.state;
}