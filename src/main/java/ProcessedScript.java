//=================================================================================================
// ProcessedScript
//  A pre-processed script, ready for execution.
//=================================================================================================

import java.lang.reflect.*;
import java.lang.invoke.*;
import java.io.*;
import java.util.*;

public class ProcessedScript implements Serializable
{
	private class ScriptLine implements Serializable
	{
		private MethodHandle m_call;
		private String[] m_args;

		public ScriptLine( Vector<String> words )
		{
			m_args = new String[words.size()];
			
			for (int i = 0; i < m_args.length; ++i)
			{
				m_args[i] = words.elementAt( i );
			}
			
			try
			{
				// Lower-case the first argument, since that's the call name.
				m_call = ScriptInterface.methods.get( m_args[0].toLowerCase() );
			}
			catch (Exception e)
			{
				Util.error( e );
			}
		}
		
		public void invoke() throws Throwable
		{
			if (m_call != null)
			{
				m_call.invokeExact( ScriptInterface.instance, m_args );
			}
		}
		
		public boolean isValid()
		{
			return m_call != null;
		}
	}

	private String m_scriptText;
	private ScriptLine[] m_scriptLines;
	
	public ProcessedScript( String script )
	{
		// Pull the script apart into processing-level chunks so it can be passed directly through
		//  the low level execute function.
		m_scriptLines = null;
		m_scriptText = script;
		
		processScript();
	}
	
	private void processScript()
	{	
		boolean whitespace = true;
		int wordStart = 0;
		
		Vector<ScriptLine> lineBuilder = new Vector<ScriptLine>();
		Vector<String> currentLine = new Vector<String>();
		
		final int scriptLen = m_scriptText.length();
		for (int i = 0; i < scriptLen; ++i)
		{
			char nextCh = m_scriptText.charAt( i );
			switch (m_scriptText.charAt( i ))
			{
			case '\n':
				// Close the line and start a new one.
				if (!currentLine.isEmpty())
				{
					ScriptLine newLine = new ScriptLine( currentLine );
					if (newLine.isValid())
					{
						lineBuilder.add( newLine );
					}
					currentLine.clear();
				}
				whitespace = true;
				break;
			case '(':
			case ')':
			case ',':
				// All the above characters are treated as token breaks.
				if (!whitespace)
				{
					currentLine.add( m_scriptText.substring( wordStart, i ).trim() );
					whitespace = true;
				}
				break;
			default:
				if (whitespace)
				{
					// Start a new word.
					wordStart = i;
					whitespace = false;
				}
				break;
			}
		}
		
		if (!whitespace)
		{
			// We finished while reading a word, add the word as the last item in the current line.
			currentLine.add( m_scriptText.substring( wordStart ) );
		}
		
		if (!currentLine.isEmpty())
		{
			// Make sure we record the last line.
			ScriptLine newLine = new ScriptLine( currentLine );
			if (newLine.isValid())
			{
				lineBuilder.add( newLine );
			}
		}
		
		m_scriptLines = new ScriptLine[lineBuilder.size()];
		
		for (int i = 0; i < m_scriptLines.length; ++i)
		{
			m_scriptLines[i] = lineBuilder.elementAt( i );
		}
	}
	
	public void execute()
	{
		if (m_scriptLines == null)
		{
			processScript();
		}

		try
		{
			for (int i = 0; i < m_scriptLines.length; ++i)
			{
				m_scriptLines[i].invoke();
			}
		}
		catch (Exception e)
		{
			Util.error( e );
		}
		catch (Throwable t)
		{
			Util.error( "Unidentifiable error parsing script" );
		}
	}
}
