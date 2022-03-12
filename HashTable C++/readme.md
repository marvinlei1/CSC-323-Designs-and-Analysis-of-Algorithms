You are to implement a hash table (an 1D array of ordered linked list) of B buckets.
The bucket size, B, will be given via argv[2] in command line.
The hash function for the hash table is the “Doit” function given in the lecture note and have discuss in the lecture.
The input to your program is a text file contains a list of triplets {<op firstName lastName >} where op is either + or - or ?; + means insert, - means delete, and ? means information retrieval; firstName and lastName are character strings.
lastName in the triplet is the key passes to the hash function to get the bucket index from the hash function for information storage and retrieval.
