Decentralized Cluster-Based NoSQL DB System - Readme file

•	Overview:
This project is aimed at building a decentralized NoSQL database system in Java. The system consists of a collection of nodes that can serve multiple users. The nodes are responsible for storing data, maintaining load balance, and ensuring data consistency. This system is designed to be fault-tolerant, highly available, and scalable.


•	Application Requirements
1. There must be a bootstrapping step to start the cluster and initiate all nodes. The bootstrapping node provides initial configuration information to all nodes and maps users to nodes in a load-balanced manner.
2. New users communicate with the bootstrapping node to obtain login information and their assigned node.
3. Nodes verify login information for users to minimize security risks.
4. The database uses JSON objects to store documents, each with a schema that belongs to the DB schema.
5. DB queries include creating or deleting a DB, creating, or deleting a document within a DB, and reading or writing specific json properties within a document.
6. Each document has a unique ID that is indexed efficiently.
7. Indexes are created on a single json property.
8. Data, schemas, and indexes are replicated across all nodes.
9. Read queries are satisfied by any node, while write queries have "node affinity" to a particular node.
10. If a written query is sent to a node where it has no affinity, the node sends it to the node with affinity.
11. Document-to-node affinity is load-balanced.
12. There is at least one pre-determined DB admin.



To run the app, you first need to start the bootstrapping node. Once the bootstrapping node is running, you can use the console to write "start" and initiate the rest of the nodes in the cluster.


[Project report](https://github.com/Alsaid8bashar/Decentralized-Cluster-Based-NoSQL-DB-System/blob/b36d0b4ba65659baa24829905328a29ebdb5f1a3/CapstonePrject%20report.pdf)
