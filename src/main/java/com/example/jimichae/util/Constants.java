package com.example.jimichae.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class Constants {
	private final static GeometryFactory geometryFactory = new GeometryFactory();
	public static final int SRID_WGS84 = 4326;

	public static Point createCoordinate(double x, double y) {
		Point point = geometryFactory.createPoint(new Coordinate(x, y));
		point.setSRID(Constants.SRID_WGS84);
		return point;
	}
}
