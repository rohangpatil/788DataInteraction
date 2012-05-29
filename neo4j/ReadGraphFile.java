import java.io.*;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.util.Random;

//import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.*;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.kernel.impl.util.FileUtils;

public class ReadGraphFile {
	
	//private String pFile;
  private static final String DB_PATH = "target/neo4j-hello-db";
  private static final String FILE_PATH = "/Users/rohanpatil/Downloads/fwd3samplesupergraphs/super_graph0.txt";
  private Random rand = new Random(123456789L);
  private Index<Node> idxNodeIds;
  

  public static void main(String... aArgs) throws FileNotFoundException {

    ReadGraphFile parser = new ReadGraphFile(FILE_PATH);
	Populate1 p = new Populate1();
	GraphDatabaseService graphDb = p.createDb(DB_PATH);
    parser.idxNodeIds = graphDb.index().forNodes("nodes");
	parser.processLineByLine(graphDb);
    
    log("Done.");
  }
  
  /**
   Constructor.
   @param aFileName full name of an existing, 
   readable Graph file in format given by Yang Zhang.
  */

  public ReadGraphFile(String aFileName){
    fFile = new File(aFileName);  
  }
  
  /** Template method that calls {@link #processLine(String)}.  */
  public final void processLineByLine(GraphDatabaseService graphDb) throws FileNotFoundException 
  {
    //Note that FileReader is used, not File, since File is not Closeable
    Scanner scanner = new Scanner(new FileReader(fFile));
	int inumnodes = 0 ;
	int inumedges = 0;    
    //Getting the first line which specifies the number of nodes and number of edges
    //in the raw graph file
    if(scanner.hasNextLine())
    {
    	Scanner flinescanner = new Scanner(scanner.nextLine());
    	//String firstLine = flinescanner.nextLine();
    	flinescanner.useDelimiter(" ");
    	if(flinescanner.hasNext())
    	{
    		String snumnodes = flinescanner.next();
    		String snumedges = flinescanner.next();
    		inumnodes = Integer.parseInt(snumnodes);
    		inumedges = Integer.parseInt(snumedges);
    	}
    	else
    	{
    		log("Empty or invalid first line. Unable to process.");
    	}	
    }
    
    // 
    try {
      //first use a Scanner to get each node line
      for ( int i = 0; i < inumnodes ; i++){
    	  //scanner.hasNextLine();
    	  processNodeLine(scanner.nextLine() , graphDb);
      }

      
      for( int j = 0; j < inumedges ; j++){
    	  processEdgeLine(scanner.nextLine(), graphDb);
      }
    }
    finally {
      //ensure the underlying stream is always closed
      //this only has any effect if the item passed to the Scanner
      //constructor implements Closeable (which it does in this case).
      scanner.close();
    }
  }
  
  /** 
	Actual Parsing logic
  */
  protected void processNodeLine(String aLine , GraphDatabaseService graphDb){
    //use a second Scanner to parse the content of each line 
    
	Scanner scanner = new Scanner(aLine);
    int inodeid , iclusterid;
    
    
    scanner.useDelimiter(" ");
    Transaction tx = graphDb.beginTx();
    try
    {
	    if( scanner.hasNext() ){
			
	    	String snodeid = scanner.next();
			String sclusterid = scanner.next();
			inodeid = Integer.parseInt(snodeid);
			iclusterid = Integer.parseInt(sclusterid);
			System.out.println("nodeid "+inodeid);
			System.out.println("clusterid"+iclusterid); 
	
			Node tempNode = graphDb.createNode();
			tempNode.setProperty("clusterid", iclusterid);
			tempNode.setProperty("nodeid",inodeid+1);
			tempNode.setProperty("x",rand.nextInt(999)+1+iclusterid);
			tempNode.setProperty("y", rand.nextInt(999)+1+iclusterid);
	        idxNodeIds.add(tempNode,"nodeid",inodeid+1);
	
	    }
	    else {
	      log("Empty or invalid node line. Unable to process.");
	    }
    }
    finally{
    	tx.finish();
    }
    
    //no need to call scanner.close(), since the source is a String
  }

  protected void processEdgeLine(String aLine , GraphDatabaseService graphDb)
  {
	    //use a second Scanner to parse the content of each line 
	    Scanner scanner = new Scanner(aLine);
	    scanner.useDelimiter(" ");
	    int isourcenodeid , idestnodeid , iweight;
	    Transaction tx = graphDb.beginTx();
	    try
	    {
		    if( scanner.hasNext() )
		    {
		    	isourcenodeid = 0;
		    	idestnodeid = 0;
				
		    	String ssourcenodeid = scanner.next();
				String sdestnodeid = scanner.next();
				String sweight = scanner.next();
				isourcenodeid = Integer.parseInt(ssourcenodeid);
				idestnodeid = Integer.parseInt(sdestnodeid);
				iweight = Integer.parseInt(sweight);
				
				isourcenodeid = isourcenodeid+1;
				idestnodeid = idestnodeid + 1;
		
				Node sourcenode = idxNodeIds.get( "nodeid", isourcenodeid ).getSingle();
				//Node sourcenode = hits.getSingle();
				//hits.close();
				Node destnode = idxNodeIds.get("nodeid", idestnodeid).getSingle();
				//Node destnode = hits.getSingle();
				//hits.close();
				Relationship temprelationship = sourcenode.createRelationshipTo(destnode, RelTypes.CONNECTS);
				temprelationship.setProperty("weight", sweight);
		        
		
		    }
		    else {
		      log("Empty or invalid node line. Unable to process.");
		    }
	    }
	    finally{
	    	tx.finish();
	    }
	    //no need to call scanner.close(), since the source is a String
  }
  
  // PRIVATE 
  private final File fFile;
  
  private static void log(Object aObject){
    System.out.println(String.valueOf(aObject));
  }
  // START SNIPPET: createReltype
  private static enum RelTypes implements RelationshipType
  {
      CONNECTS
  }
} 
