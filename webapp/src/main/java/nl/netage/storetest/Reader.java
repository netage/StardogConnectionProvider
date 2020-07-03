package nl.netage.storetest;

import java.io.IOException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.complexible.common.rdf.query.resultio.TextTableQueryResultWriter;
import com.complexible.stardog.api.Connection;
import com.complexible.stardog.api.SelectQuery;
import com.stardog.stark.query.SelectQueryResult;
import com.stardog.stark.query.io.QueryResultFormats;
import com.stardog.stark.query.io.QueryResultWriters;
import com.stardog.stark.query.io.ResultWritingFailed;

import nl.netage.stardog.connectionprovider.StardogConnectionProvider;

@Path("/")
public class Reader {
	
	@GET
	public String test()
	{
		Context initCtx;
		try {
			initCtx = new InitialContext();

			StardogConnectionProvider provider = (StardogConnectionProvider) initCtx.lookup("java:comp/env/mystore" );
			System.out.println("server: "+provider.getServer());
			System.out.println("store: "+provider.getStore());
			System.out.println("user: "+provider.getUser());
			System.out.println("password: "+provider.getPassword());
			Connection aConn = provider.getConnection();
			
			SelectQuery aQuery = aConn.select("select * where { ?s ?p ?o }");


			// But getting *all* the statements is kind of silly, so let's actually specify a limit, we only want 10 results.
			aQuery.limit(10);


			// We can go ahead and execute this query which will give us a result set.  Once we have our result set, we can do
			// something interesting with the results.
			// NOTE: We use try-with-resources here to ensure that our results sets are always closed.
			try(SelectQueryResult aResult = aQuery.execute()) {
				System.out.println("The first ten results...");

				QueryResultWriters.write(aResult, System.out,QueryResultFormats.JSON);
			} catch (ResultWritingFailed e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Connection aConn2 = provider.getConnection();
			
			SelectQuery aQuery2 = aConn2.select("select * where { ?s ?p ?o }");


			// But getting *all* the statements is kind of silly, so let's actually specify a limit, we only want 10 results.
			aQuery2.limit(10);


			// We can go ahead and execute this query which will give us a result set.  Once we have our result set, we can do
			// something interesting with the results.
			// NOTE: We use try-with-resources here to ensure that our results sets are always closed.
			try(SelectQueryResult aResult = aQuery2.execute()) {
				System.out.println("The first ten results...");

				QueryResultWriters.write(aResult, System.out, TextTableQueryResultWriter.FORMAT);
			} catch (ResultWritingFailed e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Connection aConn3 = provider.getConnection();
			
			SelectQuery aQuery3 = aConn3.select("select * where { ?s ?p ?o }");


			// But getting *all* the statements is kind of silly, so let's actually specify a limit, we only want 10 results.
			aQuery3.limit(10);


			// We can go ahead and execute this query which will give us a result set.  Once we have our result set, we can do
			// something interesting with the results.
			// NOTE: We use try-with-resources here to ensure that our results sets are always closed.
			try(SelectQueryResult aResult = aQuery3.execute()) {
				System.out.println("The first ten results...");

				QueryResultWriters.write(aResult, System.out, TextTableQueryResultWriter.FORMAT);
			} catch (ResultWritingFailed e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			aConn.close();
			aConn2.close();
			// Create a connection leak!
			//aConn3.close();
			
			
			return "provider connected";
		} catch (NamingException e) {
			System.out.println("StardogStore Exception :" + e.getMessage());
		}
	
		return "Connection failed";
	}
}


