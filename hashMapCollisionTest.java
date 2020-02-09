import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class hashMapCollisionTest
{
public static void getCollision( ArrayList list, Map m, int len, String s ) throws Exception
{
	String[] array = new String[] { "Ey", "FZ" };
	for( int i = 0; i < 2; ++i )
	{
		if( s.length() < len )
		{
			getCollision( list, m, len, s + array[ i ] );
		}
		else
		{
			String key = s + array[ i ];
			list.add( key );
		}
	}
}

public static void main( String[] args ) throws Exception
{
	ArrayList< String > list = new ArrayList<>();
	getCollision( list, null, 27, "" );
	p( list.get( 0 ) );
	p( list.get( 1 ) );
	//p( "size:" + list.size() );

	long start = System.currentTimeMillis();
	for( int i = 0; i < 100; ++i )
	{
		Map m = new HashMap( list.size() + 1000 );
		for( String s : list )
		{
			m.put( s, "1" );
		}
	}
	//p( Runtime.getRuntime().freeMemory() );
	p( ( System.currentTimeMillis() - start ) + " ms" );
}

public static void p( Object o )
{
	System.out.println( o );
}

}