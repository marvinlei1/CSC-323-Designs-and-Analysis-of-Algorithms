import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

class AstarNode {
  public int configuration[] = new int[9];
  public int gStar, hStar, fStar;
  public AstarNode parent;
  public AstarNode next;

  public AstarNode(int[] arr) {
    for (int i = 0; i < 9; i++)
     configuration[i] = arr[i];
  }

  public void printNode(AstarNode n, File outFile) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(outFile, true));
      bw.write("<" + n.fStar + ":: ");
      for (int i = 0; i < 9; i++)
        bw.write(n.configuration[i] + " ");
      bw.write("::");
      if (n.parent != null)
        for (int i = 0; i < 9; i++)
          bw.write(" " + n.parent.configuration[i]);
      else
        bw.write(" NULL");
      bw.write(">\n");
      bw.close();
    } catch (IOException e) {
    }
  }
}

class AStar {
  public AstarNode startNode, goalNode, Open, Close, childList;

  public int computeGstar(AstarNode n) {
    return n.parent.gStar + 1;
  }

  public int computeHstar(AstarNode n, int[][] matrix) {
    int distance = 0;
    for (int i = 0; i < 9; i++) {
      if (n.configuration[i] != goalNode.configuration[i]) {
        for (int j = 0; j < 9; j++) {
          if (n.configuration[i] == goalNode.configuration[j])
            distance += matrix[i][j];
        }
      }
    }
    return distance;
  }

  public boolean match(int[] configuration1, int[] configuration2) {
    for (int i = 0; i < 9; i++) {
      if (configuration1[i] != configuration2[i])
        return false;
    }
    return true;
  }

  public boolean isGoalNode(AstarNode node) {
    return match(node.configuration, goalNode.configuration);
  }

  public void listInsert(AstarNode node) {
    AstarNode temp = Open;
    if (temp.next == null)
      temp.next = node;
    else {
      while (temp.next != null && temp.next.fStar < node.fStar)
        temp = temp.next;
      node.next = temp.next;
      temp.next = node;
    }
  }

  public AstarNode remove() {
    if (Open.next == null)
      return null;
    else {
      AstarNode temp = Open.next;
      Open.next = Open.next.next;
      temp.next = null;
      return temp;
    }
  }

  public boolean checkAncestors(AstarNode currentNode) {
    AstarNode temp = currentNode;
    while (temp.parent != null) {
      if (match(temp.configuration, temp.parent.configuration))
        return true;
      temp = temp.parent;
    }
    return false;
  }

  public AstarNode constructChildList(AstarNode currentNode) {
    if (currentNode.configuration[0] == 0) { // 0

      AstarNode newNode1 = new AstarNode(currentNode.configuration);
      newNode1.configuration[0] = newNode1.configuration[1];
      newNode1.configuration[1] = 0;
      newNode1.parent = currentNode;
      if (!checkAncestors(newNode1)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode1;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode1;
        }
      }

      AstarNode newNode2 = new AstarNode(currentNode.configuration);
      newNode2.configuration[0] = newNode2.configuration[3];
      newNode2.configuration[3] = 0;
      newNode2.parent = currentNode;
      if (!checkAncestors(newNode2)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode2;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode2;
        }
      }
    
    } else if (currentNode.configuration[1] == 0) { // 1

      AstarNode newNode1 = new AstarNode(currentNode.configuration);
      newNode1.configuration[1] = newNode1.configuration[0];
      newNode1.configuration[0] = 0;
      newNode1.parent = currentNode;
      if (!checkAncestors(newNode1)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode1;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode1;
        }
      }

      AstarNode newNode2 = new AstarNode(currentNode.configuration);
      newNode2.configuration[1] = newNode2.configuration[2];
      newNode2.configuration[2] = 0;
      newNode2.parent = currentNode;
      if (!checkAncestors(newNode2)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode2;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode2;
        }
      }

      AstarNode newNode3 = new AstarNode(currentNode.configuration);
      newNode3.configuration[1] = newNode3.configuration[4];
      newNode3.configuration[4] = 0;
      newNode3.parent = currentNode;
      if (!checkAncestors(newNode3)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode3;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode3;
        }
      }

    } else if (currentNode.configuration[2] == 0) { // 2

      AstarNode newNode1 = new AstarNode(currentNode.configuration);
      newNode1.configuration[2] = newNode1.configuration[1];
      newNode1.configuration[1] = 0;
      newNode1.parent = currentNode;
      if (!checkAncestors(newNode1)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode1;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode1;
        }
      }

      AstarNode newNode2 = new AstarNode(currentNode.configuration);
      newNode2.configuration[2] = newNode2.configuration[5];
      newNode2.configuration[5] = 0;
      newNode2.parent = currentNode;
      if (!checkAncestors(newNode2)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode2;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode2;
        }
      }

    } else if (currentNode.configuration[3] == 0) { // 3

      AstarNode newNode1 = new AstarNode(currentNode.configuration);
      newNode1.configuration[3] = newNode1.configuration[0];
      newNode1.configuration[0] = 0;
      newNode1.parent = currentNode;
      if (!checkAncestors(newNode1)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode1;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode1;
        }
      }

      AstarNode newNode2 = new AstarNode(currentNode.configuration);
      newNode2.configuration[3] = newNode2.configuration[4];
      newNode2.configuration[4] = 0;
      newNode2.parent = currentNode;
      if (!checkAncestors(newNode2)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode2;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode2;
        }
      }

      AstarNode newNode3 = new AstarNode(currentNode.configuration);
      newNode3.configuration[3] = newNode3.configuration[6];
      newNode3.configuration[6] = 0;
      newNode3.parent = currentNode;
      if (!checkAncestors(newNode3)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode3;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode3;
        }
      }

    } else if (currentNode.configuration[4] == 0) { // 4

      AstarNode newNode1 = new AstarNode(currentNode.configuration);
      newNode1.configuration[4] = newNode1.configuration[1];
      newNode1.configuration[1] = 0;
      newNode1.parent = currentNode;
      if (!checkAncestors(newNode1)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode1;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode1;
        }
      }

      AstarNode newNode2 = new AstarNode(currentNode.configuration);
      newNode2.configuration[4] = newNode2.configuration[3];
      newNode2.configuration[3] = 0;
      newNode2.parent = currentNode;
      if (!checkAncestors(newNode2)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode2;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode2;
        }
      }

      AstarNode newNode3 = new AstarNode(currentNode.configuration);
      newNode3.configuration[4] = newNode3.configuration[5];
      newNode3.configuration[5] = 0;
      newNode3.parent = currentNode;
      if (!checkAncestors(newNode3)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode3;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode3;
        }
      }

      AstarNode newNode4 = new AstarNode(currentNode.configuration);
      newNode4.configuration[4] = newNode4.configuration[7];
      newNode4.configuration[7] = 0;
      newNode4.parent = currentNode;
      if (!checkAncestors(newNode4)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode4;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode4;
        }
      }

    } else if (currentNode.configuration[5] == 0) { // 5

      AstarNode newNode1 = new AstarNode(currentNode.configuration);
      newNode1.configuration[5] = newNode1.configuration[2];
      newNode1.configuration[2] = 0;
      newNode1.parent = currentNode;
      if (!checkAncestors(newNode1)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode1;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode1;
        }
      }

      AstarNode newNode2 = new AstarNode(currentNode.configuration);
      newNode2.configuration[5] = newNode2.configuration[4];
      newNode2.configuration[4] = 0;
      newNode2.parent = currentNode;
      if (!checkAncestors(newNode2)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode2;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode2;
        }
      }

      AstarNode newNode3 = new AstarNode(currentNode.configuration);
      newNode3.configuration[5] = newNode3.configuration[8];
      newNode3.configuration[8] = 0;
      newNode3.parent = currentNode;
      if (!checkAncestors(newNode3)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode3;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode3;
        }
      }

    } else if (currentNode.configuration[6] == 0) { // 6

      AstarNode newNode1 = new AstarNode(currentNode.configuration);
      newNode1.configuration[6] = newNode1.configuration[3];
      newNode1.configuration[3] = 0;
      newNode1.parent = currentNode;
      if (!checkAncestors(newNode1)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode1;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode1;
        }
      }

      AstarNode newNode2 = new AstarNode(currentNode.configuration);
      newNode2.configuration[6] = newNode2.configuration[7];
      newNode2.configuration[7] = 0;
      newNode2.parent = currentNode;
      if (!checkAncestors(newNode2)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode2;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode2;
        }
      }

    } else if (currentNode.configuration[7] == 0) { // 7

      AstarNode newNode1 = new AstarNode(currentNode.configuration);
      newNode1.configuration[7] = newNode1.configuration[4];
      newNode1.configuration[4] = 0;
      newNode1.parent = currentNode;
      if (!checkAncestors(newNode1)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode1;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode1;
        }
      }

      AstarNode newNode2 = new AstarNode(currentNode.configuration);
      newNode2.configuration[7] = newNode2.configuration[6];
      newNode2.configuration[6] = 0;
      newNode2.parent = currentNode;
      if (!checkAncestors(newNode2)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode2;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode2;
        }
      }

      AstarNode newNode3 = new AstarNode(currentNode.configuration);
      newNode3.configuration[7] = newNode3.configuration[8];
      newNode3.configuration[8] = 0;
      newNode3.parent = currentNode;
      if (!checkAncestors(newNode3)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode3;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode3;
        }
      }

    } else if (currentNode.configuration[8] == 0) { // 8

      AstarNode newNode1 = new AstarNode(currentNode.configuration);
      newNode1.configuration[8] = newNode1.configuration[5];
      newNode1.configuration[5] = 0;
      newNode1.parent = currentNode;
      if (!checkAncestors(newNode1)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode1;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode1;
        }
      }

      AstarNode newNode2 = new AstarNode(currentNode.configuration);
      newNode2.configuration[8] = newNode2.configuration[7];
      newNode2.configuration[7] = 0;
      newNode2.parent = currentNode;
      if (!checkAncestors(newNode2)) {
        AstarNode temp = childList;
        if (temp == null)
          childList = newNode2;
        else {
          while (temp.next != null)
            temp = temp.next;
          temp.next = newNode2;
        }
      }
    }
    return childList;
  }

  public AstarNode pop() {
    if (childList == null) 
      return null;
    else if (childList.next == null) {
      AstarNode node = childList;
      childList = null;
      return node;
    } else {
      AstarNode node = childList.next;
      AstarNode prev = childList;
      while (node.next != null) {
        node = node.next;
        prev = prev.next;
      }
      prev.next = null;
      return node;
    }
  }

  public boolean inOpen(AstarNode child) {
    AstarNode temp = Open.next;
    while (temp != null) {
      if (match(temp.configuration, child.configuration))
        return true;
      temp = temp.next;
    }
    return false;
  }

  public boolean inClose(AstarNode child) {
    AstarNode temp = Close.next;
    while (temp != null) {
      if (match(temp.configuration, child.configuration))
        return true;
      temp = temp.next;
    }
    return false;
  }

  public boolean openIsBetter(AstarNode child) {
    AstarNode temp = Open.next;
    while (temp != null) {
      if (match(temp.configuration, child.configuration))
        if (child.fStar < temp.fStar)
          return true;
        else
          return false;
      temp = temp.next;
    }
    return false;
  }

  public boolean closeIsBetter(AstarNode child) {
    AstarNode temp = Close.next;
    while (temp != null) {
      if (match(temp.configuration, child.configuration))
        if (child.fStar < temp.fStar)
          return true;
        else
          return false;
      temp = temp.next;
    }
    return false;
  }

  public void printList(AstarNode listHead, File outFile) {
    AstarNode temp = listHead;
    while (temp != null) {
      temp.printNode(temp, outFile);
      temp = temp.next;
    }
  }

  public void printSolution(AstarNode currentNode, File outFile) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(outFile, true));
      bw.write("Start Node \n\n");
      bw.close();
    } catch (IOException e) {
    }
    AstarNode temp;
    while (true) {
      temp = currentNode;
      while (temp.parent.parent != null)
        temp = temp.parent;
      try {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outFile, true));
        for (int i = 0; i < 9; i++) {
          bw.write(temp.parent.configuration[i] + " ");
          if ((i + 1) % 3 == 0 && i > 0)
            bw.write("\n");
        }
        bw.write("\n");
        bw.close();
        temp.parent = null;
        if (temp == currentNode) {
          bw = new BufferedWriter(new FileWriter(outFile, true));
          for (int i = 0; i < 9; i++) {
            bw.write(temp.configuration[i] + " ");
            if ((i + 1) % 3 == 0 && i > 0)
              bw.write("\n");
          }
          bw.write("\n");
          bw.close();
          break;
        }
      } catch (IOException e) {
      }
    }
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(outFile, true));
      bw.write("Goal Node");
      bw.close();
    } catch (IOException e) {
    }
  }
}

class Main {
  public static void main(String[] args) {
    int moves[] = {
      0, 1, 2, 1, 2, 3, 2, 3, 4,
      1, 0, 1, 2, 1, 2, 3, 2, 3,
      2, 1, 0, 3, 2, 1, 4, 3, 2,
      1, 2, 3, 0, 1, 2, 1, 2, 3,
      2, 1, 2, 1, 0, 1, 2, 1, 2,
      3, 2, 1, 2, 1, 0, 3, 2, 1,
      2, 3, 4, 1, 2, 3, 0, 1, 2,
      3, 2, 3, 2, 1, 2, 1, 0, 1,
      4, 3, 2, 3, 2, 1, 2, 1, 0
      };
    int movesMatrix[][] = new int[9][9];

    for (int i = 0, count = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        movesMatrix[i][j] = moves[count++];
      }
    }

    File inFile1, inFile2, outFile1, outFile2;
    inFile1 = new File(args[0]);
    inFile2 = new File(args[1]);
    outFile1 = new File(args[2]);
    outFile2 = new File(args[3]);
    AStar astar = new AStar();
    try {
      Scanner scanner = new Scanner(inFile1);
      int index = 0;
      int config[] = new int[9];
      while (scanner.hasNextInt())
        config[index++] = scanner.nextInt();
      astar.startNode = new AstarNode(config);
      scanner.close();
    } catch (IOException e) {
    }
    try {
      Scanner scanner = new Scanner(inFile2);
      int index = 0;
      int config[] = new int[9];
      while (scanner.hasNextInt())
        config[index++] = scanner.nextInt();
      astar.goalNode = new AstarNode(config);
      scanner.close();
    } catch (IOException e) {
    }

    int dummy[] = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    astar.Open = new AstarNode(dummy);
    astar.Close = new AstarNode(dummy);

    astar.startNode.gStar = 0;
    astar.startNode.hStar = astar.computeHstar(astar.startNode, movesMatrix);
    astar.startNode.fStar = astar.startNode.gStar + astar.startNode.hStar;
    astar.listInsert(astar.startNode);
    AstarNode currentNode;
    int count = 0;
    do {
      currentNode = astar.remove();
      if (astar.isGoalNode(currentNode)) {
        astar.printSolution(currentNode, outFile2);
        return;
      }
      astar.constructChildList(currentNode);

      while (astar.childList != null) {
        AstarNode child = astar.pop();
        child.gStar = astar.computeGstar(child);
        child.hStar = astar.computeHstar(child, movesMatrix);
        child.fStar = child.gStar + child.hStar;

        if (!astar.inOpen(child) && !astar.inClose(child)) {
          astar.listInsert(child);
        } else if (astar.inOpen(child) && astar.openIsBetter(child)) {
          AstarNode temp = astar.Open.next;
          AstarNode prev = astar.Open;
          while (temp != null) {
            if (astar.match(temp.configuration, child.configuration)) {
              prev.next = temp.next;
              break;
            }
            temp = temp.next;
          }
          astar.listInsert(child);
        } else if (astar.inClose(child) && astar.closeIsBetter(child)) {
          AstarNode temp = astar.Close.next;
          AstarNode prev = astar.Close;
          while (temp != null) {
            if (astar.match(temp.configuration, child.configuration)) {
              prev.next = temp.next;
              break;
            }
            temp = temp.next;
          }
          astar.listInsert(child);
        }

      }
      currentNode.next = astar.Close.next;
      astar.Close.next = currentNode;
      if (count++ < 30) {
        try {
          BufferedWriter bw = new BufferedWriter(new FileWriter(outFile1, true));
          bw.write("This is Open list:\n");
          bw.close();
        } catch (IOException e) {
        }
        astar.printList(astar.Open, outFile1);
        try {
          BufferedWriter bw = new BufferedWriter(new FileWriter(outFile1, true));
          bw.write("This is Close list:\n");
          bw.close();
        } catch (IOException e) {
        }
        astar.printList(astar.Close, outFile1);
      }
    } while (astar.Open.next != null || currentNode != astar.goalNode);

    if (astar.Open.next == null && currentNode != astar.goalNode) {
      try {
        BufferedWriter bw = new BufferedWriter(new FileWriter(outFile1, true));
        bw.write("no solution can be found in the search!\n");
        bw.close();
      } catch (IOException e) {
      }
    }
  }
}