import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;

class HuffmanCoding {

  private int charCountAry[] = new int[256];
  private String charCode[] = new String[256];

  class treeNode {
    private String chStr, code;
    private int frequency;
    private treeNode left, right, next;

    public treeNode(){
    }

    public treeNode(String chStr, int frequency, String code, treeNode left, treeNode right, treeNode next) {
      this.chStr = chStr;
      this.frequency = frequency;
      this.code = code;
      this.left = left;
      this.right = right;
      this.next = next;
    }

    public void printNode(treeNode T, File DebugFile) {
      try {
        BufferedWriter bw = new BufferedWriter(new FileWriter(DebugFile, true));
        bw.write("(" + T.chStr + ", " + T.frequency + ", " + T.code + ", " + (T.next == null ? "null" : T.next.chStr) + ", " + (T.left == null ? "null" : T.left.chStr) + ", " + (T.right == null ? "null" : T.right.chStr) + ")\n");
        bw.close();
      } catch (IOException e) {
      }
    }

    public boolean isLeaf(treeNode T) {
      return T.left == null && T.right == null;
    }
  }

  class linkedList {
    private treeNode listHead;

    public linkedList() {
      treeNode dummy = new treeNode("dummy", 0, "", null, null, null);
      listHead = dummy;
    }

    public void insertNewNode(treeNode T) {
      treeNode temp = listHead;
      while (temp.next != null && temp.next.frequency < T.frequency)
        temp = temp.next;
      T.next = temp.next;
      temp.next = T;
    }

    public void printList(File DebugFile) {
      treeNode temp = listHead;
      while (temp != null) {
        temp.printNode(temp, DebugFile);
        temp = temp.next;
      }
    }
  }

  class BinaryTree {
    treeNode Root;

    public BinaryTree(treeNode Root) {
    	this.Root = Root;
    }

    void preOrderTraversal(treeNode T, File DebugFile) {
    	if (isLeaf(T)) 
    		T.printNode(T, DebugFile);
    	else {
    		T.printNode(T, DebugFile);
    		preOrderTraversal(T.left, DebugFile);
    		preOrderTraversal(T.right, DebugFile);
    	}
    }

    void inOrderTraversal(treeNode T, File DebugFile) {
    	if (isLeaf(T)) 
    		T.printNode(T, DebugFile);
    	else {
    		inOrderTraversal(T.left, DebugFile);
    		T.printNode(T, DebugFile);
    		inOrderTraversal(T.right, DebugFile);
    	}
    }

    void postOrderTraversal(treeNode T, File DebugFile) {
    	if (isLeaf(T)) 
    		T.printNode(T, DebugFile);
    	else {
    		postOrderTraversal(T.left, DebugFile);
    		postOrderTraversal(T.right, DebugFile);
    		T.printNode(T, DebugFile);
    	}
    }

    public boolean isLeaf(treeNode T) {
      return T.left == null && T.right == null;
    }

  }

  public void computeCharCounts(File inFile) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(inFile));
      int i;
      while ((i = br.read()) != -1) {
        charCountAry[i]++;
      }
      br.close();
    } catch (IOException e) {
    }
  }

  public void printCountAry(File DebugFile) {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(DebugFile, true));
      for (int i = 0; i < 256; i++) {
        if (charCountAry[i] != 0)
          bw.write((char) i + " " + charCountAry[i] + "\n");
      }
      bw.close();
    } catch (IOException e) {
    }
  }
  
  public linkedList constructHuffmanLList(File DebugFile) {
    linkedList L = new linkedList();
    int index = 0;
    while (index < 256) {
      if (charCountAry[index] > 0) {
        String chr = "" + (char) index;
        int prob = charCountAry[index];
        treeNode newNode = new treeNode(chr, prob, "", null, null, null);
        L.insertNewNode(newNode);
        L.printList(DebugFile);
      }
      index++;
    }
    return L;
  }

  public treeNode constructHuffmanBinTree(linkedList listHead, File DebugFile) {
    linkedList L = listHead;
    while (L.listHead.next.next != null) {
      treeNode newNode = new treeNode(L.listHead.next.chStr + L.listHead.next.next.chStr, L.listHead.next.frequency + L.listHead.next.next.frequency, "", L.listHead.next, L.listHead.next.next, null);
      L.listHead.next = L.listHead.next.next.next;
      L.insertNewNode(newNode);
      L.printList(DebugFile);
    }
    return L.listHead.next;
  }

  public void constructCharCode(treeNode T, String code) {
    if (T.isLeaf(T)) {
      T.code = code;
      int index = T.chStr.charAt(0);
      charCode[index] = code;
    } else {
      constructCharCode(T.left, code + "0");
      constructCharCode(T.right, code + "1");
    }
  }

  public void userInterface(treeNode root) {
    String nameOrg, nameCompress, nameDeCompress;
    char yesNo;
    Scanner s = new Scanner(System.in);
    System.out.println("Do you want to encode a file?");
    yesNo = s.next().charAt(0);
    while (yesNo != 'N') {
      System.out.println("Please enter name of file to be compressed.");
      nameOrg = s.next();
      nameCompress = nameOrg + "_Compressed.txt";
      nameDeCompress = nameOrg + "_DeCompress.txt";
      nameOrg = nameOrg + ".txt";
      File orgFile = new File(nameOrg);
      File compFile = new File(nameCompress);
      File deCompFile = new File(nameDeCompress);
      Encode(orgFile, compFile);
      Decode (compFile, deCompFile, root);
      System.out.println("Do you want to encode a file?");
      yesNo = s.next().charAt(0);
    }
    s.close();
    System.exit(0);
  }

  public void Encode(File inFile, File outFile) {
    try {
      BufferedReader br = new BufferedReader(new FileReader(inFile));
      BufferedWriter bw = new BufferedWriter(new FileWriter(outFile, true));
      int i;
      while ((i = br.read()) != -1) {
        charCountAry[i]++;
        String code = charCode[i];
        bw.write(code);
      }
      br.close();
      bw.close();
    } catch (IOException e) {
    }
  }

  public void Decode(File inFile, File outFile, treeNode root) {
    treeNode spot = root;
    try {
      BufferedReader br = new BufferedReader(new FileReader(inFile));
      BufferedWriter bw = new BufferedWriter(new FileWriter(outFile, true));
      int oneBit;
      while ((oneBit = br.read()) != -1) {
        if (oneBit == '0')
          spot = spot.left;
        else if (oneBit == '1')
          spot = spot.right;
        else
          System.out.println("Error! The compress file contains invalid character!");
        if (spot.isLeaf(spot)) {
          bw.write(spot.chStr);
          spot = root;
        }
      }
      br.close();
      bw.close();
    } catch (IOException e) {
    }
    if (spot != root)
      System.out.println("Error: The compress file is corrupted!");
  }
}

class Main {
  public static void main(String[] args) {
    String nameInFile = args[0];
    String nameDebugFile = nameInFile + "_DeBug.txt";
    File inFile = new File(nameInFile + ".txt");
    File DebugFile = new File(nameDebugFile);
    HuffmanCoding test = new HuffmanCoding();
    test.computeCharCounts(inFile);
    test.printCountAry(DebugFile);
    HuffmanCoding.linkedList okay = test.constructHuffmanLList(DebugFile);
    HuffmanCoding.treeNode root = test.constructHuffmanBinTree(okay, DebugFile);
    test.constructCharCode(root, "");
    okay.printList(DebugFile);
    HuffmanCoding.BinaryTree bTree = test.new BinaryTree(root);
    bTree.preOrderTraversal(root, DebugFile);
    bTree.inOrderTraversal(root, DebugFile);
    bTree.postOrderTraversal(root, DebugFile);
    test.userInterface(root);
  }
}