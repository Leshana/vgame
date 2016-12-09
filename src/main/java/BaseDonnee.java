///////////////////////////////////////////////////////////////////////////////////////////////////
//
// BaseDonnee.java
//
///////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.*;
import java.lang.ref.*;
import java.sql.*;
import java.util.*;
import javax.swing.*;

///////////////////////////////////////////////////////////////////////////////////////////////////
public class BaseDonnee
{
	static private SoftReference<BaseDonnee> dataDB;
	static private SoftReference<BaseDonnee> saveDB;

	protected String chemin;
	protected Connection connexion;
	protected HashMap<ResultSet,Statement> m_statementMap;

	///////////////////////////////////////////////////////////////////////////////////////////////
	/** Creates a new instance of BaseDonnee */
	protected BaseDonnee(String ch)
	{
		File fileName = new File( ch );
		fileName = fileName.getAbsoluteFile();
		
		chemin = fileName.getPath().trim();
		m_statementMap = new HashMap<ResultSet,Statement>();

		try
		{
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

			// Build the DB connection string
			String database = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)}";
			database += ";DBQ=" + chemin;
			database += ";DriverID=22";
			database += ";READONLY=true";
			database += "}";

			connexion = DriverManager.getConnection( database, "", "" );
		}
		catch( Exception e )
		{
			Util.error( "BaseDonnee( " + ch + " )", e );
		}
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public ResultSet calculRequete(String req)
	{
		ResultSet ret = null;

		try
		{
			Statement cmd = connexion.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			cmd.execute( req );
			
			ret = cmd.getResultSet();
			
			if( ret != null )
			{
				m_statementMap.put( ret, cmd );
			}
		}
		catch( Exception e )
		{
			Util.error( "BaseDonnee::calculRequete( " + req + " )", e );
		}
		
		return ret;
	}

	public void finRequete( ResultSet rs )
	{
		try
		{
			Statement cmd = m_statementMap.get( rs );
			
			if( cmd != null )
			{
				cmd.close();
				m_statementMap.remove( rs );
			}
		}
		catch( Exception e )
		{
			Util.error( "BaseDonnee::finRequete()", e );
		}
	}
	
	public void execRequete(String req)
	{
		try
		{
			Statement cmd = connexion.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY );
			cmd.execute( req );
			cmd.close();
		}
		catch( Exception e )
		{
			Util.error( "BaseDonnee::execRequete( " + req + " )", e );
		}
	}
	
	public void close()
	{
	}

	///////////////////////////////////////////////////////////////////////////////////////////////
	public static BaseDonnee open_save()
	{
		BaseDonnee ret = null;

		if (saveDB != null)
		{
			ret = saveDB.get();
		}
		
		if (ret == null)
		{
			ret = new BaseDonnee( "VSave.mdb" );
			saveDB = new SoftReference<>( ret );
		}
		
		return ret;
	}
	
	public static BaseDonnee open_data()
	{
		BaseDonnee ret = null;

		if (dataDB != null)
		{
			ret = dataDB.get();
		}
		
		if (ret == null)
		{
			ret = new BaseDonnee( "VData.mdb" );
			dataDB = new SoftReference<>( ret );
		}
		
		return ret;
	}
}
