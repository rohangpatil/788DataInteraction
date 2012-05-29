#------------------------------------------------------------------------------
#Author: Vineet Nair
#Parsing the raw graph file and populate the features matrix 
#------------------------------------------------------------------------------
import re
import sys 
#import pickle
import random
from operator import itemgetter

# Getting the filenames from the command-line
if (len(sys.argv)) > 1:
    number = sys.argv[1]
else:
    print("Suffix of the file needed")
    exit()

file1 = "super_graph"+ str(number) +".txt"
file2 = "yang_parsed_test" + str(number) + ".gexf"

f1 = open(file1 , 'r')
f2 = open(file2 , 'w')
i = 1

# Tokenising the data in the raw graph file
tokenizer = re.compile('[a-zA-Z0-9]+', re.IGNORECASE)

#Reading the first line in the raw graph file
line1 = f1.readline()

#Processing the first line
line1 = tokenizer.findall(line1);
"""
print(line1)
"""
#Getting the number of nodes and edges from the line 1
numnodes = int(line1[0])
numedges = int(line1[1])

#printing the gexf file
headergexf = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<gexf xmlns:viz=\"htt\
p://www.gexf.net/1.1draft/viz\" xmlns:xsi=\"\
http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.gephi.org/gexf/1\
.1draft\" xmlns:ns0=\"xsi\" version=\"1.1\" ns0:schemaLocation=\"http://www.gep\
hi.org/gexf/1.1draft http://gephi.org/gexf/1.1draft.xsd\">\n    <meta lastmodif\
ied=\"2012-05-19T16:34:19.584550\">\n    <creator>Vineet -Rohan</creator>\n    \
<description>Institutions and professers IEP 1970</description>\n    </meta>\n"
#Writing the header into the new gexf file
f2.write(headergexf)

f2.write("\t<graph type=\"static\" label=\"Twitter\">\n")
"""
#Describing the node attributes
#f2.write("\t\t<attributes class=\"node\" mode=\"static\">\n")
f2.write("\t\t<attributes class=\"node\" type=\"static\">\n")
f2.write("\t\t  <attribute id=\"0\" title=\"node-attribute\" type=\"string\">\n")
f2.write("\t\t</attributes>\n")

#Describing the edge attributes
#f2.write("\t\t<attributes class=\"edge\" mode=\"static\">\n")
f2.write("\t\t<attributes class=\"edge\" type=\"static\">\n")
f2.write("\t\t  <attribute id=\"0\" title=\"edge-attribute\" type=\"string\">\n")
f2.write("\t\t</attributes>\n")"""
f2.write("\t\t<nodes>\n")
#Reading the nodes part from the raw graph file and writing them into
#gexf file
#print(numnodes)
for x in range(numnodes):
    nodeline = f1.readline()
    nodeline = tokenizer.findall(nodeline)
    f2.write("\t\t\t<node id=\"")
    nodeid = str(nodeline[0])
    nodeattr = str(nodeline[1])
    f2.write(nodeid)
    f2.write("\" label=\"")
    f2.write(nodeattr)
    f2.write("\">\n")
    b=random.randint(0,255)
    g=random.randint(0,255)
    r=random.randint(0,255)
    f2.write("\t\t\t<viz:color b=\"")
    f2.write(str(b))
    f2.write("\" g=\"")
    f2.write(str(g))
    f2.write("\" r=\"")
    f2.write(str(r))
    f2.write("\" />\n\t\t\t</node>\n")
"""
    f2.write("\t\t\t<attvalues>\n")
    f2.write("\t\t\t\t<attvalue id=\"0\" value=\"")
    nodeattr = str(nodeline[1])
    f2.write(nodeattr)
    f2.write("\"/>\n\t\t\t</attvalues>\n")
    f2.write("\t\t\t</node>\n")
""" 
f2.write("\t\t</nodes>\n")

f2.write("\t\t<edges>\n")
#Reading the edges part from the raw graph file and writing them into
#gexf file

for y in range(numedges):
    edgeline = f1.readline()
    edgeline = tokenizer.findall(edgeline)
    sourceid = str(edgeline[0])
    destid = str(edgeline[1])
    edgeattr = str(edgeline[2])
    f2.write("\t\t\t<edge id=\"")
    f2.write(str(y))
    f2.write("\" source=\"")
    f2.write(sourceid)
    f2.write("\" target=\"")
    f2.write(destid)
    f2.write("\" label=\"")
    f2.write(edgeattr)
    f2.write("\"/>\n")
"""
    f2.write("\t\t\t<attvalues>\n")
    f2.write("\t\t\t\t<attvalue id=\"0\" value=\"")
    f2.write(edgeattr)
    f2.write("\"/>\n\t\t\t</attvalues>\n")
    f2.write("\t\t\t</edge>\n")
"""
 
f2.write("\t\t</edges>\n")

f2.write("\t</graph>\n")
f2.write("</gexf>")
f2.close()
f1.close()

"""
print(numnodes)
print(numedges)
"""


"""
for line in f1: 
#{
    #print line
    line.split("<-")
    consequent.append(line.split(" <- ")[0])
    antecedent.append(line.split(" <- ")[1].split(" (")[0])
    support.append(float(line.split(" <- ")[1].split(" (")[1].split(", ")[0]))
    confidence.append(float(line.split(" <- ")[1].split(" (")[1].split(", ")[1].split(")")[0]))
    
    #print consequent[i-1] + " " + antecedent[i-1] + " " + support[i-1] + " " + confidence[i-1]
    
    t.append( (consequent[i-1] , antecedent[i-1] , confidence[i-1] , support[i-1] ) )
    
    i = i+1 
   """ 
