"""--------------------------------------------------------------------------------------------#\
Vineet Nair
Code for tree based clustering
-----------------------------------------------------------------------------------------------"""
import re
import sys
import random
import math
from math import sqrt
from numpy import *


#Cluster node definition
class ClusterNode:
    def __init__(self,vector,left=None,right=None,distance=0.0,id=None,count=1):
        self.left=left #left hand side of the node
        self.right=right #right hand side of the node
        self.vector=vector #cluster vector
        self.id=id #cluster id
        self.distance=distance 
        self.count=count #only used for weighted average 

#Euclidean distance
def EuclideanDistance(v1,v2):
    return sqrt(sum((v1-v2)**2))

#Manhattan distance   
#def Manhattandist(v1,v2):
#    return sum(abs(v1-v2))

# def Chi2dist(v1,v2):
#     return sqrt(sum((v1-v2)**2))

def hcluster(features,distance=EuclideanDistance):
    #cluster the rows of the "features" matrix
    distances={}
    currentclustid=-1

    # clusters are initially just the individual rows
    clust=[ClusterNode(array(features[i]),id=i) for i in range(len(features))]

    while len(clust)>1:
        lowestpair=(0,1)
        closest=distance(clust[0].vector,clust[1].vector)
    
        # loop through every pair looking for the smallest distance
        for i in range(len(clust)):
            for j in range(i+1,len(clust)):
                # distances is the cache of distance calculations
                if (clust[i].id,clust[j].id) not in distances: 
                    distances[(clust[i].id,clust[j].id)]=distance(clust[i].vector,clust[j].vector)
        
                d=distances[(clust[i].id,clust[j].id)]
        
                if d<closest:
                    closest=d
                    lowestpair=(i,j)
        
        # calculate the average of the two clusters
        mergevec=[(clust[lowestpair[0]].vector[i]+clust[lowestpair[1]].vector[i])/2.0] 
            #for i in range(len(clust[0].vector))]
        
        # create the new cluster
        newcluster=[ClusterNode(array(mergevec),left=clust[lowestpair[0]],right=clust[lowestpair[1]],distance=closest,id=currentclustid)]
        
        # cluster ids that weren't in the original set are negative
        currentclustid-=1
        del clust[lowestpair[1]]
        del clust[lowestpair[0]]
        clust.append(newcluster)

    return clust[0]


def extract_clusters(clust,dist):
    # extract list of sub-tree clusters from hcluster tree with distance<dist
    clusters = {}
    if clust.distance<dist:
        # we have found a cluster subtree
        return [clust] 
    else:
        # check the right and left branches
        cl = []
        cr = []
        if clust.left!=None: 
            cl = extract_clusters(clust.left,dist=dist)
        if clust.right!=None: 
            cr = extract_clusters(clust.right,dist=dist)
        return cl+cr 
        
def get_cluster_elements(clust):
    # return ids for elements in a cluster sub-tree
    if clust.id>0:
        # positive id means that this is a leaf
        return [clust.id]
    else:
        # check the right and left branches
        cl = []
        cr = []
        if clust.left!=None: 
            cl = get_cluster_elements(clust.left)
        if clust.right!=None: 
            cr = get_cluster_elements(clust.right)
        return cl+cr
