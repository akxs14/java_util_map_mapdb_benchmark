# java_util_map_mapdb_benchmark
A time and space benchmark between the java.util.Map and MapDB's Off-heap Map

# Sample run

The average of 10 sample runs with 2 million items has been performed and the averaged
runtime and occupied memory are given below.

All of them were cold runs on default configured virtual machines. 

	java.util.HashMap inserting 2.0 million elements in 785ms
	Consumed memory: 0.187845088GB
	
	org.mapdb.HashMap inserting 2.0 million elements in 20012ms
	Consumed memory: 0.062233968GB
	
	org.mapdb.TreeMap inserting 2.0 million elements in 8477ms
	Consumed memory: 0.050914768GB
