#include <iostream>
#include <fstream>

using namespace std;

class uEdge {
  public:
    int Ni, Nj, cost;
    uEdge *next;
    uEdge();
    uEdge(int, int, int, uEdge *);
    void printEdge(uEdge *, ofstream &);
};

uEdge::uEdge() {
}

uEdge::uEdge(int Ni, int Nj, int cost, uEdge *next) {
  this->Ni = Ni;
  this->Nj = Nj;
  this->cost = cost;
  this->next = next;
}

void uEdge::printEdge(uEdge *edge, ofstream &outFile) {
  outFile << edge->Ni << "," << edge->Nj << "," << edge->cost << "\n";
}

class PrimMST {
  public:
    int numNodes, nodeInSetA, *whichSet, totalMSTCost;
    uEdge *edgelistHead, *MSTlistHead, *removeEdge();
    void listInsert(uEdge *);
    void addEdge(uEdge *);
    void printSet(ofstream &);
    void printEdgeList(ofstream &);
    void printMSTList(ofstream &);
    void printMSTListFinal(ofstream &);
    bool setBisEmpty();
    void updateMST(uEdge *);
};

void PrimMST::listInsert(uEdge *newEdge)  {
  uEdge *temp = edgelistHead;
  if (temp->next == NULL) {
    temp->next = newEdge;
  } else {
    while (temp->next != NULL && temp->next->cost < newEdge->cost)
      temp = temp->next;
    newEdge->next = temp->next;
    temp->next = newEdge;
  }
}

uEdge *PrimMST::removeEdge() {
  uEdge *prev = edgelistHead;
  uEdge *e = edgelistHead->next;
  while (e != NULL) {
    if (whichSet[e->Ni] != whichSet[e->Nj] && (whichSet[e->Ni] == 1 || whichSet[e->Nj] == 1)) {
      prev->next = e->next;
      return e;
    }
    e = e->next;
    prev = prev->next;
  }
  return NULL;
}

void PrimMST::addEdge(uEdge *edge) {
  if (MSTlistHead->next != NULL)
    edge->next = MSTlistHead->next;
  MSTlistHead->next = edge;
}

void PrimMST::printSet(ofstream &outFile) {
  for (int i = 1; i <= numNodes; i++) {
    outFile << whichSet[i] << " ";
  }
  outFile << "\n";
}

void PrimMST::printEdgeList(ofstream &outFile) {
  outFile << "edgelistHead->";
  uEdge *temp = edgelistHead;
  while (temp != NULL) {
    if (temp->next != NULL)
      outFile << "<" << temp->Ni << "," << temp->Nj << "," << temp->cost << "," << temp->next->Ni << ">->";
    else
      outFile << "<" << temp->Ni << "," << temp->Nj << "," << temp->cost << ",NULL>";
    temp = temp->next;
  }
  outFile << "\n";
}

void PrimMST::printMSTList(ofstream &outFile) {
  outFile << "MSTlistHead->";
  uEdge *temp = MSTlistHead;
  while (temp != NULL) {
    if (temp->next != NULL)
      outFile << "<" << temp->Ni << "," << temp->Nj << "," << temp->cost << "," << temp->next->Ni << ">->";
    else
      outFile << "<" << temp->Ni << "," << temp->Nj << "," << temp->cost << ",NULL>";
    temp = temp->next;
  }
  outFile << "\n";
}

void PrimMST::printMSTListFinal(ofstream &outFile) {
  uEdge *temp = MSTlistHead->next;
  while (temp != NULL) {
    outFile << temp->Ni << " " << temp->Nj << " " << temp->cost << "\n";
    temp = temp->next;
  }
}

bool PrimMST::setBisEmpty() {
  for (int i = 1; i <= numNodes; i++)
    if (whichSet[i] != 1)
      return false; 
  return true;
}

void PrimMST::updateMST(uEdge *newEdge) {
  addEdge(newEdge);
  totalMSTCost += newEdge->cost;
  if (whichSet[newEdge->Ni] == 1)
    whichSet[newEdge->Nj] = 1;
  else
    whichSet[newEdge->Ni] = 1;
}



int main(int argc, char *argv[]) {
  ifstream inFile;
  ofstream MSTfile, deBugFile;
  inFile.open(argv[1]);
  deBugFile.open(argv[4]);
  MSTfile.open(argv[3]);
  PrimMST *t = new PrimMST();
  inFile >> t->numNodes;
  t->nodeInSetA = atoi(argv[2]);
  t->whichSet = new int[t->numNodes + 1];
  for (int i = 1; i <= t->numNodes; i++) 
    t->whichSet[i] = 2;
  t->whichSet[t->nodeInSetA] = 1;
  t->printSet(deBugFile);
  uEdge *dummy1 = new uEdge(0, 0, 0, NULL);
  uEdge *dummy2 = new uEdge(0, 0, 0, NULL);
  t->edgelistHead = dummy1;
  t->MSTlistHead = dummy2;
  t->totalMSTCost = 0;

  while (!inFile.eof()) {
    int Ni = -1, Nj, edgeCost;
    inFile >> Ni >> Nj >> edgeCost;
    if (Ni > -1) {
      uEdge *newEdge = new uEdge(Ni, Nj, edgeCost, NULL);
      t->listInsert(newEdge);
      t->printEdgeList(deBugFile);
    }
  }

  while (!t->setBisEmpty()) {
    uEdge *nextEdge = t->removeEdge();
    uEdge *newEdge = new uEdge(nextEdge->Ni, nextEdge->Nj, nextEdge->cost, NULL);
    newEdge->printEdge(newEdge, deBugFile);
    t->updateMST(newEdge);
    t->printSet(deBugFile);
    t->printEdgeList(deBugFile);
    t->printMSTList(deBugFile);
  }

  MSTfile << "*** Prim’s MST of the input graph, G is: ***\n" << t->numNodes << "\n";
  t->printMSTListFinal(MSTfile);
  MSTfile << "*** MST total cost = " << t->totalMSTCost;


  inFile.close();
  deBugFile.close();
  MSTfile.close();
}