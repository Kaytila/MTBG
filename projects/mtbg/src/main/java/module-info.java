open module net.ck.mtbg
{
    requires static lombok;
    requires java.desktop;
    requires java.xml;
    requires java.sql;
    //requires javafx.media;
    //requires javafx.swing;
    requires org.slf4j;
    requires org.apache.logging.log4j.core;
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;
    requires com.google.common;
    requires java.datatransfer;
    requires org.apache.commons.io;
    requires org.apache.tika.core;
    requires com.opencsv;
    requires com.fasterxml.jackson.core;

    requires eventbus.java;
    requires imgscalr.lib;
    //requires org.openimaj;

    exports net.ck.mtbg.backend.game;
    exports net.ck.mtbg.backend.state;
    exports net.ck.mtbg.backend.configuration;
    exports net.ck.mtbg.backend.queuing;
    exports net.ck.mtbg.backend.threading;
    exports net.ck.mtbg.backend.time;
    exports net.ck.mtbg.backend.actions;
    exports net.ck.mtbg.backend.entities;
    exports net.ck.mtbg.ui.dialogs;
    exports net.ck.mtbg.ui.listeners.game;
    exports net.ck.mtbg.ui.listeners.charactereditor;
    exports net.ck.mtbg.ui.listeners.mapeditor;
    exports net.ck.mtbg.ui.dnd;
    exports net.ck.mtbg.ui.highlighting;
    exports net.ck.mtbg.ui.state;
    exports net.ck.mtbg.animation.missile;
    exports net.ck.mtbg.animation.background;
    exports net.ck.mtbg.animation.foreground;
    exports net.ck.mtbg.animation.lifeform;
    exports net.ck.mtbg.graphics;
    exports net.ck.mtbg.map;
    exports net.ck.mtbg.weather;
    exports net.ck.mtbg.music;
    exports net.ck.mtbg.items;
    exports net.ck.mtbg.soundeffects;
    exports net.ck.mtbg.util.communication.graphics;
    exports net.ck.mtbg.util.communication.sound;
    exports net.ck.mtbg.backend.entities.attributes;
    exports net.ck.mtbg.backend.entities.ai;
    exports net.ck.mtbg.backend.entities.entities;
    exports net.ck.mtbg.backend.entities.skills;
    exports net.ck.mtbg.util.communication.time;
    exports net.ck.mtbg.backend.applications;
    exports net.ck.mtbg.ui.controllers;
    exports net.ck.mtbg.ui.renderers.charactereditor;
    exports net.ck.mtbg.ui.renderers.game;
    exports net.ck.mtbg.ui.renderers.mapeditor;
    exports net.ck.mtbg.ui.models.charactereditor;
    exports net.ck.mtbg.ui.models.game;
    exports net.ck.mtbg.ui.mainframes.mapeditor;
    exports net.ck.mtbg.ui.mainframes.charactereditor;
    exports net.ck.mtbg.ui.mainframes.game;
    exports net.ck.mtbg.ui.components.game;
    exports net.ck.mtbg.ui.components.charactereditor;
    exports net.ck.mtbg.ui.components.mapeditor;
    exports net.ck.mtbg.ui.buttons.game;
    exports net.ck.mtbg.ui.buttons.mapeditor;
    exports net.ck.mtbg.ui.buttons.general;
    exports net.ck.mtbg.util.communication.keyboard.framework;
    exports net.ck.mtbg.util.communication.keyboard.gameactions;
}