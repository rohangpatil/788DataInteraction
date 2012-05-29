
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

//import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.*;
import org.neo4j.graphdb.index.Index;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.impl.util.FileUtils;
public class Populate1 {

	/**
	 * @param args
	 */
    private static final String DB_PATH = "target/neo4j-hello-db";
    private static Random rand = new Random(123456789L);
    String greeting;
    // START SNIPPET: vars
    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Index<Node> idxNodes;
    Relationship relationship;
    // END SNIPPET: vars

    // START SNIPPET: createReltype
    private static enum RelTypes implements RelationshipType
    {
        KNOWS
    }
    // END SNIPPET: createReltype


    GraphDatabaseService createDb(String DB_PATH)
    {
        clearDb();
        // START SNIPPET: startDb
        graphDb =  new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        idxNodes = graphDb.index().forNodes("nodes");
        registerShutdownHook( graphDb );
        // END SNIPPET: startDb

        // START SNIPPET: transaction
        Transaction tx = graphDb.beginTx();
        try
        {
            
            ArrayList<Node> allNodes = new ArrayList<Node>();
            
            int clusterIds[]={1,2,3,4,5,6,7,8,9,10};
            for(int i=1;i<=1000;i++){
            	Node tempNode = graphDb.createNode();
            	tempNode.setProperty("id", i);
            	int clusterId = clusterIds[rand.nextInt(9)];
            	tempNode.setProperty("clusterId", clusterId);
            	tempNode.setProperty("x", (rand.nextInt(999)+1)+clusterId+(clusterId*1000));
            	tempNode.setProperty("y", (rand.nextInt(999)+1)+clusterId+(clusterId*2000));
            	allNodes.add(tempNode);
            	idxNodes.add(tempNode,"id", i);
            }
            for(int id = 1 ; id <= 1000 ; id++){
            	Node sourceNode = allNodes.get(id-1);
            	int numEdges = rand.nextInt(50)+1;
            	for(int i=0;i<numEdges;i++){
            		int tempNodeId = rand.nextInt(999)+1;
            		if(tempNodeId==id)
            			continue;
            		Node tempNode = allNodes.get(tempNodeId-1);
                	relationship = sourceNode.createRelationshipTo(tempNode, RelTypes.KNOWS);
            	}
            }
            tx.success();
        }
        finally
        {
            tx.finish();
        }
        // END SNIPPET: transaction
        return graphDb;
    }

    private void clearDb()
    {
        try
        {
            FileUtils.deleteRecursively( new File( DB_PATH ) );
        }
        catch ( IOException e )
        {
            throw new RuntimeException( e );
        }
    }
    void removeData()
    {
        Transaction tx = graphDb.beginTx();
        try
        {
            // START SNIPPET: removingData
            // let's remove the data
            firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING ).delete();
            firstNode.delete();
            secondNode.delete();
            // END SNIPPET: removingData

            tx.success();
        }
        finally
        {
            tx.finish();
        }
    }

    void shutDown()
    {
        System.out.println();
        System.out.println( "Shutting down database ..." );
        // START SNIPPET: shutdownServer
        graphDb.shutdown();
        // END SNIPPET: shutdownServer
    }

	
    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
            }
        } );
    }
	
    public static void main( final String[] args )
    {
        Populate1 hello = new Populate1();
        hello.graphDb = new EmbeddedGraphDatabase(DB_PATH);
        //hello.graphDb = hello.createDb(DB_PATH);
        if(hello.idxNodes==null)
        	hello.idxNodes = hello.graphDb.index().forNodes("nodes");
        Node node = hello.idxNodes.get("id", 31).getSingle();
        System.out.println(node.getProperty("clusterId"));
        hello.shutDown();
    }

}
