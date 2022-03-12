#include <iostream>
#include <string>
#include <fstream>

using namespace std;

class listNode {
    friend class hashTable;
    string firstName, lastName;
    listNode *next;
  public:
    listNode(string, string);
    void printNode(listNode *, ofstream &);
    listNode() {};
};

listNode::listNode(string firstName, string lastName) {
  this->firstName = firstName;
  this->lastName = lastName;
}

void listNode::printNode(listNode *node, ofstream &output) {
  if (node->next != NULL) 
    output << "(" << node->firstName << " " << node->lastName << " " << node->next->firstName << ")->";
  else
    output << "(" << node->firstName << " " << node->lastName << " NULL)->";
}


class hashTable {
    char op;
    int bucketSize;
    listNode *hashTable;
  public:
    void createHashTable(int);
    int Doit(string);
    void informationProcessing(ifstream &, ofstream &);
    listNode *findSpot(int, string, string);
    void hashInsert(int, string, string, ofstream &);
    void hashDelete(int, string, string, ofstream &);
    void hashRetrieval(int, string, string, ofstream &);
    void printList(int, ofstream &);
    void printHashTable(ofstream &);
};

void hashTable::createHashTable(int bucketSize) {
  this->bucketSize = bucketSize;
  hashTable = new listNode[bucketSize];
  for (int i = 0; i < bucketSize; i++) {
    hashTable[i] = *new listNode("dummyfirst", "dummylast");
  }
}

int hashTable::Doit(string s) {
  unsigned int val = 1;
  for (int i = 0; i < s.length(); i++) {
    val = val * 32 + (int) s[i];
  }
  return val % bucketSize;
}

void hashTable::informationProcessing(ifstream &input, ofstream &output) {
  string firstName, lastName;
  int index;
  while (!input.eof()) {
    input >> op >> firstName >> lastName;
    output << op << " " << firstName << " " << lastName << '\n';
    index = Doit(lastName);
    output << "Index " << index << '\n';
    printList(index, output);
    if (op == '+')
      hashInsert(index, firstName, lastName, output);
    else if (op == '-')
      hashDelete(index, firstName, lastName, output);
    else if (op == '?')
      hashRetrieval(index, firstName, lastName, output);
  }
}

listNode *hashTable::findSpot(int index, string firstName, string lastName) {
  listNode *spot = &hashTable[index];
  while (spot->next != NULL && spot->next->lastName.compare(lastName) < 0)
    spot = spot->next;
  while (spot->next != NULL && spot->next->lastName.compare(lastName) == 0 && spot->next->firstName.compare(firstName) < 0)
    spot = spot->next;
  return spot;
}

void hashTable::hashInsert(int index, string firstName, string lastName, ofstream &outFile2) {
  outFile2 << "***Performing hashInsert on " << firstName << ", " << lastName << '\n';
  listNode *spot = findSpot(index, firstName, lastName);
  if (spot->next != NULL && spot->next->lastName.compare(lastName) == 0 && spot->next->firstName.compare(firstName) == 0)
    outFile2 << "*** Warning, the record is already in the database!\n";
  else {
    listNode *newNode = new listNode(firstName, lastName);
    newNode->next = spot->next;
    spot->next = newNode;
    printList(index, outFile2);
  }
}

void hashTable::hashDelete(int index, string firstName, string lastName, ofstream &outFile2) {
  outFile2 << "*** Performing hashDelete on " << firstName << ", " << lastName << '\n';
  listNode *spot = findSpot(index, firstName, lastName);
  if (spot->next != NULL && spot->next->lastName.compare(lastName) == 0 && spot->next->firstName.compare(firstName) == 0) {
    listNode *junk = spot->next;
    spot->next = spot->next->next;
    junk->next = NULL;
    delete junk;
    printList(index, outFile2);
  } else 
    outFile2 << "*** Warning, the record is *not* in the database!\n";
}

void hashTable::hashRetrieval(int index, string firstName, string lastName, ofstream &outFile2) {
  outFile2 << "*** Performing hashRetrieval on " << firstName << ", " << lastName << '\n';
  listNode *spot = findSpot(index, firstName, lastName);
  if (spot->next != NULL && spot->next->lastName.compare(lastName) == 0 && spot->next->firstName.compare(firstName) == 0)
    outFile2 << "Yes, the record is in the database!\n";
  else
    outFile2 << "No, the record is not in the database!\n";
}

void hashTable::printList(int index, ofstream &outFile) {
  listNode *node = &hashTable[index];
  while (node != NULL) {
    node->printNode(node, outFile);
    node = node->next;
  }
  outFile << '\n';
}

void hashTable::printHashTable(ofstream &outFile) {
  for (int i = 0; i < bucketSize; i++) {
    outFile << "HashTable[" << i << "]: ";
    printList(i, outFile);
  }
}


int main(int argc, char *argv[]) {
  ifstream inFile(argv[1]);
  ofstream outFile1(argv[3]), outFile2(argv[4]);
  hashTable test;
  test.createHashTable(atoi(argv[2]));
  test.informationProcessing(inFile, outFile2);
  test.printHashTable(outFile1);
  inFile.close();
  outFile1.close();
  outFile2.close();
}