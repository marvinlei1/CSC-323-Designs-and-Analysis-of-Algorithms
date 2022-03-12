#include <iostream>
#include <fstream>
using namespace std;

class listNode {
    friend class LLStack;
    friend class LLQueue;
    friend class RadixSort;
    int data;
    listNode *next;
  public:
    listNode(int);
    void printNode(listNode *, ofstream &);
};

listNode::listNode(int data) {
  this->data = data;
}

void listNode::printNode(listNode *node, ofstream &output) {
  if (node  != NULL) {
    if (node->next == NULL) 
      output << "(" + to_string(data) + ", NULL)->";
    else 
      output <<  "(" + to_string(data) + ", " + to_string(next->data) + ")->";
  }
}

class LLStack {
    friend class RadixSort;
    listNode *top;
  public:
    LLStack();
    void buildStack(ifstream &, int);
    void push(listNode *);
    listNode *pop();
    bool isEmpty();
    void printStack(ofstream &);
};

LLStack::LLStack() {
  listNode *dummy = new listNode(-9999);
  dummy->next = NULL;
  top = dummy;
};

void LLStack::buildStack(ifstream &input, int offSet) {
  int data;
  while (!input.eof()) {
    if (input >> data) {
      listNode *temp = new listNode(data + offSet);
      push(temp);
    }
  }
}

void LLStack::push(listNode *node) {
  if (!isEmpty()) {
    node->next = top->next;
    top->next = node;
  } else 
    top->next = node;
}

listNode *LLStack::pop() {
  if (isEmpty()) 
    return NULL;
  listNode *temp = top->next;
  if (top->next->next != NULL) {
    top->next = top->next->next;
    temp->next = NULL;
  } else
    top->next = NULL;
  return temp;
}

bool LLStack::isEmpty() {
  return top->next == NULL;
}

void LLStack::printStack(ofstream &output) {
  output << "Top->";
  listNode *temp = top;
  while (temp != NULL) {
    temp->printNode(temp, output);
    temp = temp->next;
  }
  output << "NULL\n";
}

class LLQueue {
    friend class RadixSort;
    listNode *head;
    listNode *tail;
  public:
    LLQueue();
    void buildQueue(ifstream &, ofstream &);
    void insertQ(listNode *);
    listNode *deleteQ();
    bool isEmpty();
    void printQ(ofstream &);
    void printQueue(int, int, ofstream &);
};

LLQueue::LLQueue() {
  listNode *dummy = new listNode(-9999);
  dummy->next = NULL;
  head = tail = dummy;
}

void LLQueue::buildQueue(ifstream &input, ofstream &output) {
  char op;
  int data;
  while (!input.eof()) {
    input >> op;
    input >> data;
    output << op << data << '\n';
    if (op == '+') {
      listNode *temp = new listNode(data);
      insertQ(temp);
    } else if (op == '-') {
      listNode *junk = deleteQ();
      if (junk != NULL)
        free(junk);
      else
        output << "the Queue is empty.\n";
    }
    printQ(output);
  }
}

void LLQueue::insertQ(listNode *node) {
  tail->next = node;
  tail = node;
}

listNode *LLQueue::deleteQ() {
  if (head->next == NULL) {
    tail = head;
    return NULL;
  }
  listNode *temp = head->next;
  if (temp->next != NULL) {
    head->next = temp->next;
  } else {
    head->next = NULL;
    tail = head;
  }
  return temp;
}

bool LLQueue::isEmpty() {
  return tail == head;
}

void LLQueue::printQ(ofstream &output) {
  output << "Front->";
  listNode *temp = head;
  while (temp != NULL) {
    temp->printNode(temp, output);
    temp = temp->next;
  }
  output << "NULL\n";
}

void LLQueue::printQueue(int whichTable, int index, ofstream &output) {
  output << "Table[" << whichTable << "][" << index << "]: ";
  listNode *temp = head;
  while (temp != NULL) {
    temp->printNode(temp, output);
    temp = temp->next;
  }
  output << "NULL\n";
}

class RadixSort {
    int tableSize = 10;
    LLQueue **hashTable;
    int data;
    int currentTable;
    int previousTable;
    int numDigits;
    int offSet;
    int currentPosition;
    int currentDigit;
  public:
    RadixSort();
    void firstReading(ifstream &, ofstream &);
    LLStack loadStack(ifstream &, ofstream &);
    void RSort(LLStack, ofstream &, ofstream &);
    void moveStack(LLStack, int, int, ofstream &);
    int getLength(int);
    int getDigit(int, int);
    void printTable(int, ofstream &);
    void printSortedData(int, ofstream &);

};

RadixSort::RadixSort() {
  hashTable = new LLQueue*[2];
  hashTable[0] = new LLQueue[tableSize];
  hashTable[1] = new LLQueue[tableSize];
  for (int i = 0; i < 2; i++) {
    for (int j = 0; j < tableSize; j++) {
      LLQueue *queue = new LLQueue();
      hashTable[i][j] = *queue;
    }
  }
}

void RadixSort::firstReading(ifstream &inFile, ofstream &outFile2) {
  outFile2 << "*** Performing firstReading\n";
  int negativeNum = 0;
  int positiveNum = 0;
  int number;
  while (!inFile.eof()) {
    inFile >> number;
    if (number < negativeNum)
      negativeNum = number;
    if (number > positiveNum)
      positiveNum = number;
  }
  if (negativeNum < 0)
    offSet = abs(negativeNum);
  else
    offSet = 0;
  positiveNum = positiveNum + offSet;
  numDigits = getLength(positiveNum);
  outFile2 << "Positive Number : " << positiveNum - offSet << ", Negative Number : " << negativeNum << ", Offset : " << offSet << ", Length of longest digit : " << numDigits << '\n';
}

LLStack RadixSort::loadStack(ifstream &inFile, ofstream &outFile2) {
  outFile2 << "*** Performing loadStack\n";
  LLStack *S = new LLStack();
  S->buildStack(inFile, offSet);
  return *S;
}

void RadixSort::RSort(LLStack S, ofstream &outFile2, ofstream &outFile1) {
  outFile2 << "*** Performing RSort\n";
  currentPosition = 0;
  currentTable = 0;
  moveStack(S, currentPosition, currentTable, outFile2);
  printTable(currentTable, outFile2);
  currentPosition++;
  currentTable = 1;
  previousTable = 0;
  int currentQueue = 0;
  while (currentPosition < numDigits) {
    while (currentQueue < tableSize) {
      while (!hashTable[previousTable][currentQueue].isEmpty()) {
        listNode *newNode = hashTable[previousTable][currentQueue].deleteQ();
        newNode->next = NULL;
        int hashIndex = getDigit(newNode->data, currentPosition);
        hashTable[currentTable][hashIndex].insertQ(newNode);
      }
      currentQueue++;
    }
    printTable(currentTable, outFile2);
    previousTable = currentTable;
    currentTable = (currentTable + 1) % 2;
    currentQueue = 0;
    currentPosition++;
  }
  printSortedData(previousTable, outFile1);
}

void RadixSort::moveStack(LLStack S, int currentPosition, int currentTable, ofstream &outFile2) {
  outFile2 << "*** Performing moveStack\n";
  while (!S.isEmpty()) {
    listNode *newNode = S.pop();
    int hashIndex = getDigit(newNode->data, currentPosition);
    hashTable[currentTable][hashIndex].insertQ(newNode);
  }
}

int RadixSort::getLength(int data) {
  return to_string(data).length();
}

int RadixSort::getDigit(int data, int position) {
  string number = to_string(data);
  while (number.length() < numDigits) {
    number = "0" + number;
  }
  return number[numDigits - position - 1] - 48;
}

void RadixSort::printTable(int whichTable, ofstream &outFile2) {
  for (int i = 0; i < tableSize; i++) {
    if (!hashTable[whichTable][i].isEmpty())
      hashTable[whichTable][i].printQueue(whichTable, i, outFile2);
  }
  outFile2 << '\n';
}

void RadixSort::printSortedData(int whichTable, ofstream &outFile1) {
  for (int i = 0; i < tableSize; i++) {
    if (!hashTable[whichTable][i].isEmpty()) {
      while (!hashTable[whichTable][i].isEmpty()) {
        outFile1 << hashTable[whichTable][i].deleteQ()->data - offSet << endl;
      }
    }
  }
}

int main(int argc, char *argv[]) {
  ifstream inFile(argv[1]);
  ofstream outFile1(argv[2]);
  ofstream outFile2(argv[3]);
  RadixSort *sort = new RadixSort();
  sort->firstReading(inFile, outFile2);
  inFile.close();
  inFile.open(argv[1]);
  LLStack S = sort->loadStack(inFile, outFile2);
  S.printStack(outFile2);
  sort->RSort(S, outFile2, outFile1);
  inFile.close();
  outFile1.close();
  outFile2.close();
}