#include <iostream>
#include <fstream>

using namespace std;

class image {
  public:
    int numRows, numCols, minVal, maxVal, power2Size, **imgAry;
    image(int, int, int, int);
    int computePower2(int, int);
    void loadImage(ifstream &, ofstream &);
    void zero2DAry();
};

image::image(int numRows, int numCols, int minVal, int maxVal) {
  this->numRows = numRows;
  this->numCols = numCols;
  this->minVal = minVal;
  this->maxVal = maxVal;
  power2Size = computePower2(numRows, numCols);
  imgAry = new int*[power2Size];
  for (int i = 0; i < power2Size; i++)
    imgAry[i] = new int[power2Size];
}

int image::computePower2(int numRows, int numCols) {
  int size = max(numRows, numCols);
  int power2 = 2;
  while (size > power2)
    power2 *= 2;
  return power2;
}

void image::loadImage(ifstream &inFile, ofstream &outFile2) {
  int color;
  for (int i = 0; i < power2Size; i++) {
    for (int j = 0; j < power2Size; j++) {
      if (j < numCols && i < numRows) {
        inFile >> color;
        imgAry[i][j] = color;
      }
      outFile2 << imgAry[i][j];
    }
    outFile2 << '\n';
  }
}

void image::zero2DAry() {
  for (int i = 0; i < power2Size; i++)
    for (int j = 0; j < power2Size; j++)
      imgAry[i][j] = 0;
}

class QtTreeNode {
  public:
    int color, upperR, upperC, Size;
    QtTreeNode *NWkid = NULL, *NEkid = NULL, *SWkid = NULL, *SEkid = NULL;
    QtTreeNode(int, int, int, QtTreeNode *, QtTreeNode *, QtTreeNode *, QtTreeNode *);
    void printQtNode(QtTreeNode *, ofstream &);
};

QtTreeNode::QtTreeNode(int upperR, int upperC, int Size, QtTreeNode *NWkid, QtTreeNode *NEkid, QtTreeNode *SWkid, QtTreeNode *SEkid) {
  this->upperR = upperR;
  this->upperC = upperC;
  this->Size = Size;
  this->NWkid = NWkid;
  this->NEkid = NEkid;
  this->SWkid = SWkid;
  this->SEkid = SEkid;
}

void QtTreeNode::printQtNode(QtTreeNode *node, ofstream &outFile2) {
  outFile2 << node->color << "," << node->upperR  << "," << node->upperC << "," << (node->NWkid == NULL ? node->color : node->NWkid->color) << "," << (node->NEkid == NULL ? node->color : node->NEkid->color) << "," << (node->SWkid == NULL ? node->color : node->SWkid->color) << "," << (node->SEkid == NULL ? node->color : node->SEkid->color) << "\n";
}

class QuadTree {
  public:
    QtTreeNode *QtRoot;
    QtTreeNode *buildQuadTree(int **, int, int, int, ofstream &);
    bool isLeaf(QtTreeNode *);
    void preOrder(QtTreeNode *, ofstream &);
    void postOrder(QtTreeNode *, ofstream &);
};

QtTreeNode* QuadTree::buildQuadTree(int **imgAry, int upR, int upC, int size, ofstream &outFile2) {
  QtTreeNode *newQtNode = new QtTreeNode(upR, upC, size, NULL, NULL, NULL, NULL);
  newQtNode->printQtNode(newQtNode, outFile2);
  if (size == 1) 
    newQtNode->color = imgAry[upR][upC];
  else {
    int halfSize = size/2;
    newQtNode->NWkid = buildQuadTree(imgAry, upR, upC, halfSize, outFile2);
    newQtNode->NEkid = buildQuadTree(imgAry, upR, upC + halfSize, halfSize, outFile2);
    newQtNode->SWkid = buildQuadTree(imgAry, upR + halfSize, upC, halfSize, outFile2);
    newQtNode->SEkid = buildQuadTree(imgAry, upR + halfSize, upC + halfSize, halfSize, outFile2);
    int sumColor = 0;
    sumColor = newQtNode->NWkid->color + newQtNode->NEkid->color + newQtNode->SWkid->color + newQtNode->SEkid->color;
    if (sumColor == 0) {
      newQtNode->color = 0;
      newQtNode->NWkid = NULL;
      newQtNode->NEkid = NULL;
      newQtNode->SWkid = NULL;
      newQtNode->SEkid = NULL;
    } else if (sumColor == 4) {
      newQtNode->color = 1;
      newQtNode->NWkid = NULL;
      newQtNode->NEkid = NULL;
      newQtNode->SWkid = NULL;
      newQtNode->SEkid = NULL;
    } else 
      newQtNode->color = 5;
  }
  return newQtNode;
}

bool QuadTree::isLeaf(QtTreeNode *node) {
  return node->NWkid == NULL && node->NEkid == NULL && node->SWkid == NULL && node->SEkid == NULL;
}

void QuadTree::preOrder(QtTreeNode *Qt, ofstream &outFile1) {
  if (isLeaf(Qt))
    Qt->printQtNode(Qt, outFile1);
  else {
    Qt->printQtNode(Qt, outFile1);
    preOrder(Qt->NWkid, outFile1);
    preOrder(Qt->NEkid, outFile1);
    preOrder(Qt->SWkid, outFile1);
    preOrder(Qt->SEkid, outFile1);
  }
}

void QuadTree::postOrder(QtTreeNode *Qt, ofstream &outFile1) {
  if (isLeaf(Qt))
    Qt->printQtNode(Qt, outFile1);
  else {
    postOrder(Qt->NWkid, outFile1);
    postOrder(Qt->NEkid, outFile1);
    postOrder(Qt->SWkid, outFile1);
    postOrder(Qt->SEkid, outFile1);
    Qt->printQtNode(Qt, outFile1);
  }
}

int main(int argc, char *argv[]) {
  ifstream inFile;
  ofstream outFile1, outFile2;
  inFile.open(argv[1]);
  outFile1.open(argv[2]);
  outFile2.open(argv[3]);
  int numRows, numCols, minVal, maxVal;
  inFile >> numRows >> numCols >> minVal >> maxVal;
  image *im = new image(numRows, numCols, minVal, maxVal);
  im->zero2DAry();
  im->loadImage(inFile, outFile2);
  QuadTree QTree;
  QTree.QtRoot = QTree.buildQuadTree(im->imgAry, 0, 0, im->power2Size, outFile2);
  QTree.preOrder(QTree.QtRoot, outFile1);
  outFile1 << "\n";
  QTree.postOrder(QTree.QtRoot, outFile1);
  inFile.close();
  outFile1.close();
  outFile2.close();
}