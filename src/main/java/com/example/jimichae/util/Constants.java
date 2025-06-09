package com.example.jimichae.util;

import java.util.List;

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

	public static List<String> NO_CLIENT_URLS = List.of(
		"/api/v1/institution/save", "/api/v1/weather_guide/save", "/api/v1/weather_guide/detail/accident/{weatherType}"
	);
}
