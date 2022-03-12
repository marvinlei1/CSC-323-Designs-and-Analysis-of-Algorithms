#include <iostream>
#include <fstream>
using namespace std;

class listNode {
    friend class LLStack;
    friend class LLQueue;
    friend class LLlist;
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
    listNode *top;
  public:
    LLStack();
    void buildStack(ifstream &, ofstream &);
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

void LLStack::buildStack(ifstream &input, ofstream &output) {
  char op;
  int data;
  while (!input.eof()) {
    input >> op;
    input >> data;
    output << op << data << '\n';
    if (op == '+') {
      listNode *temp = new listNode(data);
      push(temp);
    } else if (op == '-') {
      listNode *junk = pop();
      if (junk != NULL)
        free(junk);
      else
        output << "the Stack is empty.\n";
    }
    printStack(output);
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
    listNode *head;
    listNode *tail;
  public:
    LLQueue();
    void buildQueue(ifstream &, ofstream &);
    void insertQ(listNode *);
    listNode *deleteQ();
    bool isEmpty();
    void printQ(ofstream &);
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
  if (head->next == NULL) 
    return NULL;
  listNode *temp = head->next;
  if (head->next->next != NULL)
    head->next = head->next->next;
  else
    head->next = NULL;
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

class LLlist {
    listNode *listHead;
  public:
    LLlist();
    void buildList(ifstream &, ofstream &);
    void listInsert(listNode *);
    listNode *deleteOneNode(int);
    void printList(ofstream &);
};

LLlist::LLlist() {
  listNode *dummy = new listNode(-9999);
  listHead = dummy;
}

void LLlist::buildList(ifstream &input, ofstream &output) {
  char op;
  int data;
  while (!input.eof()) {
    input >> op;
    input >> data;
    output << op << data << '\n';
    if (op == '+') {
      listNode *temp = new listNode(data);
      listInsert(temp);
    } else if (op == '-') {
      listNode *junk = deleteOneNode(data);
      if (junk != NULL)
        free(junk);
      else
        output << "the data is not in the list\n";
    }
    printList(output);
  }
}

void LLlist::listInsert(listNode *node) {
  listNode *temp = listHead;
  while (temp->next != NULL && temp->next->data < node->data)
    temp = temp->next;
  node->next = temp->next;
  temp->next = node;
}

listNode *LLlist::deleteOneNode(int data) {
  listNode *temp = listHead;
  while (temp->next != NULL && temp->next->data != data)
    temp = temp->next;
  if (temp->next != NULL && temp->next->data == data) {
    listNode *gone = temp->next;
    temp->next = temp->next->next;
    return gone;
  }
  return NULL;
}

void LLlist::printList(ofstream &output) {
  output << "listHead->";
  listNode *temp = listHead;
  while (temp != NULL) {
    temp->printNode(temp, output);
    temp = temp->next;
  }
  output << "NULL\n";
}

int main(int argc, char *argv[]) {
  LLStack *S = new LLStack();
  LLQueue *Q = new LLQueue();
  LLlist *listHead = new LLlist();
  ifstream inFile;
  ofstream outFile1, outFile2, outFile3;
  inFile.open(argv[1]);
  outFile1.open(argv[2]);
  outFile2.open(argv[3]);
  outFile3.open(argv[4]);
  S->buildStack(inFile, outFile1);
  inFile.close();
  inFile.open(argv[1]);
  Q->buildQueue(inFile, outFile2);
  inFile.close();
  inFile.open(argv[1]);
  listHead->buildList(inFile, outFile3);
  inFile.close();
  outFile1.close();
  outFile2.close();
  outFile3.close();
}