"""--------------------------------------------------------------------------------------------#\
Vineet Nair Code for tree based clustering
-----------------------------------------------------------------------------------------------"""
import os 
import random 
#from numpy import * 
import hclusttest
import hcluster

#Synthetic Graph consisting of 1000 nodes and 100000 edges
numnodes = 1000
numedges = 100000

#create a list of nodes 
nodelist = []
for i in range(numnodes):
        nodelist.append(i)
n = len(nodelist)

#creating list of edges
#Creating a synthetic graph
edgelist= []
for j in range(numedges):
    edgelist.append(random.sample(nodelist, 2))

#sort the edgelist
edgelist.sort()

featurematrix = []
temp = edgelist[0][0]
#print(edgelist[0])

#Generating the sparse matrix
fmline = []
for index , item in enumerate(edgelist):
    newtemp = item[0]
    if(newtemp != temp):
        temp = newtemp
        featurematrix.append(fmline)
        fmline = []
        fmline.append(item[1])
    else:
        fmline.append(item[1])

#print(featurematrix)
#print(len(featurematrix))        
        
        
    

"""
Building feature matrix from the edge list
extract feature vector for each node 
"""
#tree = hclusttest.hcluster(featurematrix)
tree = hcluster.hcluster(featurematrix)
