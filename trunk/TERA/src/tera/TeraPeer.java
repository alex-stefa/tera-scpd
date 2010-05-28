package tera;

import tera.simulator.TeraSimulator;

public class TeraPeer
{
	public static void main(String[] args)
	{
		(new TeraSimulator("settings.properties")).simulate();
	}
}
