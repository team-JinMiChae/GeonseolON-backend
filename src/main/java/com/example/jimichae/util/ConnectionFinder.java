package com.example.jimichae.util;

import java.lang.reflect.Field;
import java.sql.Connection;

import org.geolatte.geom.codec.db.oracle.DefaultConnectionFinder;

public class ConnectionFinder extends DefaultConnectionFinder {
	// hibernate spatial이 커넥션을 못 찾는 문제 해결하기 위한 코드.
	// https://stackoverflow.com/questions/47753350/couldnt-get-at-the-oraclespatial-connection-object-from-the-preparedstatement
	@Override
	public Connection find(Connection con) {
		try {
			Field delegate = con.getClass().getSuperclass().getDeclaredField("delegate");
			delegate.setAccessible(true);
			return (Connection)delegate.get(con);
		}catch (Exception e){
			throw new RuntimeException(e);
		}
	}
}
