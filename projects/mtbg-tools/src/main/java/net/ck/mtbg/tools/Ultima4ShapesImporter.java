package net.ck.mtbg.tools;

import net.ck.mtbg.util.CodeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * https://github.com/jtauber/ultima4/blob/master/shapes.py
 * @author Claus
 *EGA2RGB = [
    (0x00, 0x00, 0x00),
    (0x00, 0x00, 0xAA),
    (0x00, 0xAA, 0x00),
    (0x00, 0xAA, 0xAA),
    (0xAA, 0x00, 0x00),
    (0xAA, 0x00, 0xAA),
    (0xAA, 0x55, 0x00),
    (0xAA, 0xAA, 0xAA),
    (0x55, 0x55, 0x55),
    (0x55, 0x55, 0xFF),
    (0x55, 0xFF, 0x55),
    (0x55, 0xFF, 0xFF),
    (0xFF, 0x55, 0x55),
    (0xFF, 0x55, 0xFF),
    (0xFF, 0xFF, 0x55),
    (0xFF, 0xFF, 0xFF),
]


def load_shapes():
    shapes = []
    bytes = open("ULT/SHAPES.EGA").read()

    for i in range(256):
        shape = []
        for j in range(16):
            for k in range(8):
                d = ord(bytes[k + 8 * j + 128 * i])
                a, b = divmod(d, 16)
                shape.append(EGA2RGB[a])
                shape.append(EGA2RGB[b])
        shapes.append(shape)

    return shapes
 */
public class Ultima4ShapesImporter
{
	private final Logger logger = LogManager.getLogger(CodeUtils.getRealClass(this));

	private static ArrayList<Image> shapes;

	public static void main(String[] args)
	{
		File file = new File("ultima4" + File.separator + "shapes.ega");
		byte[] shapebytes;
		try
		{
			shapebytes = Files.readAllBytes(Path.of("ultima4" + File.separator + "shapes.ega"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		for (int i = 0; i <= 256; i++)
		{
			//BufferedImage img = new BufferedImage();
			for (int j = 0; j <= 16; j++)
			{
				for (int k = 0; k <= 8; k++)
				{
				//	int d = Character.
				}
			}
		}
		

	}
}
