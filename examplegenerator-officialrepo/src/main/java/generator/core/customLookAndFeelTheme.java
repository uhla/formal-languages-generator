
package generator.core;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.metal.DefaultMetalTheme;

public class customLookAndFeelTheme extends DefaultMetalTheme
{
	public String getName()
	{
		return "Toms";
	}

	private final ColorUIResource primary1 = new ColorUIResource(255, 255, 0);
	private final ColorUIResource primary2 = new ColorUIResource(0, 105, 255);
	private final ColorUIResource primary3 = new ColorUIResource(105, 0, 255);

	protected ColorUIResource getPrimary1()
	{
		return primary1;
	}

	protected ColorUIResource getPrimary2()
	{
		return primary2;
	}

	protected ColorUIResource getPrimary3()
	{
		return primary3;
	}

}
