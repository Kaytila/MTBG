package net.ck.mtbg.items;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

@Log4j2
@Getter
@Setter
public abstract class AbstractItem implements Transferable, Serializable
{
    private ArrayList<Effects> additionalEffects;
    private int id;
    private boolean isContainer;

    private boolean furniture;

    private String name;
    private double value;
    private double weight;
    private Point mapPosition;

    public AbstractItem()
    {
        setFurniture(false);
    }

    public abstract BufferedImage getItemImage();


    @Override
    public DataFlavor[] getTransferDataFlavors()
    {
        logger.info("getTransferDataFlavors used");
        DataFlavor flavor1 = new DataFlavor(Object.class, "X-test/test; class=<java.lang.Object>; foo=bar");
        DataFlavor flavor2 = new DataFlavor(Object.class, "X-test/test; class=<java.lang.Object>; x=y");
        DataFlavor[] dataFlavor = new DataFlavor[2];
        dataFlavor[0] = flavor1;
        dataFlavor[1] = flavor2;
        return dataFlavor;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor)
    {
        logger.info("this is used");
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor flavor)
    {
        logger.info("this is used");
        return this;
    }
}
