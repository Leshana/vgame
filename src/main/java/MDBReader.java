///////////////////////////////////////////////////////////////////////////////////////////////////
// Utility class for reading data tables from the database.
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.io.*;
import java.lang.reflect.*;
import java.lang.invoke.*;
import java.sql.*;
import java.util.*;

public class MDBReader
{
	private interface IFieldParser
	{
		public Object parseField( ResultSet results, String name ) throws SQLException;
	}
	
	private static Hashtable<Class,IFieldParser> fieldParsers;
	
	static
	{
		fieldParsers = new Hashtable<Class,IFieldParser>();
		
		fieldParsers.put( int.class, new IFieldParser() {
			public Object parseField( ResultSet row, String name ) throws SQLException
			{
				return new Integer( row.getInt( name ) );
			}
		});
		fieldParsers.put( long.class, new IFieldParser() {
			public Object parseField( ResultSet row, String name ) throws SQLException
			{
				return new Long( row.getLong( name ) );
			}
		});
		fieldParsers.put( String.class, new IFieldParser() {
			public Object parseField( ResultSet row, String name ) throws SQLException
			{
				return row.getString( name );
			}
		});
	}
	
	private static class DBField
	{
		private Field m_field;
		private IFieldParser m_parser;
		
		public DBField( Field field )
		{
			m_field = field;
			m_parser = fieldParsers.get( field.getGenericType() );
			
			if (m_parser == null)
			{
				Util.error( "No field parser available for type " + field.getGenericType() );
			}
		}
		
		public void parseField( Object into, ResultSet row )
		{
			if (m_parser != null)
			{
				try
				{
					Object value = m_parser.parseField( row, m_field.getName() );
					m_field.set( into, value );
				}
				catch (Throwable t)
				{
				}
			}
		}
		
		public String getName()
		{
			return m_field.getName();
		}
		
		public Object get( Object onObject )
		{
			try
			{
				return m_field.get( onObject );
			}
			catch (Throwable t)
			{
				return null;
			}
		}
	}
	
	public static <T> Hashtable<Integer,T> loadTable( String tableName, Class dataType )
	{
		Hashtable<Integer,T> ret = new Hashtable<Integer,T>();
		
		Vector<T> rows = loadRows( tableName, dataType );
		if (!rows.isEmpty())
		{
			final Field[] fields = dataType.getDeclaredFields();
				
			// The first field is treated as the key.
			final DBField keyField = new DBField( fields[0] );
			
			Iterator<T> rowIter = rows.iterator();
			while (rowIter.hasNext())
			{
				T row = rowIter.next();
				ret.put( (Integer)keyField.get( row ), row );
			}
		}
		
		return ret;
	}
	
	public static <T> Hashtable<Integer,Vector<T>> loadMappedTable( String tableName, Class dataType )
	{
		Hashtable<Integer,Vector<T>> ret = new Hashtable<Integer,Vector<T>>();
		
		Vector<T> rows = loadRows( tableName, dataType );
		if (!rows.isEmpty())
		{
			final Field[] fields = dataType.getDeclaredFields();

			// The first field is treated as the mapping key
			final DBField keyField = new DBField( fields[0] );
			
			// The second field is treated as a sorting parameter.
			final DBField mapField = new DBField( fields[1] );
			
			Iterator<T> rowIter = rows.iterator();
			while (rowIter.hasNext())
			{
				T row = rowIter.next();
				
				Integer mapValue = (Integer)mapField.get( row );
				
				Vector<T> mapVec = ret.get( mapValue );
				if (mapVec == null)
				{
					mapVec = new Vector<T>();
					ret.put( mapValue, mapVec );
				}
				
				mapVec.add( row );
			}
			
			// Sort each of the mapped lists by the key field.
			Iterator<Vector<T>> mapIter = ret.values().iterator();
			while (mapIter.hasNext())
			{
				Vector<T> mapVec = mapIter.next();
				Collections.sort( mapVec, new Comparator<T>() {
					public int compare( T a, T b )
					{
						Comparable ca = (Comparable)keyField.get( a );
						Comparable cb = (Comparable)keyField.get( b );
						return ca.compareTo( cb );
					} } );
			}
		}
		
		return ret;
	}
	
	public static <T> Vector<T> loadRows( String tableName, Class dataType )
	{
		Vector<T> ret = new Vector<T>();
		
		Vector<DBField> dataFields = new Vector<DBField>();
		Field[] fields = dataType.getDeclaredFields();
		
		for (int i = 0; i < fields.length; ++i)
		{
			dataFields.add( new DBField( fields[i] ) );
		}
		
		Connection db = openDB();

		if (db != null)
		{
			try
			{
				Statement cmd = db.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
				
				cmd.execute( "SELECT * FROM " + tableName );
				ResultSet resultSet = cmd.getResultSet();
				
				if (resultSet.first())
				{
					resultSet.last();
					ret.ensureCapacity( resultSet.getRow() );
					resultSet.first();
					
					do
					{
						T newData = (T)dataType.newInstance();
						
						Iterator<DBField> fieldIter = dataFields.iterator();
						while (fieldIter.hasNext())
						{
							fieldIter.next().parseField( newData, resultSet );
						}

						ret.add( newData );
					}
					while (resultSet.next());
				}
				
				cmd.close();
			}
			catch( Throwable t )
			{
			}
			
			closeDB( db );
		}
		
		return ret;
	}
	
	private static Connection openDB()
	{
		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// Build the DB connection string
			String conStr = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)}";
			conStr += ";DBQ=VData.mdb";
			conStr += ";DriverID=22";
			conStr += ";READONLY=true";
			conStr += "}";

			return DriverManager.getConnection( conStr, "", "" );
		}
		catch (Throwable t)
		{
			return null;
		}
	}
	
	private static void closeDB( Connection db )
	{
		try
		{
			db.close();
		}
		catch (Throwable t)
		{
		}
	}
}